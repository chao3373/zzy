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

    Map<String, Object> selectBalance(String medicalCardNumber);

    Map<String, Object> selectMessage(String medicalCardNumber, String admissionNumber);

    Map<String, Object> selectArrearageUserMessage(Double prePay);

    Map<String, Object> selectAllInHospital();

    Map<String, Object> selectOneDay(String medicalCardNumber, String date, String admissionNumber);

    Map<String, Object> paymentSuccess(String medicalCardNumber, String outTradeNo, String amount, String payTime, String payType, String detailId);
}
