package com.shenke.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GetRequestBodyUtil {

    public static String getString(HttpServletRequest request, String name) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str = "";
            while ((str = br.readLine()) != null) {
                sb.append(new String(str));
            }
            if (sb.length() != 0) {
                JSONObject jsonObject = JSONUtil.parseObj(sb.toString());
                System.out.println(jsonObject);
                System.out.println(jsonObject.get(name).toString());
                return jsonObject.get(name).toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStringg(HttpServletRequest request, List<String> strs) {
        System.out.println(strs);
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();
        try {
            BufferedReader reader = request.getReader();
            String str = "";
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            if (sb.length()!=0){
                JSONObject jsonObject = JSONUtil.parseObj(sb.toString());
                for (int i = 0; i< strs.size(); i++){
                    System.out.println(strs.get(i) + ": " + jsonObject.get(strs.get(i)));
                    strings.add(jsonObject.get(strs.get(i)).toString());
                }
            }
            System.out.println(strings);
            return strings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
