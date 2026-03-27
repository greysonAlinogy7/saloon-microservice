package com.salon.service.impl;

import com.salon.domain.PaymentMethod;
import com.salon.entity.PaymentOrder;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.response.PaymentLinkResponse;

public interface IPaymentService {
    PaymentLinkResponse createOrder(UserDTO user, BookingDTO bookingDTO, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String paymentId);
    PaymentLinkResponse createRazorpayPaymentLink(UserDTO user, Long amount, Long orderId);
    String createStripePaymentLink(UserDTO user, Long amount, Long orderId);
    Boolean proceedPayment(PaymentOrder paymentOrder, Long paymentId, String paymentLinkId);




}
