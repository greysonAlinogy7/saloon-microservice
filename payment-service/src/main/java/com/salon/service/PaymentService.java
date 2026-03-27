package com.salon.service;

import com.salon.domain.PaymentMethod;
import com.salon.domain.PaymentOrderStatus;
import com.salon.entity.PaymentOrder;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.response.PaymentLinkResponse;
import com.salon.repository.PaymentRepository;
import com.salon.service.impl.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private  final PaymentRepository paymentRepository;

    @Value("${razorpay.api.key:}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret:}")
    private String razorpaySecret;

    @Value("${stripe.api.key:}")
    private String stripeSecreteKey;

    public boolean isRazorpayEnabled() {
        return razorpayApiKey != null && !razorpayApiKey.isEmpty()
                && razorpaySecret != null && !razorpaySecret.isEmpty();
    }

    public boolean isStripeEnabled() {
        return stripeSecreteKey != null && !stripeSecreteKey.isEmpty();
    }

    @Override
    public PaymentLinkResponse createOrder(UserDTO user, BookingDTO booking, PaymentMethod paymentMethod) {
        Long amount =(long) booking.getTotalPrice();
        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
        order.setBookingId(booking.getId());
        order.setSalonId(booking.getSalonId());
        PaymentOrder savedOrder = paymentRepository.save(order);
        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.NMB)) {

            if (!isRazorpayEnabled()) {
                String dummyUrl = "http://localhost:3000/payment-success?orderId=" + savedOrder.getId();

                paymentLinkResponse.setPayment_link_url(dummyUrl);
                paymentLinkResponse.setGetPayment_link_id("dummy_razorpay_" + savedOrder.getId());

                savedOrder.setPaymentLinkId(dummyUrl);
                paymentRepository.save(savedOrder);

            }

        } else {

            if (!isStripeEnabled()) {
                // 🔥 Dummy Stripe flow
                String dummyUrl = "http://localhost:3000/payment-success?orderId=" + savedOrder.getId();

                paymentLinkResponse.setPayment_link_url(dummyUrl);

            } else {
                // ✅ Real Stripe
                String paymentUrl = createStripePaymentLink(
                        user,
                        savedOrder.getAmount(),
                        savedOrder.getId()
                );

                paymentLinkResponse.setPayment_link_url(paymentUrl);
            }
        }
        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentRepository.findById(id).orElse(null);
        if (paymentOrder==null){
            throw  new Exception("payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLinkResponse createRazorpayPaymentLink(UserDTO user, Long Amount, Long orderId) {
        Long amount = Amount*100;



        return null;
    }

    @Override
    public String createStripePaymentLink(UserDTO user, Long amount, Long orderId) {
        return "";
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, Long paymentId, String paymentLinkId) {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.NMB)){
                PaymentOrder paymentOrder1 = new PaymentOrder();
                paymentOrder1.setStatus(PaymentOrderStatus.SUCCESS);
                paymentRepository.save(paymentOrder1);

            }
        }
        return true;
    }
}
