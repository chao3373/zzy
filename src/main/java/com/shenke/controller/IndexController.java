package com.shenke.controller;

import com.shenke.service.IndexService;
import com.shenke.util.GetRequestBodyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     * @return
     */
    @RequestMapping("/insertCard")
    public Map<String, Object> insertCard(HttpServletRequest request) throws IOException {
        List<String> strs = new ArrayList<>();
        strs.add("medicalCardNumber");
        strs.add("name");
        strs.add("sex");
        strs.add("birthday");
        strs.add("address");
        strs.add("idcard");
        strs.add("phone");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, strs);
        Map<String, Object> map = indexService.insertCard(stringg.get(0), stringg.get(1),stringg.get(2), stringg.get(3), stringg.get(4), stringg.get(5), stringg.get(6));
        if (map != null) {
            return map;
        }
        return null;
    }

    /***
     *根据就诊卡号查询用户信息
     * @param
     * @return
     */
    @RequestMapping("/selectByMedicalCardNumber")
    public Map<String, Object> selectByMedicalCardNumber(HttpServletRequest request) {
        return indexService.selectByMedicalCardNumber(GetRequestBodyUtil.getString(request, "medicalCardNumber"));
    }

    /***
     *推送电子就诊卡信息
     * @return
     */
    @RequestMapping("/updateMedicalCardNumber")
    public Map<String, Object> updateMedicalCardNumber(HttpServletRequest request) {
        List<String> strings = new ArrayList<>();
        strings.add("name");
        strings.add("oldMedicalCardNumber");
        strings.add("newMedicalCardNumber");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, strings);
        return indexService.updateMedicalCardNumber(stringg.get(0), stringg.get(1), stringg.get(2));
    }

    /***
     * 待缴费信息查询接口
     * @param
     * @return
     */
    @RequestMapping("/selectPaymentInformation")
    public Map<String, Object> selectPaymentInformation(HttpServletRequest request) {
        return indexService.selectPaymentInformation(GetRequestBodyUtil.getString(request, "medicalCardNumber"));
    }

    /***
     * 查询就诊卡余额
     * @param
     * @return
     */
    @RequestMapping("/selectBalance")
    public Map<String, Object> selectBalance(HttpServletRequest request) {
        return indexService.selectBalance(GetRequestBodyUtil.getString(request, "medicalCardNumber"));
    }

    /***
     * 查询住院信息
     * @param
     * @param
     * @return
     */
    @RequestMapping("/selectMessage")
    public Map<String, Object> selectMessage(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("admissionNumber");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.selectMessage(stringg.get(0), stringg.get(1));
    }

    /***
     * 获取住院押金欠费用户信息
     * @param
     * @return
     */
    @RequestMapping("/selectArrearageUserMessage")
    public Map<String, Object> selectArrearageUserMessage(HttpServletRequest request) {
        return indexService.selectArrearageUserMessage(Double.parseDouble(GetRequestBodyUtil.getString(request, "prePay")));
    }

    /***
     * 查询所有住院状态的电子健康卡用户信息
     * @return
     */
    @RequestMapping("/selectAllInHospital")
    public Map<String, Object> selectAllInHospital(){
        return indexService.selectAllInHospital();
    }

    /***
     * 查询一日住院费用明细
     * @param
     * @param
     * @param
     * @return
     */
    @RequestMapping("/selectOneDay")
    public Map<String, Object> selectOneDay(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("admissionNumber");
        list.add("date");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.selectOneDay(stringg.get(0), stringg.get(1), stringg.get(2));
    }

    /***
     * 用户支付成功后，公众号系统调用此接口将支付成功信息通知院内系统
     * @return
     */
    @RequestMapping("/paymentSuccess")
    public Map<String, Object> paymentSuccess(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("outTradeNo");
        list.add("amount");
        list.add("payTime");
        list.add("payType");
        list.add("detailId");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.paymentSuccess(stringg.get(0), stringg.get(1), stringg.get(2), stringg.get(3), stringg.get(4), stringg.get(5));
    }

    /***
     * 就诊卡充值
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    @RequestMapping("/cardRecharge")
    public Map<String, Object> cardRecharge(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("outTradeNo");
        list.add("payAmount");
        list.add("payType");
        list.add("payTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.cardRecharge(stringg.get(0), stringg.get(1), stringg.get(2), stringg.get(3), stringg.get(4));
    }

    /***
     * 缴纳住院押金
     * @param/insertCard
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    @RequestMapping("/paymentPledge")
    public Map<String, Object> paymentPledge(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("admissionNumber");
        list.add("amount");
        list.add("outTradeNo");
        list.add("payType");
        list.add("payTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.paymentPledge(stringg.get(0), stringg.get(1), stringg.get(2), stringg.get(3), stringg.get(4), stringg.get(5));
    }
}
