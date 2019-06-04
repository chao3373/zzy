package com.shenke.service;

import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/6/3 11:52
 * @Description:
 */
public interface IndexService {

    public Map<String, Object> insertCard(String medicalCardNumber, String name, String sex, String birthday, String address, String idcard, String phone);

    Map<String, Object> selectByMedicalCardNumber(String medicalCardNumber);

    Map<String, Object> updateMedicalCardNumber(String name, String oldMedicalCardNumber, String newMedicalCardNumber);

    Map<String, Object> selectPaymentInformation(String medicalCardNumber);

}
