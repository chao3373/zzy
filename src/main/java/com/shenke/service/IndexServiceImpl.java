package com.shenke.service;

import com.shenke.util.DaoUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/6/3 11:52
 * @Description:
 */
@Service("indexService")
public class IndexServiceImpl implements IndexService {

    /***
     * 办理电子健康卡
     * @param medicalCardNumber
     * @param name
     * @param sex
     * @param birthday
     * @param address
     * @param idcard
     * @param phone
     * @return
     */
    @Override
    public Map<String, Object> insertCard(String medicalCardNumber, String name, String sex, String birthday, String address, String idcard, String phone) {

        Map<String, Object> map = new HashMap<>();

        Connection connection = DaoUtil.getConnection();
        CallableStatement callableStatement = null;
        String msg = "";
        try {
            callableStatement = connection.prepareCall("{? = call WeiXin_InsertCard(?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setObject(2, medicalCardNumber);
            callableStatement.setObject(3, name);
            callableStatement.setObject(4, sex);
            callableStatement.setObject(5, birthday);
            callableStatement.setObject(6, address);
            callableStatement.setObject(7, idcard);
            callableStatement.setObject(8, phone);
            callableStatement.execute();

            switch (callableStatement.getInt(1)) {
                case 1:
                    msg = "获取病例号出错";
                    break;
                case 2:
                    msg = "计算年龄出错";
                    break;
                case 3:
                    msg = "插入信息主表错误";
                    break;
                case 4:
                    msg = "插入扩展信息主表错误";
                    break;
                case 5:
                    msg = "插入卡信息主表错误";
                    break;
            }

            if (callableStatement.getInt(1) != 0) {
                map.put("success", false);
                map.put("msg", msg);
                map.put("code", "10002");
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
            }

            callableStatement.close();
            connection.close();
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            msg = "未知错误";
            map.put("success", false);
            map.put("msg", msg);
            map.put("code", "10003");
            return map;
        }
    }

    /***
     * 根据就诊卡号查询用户信息
     * @param medicalCardNumber
     * @return
     */
    @Override
    public Map<String, Object> selectByMedicalCardNumber(String medicalCardNumber) {

        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select b.xingming as name, b.LianXiDH as  phone, b.XingBie as  sex, b.ZhuZhi as address, b.ChuShengNY as  birthdate, c.ShenFenZH as  idCardNo, a.CardNo as medicalCardNumber From Card_MasterInfo a,PA_PatientInfo b,PA_PatientInfoExt c where a.CardNo=b.JiuZhenKH and b.BingLiLH=c.BingLiLH and a.CardNo = ?");

            preparedStatement.setString(1, medicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功但是未查询到结果");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", list.get(0));
                return map;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "发生未知错误");
            map.put("code", "10003");
            return map;
        }
    }

    /***
     * 推送电子就诊卡信息
     * @param name
     * @param oldMedicalCardNumber
     * @param newMedicalCardNumber
     * @return
     */
    @Override
    public Map<String, Object> updateMedicalCardNumber(String name, String oldMedicalCardNumber, String newMedicalCardNumber) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select \n" +
                    "a.RiQi as createDate,\n" +
                    "b.xingming as name,\n" +
                    "b.LianXiDH as  phone,\n" +
                    "b.XingBie as  sex,\n" +
                    "b.ZhuZhi as address,\n" +
                    "b.ChuShengNY as  birthdate,\n" +
                    "c.ShenFenZH as  idCardNo,\n" +
                    "a.CardNo as medicalCardNumber\n" +
                    "From Card_MasterInfo a,PA_PatientInfo b,PA_PatientInfoExt c\n" +
                    "where a.CardNo=b.JiuZhenKH and b.BingLiLH=c.BingLiLH AND a.CardNo = ?");
            preparedStatement.setString(1, oldMedicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);

            System.out.println(list);
            System.out.println(list.size());
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到该卡号");
                map.put("code", "10003");
                return map;
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("update Card_MasterInfo set CardNo= ? where CardNo =?");
            preparedStatement1.setString(1, newMedicalCardNumber);
            preparedStatement1.setString(2, oldMedicalCardNumber);

            PreparedStatement preparedStatement2 = connection.prepareStatement("update PA_PatientInfo set CardNo= ?,JiuZhenKH=? where CardNo= ?");
            preparedStatement2.setString(1, newMedicalCardNumber);
            preparedStatement2.setString(2, newMedicalCardNumber);
            preparedStatement2.setString(3, oldMedicalCardNumber);

            int i = preparedStatement1.executeUpdate();
            int j = preparedStatement2.executeUpdate();
            if (i > 0 && j > 0) {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", list.get(0).get("createDate"));
                return map;
            } else {
                map.put("success", false);
                map.put("msg", "未知错误");
                map.put("code", "10003");
                return map;
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "未知错误");
            map.put("code", "10003");
            return map;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
    }

    /***
     * 待缴费信息查询接口
     * @param medicalCardNumber
     * @return
     */
    @Override
    public Map<String, Object> selectPaymentInformation(String medicalCardNumber) {
        System.out.println(medicalCardNumber);

        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select cast(sum(b.Shuliang *b.DanJia) as decimal(10,2)) as JinE\n" +
                    "From Out_Recipe a,Out_RecipeMaster b,Dict_Personnel c\n" +
                    "where a.JieSuanBZ=0 and a.JiuZhenID=b.JiuZhenID \n" +
                    "and a.ChuFangLH=b.ChuFangLH\n" +
                    "and a.YiShengBM=c.RenYuanBM\n" +
                    "and a.JiuzhenKH=?");
            preparedStatement.setString(1, medicalCardNumber);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if(list.size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功没有待缴费信息");
                map.put("code", "10002");
                return map;
            } else {
                PreparedStatement preparedStatement1 = connection.prepareStatement("select \n" +
                        "B.YaoPinID AS id,\n" +
                        "b.YaoMing as item,\n" +
                        "b.DanJia as price,\n" +
                        "b.Shuliang as quantity,\n" +
                        "b.DanWei as unit,\n" +
                        "c.XingMing as  doctor,\n" +
                        "b.SerialNo as createTime\n" +
                        "From Out_Recipe a,Out_RecipeMaster b,Dict_Personnel c\n" +
                        "where a.JieSuanBZ=0 and a.JiuZhenID=b.JiuZhenID \n" +
                        "and a.ChuFangLH=b.ChuFangLH\n" +
                        "and a.YiShengBM=c.RenYuanBM\n" +
                        "and a.JiuzhenKH = ?");

                preparedStatement1.setString(1, medicalCardNumber);

                ResultSet resultSet1 = preparedStatement1.executeQuery();
                List<Map<String, Object>> list1 = DaoUtil.getresultSet(resultSet1);
                if (list1.size() == 0) {
                    map.put("success", false);
                    map.put("msg", "查询成功没有待缴费信息");
                    map.put("code", "10002");
                    return map;
                }else {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("amount", list.get(0).get("JinE"));
                    map1.put("detail", list1.get(0));
                    map.put("success", true);
                    map.put("msg", "查询成功没有待缴费信息");
                    map.put("code", "10002");
                    map.put("data", map1);
                    return map;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "发生未知错误");
            map.put("code", "10003");
            return map;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
