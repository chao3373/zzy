package com.shenke.service;

import com.shenke.Entity.BangKa;
import com.shenke.repository.BangKaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("bangKaService")
public class BangKaServiceImpl implements BangKaService {

    @Resource
    private BangKaRepository bangKaRepository;

    @Override
    public void save(BangKa bangKa) {
        bangKaRepository.save(bangKa);
    }
}
