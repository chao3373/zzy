package com.shenke.service;

import com.shenke.Entity.BanKa;
import com.shenke.repository.BanKaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("banKaService")
public class BanKaServiceImpl implements BanKaService {

    @Resource
    private BanKaRepository banKaRepository;

    @Override
    public void save(BanKa banKa) {
        banKaRepository.save(banKa);
    }
}
