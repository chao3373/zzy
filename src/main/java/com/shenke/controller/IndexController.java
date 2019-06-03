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

    @RequestMapping("/insertCard")
    public Map<String, Object> insertCard(String medicalCardNumber, String name, String sex, String birthday, String address, String idcard, String phone) {
        Map<String, Object> map = new HashMap<>();
        indexService.insertCard(medicalCardNumber, name, sex, birthday, address, idcard, phone);
        System.out.println(medicalCardNumber + "==========" + name + "==========" + sex + "==========" + birthday + "==========" + address + "==========" + idcard + "==========" + phone);



        map.put("success", true);
        map.put("msg", "成功");
        map.put("code", "10001");
        return map;
    }
}
