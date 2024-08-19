package com.nothing.stella.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nothing.stella.entity.Address;
import com.nothing.stella.entity.Cart;
import com.nothing.stella.entity.CartItem;
import com.nothing.stella.entity.Order;
import com.nothing.stella.entity.OrderItem;
import com.nothing.stella.entity.Product;
import com.nothing.stella.entity.User;
import com.nothing.stella.exception.AddressNotFoundException;
import com.nothing.stella.exception.InvalidCartIdException;
import com.nothing.stella.exception.InvalidOrderIdException;
import com.nothing.stella.exception.InvalidOrderItemIdException;
import com.nothing.stella.exception.InvalidProductIdException;
import com.nothing.stella.exception.InvalidProductQuantityException;
import com.nothing.stella.exception.InvalidUpdateOrderItemRequestException;
import com.nothing.stella.exception.OrderNotFoundException;
import com.nothing.stella.exception.UnAuthorizedPaymentCallbackException;
import com.nothing.stella.exception.UnAuthorizedUserException;
import com.nothing.stella.exception.UnknownErrorException;
import com.nothing.stella.exception.UserIdNotFoundException;
import com.nothing.stella.exception.UserNotFoundException;
import com.nothing.stella.miscellaneous.EmailTemplate;
import com.nothing.stella.model.OrderRequest;
import com.nothing.stella.model.OrderViewModel;
import com.nothing.stella.model.PaymentCallbackRequest;
import com.nothing.stella.model.ProductIdAndNameModel;
import com.nothing.stella.model.OrderPaymentRequest;
import com.nothing.stella.model.ProductOrderRequest;
import com.nothing.stella.model.ProductOrderViewModel;
import com.nothing.stella.model.SellerOrderViewModel;
import com.nothing.stella.repository.OrderItemRepository;
import com.nothing.stella.repository.OrderRepository;
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
    @Autowired
    private EmailService emailService;
    @Autowired
    private CartService cartService;

    private RazorpayClient razorpayClient;
    private static final long EXPIRATION_TIME_LIMIT = 30 * 60 * 1000; // 30 minutes

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.secret.key}")
    private String razorpaySecret;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    // --------------------------------------------------------
    // Functions for Buyer
    // --------------------------------------------------------
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
        for (ProductOrderRequest productRequest : productsOrderRequest) {
            if (productRequest.getQuantity() >= 1) {
                int productId = productRequest.getProductId();
                Double price = productService.findPriceById(productId);
                if (price == 0.0) {
                    throw new InvalidProductIdException("Error: Product not found with id " + productId);
                } else {
                    productPrices.add(price);
                }
            } else {
                throw new InvalidProductQuantityException("Error: Quantity must be one or more");
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
    public OrderPaymentRequest create(String reference, int addressId, int cartId) {

        int userId = userService.findUserIdByReference(reference);

        // verity user Address
        Address address = addressService.findById(addressId);
        if (address == null) {
            throw new AddressNotFoundException("Error: Address not found by id " + addressId);
        } else if (userId != address.getUserId()) {
            throw new UnAuthorizedUserException("Error: accress denied, you are not allowed to access this address");
        }

        Cart cart = cartService.getCart(cartId);
        List<CartItem> cartItems = cartService.getCartItems(cartId);

        // Verify cart
        if (cart == null) {
            throw new InvalidCartIdException("Error: cart not found");
        } else if (cart.getUserId() != userId) {
            throw new InvalidCartIdException("Error: you are not allowed to access this cart");
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
            for (CartItem cartItem : cartItems) {
                int productId = cartItem.getProductId();
                int quantity = cartItem.getQuantity();
                Double price = cartItem.getPrice();

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

                    // Send email to the customer
                    emailSender(order.getUserId(), fetchOrder(order.getOrderId()));
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

                List<ProductOrderViewModel> products = new ArrayList<ProductOrderViewModel>();

                for (OrderItem orderItem : orderItems) {
                    Product product = productService.findById(orderItem.getProductId());
                    products.add(new ProductOrderViewModel(product, orderItem.getQuantity()));
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

    @Override
    public OrderViewModel fetchOrder(int orderId) {
        Optional<Order> optinalOrder = orderRepository.findById(orderId);

        if (optinalOrder.isPresent()) {
            Order order = optinalOrder.get();

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
            List<ProductOrderViewModel> products = new ArrayList<ProductOrderViewModel>();

            for (OrderItem orderItem : orderItems) {
                Product product = productService.findById(orderItem.getProductId());
                products.add(new ProductOrderViewModel(product, orderItem.getQuantity()));
            }

            return new OrderViewModel(order, products);
        } else {
            throw new InvalidOrderIdException("Error: Order with id #" + orderId + " not found");
        }
    }

    @Override
    public String fetchOrderStatus(int orderId) {
        return orderRepository.findStatusByOrderId(orderId).orElse("Error: Invalid Order Id");
    }

    @Override
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

                    // Send email to the customer
                    emailSender(order.getUserId(), fetchOrder(order.getOrderId()));

                    return orderRepository.save(order);
                }
            }

            return order;
        } catch (RazorpayException e) {
            throw new UnknownErrorException("Error: error creating Razorpay client");
        }
    }

    @Override
    public void emailSender(int userId, OrderViewModel orderViewModel) {

        User user = userService.findById(userId);

        if (user != null) {

            String companyName = "Stella";
            String productDetails = productDetailsConverter(orderViewModel.getProducts());
            String trackingLink = "http://localhost:8080/api/orders/track?order_id=";
            String email = user.getEmail();
            String emailSubject = "Order Purchase Details";

            String orderTemplate = EmailTemplate.EMAIL_ORDER_TEMPLATE;

            String formatedMessage = String.format(
                    orderTemplate, orderViewModel.getOrderId(),
                    user.getName(), companyName,
                    orderViewModel.getOrderDate(), orderViewModel.getTotalAmount(),
                    orderViewModel.getStatus(), orderViewModel.getShippingAddress(), productDetails,
                    trackingLink + orderViewModel.getOrderId(),
                    companyName);

            try {

                emailService.sendEmail(email, emailSubject, formatedMessage);

            } catch (Exception e) {

                logger.error("Error sending email: " + e.getMessage(), e);

            }
        } else {
            throw new UserNotFoundException("Error: User not found");
        }
    }

    String productDetailsConverter(List<ProductOrderViewModel> products) {
        StringBuilder productDetailBuilder = new StringBuilder();

        productDetailBuilder.append("Product Name")
                .append("\t")
                .append("Price")
                .append("\t")
                .append("Quantity")
                .append("\n");

        for (ProductOrderViewModel product : products) {
            productDetailBuilder.append(product.getName())
                    .append("\t")
                    .append(product.getPrice())
                    .append("\t")
                    .append(product.getQuantity())
                    .append("\n");
        }

        return productDetailBuilder.toString();
    }

    // ----------------------------------------------------------------
    // Functions for SellerDashBoard
    // ----------------------------------------------------------------
    @Override
    public List<SellerOrderViewModel> fetchAllOrders(int sellerId) {
        // fetch product name and ID from the database
        List<ProductIdAndNameModel> products = productService.findProductIdAndNameByUserId(sellerId);

        List<SellerOrderViewModel> sellerOrders = new ArrayList<SellerOrderViewModel>();

        for (ProductIdAndNameModel product : products) {
            // fetch Order Items from the database
            List<OrderItem> orderItems = orderItemRepository.findByProductId(product.getProductId());

            // fetch date for all items seperatly and add to sellerOrders list
            for (OrderItem orderItem : orderItems) {
                Optional<Date> optionalDate = orderRepository
                        .findPaidOrderDateByOrderId(orderItem.getOrderId());

                if (optionalDate.isPresent()) {
                    Date orderDate = optionalDate.get();

                    SellerOrderViewModel order = new SellerOrderViewModel(orderItem, product.getName(), orderDate);

                    sellerOrders.add(order); // add order to the sellerOrders List
                }
            }
        }

        return sellerOrders;
    }

    @Override
    public Boolean verifySellerAccessByOrderItemId(int sellerId, int orderItemId) {
        // fetch PoductID by orderItemId
        Optional<Integer> optionalProductId = orderItemRepository.findProductIdByOrderItemId(orderItemId);

        if (optionalProductId.isPresent()) {
            int productId = optionalProductId.get();

            // fetch seller ID from product ID
            int fetchedSellerId = productService.findUserIdByProductId(productId);

            if (fetchedSellerId != 0) {

                // Compare fetched seller ID with the given seller ID
                return sellerId == fetchedSellerId;

            } else {
                throw new UserIdNotFoundException("Error: Seller ID not found for this product");
            }

        } else {
            throw new InvalidOrderItemIdException("Error: order item ID is not valid");
        }
    }

    @Override
    public void updateOrderItemStatusToAccepted(int orderItemId) {
        // fetch OrderItem by orderItemId
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();

            if (orderItem.getStatus().equals("waiting")) {
                // update status to accepted
                orderItem.setStatus("accepted");

                orderItemRepository.save(orderItem);
            } else {
                throw new InvalidUpdateOrderItemRequestException("Error: faulty request, Access Denied!");
            }
        } else {
            throw new InvalidOrderItemIdException("Error: Order Item Id is invalid");
        }
    }

    @Override
    public void updateOrderItemStatusToCanceled(int orderItemId) {
        // fetch OrderItem by orderItemId
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();

            if (orderItem.getStatus().equals("waiting")) {
                Optional<Order> optionalOrder = orderRepository.findById(orderItem.getOrderId());

                if (optionalOrder.isPresent()) {
                    Order order = optionalOrder.get();

                    Double totalAmount = order.getTotalAmount();
                    totalAmount = totalAmount - orderItem.getTotalPrice();

                    // update total amount
                    order.setTotalAmount(totalAmount);
                    // update order status to cancelled if total amount is zero
                    if (totalAmount == 0) {
                        order.setStatus("canceled");
                    }
                    orderRepository.save(order);

                    // update status to accepted
                    orderItem.setStatus("canceled");
                    orderItemRepository.save(orderItem);

                } else {
                    throw new OrderNotFoundException("Error: Order not found for this Order Item");
                }
            } else {
                throw new InvalidUpdateOrderItemRequestException("Error: faulty request, Access Denied!");
            }
        } else {
            throw new InvalidOrderItemIdException("Error: Order Item Id is invalid");
        }
    }

    @Override
    public void updateOrderItemStatusToShipped(int orderItemId) {
        // fetch OrderItem by orderItemId
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();

            if (orderItem.getStatus().equals("accepted")) {
                // update status to accepted
                orderItem.setStatus("shipped");

                orderItemRepository.save(orderItem);

            } else {
                throw new InvalidUpdateOrderItemRequestException("Error: faulty request, Access Denied!");
            }
        } else {
            throw new InvalidOrderItemIdException("Error: Order Item Id is invalid");
        }
    }

    // ----------------------------------------------------------------
    // Functions for Review And Rating
    // ----------------------------------------------------------------
    @Override
    public Boolean isApplicableForReview(int userId, int productId) {
        List<Integer> orderIds = orderRepository.findOrderIdByUserId(userId);

        for (int orderId : orderIds) {
            Boolean isApplicable = orderItemRepository.existsByOrderIdAndProductId(orderId, productId);

            if (isApplicable) {
                return true;
            }
        }

        return false;
    }
}
