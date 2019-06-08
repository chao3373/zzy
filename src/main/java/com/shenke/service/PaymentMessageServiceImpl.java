package com.shenke.service;

import com.shenke.Entity.PaymentMessage;
import com.shenke.repository.PaymentMessageRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("paymentMessageService")
public class PaymentMessageServiceImpl implements PaymentMessageService {

    @Resource
    private PaymentMessageRepository paymentMessageRepository;
    @Override
    public void save(PaymentMessage paymentMessage) {
        paymentMessageRepository.save(paymentMessage);
    }
}
