package com.sportaccess.backend.service;

import com.sportaccess.backend.model.Booking;
import com.sportaccess.backend.model.Payment;
import com.sportaccess.backend.model.Payment.PaymentStatus;
import com.sportaccess.backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment simulatePayment(Booking booking, Double amount) {
        // Here we would normally interact with Stripe SDK
        // PaymentIntent intent = PaymentIntent.create(params);
        
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .paymentDate(LocalDateTime.now())
                .status(PaymentStatus.SUCCEEDED)
                .stripePaymentIntentId("pi_simulated_" + System.currentTimeMillis())
                .build();
        
        return paymentRepository.save(payment);
    }
}
