package com.shenke.service;

import com.shenke.Entity.CardRecharge;
import com.shenke.repository.CardRechargeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("cardRechargeService")
public class CardRechargeServiceImpl implements CardRechargeService {

    @Resource
    private CardRechargeRepository cardRechargeRepository;

    @Override
    public void save(CardRecharge cardRecharge) {
        cardRechargeRepository.save(cardRecharge);
    }
}
