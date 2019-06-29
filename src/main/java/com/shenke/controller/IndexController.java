package com.shenke.controller;

import com.shenke.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/6/3 10:47
 * @Description:
 */
@ResponseBody
@Controller
public class IndexController {

    @Resource
    private IndexService indexService;

    /***
     *办理电子健康卡
     * @param medicalCardNumber
     * @param name
     * @param sex
     * @param birthday
     * @param address
     * @param idcard
     * @param phone
     * @return
     */
    @RequestMapping("/insertCard")
    public Map<String, Object> insertCard(String medicalCardNumber, String name, String sex, String birthday, String address, String idcard, String phone) {
        Map<String, Object> map = indexService.insertCard(medicalCardNumber, name, sex, birthday, address, idcard, phone);
        System.out.println(map);
        if (map != null) {
            return map;
        }
        return null;
    }

    /***
     *根据就诊卡号查询用户信息
     * @param medicalCardNumber
     * @return
     */
    @RequestMapping("/selectByMedicalCardNumber")
    public Map<String, Object> selectByMedicalCardNumber(HttpServletRequest request, String medicalCardNumber) {
        System.out.println("请求参数：" + request.getQueryString());
        System.out.println("==========");
        System.out.println(medicalCardNumber);
        return indexService.selectByMedicalCardNumber(medicalCardNumber);
    }

    /***
     *推送电子就诊卡信息
     * @return
     */
    @RequestMapping("/updateMedicalCardNumber")
    public Map<String, Object> updateMedicalCardNumber(String name, String oldMedicalCardNumber, String newMedicalCardNumber) {
        return indexService.updateMedicalCardNumber(name, oldMedicalCardNumber, newMedicalCardNumber);
    }

    /***
     * 待缴费信息查询接口
     * @param medicalCardNumber
     * @return
     */
    @RequestMapping("/selectPaymentInformation")
    public Map<String, Object> selectPaymentInformation(String medicalCardNumber) {
        return indexService.selectPaymentInformation(medicalCardNumber);
    }

    /***
     * 查询就诊卡余额
     * @param medicalCardNumber
     * @return
     */
    @RequestMapping("/selectBalance")
    public Map<String, Object> selectBalance(String medicalCardNumber) {
        System.out.println(medicalCardNumber);
        return indexService.selectBalance(medicalCardNumber);
    }

    /***
     * 查询住院信息
     * @param medicalCardNumber
     * @param admissionNumber
     * @return
     */
    @RequestMapping("/selectMessage")
    public Map<String, Object> selectMessage(String medicalCardNumber, String admissionNumber) {
        return indexService.selectMessage(medicalCardNumber, admissionNumber);
    }

    /***
     * 获取住院押金欠费用户信息
     * @param prePay
     * @return
     */
    @RequestMapping("/selectArrearageUserMessage")
    public Map<String, Object> selectArrearageUserMessage(Double prePay) {
        return indexService.selectArrearageUserMessage(prePay);
    }

    /***
     * 查询所有住院状态的电子健康卡用户信息
     * @return
     */
    @RequestMapping("/selectAllInHospital")
    public Map<String, Object> selectAllInHospital() {
        return indexService.selectAllInHospital();
    }

    /***
     * 查询一日住院费用明细
     * @param medicalCardNumber
     * @param date
     * @param admissionNumber
     * @return
     */
    @RequestMapping("/selectOneDay")
    public Map<String, Object> selectOneDay(String medicalCardNumber, String date, String admissionNumber) {

        return indexService.selectOneDay(medicalCardNumber, date, admissionNumber);
    }

    /***
     * 用户支付成功后，公众号系统调用此接口将支付成功信息通知院内系统
     * @return
     */
    @RequestMapping("/paymentSuccess")
    public Map<String, Object> paymentSuccess(String medicalCardNumber, String outTradeNo, String amount, String payTime, String payType, String detailId) {
        return indexService.paymentSuccess(medicalCardNumber, outTradeNo, amount, payTime, payType, detailId);
    }

    /***
     * 就诊卡充值
     * @param medicalCardNumber
     * @param outTradeNo
     * @param payAmount
     * @param payType
     * @param payTime
     * @return
     */
    @RequestMapping("/cardRecharge")
    public Map<String, Object> cardRecharge(String medicalCardNumber, String outTradeNo, String payAmount, String payType, String payTime) {
        return indexService.cardRecharge(medicalCardNumber, outTradeNo, payAmount, payType, payTime);
    }

    /***
     * 缴纳住院押金
     * @param medicalCardNumber
     * @param admissionNumber
     * @param amount
     * @param outTradeNo
     * @param payType
     * @param payTime
     * @return
     */
    @RequestMapping("/paymentPledge")
    public Map<String, Object> paymentPledge(String medicalCardNumber, String admissionNumber, String amount, String outTradeNo, String payType, String payTime) {
        return indexService.paymentPledge(medicalCardNumber, admissionNumber, amount, outTradeNo, payType, payTime);
    }
}
