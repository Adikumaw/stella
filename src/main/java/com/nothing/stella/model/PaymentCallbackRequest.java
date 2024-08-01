package com.nothing.stella.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCallbackRequest {
    String razorpay_payment_id;
    String razorpay_order_id;
    String razorpay_signature;
}
