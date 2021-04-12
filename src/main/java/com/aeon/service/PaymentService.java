package com.aeon.service;

import com.aeon.domain.Payment;
import com.aeon.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void doPayment(String username) {
        Payment payment = paymentRepository.save(new Payment());
        userService.doPayment(username, payment);
    }
}
