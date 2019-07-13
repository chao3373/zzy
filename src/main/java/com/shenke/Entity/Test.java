package com.shenke.Entity;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.*;

/**
 * @Auther: Administrator
 * @Date: 2019/6/3 14:30
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        String jsonArr = "[{\"patientName\":\"翟红梅\",\"total\":\"0.06\",\"item\":\"艾叶\",\"amount\":\"0.02\",\"quantity\":\"1.00\",\"price\":\"0.0150\",\"invoiceId\":\"20190710105134233\",\"time\":\"2019-07-10 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.06\",\"item\":\"生地黄\",\"amount\":\"0.04\",\"quantity\":\"1.00\",\"price\":\"0.0350\",\"invoiceId\":\"20190710105134233\",\"time\":\"2019-07-10 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.02\",\"item\":\"艾叶\",\"amount\":\"0.02\",\"quantity\":\"1.00\",\"price\":\"0.0150\",\"invoiceId\":\"20190709151138640\",\"time\":\"2019-07-09 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.08\",\"item\":\"紫苏梗\",\"amount\":\"0.02\",\"quantity\":\"1.00\",\"price\":\"0.0175\",\"invoiceId\":\"20190708141248050\",\"time\":\"2019-07-08 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.08\",\"item\":\"制何首乌\",\"amount\":\"0.06\",\"quantity\":\"1.00\",\"price\":\"0.0625\",\"invoiceId\":\"20190708141248050\",\"time\":\"2019-07-08 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.08\",\"item\":\"甘草\",\"amount\":\"0.06\",\"quantity\":\"1.00\",\"price\":\"0.0625\",\"invoiceId\":\"20190708184301890\",\"time\":\"2019-07-08 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"},{\"patientName\":\"翟红梅\",\"total\":\"0.08\",\"item\":\"紫苏梗\",\"amount\":\"0.02\",\"quantity\":\"1.00\",\"price\":\"0.0175\",\"invoiceId\":\"20190708184301890\",\"time\":\"2019-07-08 00:00:00.0\",\"hospitalName\":\"淄博市张店区中医院\",\"type\":\"中草药费\",\"hospitalCode\":\"0533_0019\"}]";

        String s1 = "001000005186594";
        String s2 = "2018-07-13 00:00:00";
        String s3 = "2019-07-13 23:59:59";
        String sql = "select name as patientName,\n" +
                "report_form_no as id,\n" +
                "'0' as type,\n" +
                "convert(varchar(19),author_Dtime,120) as time,\n" +
                "author_name as doctor,\n" +
                "class_name as item\n" +
                "From Lis.hosdata.dbo.LabMaster a,pa_patientinfo b\n" +
                "where  a.Patient_Id=b.JiuZhenKH and (b.JiuZhenKH='" + s1 + "' or b.medicalCardNumber='" + s1 + "')\n" +
                "and convert(varchar(19),author_dtime,120) between '" + s2 + "' and '" + s2 + "'";
        System.out.println(sql);
//

//        List<Map<String, Object>> detaList = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            Map<String, Object> map3 = (Map<String, Object>) jsonArray.get(i);
//            Map<String, Object> oneDate = new HashMap<>();
//            oneDate.put("invoiceId", map3.get("invoiceId"));
//            oneDate.put("time", map3.get("time"));
//            oneDate.put("total", map3.get("total"));
//            oneDate.put("hospitalCode", map3.get("hospitalCode"));
//            oneDate.put("hospitalName", map3.get("hospitalName"));
//            oneDate.put("patientName", map3.get("patientName"));
//            List<Map<String, Object>> detailsList = new ArrayList<>();
//            for (int j = 0; j < jsonArray.size(); j++) {
//                Map<String, Object> map1 = (Map<String, Object>) jsonArray.get(j);
//                if (map3.get("invoiceId").equals(map1.get("invoiceId"))) {
//                    Map<String, Object> map2 = new HashMap<>();
//                    map2.put("type", map1.get("type"));
//                    map2.put("item", map1.get("item"));
//                    map2.put("price", map1.get("price"));
//                    map2.put("quantity", map1.get("quantity"));
//                    map2.put("amount", map1.get("amount"));
//                    detailsList.add(map2);
//                }
//            }
//            System.out.println("detailsList: " + detailsList);
//            oneDate.put("details", detailsList);
//            if (i != 0) {
//                Map<String, Object> map5 = (Map<String, Object>) jsonArray.get(i - 1);
//                if (!map3.get("invoiceId").equals(map5.get("invoiceId"))) {
//                    detaList.add(oneDate);
//                }
//            } else {
//                detaList.add(oneDate);
//            }
//            System.out.println("jsonArray: " + JSONUtil.parseArray(detaList));
//        }
//        System.out.println(detaList);
    }

}
