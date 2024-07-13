package com.nothing.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.entity.Order;
import com.nothing.ecommerce.entity.OrderItem;
import com.nothing.ecommerce.exception.AddressNotFoundException;
import com.nothing.ecommerce.exception.InvalidProductIdException;
import com.nothing.ecommerce.exception.UnAuthorizedUserException;
import com.nothing.ecommerce.exception.UnknownErrorException;
import com.nothing.ecommerce.model.OrderRequest;
import com.nothing.ecommerce.model.OrderResponse;
import com.nothing.ecommerce.model.ProductOrderRequest;
import com.nothing.ecommerce.repository.OrderItemRepository;
import com.nothing.ecommerce.repository.OrderRepository;
import com.razorpay.*;

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

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.secret.key}")
    private String razorpaySecret;

    @Override
    public OrderResponse create(String reference, OrderRequest orderRequest) {
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

            return new OrderResponse(order); // return OrderResponse object

        } catch (Exception e) {

            for (OrderItem orderItem : orderItems) {
                orderItemRepository.delete(orderItem); // delete the orderItems from DataBase
            }
            orderRepository.delete(order); // delete the order from DataBase

            if (e.getClass() == RazorpayException.class) {
                e.printStackTrace();
                throw new UnknownErrorException("Error: error creating Razorpay client");
            }
            throw new UnknownErrorException("Error: error creating Order");
        }
    }

}
