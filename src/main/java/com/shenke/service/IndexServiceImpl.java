package com.shenke.service;

import cn.hutool.json.JSONObject;
import com.shenke.Entity.CardRecharge;
import com.shenke.Entity.PaymentMessage;
import com.shenke.Entity.PaymentPledge;
import com.shenke.util.DaoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.text.SimpleDateFormat;
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

    @Resource
    private PaymentMessageService paymentMessageService;

    @Resource
    private CardRechargeService cardRechargeService;

    @Resource
    private PaymentPledgeService paymentPledgeService;

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
                case 6:
                    msg = "该卡号已占用";
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
                    "b.xingming as name,\n" +
                    "b.LianXiDH as  phone,\n" +
                    "b.XingBie as  sex,\n" +
                    "b.ZhuZhi as address,\n" +
                    "b.ChuShengNY as  birthdate,\n" +
                    "c.ShenFenZH as  idCardNo,\n" +
                    "a.CardNo as medicalCardNumber\n" +
                    "From Card_MasterInfo a,PA_PatientInfo b,PA_PatientInfoExt c\n" +
                    "where a.CardNo=b.JiuZhenKH and b.BingLiLH=c.BingLiLH\n" +
                    "and b.medicalCardNumber= ?");
            preparedStatement.setString(1, oldMedicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);

            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到该卡号");
                map.put("code", "10003");
                return map;
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("update Card_MasterInfo set CardNo= ?\n" +
                    "where CardNo=?");
            preparedStatement1.setString(1, newMedicalCardNumber);
            preparedStatement1.setString(2, oldMedicalCardNumber);

            PreparedStatement preparedStatement2 = connection.prepareStatement("update PA_PatientInfo set CardNo= ?,JiuZhenKH=?\n" +
                    "where CardNo= ?");
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
                    "and CONVERT(varchar(10),a.ChuFangRQ,120)=CONVERT(varchar(10),GETDATE(),120)\n" +
                    "and a.JiuzhenKH=?");
            preparedStatement.setString(1, medicalCardNumber);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
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
                        "and CONVERT(varchar(10),a.ChuFangRQ,120)=CONVERT(varchar(10),GETDATE(),120)\n " +
                        "and a.JiuzhenKH = ?");

                preparedStatement1.setString(1, medicalCardNumber);

                ResultSet resultSet1 = preparedStatement1.executeQuery();
                List<Map<String, Object>> list1 = DaoUtil.getresultSet(resultSet1);
                if (list1.size() == 0) {
                    map.put("success", false);
                    map.put("msg", "查询成功没有待缴费信息");
                    map.put("code", "10002");
                    return map;
                } else {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("amount", list.get(0).get("JinE"));
                    map1.put("detail", list1);
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

    /***
     * 查询就诊卡余额
     * @param medicalCardNumber
     * @return
     */
    @Override
    public Map<String, Object> selectBalance(String medicalCardNumber) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select LeftJinE as balance From Card_MasterInfo where CardNo= ?");
            preparedStatement.setString(1, medicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到记录");
                map.put("code", "10003");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", list.get(0).get("balance"));
                return map;
            }
        } catch (SQLException e) {
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
     * 查询住院信息
     * @param medicalCardNumber
     * @param admissionNumber
     * @return
     */
    @Override
    public Map<String, Object> selectMessage(String medicalCardNumber, String admissionNumber) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (admissionNumber == null) {

                preparedStatement = connection.prepareStatement("select \n" +
                        "b.medicalCardNumber as medicalCardNumber,\n" +
                        "a.ZhuYuanID as  admissionNumber,\n" +
                        "a.XingMing as  inPatName,\n" +
                        "d.KeShiMC as  deptName,\n" +
                        "a.ChuangHao as  bedNumber,\n" +
                        "a.JiZhangKuan as  totalCost,\n" +
                        "a.YuFuKuan as  totalPrepay,\n" +
                        "a.JieYuKuan  prepayLeft,\n" +
                        "0 as minimumPayment,\n" +
                        "100000 as maximumPayment,\n" +
                        "a.XingBie  sex,\n" +
                        "a.RuYuanSJ as  patInTime,\n" +
                        "c.ShenFenZH as idCard,\n" +
                        "b.LianXiDH as phone\n" +
                        "From LD_Register a,PA_PatientInfo b,PA_PatientInfoExt c,Dict_Depart d\n" +
                        "where a.ZhuYuanHao=b.ZhuYuanHao and b.BingLiLH=c.BingLiLH and a.KeShiBM =d.KeShiBM \n" +
                        "and b.medicalCardNumber=?");

                preparedStatement.setString(1, medicalCardNumber);
                resultSet = preparedStatement.executeQuery();

            } else {
                preparedStatement = connection.prepareStatement("select \n" +
                        "b.medicalCardNumber as medicalCardNumber,\n" +
                        "a.ZhuYuanID as  admissionNumber,\n" +
                        "a.XingMing as  inPatName,\n" +
                        "d.KeShiMC as  deptName,\n" +
                        "a.ChuangHao as  bedNumber,\n" +
                        "a.JiZhangKuan as  totalCost,\n" +
                        "a.YuFuKuan as  totalPrepay,\n" +
                        "a.JieYuKuan  prepayLeft,\n" +
                        "0 as minimumPayment,\n" +
                        "100000 as maximumPayment,\n" +
                        "a.XingBie  sex,\n" +
                        "a.RuYuanSJ as  patInTime,\n" +
                        "c.ShenFenZH as idCard,\n" +
                        "b.LianXiDH as phone\n" +
                        "From LD_Register a,PA_PatientInfo b,PA_PatientInfoExt c,Dict_Depart d\n" +
                        "where a.ZhuYuanHao=b.ZhuYuanHao and b.BingLiLH=c.BingLiLH and a.KeShiBM =d.KeShiBM \n" +
                        "and a.ZhuYuanID=?");

                preparedStatement.setString(1, admissionNumber);
                resultSet = preparedStatement.executeQuery();
            }

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到住院信息");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", list.get(0));
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
     * 获取住院押金欠费用户信息
     * @param prePay
     * @return
     */
    @Override
    public Map<String, Object> selectArrearageUserMessage(Double prePay) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select \n" +
                    "b.medicalCardNumber as medicalCardNumber,\n" +
                    "a.ZhuYuanHao as admissionNumber,\n" +
                    "a.XingMing as  userName,\n" +
                    "d.KeShiMC as  deptName,\n" +
                    "a.ChuangHao as  bedNumber,\n" +
                    "a.JieYuKuan as  balance,\n" +
                    "a.RuYuanSJ as  patInTime\n" +
                    "From LD_Register a,PA_PatientInfo b,PA_PatientInfoExt c,Dict_Depart d\n" +
                    "where a.ZhuYuanHao=b.ZhuYuanHao and b.BingLiLH=c.BingLiLH and a.KeShiBM =d.KeShiBM \n" +
                    "and a.JieSuanSJ is null and JieYuKuan < ?");

            preparedStatement.setDouble(1, prePay);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);

            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到符合条件的记录");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
//                System.out.println(list.get(0));
                map.put("data", list);
                return map;
            }

        } catch (SQLException e) {
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
     * 查询所有住院状态的电子健康卡用户信息
     * @return
     */
    @Override
    public Map<String, Object> selectAllInHospital() {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select \n" +
                    "b.medicalCardNumber as medicalCardNumber,\n" +
                    "a.ZhuYuanHao as admissionNumber,\n" +
                    "a.XingMing as  userName,\n" +
                    "d.KeShiMC as  deptName,\n" +
                    "a.ChuangHao as  bedNumber\n" +
                    "From LD_Register a,PA_PatientInfo b,PA_PatientInfoExt c,Dict_Depart d\n" +
                    "where a.ZhuYuanHao=b.ZhuYuanHao and b.BingLiLH=c.BingLiLH and a.KeShiBM =d.KeShiBM \n" +
                    "and a.JieSuanSJ is null and b.medicalCardNumber is not null");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有记录");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", list);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "发生未知错误");
            map.put("code", "10003");
            return map;
        }
    }

    /***
     *查询一日住院费用明细
     * @param medicalCardNumber
     * @param date
     * @param admissionNumber
     * @return
     */
    @Override
    public Map<String, Object> selectOneDay(String medicalCardNumber, String date, String admissionNumber) {

        Connection connection = DaoUtil.getConnection();
        Map<String, Object> map = new HashMap<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select \n" +
                    "a.JiuZhenKH,\n" +
                    "b.jiesuanrq,\n" +
                    "a.XingMing as  name,\t-- 患者姓名,\n" +
                    "a.XingBie as sex,\t-- 患者性别，男，女,\n" +
                    "c.XingMing as doctor,\t-- 医生姓名,\n" +
                    "a.JiZhangKuan as  total,\t-- 总费用,\n" +
                    "a.ZhuYuanHao as admissionNumber,\t-- 住院号,\n" +
                    "a.RuYuanZD as diagnosis,\t-- 诊断,\n" +
                    "CONVERT(varchar(19),b.jiesuanrq,121) as time,\t-- 时间yyyy-MM-dd HH,\t--mm,\t--ss,\n" +
                    "b.YaoMing as  item,\t-- 项目名称,\n" +
                    "d.HeSuanMC as type,\t-- 费用类型,\n" +
                    "b.DanJia as  price,\t-- 单价,\n" +
                    "b.ShuLiang as quantity,\t-- 数量,\n" +
                    "b.JinE  as  amount\t-- 金额（小计）\n" +
                    "From LD_Register a,LD_ChargeDetail b,Dict_Personnel  c,Dict_CheckItem d\n" +
                    "where a.ZhuYuanID=b.ZhuYuanID and b.JieSuanPC=a.JieZhangPC and b.HeSuanBM=d.HeSuanBM \n" +
                    "and a.YiShengBM =c.RenYuanBM \n" +
                    "and a.JieSuanSJ is null\n" +
                    "AND a.ZhuYuanHao = ?\n" +
                    "AND a.JiuZhenKH = ?\n" +
                    "AND b.jiesuanrq > ? \n" +
                    "AND b.jiesuanrq < ?");
            preparedStatement.setString(1, admissionNumber);
            preparedStatement.setString(2, medicalCardNumber);
            System.out.println(date);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(date + " 23:59:59"));
            preparedStatement.setString(3, date + " 00:00:00");
            preparedStatement.setString(4, date + " 23:59:59");
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", list.get(0).get("name"));
            map1.put("sex", list.get(0).get("sex"));
            map1.put("doctor", list.get(0).get("doctor"));
            map1.put("total", list.get(0).get("total"));
            map1.put("admissionNumber", list.get(0).get("admissionNumber"));
            map1.put("diagnosis", list.get(0).get("diagnosis"));

            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("name");
                list.get(i).remove("sex");
                list.get(i).remove("doctor");
                list.get(i).remove("total");
                list.get(i).remove("admissionNumber");
                list.get(i).remove("diagnosis");
            }

            map1.put("details", list);

            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有记录");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", map1);
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
     * 用户支付成功后，公众号系统调用此接口将支付成功信息通知院内系统
     * @param medicalCardNumber
     * @param outTradeNo
     * @param amount
     * @param payTime
     * @param payType
     * @param detailId
     * @return
     */
    @Override
    public Map<String, Object> paymentSuccess(String medicalCardNumber, String outTradeNo, String amount, String payTime, String payType, String detailId) {


        Map<String, Object> map = new HashMap<>();

        Connection connection = DaoUtil.getConnection();
        String msg = "";
        try {

            CallableStatement callableStatement = connection.prepareCall("{? = call WeiXin_CreateRecipeDetail(?,?)}");
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, medicalCardNumber);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            System.out.println(callableStatement.getInt(1));
            System.out.println(callableStatement.getString(3));

            switch (callableStatement.getInt(1)) {
                case 1:
                    msg = "插入临时费用表出错";
                    break;
                case 2:
                    msg = "插入药品明细错误";
                    break;
                case 3:
                    msg = "获取卡信息出错";
                    break;
                case 4:
                    msg = "缺药@DrugInfo 缺药信息";
                    break;
                case 5:
                    msg = "更新处方金额出错";
                    break;
            }

            if (callableStatement.getInt(1) != 0) {
                map.put("success", false);
                map.put("msg", msg);
                map.put("code", "10002");
            } else {

                CallableStatement callableStatement1 = connection.prepareCall("{? = call WeiXin_RecipeSettl(?,?,?)}");
                callableStatement1.registerOutParameter(1, Types.NUMERIC);
                callableStatement1.setString(2, medicalCardNumber);
                callableStatement1.setString(3, amount);
                callableStatement1.registerOutParameter(4, Types.VARCHAR);
                callableStatement1.execute();
                System.out.println(callableStatement1.getInt(1));
                System.out.println(callableStatement1.getString(4));

                switch (callableStatement1.getInt(1)) {
                    case 1:
                        msg = "未查询到该卡姓名信息";
                        break;
                    case 2:
                        msg = "处方金额跟支付金额不符";
                        break;
                    case 3:
                        msg = "更新处方标志出错";
                        break;
                    case 4:
                        msg = "生成单据号错误";
                        break;
                    case 5:
                        msg = "更新处方明细表错误";
                        break;
                    case 6:
                        msg = "生成发票明细表错误";
                        break;
                    case 7:
                        msg = "生成发票主表错误";
                        break;
                    case 8:
                        msg = "生成发药表信息错误";
                        break;
                    case 9:
                        msg = "生成发药状态明细表错误";
                        break;
                    case 10:
                        msg = "更新总量库存信息错误";
                        break;
                    case 11:
                        msg = "更新批次库存信息错误";
                        break;
                    case 12:
                        msg = "生成处方明细表错误";
                        break;
                    case 13:
                        msg = "更新处方金额错误";
                        break;
                    case 14:
                        msg = "插入支付信息错误";
                        break;
                    case 15:
                        msg = "删除临时数据错误";
                        break;
                    case 16:
                        msg = "删除master表错误";
                        break;
                    case 17:
                        msg = "发票主表跟发票细表不符";
                        break;
                }

                if (callableStatement1.getInt(1) != 0) {
                    map.put("success", false);
                    map.put("msg", msg);
                    map.put("code", "10002");
                } else {
                    PaymentMessage paymentMessage = new PaymentMessage();
                    paymentMessage.setMedicalCardNumber(medicalCardNumber);
                    paymentMessage.setAmount(Double.parseDouble(amount));
                    paymentMessage.setDetailId(detailId);
                    paymentMessage.setOutTradeNo(outTradeNo);
                    paymentMessage.setPayTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(payTime).getTime()));
                    paymentMessage.setPayType(payType);
                    paymentMessageService.save(paymentMessage);
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("outPrepayId", callableStatement1.getString(4));
                    Map<String, Object> map2 = this.selectPaymentInformation(medicalCardNumber);
                    Map<String, Object> map3 = (Map<String, Object>) map2.get("data");
                    String string = (String) map3.get("amount");
                    double amount1 = Double.parseDouble(string);
                    map1.put("amount", amount1 - Double.parseDouble(amount));
                    map.put("success", true);
                    map.put("msg", "成功");
                    map.put("code", "10001");
                    map.put("data", map1);
                }
            }

            connection.close();
            callableStatement.close();
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
     * 就诊卡充值
     * @param medicalCardNumber
     * @param outTradeNo
     * @param payAmount
     * @param payType
     * @param payTime
     * @return
     */
    @Override
    public Map<String, Object> cardRecharge(String medicalCardNumber, String outTradeNo, String payAmount, String payType, String payTime) {
        Map<String, Object> map = new HashMap<>();

        Connection connection = DaoUtil.getConnection();
        String msg = "";
        try {
            CallableStatement callableStatement = connection.prepareCall("{? = call WeiXin_CardCharge(?,?,?,?,?)}");
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, medicalCardNumber);
            callableStatement.setString(3, outTradeNo);
            callableStatement.setDouble(4, Double.parseDouble(payAmount));
            callableStatement.setString(5, payType);
            callableStatement.setDate(6, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(payTime).getTime()));
            callableStatement.execute();

            switch (callableStatement.getInt(1)) {
                case 1:
                    msg = "获取卡信息出错";
                    break;
                case 2:
                    msg = "插入充值明细出错";
                    break;
                case 3:
                    msg = "更新就诊卡余额出错";
                    break;
            }
            if (callableStatement.getInt(1) != 0) {
                map.put("success", false);
                map.put("msg", msg);
                map.put("code", "10002");
                return map;
            } else {
                CardRecharge cardRecharge = new CardRecharge();
                cardRecharge.setMedicalCardNumber(medicalCardNumber);
                cardRecharge.setOutTradeNo(outTradeNo);
                cardRecharge.setPayAmount(Double.parseDouble(payAmount));
                cardRecharge.setPayTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(payTime).getTime()));
                cardRecharge.setPayType(payType);
                cardRechargeService.save(cardRecharge);

                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10002");
                Map<String, Object> map1 = new HashMap<>();
//                map.put("data", );
                return map;
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "未知错误");
            map.put("code", "10003");
            return map;
        }
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
    @Override
    public Map<String, Object> paymentPledge(String medicalCardNumber, String admissionNumber, String amount, String outTradeNo, String payType, String payTime) {
        Map<String, Object> map = new HashMap<>();

        Connection connection = DaoUtil.getConnection();
        String msg = "";

        try {
            CallableStatement callableStatement = connection.prepareCall("{? = call WeiXin_LDPreCharge()}");
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.execute();

            switch (callableStatement.getInt(1)) {
                case 1:
                    msg = "查询住院记录信息错误，有二条以上未了出院记录，请根据住院ID缴费";
                    break;
                case 2:
                    msg = "未查询到住院记录信息";
                    break;
                case 3:
                    msg = "提取住院信息出错";
                    break;
                case 4:
                    msg = "插入充值明细出错";
                    break;
                case 5:
                    msg = "更新住院总表信息出错";
                    break;
            }
            if (callableStatement.getInt(1) != 0) {
                map.put("success", false);
                map.put("msg", msg);
                map.put("code", "10002");
                return map;
            } else {
                PaymentPledge paymentPledge = new PaymentPledge();
                paymentPledge.setAdmissionNumber(admissionNumber);
                paymentPledge.setAmount(Double.parseDouble(amount));
                paymentPledge.setMedicalCardNumber(medicalCardNumber);
                paymentPledge.setOutTradeNo(outTradeNo);
                paymentPledge.setPayType(payType);
                paymentPledge.setPayTime(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(payTime).getTime()));
                paymentPledgeService.save(paymentPledge);
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10002");
                Map<String, Object> map1 = new HashMap<>();
//                map.put("data", );
                return map;
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", "未知错误");
            map.put("code", "10003");
            return map;
        }
    }

}
