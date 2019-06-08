package com.shenke.service;

import com.shenke.Entity.PaymentPledge;
import com.shenke.repository.PaymentPledgeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("paymentPledgeService")
public class PaymentPledgeServiceImpl implements PaymentPledgeService{

    @Resource
    private PaymentPledgeRepository paymentPledgeRepository;
    @Override
    public void save(PaymentPledge paymentPledge) {
        paymentPledgeRepository.save(paymentPledge);
    }
}
