package com.shenke.controller;

import com.shenke.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
    public Map<String, Object> selectByMedicalCardNumber(String medicalCardNumber) {
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

}
