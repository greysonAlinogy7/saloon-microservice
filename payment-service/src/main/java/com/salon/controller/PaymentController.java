package com.salon.controller;

import com.salon.domain.PaymentMethod;
import com.salon.entity.PaymentOrder;
import com.salon.payload.dto.BookingDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.payload.response.PaymentLinkResponse;
import com.salon.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private PaymentService paymentService;


    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking, @RequestParam PaymentMethod paymentMethod){
        UserDTO user = new UserDTO();
        user.setFullName("greyson");
        user.setEmail("greysonshawa@gmail.com");
        user.setId(1L);

        PaymentLinkResponse response = paymentService.createOrder(user, booking, paymentMethod);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentOrder> getPaymentById(@PathVariable Long paymentId) throws Exception {
        PaymentOrder response = paymentService.getPaymentOrderById(paymentId);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> processPayment(@RequestParam Long paymentId, @RequestParam String paymentLinkId) throws Exception {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentId);
        Boolean response = paymentService.proceedPayment(paymentOrder,  paymentId, paymentLinkId);
        return ResponseEntity.ok(response);

    }



}
