package com.shenke.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    Map<String, Object> cardRecharge(String medicalCardNumber, String outTradeNo, String payAmount, String payType, String payTime);

    Map<String, Object> paymentPledge(String medicalCardNumber, String admissionNumber, String amount, String outTradeNo, String payType, String payTime);


    Map<String, Object> queryHospitalizationRecord(String medicalCardNumber, String s1, String s2, String s3);

    Map<String, Object> queryOutpatientCostDetail(String medicalCardNumber, String startTime, String endTime);

    Map<String, Object> getSeeklingDocList(String medicalCardNumber, String beginTime, String endTime);

    Map<String, Object> getOpenDrugList();

    Map<String, Object> getOpenLISList();

    Map<String, Object> getOpenCheckList();

    Map<String, Object> getOpenDocItem();

    Map<String, Object> baogaoMain(List<String> stringg);

    Map<String, Object> baogaoMingXi(List<String> stringg);

    Map<String, Object> tongji(List<String> stringg);

    Map<String, Object> queryHospitalizationDetail(List<String> stringg);
}
