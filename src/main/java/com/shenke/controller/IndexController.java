package com.shenke.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.shenke.Entity.Charges;
import com.shenke.Entity.HuanZhe;
import com.shenke.repository.ChargesRepository;
import com.shenke.repository.HuanZheRepository;
import com.shenke.service.IndexService;
import com.shenke.util.GetRequestBodyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Resource
    private HuanZheRepository huanZheRepository;

    @Resource
    private ChargesRepository chargesRepository;

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
        Map<String, Object> map = indexService.insertCard(stringg.get(0), stringg.get(1), stringg.get(2), stringg.get(3), stringg.get(4), stringg.get(5), stringg.get(6));
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
    public Map<String, Object> selectAllInHospital() {
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

    /***
     * 住院记录查询
     * @param request
     * @return
     */
    @RequestMapping("/queryHospitalizationRecord")
    public Map<String, Object> queryHospitalizationRecord(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("admissionNumber");
        list.add("startTime");
        list.add("endTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.queryHospitalizationRecord(stringg.get(0), stringg.get(1), stringg.get(2), stringg.get(3));
    }

    /***
     * 门诊费用查询
     * @param request
     * @return
     */
    @RequestMapping("/queryOutpatientCostDetail")
    public Map<String, Object> queryOutpatientCostDetail(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("startTime");
        list.add("endTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.queryOutpatientCostDetail(stringg.get(0), stringg.get(1), stringg.get(2));
    }

    /***
     * 查询患者就诊过的医生列表信息
     */
    @RequestMapping("/getSeeklingDocList")
    public Map<String, Object> getSeeklingDocList(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("beginTime");
        list.add("endTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.getSeeklingDocList(stringg.get(0), stringg.get(1), stringg.get(2));
    }

    /***
     * 查询医院开放的药品销售目录
     * @param request
     * @return
     */
    @RequestMapping("/getOpenDrugList")
    public Map<String, Object> getOpenDrugList(HttpServletRequest request) {
        return indexService.getOpenDrugList();
    }

    /***
     * 查询医院开放的检验销售目录
     * @param request
     * @return
     */
    @RequestMapping("/getOpenLISList")
    public Map<String, Object> getOpenLISList(HttpServletRequest request) {
        return indexService.getOpenLISList();
    }

    /***
     * 查询医院开放的检查销售目录
     * @param request
     * @return
     */
    @RequestMapping("/getOpenCheckList")
    public Map<String, Object> getOpenCheckList(HttpServletRequest request) {
        return indexService.getOpenCheckList();
    }

    /***
     * 获取药品、检查、检验项目列表
     * @param request
     * @return
     */
    @RequestMapping("/getOpenDocItem")
    public Map<String, Object> getOpenDocItem(HttpServletRequest request) {
        return indexService.getOpenDocItem();
    }

    /***
     * 同步患者支付信息
     * @param request
     * @return
     */
    @RequestMapping("/pushPaintPayInfo")
    public Map<String, Object> pushPaintPayInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("patName");
        list.add("general");
        list.add("phone");
        list.add("idCard");
        list.add("birthday");
        list.add("diagnoseName");
        list.add("tradeNo");
        list.add("tradeTime");
        list.add("billDocHospital");
        list.add("billDocName");
        list.add("billDocCode");
        list.add("medicalCardNumber");
        list.add("total");
        list.add("charges");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        String charges = stringg.get(stringg.size() - 1);
        System.out.println(charges);
        JSONArray array = JSONUtil.parseArray(charges);

        HuanZhe huanZhe = new HuanZhe();
        huanZhe.setPatName(list.get(0));
        huanZhe.setGeneral(list.get(1));
        huanZhe.setPhone(list.get(2));
        huanZhe.setIdCard(list.get(3));
        huanZhe.setBirthday(list.get(4));
        huanZhe.setDiagnoseName(list.get(5));
        huanZhe.setTradeNo(list.get(6));
        huanZhe.setTradeTime(list.get(7));
        huanZhe.setBillDocHospital(list.get(8));
        huanZhe.setBillDocName(list.get(9));
        huanZhe.setBillDocCode(list.get(10));
        huanZhe.setMedicalCardNumber(list.get(11));
        huanZhe.setTotal(list.get(12));
        huanZheRepository.save(huanZhe);

        for (int i = 0; i < array.size(); i++) {
            Map<String, String> map1 = (Map<String, String>) array.get(i);
            Charges charges1 = new Charges();
            charges1.setChargeId(map1.get("chargeId"));
            charges1.setQuantity(map1.get("quantity"));
            charges1.setPrice(map1.get("price"));
            charges1.setChargeName(map1.get("chargeName"));
            charges1.setFrequencyName(map1.get("frequencyName"));
            charges1.setUsageName(map1.get("usageName"));
            charges1.setDose(map1.get("dose"));
            charges1.setGroupId(map1.get("groupId"));
            charges1.setChargeType(map1.get("chargeType"));
            charges1.setMemo(map1.get("memo"));
            charges1.setTradeNo(list.get(6));
            chargesRepository.save(charges1);
        }
        map.put("success", true);
        map.put("msg", "成功");
        map.put("code", "10001");
        return map;
    }

    /***
     * 查询报告主信息
     * @param request
     * @return
     */
    @RequestMapping("/baogaoMain")
    public Map<String, Object> baogaoMain(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("medicalCardNumber");
        list.add("startTime");
        list.add("endTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        System.out.println("获取到的参数列表：" + stringg);
        return indexService.baogaoMain(stringg);
    }

    /***
     * 报告明细信息查询
     * @param request
     * @return
     */
    @RequestMapping("/baogaoMingXi")
    public Map<String, Object> baogaoMingXi(HttpServletRequest request) {
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("type");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.baogaoMingXi(stringg);
    }

    /***
     * 数据统计
     * @param request
     * @return
     */
    @RequestMapping("/tongJi")
    public Map<String, Object> tongji(HttpServletRequest request){
        List<String> list = new ArrayList<>();
        list.add("startTime");
        list.add("endTime");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.tongji(stringg);
    }

    /***
     *查询住院费用明细
     * @param request
     * @return
     */
    @RequestMapping("/queryHospitalizationDetail")
    public Map<String, Object> queryHospitalizationDetail(HttpServletRequest request){
        List<String> list = new ArrayList<>();
        list.add("id");
        List<String> stringg = GetRequestBodyUtil.getStringg(request, list);
        return indexService.queryHospitalizationDetail(stringg);
    }

}
