package com.nothing.ecommerce.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.entity.Order;
import com.nothing.ecommerce.entity.OrderItem;
import com.nothing.ecommerce.entity.Product;
import com.nothing.ecommerce.exception.AddressNotFoundException;
import com.nothing.ecommerce.exception.InvalidProductIdException;
import com.nothing.ecommerce.exception.OrderNotFoundException;
import com.nothing.ecommerce.exception.UnAuthorizedPaymentCallbackException;
import com.nothing.ecommerce.exception.UnAuthorizedUserException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.model.OrderRequest;
import com.nothing.ecommerce.model.OrderViewModel;
import com.nothing.ecommerce.model.PaymentCallbackRequest;
import com.nothing.ecommerce.model.OrderPaymentRequest;
import com.nothing.ecommerce.model.ProductOrderRequest;
import com.nothing.ecommerce.model.ProductOrderResponse;
import com.nothing.ecommerce.repository.OrderItemRepository;
import com.nothing.ecommerce.repository.OrderRepository;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressService addressService;

    private RazorpayClient razorpayClient;
    private static final long EXPIRATION_TIME_LIMIT = 30 * 60 * 1000; // 30 minutes

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.secret.key}")
    private String razorpaySecret;

    @Override
    public OrderPaymentRequest create(String reference, OrderRequest orderRequest) {

        int addressId = orderRequest.getAddressId();
        List<ProductOrderRequest> productsOrderRequest = orderRequest.getOrders();

        // verity userId
        int userId = userService.findUserIdByReference(reference);

        // verity user Address
        Address address = addressService.findById(addressId);
        if (address == null) {
            throw new AddressNotFoundException("Error: Address not found by id " + addressId);
        } else if (userId != address.getUserId()) {
            throw new UnAuthorizedUserException("Error: accress denied, you are not allowed to access this address");
        }

        List<Double> productPrices = new ArrayList<Double>();

        // verify product Id and create product prices list
        for (ProductOrderRequest ProductRequest : productsOrderRequest) {
            int productId = ProductRequest.getProductId();
            Double price = productService.findPriceById(productId);
            if (price == 0.0) {
                throw new InvalidProductIdException("Error: Product not found with id " + productId);
            } else {
                productPrices.add(price);
            }
        }

        Order order = null;
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        try {
            // Create a new dummy order object
            order = new Order(userId, 0, "creating", address.toString(), "null");
            order = orderRepository.save(order); // Save the order object
            int orderId = order.getOrderId(); // Get the order id

            Double totalPrice = 0.0;
            // create Order Items in DataBase and find total price
            for (int i = 0; i < productsOrderRequest.size(); i++) {
                int productId = productsOrderRequest.get(i).getProductId();
                int quantity = productsOrderRequest.get(i).getQuantity();
                Double price = productPrices.get(i);

                OrderItem orderItem = new OrderItem(orderId, productId, quantity, price); // create a new orderItem
                orderItem = orderItemRepository.save(orderItem); // save the orderItem
                orderItems.add(orderItem);

                totalPrice += orderItem.getTotalPrice(); // fetch the total price and add to the totalPrice
            }

            // create order in razorPay
            JSONObject razorpayOrderRequest = new JSONObject();
            razorpayOrderRequest.put("amount", totalPrice * 100); // amount in paisa
            razorpayOrderRequest.put("currency", "INR");
            razorpayOrderRequest.put("receipt", reference);

            // create razorpay Client instance
            razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

            // create razorpay Order
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(razorpayOrderRequest);

            // fetch razorpay id, status and total price and set to product order
            order.setTotalAmount(totalPrice);
            order.setRazorpayId(razorpayOrder.get("id"));
            order.setStatus(razorpayOrder.get("status"));
            order = orderRepository.save(order); // update order to DataBase

            return new OrderPaymentRequest(order, razorpayKey); // return OrderResponse object
        }
        // Delete order from DataBase any issue occurred
        catch (Exception e) {

            for (OrderItem orderItem : orderItems) {
                orderItemRepository.delete(orderItem); // delete the orderItems from DataBase
            }
            orderRepository.delete(order); // delete the order from DataBase

            throw new UnknownErrorException("Error: error creating Order {" + e.getMessage() + "}");
        }
    }

    @Override
    public void handlePaymentCallback(PaymentCallbackRequest request) {
        try {
            // fetch callback information
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpay_order_id());
            options.put("razorpay_payment_id", request.getRazorpay_payment_id());
            options.put("razorpay_signature", request.getRazorpay_signature());

            // verify from razorpay Api
            boolean status = Utils.verifyPaymentSignature(options, razorpaySecret);

            if (status) {
                Optional<Order> optionalOrder = orderRepository.findByRazorpayId(request.getRazorpay_order_id());

                // update status / payment id to database
                if (optionalOrder.isPresent()) {
                    Order order = optionalOrder.get();

                    order.setStatus("paid");
                    order.setRazorpayPaymentId(request.getRazorpay_payment_id());

                    orderRepository.save(order); // update order status to paid in DataBase
                } else {
                    throw new OrderNotFoundException("Error: order not found");
                }
            } else {
                throw new UnAuthorizedPaymentCallbackException("Error: unauthorized payment callback");
            }
        } catch (RazorpayException e) {
            throw new UnknownErrorException("Error: error creating Razorpay client");
        }
    }

    @Override
    public List<OrderViewModel> fetchOrders(String reference) {
        int userId = userService.findUserIdByReference(reference);

        List<OrderViewModel> orderViewModels = new ArrayList<OrderViewModel>();
        List<Order> orders = orderRepository.findByUserId(userId);
        Date currentDate = new Date();

        for (Order order : orders) {
            // create order Expiry data/time
            Date orderExpiry = new Date(order.getOrderDate().getTime() + EXPIRATION_TIME_LIMIT);
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());

            // Check for payments are completed or not completed
            if (order.getStatus().equals("created")) {
                order = checkRazorpayStatus(order);
                // continue for orders which are not created yet
                if (order == null) {
                    continue;
                }
            }

            // check if the order is not expired
            if (orderExpiry.after(currentDate) || !order.getStatus().equals("created")) {

                List<ProductOrderResponse> products = new ArrayList<ProductOrderResponse>();

                for (OrderItem orderItem : orderItems) {
                    Product product = productService.findById(orderItem.getProductId());
                    products.add(new ProductOrderResponse(product, orderItem.getQuantity()));
                }

                orderViewModels.add(new OrderViewModel(order, products));
            }
            // delete expired order and order items
            else {
                for (OrderItem orderItem : orderItems) {
                    orderItemRepository.delete(orderItem);
                }
                orderRepository.delete(order);
            }
        }

        return orderViewModels;
    }

    public Order checkRazorpayStatus(Order order) {
        try {
            razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

            String orderId = order.getRazorpayId();
            // for orders which are not created yet
            if (orderId == null) {
                return null;
            }

            List<Payment> payments = razorpayClient.orders.fetchPayments(orderId);

            for (Payment payment : payments) {

                Boolean captured = payment.get("captured");
                String paymentId = payment.get("id");

                // update status and payment id if captured
                if (captured) {
                    order.setStatus("paid");
                    order.setRazorpayPaymentId(paymentId);
                    return orderRepository.save(order);
                }
            }

            return order;
        } catch (RazorpayException e) {
            throw new UnknownErrorException("Error: error creating Razorpay client");
        }
    }
}
