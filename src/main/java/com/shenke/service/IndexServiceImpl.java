package com.shenke.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.shenke.Entity.*;
import com.shenke.util.DaoUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.swing.text.MaskFormatter;
import java.sql.*;
import java.text.SimpleDateFormat;
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

    @Resource
    private BanKaService banKaService;

    @Resource
    private BangKaService bangKaService;

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
                BanKa banKa = new BanKa();
                banKa.setMedicalCardNumber(medicalCardNumber);
                banKa.setName(name);
                banKa.setSex(sex);
                banKa.setBirthday(birthday);
                banKa.setAddress(address);
                banKa.setIdcard(idcard);
                banKa.setPhone(phone);
                banKa.setDate(new Date(new java.util.Date().getTime()));
                banKaService.save(banKa);
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
                    "b.LianXiDH as phone,\n" +
                    "b.SerialNo as createDate,\n" +
                    "b.XingBie as sex,\n" +
                    "b.ZhuZhi as address,\n" +
                    "b.ChuShengNY as birthdate,\n" +
                    "c.ShenFenZH as idCardNo,\n" +
                    "a.CardNo as medicalCardNumber\n" +
                    "From Card_MasterInfo a,PA_PatientInfo b,PA_PatientInfoExt c\n" +
                    "where a.CardNo=b.JiuZhenKH and b.BingLiLH=c.BingLiLH\n" +
                    "and b.JiuZhenKH= ?");
            preparedStatement.setString(1, oldMedicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);

            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到该卡号");
                map.put("code", "10002");
                return map;
            }

            PreparedStatement preparedStatement1 = connection.prepareStatement("update Card_MasterInfo set medicalCardNumber=? where CardNo=?");
            preparedStatement1.setString(1, newMedicalCardNumber);
            preparedStatement1.setString(2, oldMedicalCardNumber);

            PreparedStatement preparedStatement2 = connection.prepareStatement("update PA_PatientInfo set medicalCardNumber=? where CardNo= ?");
            preparedStatement2.setString(1, newMedicalCardNumber);
            preparedStatement2.setString(2, oldMedicalCardNumber);

            int i = preparedStatement1.executeUpdate();
            int j = preparedStatement2.executeUpdate();
            if (i > 0 && j > 0) {
                BangKa bangKa = new BangKa();
                bangKa.setName((String) list.get(0).get("name"));
                bangKa.setPhone((String) list.get(0).get("phone"));
                bangKa.setCreateDate((String) list.get(0).get("createDate"));
                bangKa.setSex((String) list.get(0).get("sex"));
                bangKa.setAddress((String) list.get(0).get("address"));
                bangKa.setBirthdate((String) list.get(0).get("birthdate"));
                bangKa.setIdCardNo((String) list.get(0).get("idCardNo"));
                bangKa.setMedicalCardNumber((String) list.get(0).get("medicalCardNumber"));
                bangKa.setDate(new Date(new java.util.Date().getTime()));
                bangKaService.save(bangKa);
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

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select cast(sum(b.Shuliang *b.DanJia) as decimal(10,2)) as JinE\n" +
                            "From Out_Recipe a,Out_RecipeMaster b,Dict_Personnel c, Pa_patientInfo d\n" +
                            "where a.JieSuanBZ=0 and a.JiuZhenID=b.JiuZhenID \n" +
                            "and a.ChuFangLH=b.ChuFangLH\n" +
                            "and a.YiShengBM=c.RenYuanBM\n" +
                            "and CONVERT(varchar(10),a.ChuFangRQ,120)=CONVERT(varchar(10),GETDATE(),120)\n" +
                            "and a.JiuzhenKH=d.JiuzhenKH and d.medicalCardNumber=?"
            );
            preparedStatement.setString(1, medicalCardNumber);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功没有待缴费信息");
                map.put("code", "10002");
                return map;
            } else {
                PreparedStatement preparedStatement1 = connection.prepareStatement(
                        "select \n" +
                                "B.id AS id,\n" +
                                "b.YaoMing as item,\n" +
                                "b.DanJia as price,\n" +
                                "b.Shuliang as quantity,\n" +
                                "b.DanWei as unit,\n" +
                                "c.XingMing as doctor,\n" +
                                "convert(varchar(19),b.SerialNo,120) as createTime\n" +
                                "From Out_Recipe a,Out_RecipeMaster b,Dict_Personnel c, Pa_patientInfo d\n" +
                                "where a.JieSuanBZ=0 and a.JiuZhenID=b.JiuZhenID \n" +
                                "and a.ChuFangLH=b.ChuFangLH\n" +
                                "and a.YiShengBM=c.RenYuanBM\n" +
                                "and CONVERT(varchar(10),a.ChuFangRQ,120)=CONVERT(varchar(10),GETDATE(),120) \n" +
                                "and a.JiuzhenKH=d.JiuzhenKH and d.medicalCardNumber=?"
                );

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
                    map.put("msg", "成功");
                    map.put("code", "10001");
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
            PreparedStatement preparedStatement = connection.prepareStatement("select LeftJinE as balance From Card_MasterInfo where medicalCardNumber = ?");
            preparedStatement.setString(1, medicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            System.out.println(list.size());
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有查询到记录");
                map.put("code", "10003");
                return map;
            } else {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("balance", list.get(0).get("balance"));
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", map1);
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
        System.out.println("medicalCardNumber长度：" + medicalCardNumber.length());
        try {
            String sql = "select \n" +
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
                    "convert(varchar(19),a.RuYuanSJ,120) as patInTime,\n" +
                    "c.ShenFenZH as idCard,\n" +
                    "b.LianXiDH as phone\n" +
                    "From LD_Register a,PA_PatientInfo b,PA_PatientInfoExt c,Dict_Depart d\n" +
                    "where a.ZhuYuanHao=b.ZhuYuanHao and b.BingLiLH=c.BingLiLH and a.KeShiBM =d.KeShiBM \n" +
                    "and (b.medicalCardNumber=? or a.ZhuYuanID=?)";

            System.out.println(sql);
            System.out.println(medicalCardNumber);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (!StringUtils.isEmpty(medicalCardNumber)) {
                preparedStatement.setString(1, medicalCardNumber);
                preparedStatement.setString(2, medicalCardNumber);
            } else {
                preparedStatement.setString(1, admissionNumber);
                preparedStatement.setString(2, admissionNumber);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
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

        } catch (
                Exception e) {
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
                    "a.XingMing as  name, -- 患者姓名,\n" +
                    "a.XingBie as sex, -- 患者性别，男，女,\n" +
                    "c.XingMing as doctor, -- 医生姓名,\n" +
                    "a.JiZhangKuan as total, -- 总费用,\n" +
                    "a.ZhuYuanHao as admissionNumber, -- 住院号,\n" +
                    "a.RuYuanZD as diagnosis, -- 诊断,\n" +
                    "CONVERT(varchar(19),b.jiesuanrq,121) as time, -- 时间yyyy-MM-dd HH, --mm, --ss,\n" +
                    "b.YaoMing as  item, -- 项目名称,\n" +
                    "d.HeSuanMC as type, -- 费用类型,\n" +
                    "b.DanJia as  price, -- 单价,\n" +
                    "b.ShuLiang as quantity, -- 数量,\n" +
                    "b.JinE  as  amount -- 金额（小计）\n" +
                    "From LD_Register a,LD_ChargeDetail b,Dict_Personnel  c,Dict_CheckItem d,pa_patientinfo e\n" +
                    "where a.ZhuYuanID=b.ZhuYuanID and b.JieSuanPC=a.JieZhangPC and b.HeSuanBM=d.HeSuanBM \n" +
                    "and a.YiShengBM =c.RenYuanBM \n" +
                    "and a.zhuyuanhao=e.zhuyuanhao and (e.medicalcardnumber=? or a.ZhuYuanID=?)\n" +
                    "and a.JieSuanSJ is null\n" +
                    "And convert(varchar(10), b.jiesuanrq, 120) = ?");
            preparedStatement.setString(1, medicalCardNumber);
            preparedStatement.setString(2, admissionNumber);
            preparedStatement.setString(3, date);
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
            PreparedStatement preparedStatement = connection.prepareStatement(" select *\n" +
                    " From Out_Recipe a,Out_RecipeMaster b,Dict_Personnel c,PA_PatientInfo d\n" +
                    " where a.JieSuanBZ=0 and a.JiuZhenID=b.JiuZhenID \n" +
                    " and a.ChuFangLH=b.ChuFangLH and a.BingLiHao=d.BingLiHao\n" +
                    " and a.YiShengBM=c.RenYuanBM\n" +
                    " and CONVERT(varchar(10),a.ChuFangRQ,120)=CONVERT(varchar(10),GETDATE(),120)\n" +
                    " and d.medicalCardNumber=?");
            preparedStatement.setString(1, medicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "不存在未结算处方");
                map.put("code", "10003");
                return map;
            }
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
                System.out.println(callableStatement1.getString(1));
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
                    Map<String, Object> data = new HashMap<>();
                    data.put("outPrepayId", callableStatement1.getString(4));
//                    Map<String, Object> map2 = this.selectPaymentInformation(medicalCardNumber);
//                    Map<String, Object> map3 = (Map<String, Object>) map2.get("data");
//                    String string = (String) map3.get("amount");
//                    double amount1 = Double.parseDouble(string);
                    data.put("amount", 0);
                    map.put("success", true);
                    map.put("msg", "成功");
                    map.put("code", "10001");
                    map.put("data", data);
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
            callableStatement.setDate(6, new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payTime).getTime()));
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
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payTime);
                cardRecharge.setPayTime(new Date(date.getTime()));
                cardRecharge.setPayType(payType);
                cardRechargeService.save(cardRecharge);

                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
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
            CallableStatement callableStatement = connection.prepareCall("{? = call WeiXin_LDPreCharge(?,?,?,?,?,?)}");
            callableStatement.registerOutParameter(1, Types.NUMERIC);
            callableStatement.setString(2, medicalCardNumber);
            callableStatement.setString(3, admissionNumber);
            callableStatement.setString(4, outTradeNo);
            callableStatement.setDouble(5, Double.parseDouble(amount));
            callableStatement.setString(6, payType);
            callableStatement.setString(7, payTime);
            callableStatement.execute();
            System.out.println(callableStatement.getInt(1));
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
                java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payTime);
                paymentPledge.setPayTime(new Date(date.getTime()));
                paymentPledgeService.save(paymentPledge);
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
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
     * 查询住院记录
     * @param medicalCardNumber
     * @param admissionNumber
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String, Object> queryHospitalizationRecord(String medicalCardNumber, String admissionNumber, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String sql = "select a.ZhuYuanID as id,\n" +
                "RuYuanSJ as dateOfAdmission,\n" +
                "case when ChuYuanSJ IS null then '' else ChuYuanSJ end as dateOfDischarge,\n" +
                "b.KeShiMC as dept,\n" +
                "a.RuYuanZD as diagnosis,\n" +
                "a.JiZhangKuan as cost\n" +
                "From LD_Register a,Dict_Depart b,PA_PatientInfo c\n" +
                "where a.ZhuYuanHao=c.ZhuYuanHao and a.BingQuBM=b.KeShiBM \n" +
                "and (c.medicalCardNumber=? or a.ZhuYuanHao=? or a.ZhuYuanID =?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, medicalCardNumber);
            preparedStatement.setString(2, admissionNumber);
            preparedStatement.setString(3, admissionNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);
            return map;
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
     * 门诊费用查询
     * @param medicalCardNumber
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String, Object> queryOutpatientCostDetail(String medicalCardNumber, String startTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select c.DanJuID as invoiceId,\n" +
                "c.DanJuRQ as time,\n" +
                "c.ShiShouZLJE as total,\n" +
                "'0533_0019' as hospitalCode,\n" +
                "'淄博市张店区中医院' as hospitalName,\n" +
                "c.XingMing as patientName,\n" +
                "e.HeSuanMC as type,\n" +
                "b.YaoMing as  item,\n" +
                "b.DanJia as  price,\n" +
                "b.Shuliang as  quantity,\n" +
                "b.JinE as  amount\n" +
                "From Out_Recipe a,Out_RecipeDetail b,Out_Invoice c,PA_PatientInfo d,Dict_CheckItem e\n" +
                "where a.JiuZhenID=b.JiuZhenID and a.ChuFangLH=b.ChuFangLH and b.HeSuanBM=e.HeSuanBM \n" +
                "and b.DanJuID=c.DanJuID \n" +
                "and c.JieSuanZT=1 and a.BingLiHao=d.BingLiHao\n" +
                "and (d.medicalCardNumber =? or d.JiuZhenKH=?) order by c.DanJuID";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, medicalCardNumber);
            preparedStatement.setString(2, medicalCardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");

            JSONArray jsonArray = new JSONArray(list);
            List<Map<String, Object>> detaList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> map3 = (Map<String, Object>) jsonArray.get(i);
                Map<String, Object> oneDate = new HashMap<>();
                oneDate.put("invoiceId", map3.get("invoiceId"));
                oneDate.put("time", map3.get("time"));
                oneDate.put("total", map3.get("total"));
                oneDate.put("hospitalCode", map3.get("hospitalCode"));
                oneDate.put("hospitalName", map3.get("hospitalName"));
                oneDate.put("patientName", map3.get("patientName"));
                List<Map<String, Object>> detailsList = new ArrayList<>();
                for (int j = 0; j < jsonArray.size(); j++) {
                    Map<String, Object> map1 = (Map<String, Object>) jsonArray.get(j);
                    if (map3.get("invoiceId").equals(map1.get("invoiceId"))) {
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("type", map1.get("type"));
                        map2.put("item", map1.get("item"));
                        map2.put("price", map1.get("price"));
                        map2.put("quantity", map1.get("quantity"));
                        map2.put("amount", map1.get("amount"));
                        detailsList.add(map2);
                    }
                }
                System.out.println("detailsList: " + detailsList);
                oneDate.put("details", detailsList);
                if (i != 0) {
                    Map<String, Object> map5 = (Map<String, Object>) jsonArray.get(i - 1);
                    if (!map3.get("invoiceId").equals(map5.get("invoiceId"))) {
                        detaList.add(oneDate);
                    }
                } else {
                    detaList.add(oneDate);
                }
                System.out.println("jsonArray: " + JSONUtil.parseArray(detaList));
            }
            System.out.println("返回值：" + detaList);
            map.put("data", detaList);
            return map;
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
     *查询患者就诊过的医生列表信息
     * @param medicalCardNumber
     * @param beginTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String, Object> getSeeklingDocList(String medicalCardNumber, String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select b.XingMing as  doctorName,\n" +
                "b.RenYuanBM as doctorCode,\n" +
                "e.KeShiMC as dept,\n" +
                "b.ZhiWu as post\n" +
                "From Out_Recipe a,Out_Invoice c,PA_PatientInfo d,Dict_Depart e,Dict_Personnel b\n" +
                "where a.JiuZhenID=c.JiuZhenID and a.YiShengBM =b.RenYuanBM\n" +
                "and c.JieSuanZT=1 and a.BingLiHao=d.BingLiHao and b.KeShiBM=e.KeShiBM \n" +
                "and (d.medicalCardNumber =? or d.JiuZhenKH=?)\n" +
                "and CONVERT(varchar(10),DanJuRQ ,120) between ? and ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, medicalCardNumber);
            preparedStatement.setString(2, medicalCardNumber);
            preparedStatement.setString(3, beginTime);
            preparedStatement.setString(4, endTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);
            return map;
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
     * 查询所有开放药品
     * @return
     */
    @Override
    public Map<String, Object> getOpenDrugList() {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select \n" +
                "b.YaoMing as chargeName,\n" +
                "max(a.PiHao) as gyzz,\n" +
                "b.GuiGe as ggmc,\n" +
                "b.DanWei as bzdw,\n" +
                "b.JiLiangDW as jldw\n" +
                "From Dr_StoreByBatch a,vDM_DS_List b,Dict_Depart c\n" +
                "where a.KeShiBM=c.KeShiBM and c.YuanQu=1\n" +
                "and a.YaoPinID=b.YaoPinID\n" +
                "and a.ZhangMianShu>0\n" +
                "group by b.YaoMing,b.GuiGe,b.JiLiangDW,b.DanWei";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);
            return map;
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
     * 查询医院开放的检验销售目录
     * @return
     */
    @Override
    public Map<String, Object> getOpenLISList() {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select \n" +
                "FeiYongNo as chargeCode,\n" +
                "FeiYongMC as chargeName\n" +
                "From Dict_HosCharge where \n" +
                "HeSuanBM='34' and YiYuanSF <>9999\n" +
                "and ZhuXiaoBZ=0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);
            return map;
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
     * 查询医院开放的检查销售目录
     * @return
     */
    @Override
    public Map<String, Object> getOpenCheckList() {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select \n" +
                "max(FeiYongNo) as chargeCode,\n" +
                "FeiYongMC as chargeName\n" +
                " From Dict_HosCharge where \n" +
                "HeSuanBM<>'34' and YiYuanSF <>9999\n" +
                "and ZhuXiaoBZ=0\n" +
                "group by FeiYongMC";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);
            return map;
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
     * 获取药品、检查、检验项目列表
     * @return
     */
    @Override
    public Map<String, Object> getOpenDocItem() {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select \n" +
                "b.YaoPinID as chargeId,\n" +
                "a.ShouJia as chargePrice,\n" +
                "b.YaoMing as chargeName\n" +
                "From Dr_StoreByBatch a,vDM_DS_List b,Dict_Depart c\n" +
                "where a.KeShiBM=c.KeShiBM and c.YuanQu=1\n" +
                "and a.YaoPinID=b.YaoPinID\n" +
                "and a.ZhangMianShu>0\n" +
                "group by b.YaoPinID,b.YaoMing,a.ShouJia\n" +
                "union all\n" +
                "select \n" +
                "FeiYongNo as chargeId,\n" +
                "yiyuansf as chargePrice,\n" +
                "FeiYongMC as chargeName\n" +
                "From Dict_HosCharge where \n" +
                "HeSuanBM='34' and YiYuanSF <>9999\n" +
                "and ZhuXiaoBZ=0\n" +
                "union all\n" +
                "select \n" +
                "FeiYongNo as chargeId,\n" +
                "yiyuansf as chargePrice,\n" +
                "FeiYongMC as chargeName\n" +
                " From Dict_HosCharge where \n" +
                "HeSuanBM<>'34' and YiYuanSF <>9999\n" +
                "and ZhuXiaoBZ=0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0 || list.get(0).size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            map.put("data", list);

            return map;
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
     * 查询报告主信息
     * @param
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String, Object> baogaoMain(List<String> stringg) {
        System.out.println(stringg.size());
        System.out.println(stringg);
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String medicalCardNumber = stringg.get(0);
        String startTime = stringg.get(1);
        String endTime = stringg.get(2);
        String sql = "select name as patientName,report_form_no as id,'0' as type,convert(varchar(19),author_Dtime,120) as time,author_name as doctor,class_name as item From Lis.hosdata.dbo.LabMaster a, pa_patientinfo b where  a.Patient_Id=b.JiuZhenKH and (b.JiuZhenKH='" + stringg.get(0) + "' or b.medicalCardNumber='" + stringg.get(0) + "')and convert(varchar(19),author_dtime,120) between '" + stringg.get(1) + "' and '" + stringg.get(2) + "'";
        System.out.println(sql);
        try {
            System.out.println(new java.util.Date());
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, stringg.get(0));
//            preparedStatement.setString(2, stringg.get(0));
//            preparedStatement.setString(3, stringg.get(1));
//            preparedStatement.setString(4, stringg.get(2));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            System.out.println("list长度：" + list.size());
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("patientName", list.get(0).get("patientName"));
            List<Map<String, Object>> reportList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map2 = list.get(i);
                map2.remove("patientName");
                reportList.add(map2);
            }
            JSONArray jsonArray = JSONUtil.parseArray(reportList);
            map1.put("report", jsonArray);
            map.put("data", map1);
            System.out.println(new java.util.Date());
            System.out.println("返回值：" + map);
            return map;
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
     * 查询报告明细
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String, Object> baogaoMingXi(List<String> stringg) {
        Map<String, Object> map = new HashMap<>();
        Connection connection = DaoUtil.getConnection();
        String msd = "";
        String sql = "select b.name as patientName,\n" +
                "sex_name as sex,\n" +
                "c.NianLing as age,\n" +
                "participant_dept as dept,\n" +
                "participant_dept as wardnumber,\n" +
                "'' as bednumber,\n" +
                "'' as specimenCategory,\n" +
                "'' as specimenNumber,\n" +
                "Participant_Name as doctor,\n" +
                "Participant_DTime as time,\n" +
                "Author_Name as executor,\n" +
                "Authenticator_Name as  auditor,\n" +
                "a.class_name as item,result_value as result,\n" +
                "Norm_lower_limit as  referValueLower,\n" +
                "norm_upper_limit as  referValueUpper,\n" +
                "'' as multiRowValue,\n" +
                "result_unit as unit\n" +
                "  From  Lis.hosdata.dbo.Lab_SubItem a,Lis.hosdata.dbo.LabMaster b,PA_PatientInfo c\n" +
                "where a.Report_Form_No=b.Report_Form_No and  b.Report_Form_No=? \n" +
                "and b.Patient_Id=c.JiuZhenKH\n" +
                "order by Serial_No";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, stringg.get(0));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);
            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "查询成功，但是没有信息");
                map.put("code", "10002");
                return map;
            }
            map.put("success", true);
            map.put("msg", "成功");
            map.put("code", "10001");
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> details = new ArrayList<>();
            data.put("patientName", list.get(0).get("patientName"));
            data.put("sex", list.get(0).get("sex"));
            data.put("age", list.get(0).get("age"));
            data.put("dept", list.get(0).get("dept"));
            data.put("bedNumber", list.get(0).get("bedNumber"));
            data.put("specimenCategory", list.get(0).get("specimenCategory"));
            data.put("specimenNumber", list.get(0).get("specimenNumber"));
            data.put("doctor", list.get(0).get("doctor"));
            data.put("time", list.get(0).get("time"));
            data.put("executor", list.get(0).get("executor"));
            data.put("auditor", list.get(0).get("auditor"));

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> detail = list.get(i);
                detail.remove("patientName");
                detail.remove("sex");
                detail.remove("age");
                detail.remove("dept");
                detail.remove("bedNumber");
                detail.remove("specimenCategory");
                detail.remove("specimenNumber");
                detail.remove("doctor");
                detail.remove("time");
                detail.remove("executor");
                detail.remove("auditor");
                details.add(detail);
            }
            data.put("details", details);
            map.put("data", data);
            System.out.println("返回值：" + map);
            return map;
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
     * 数据统计
     * @param stringg
     * @return
     */
    @Override
    public Map<String, Object> tongji(List<String> stringg) {
        Map<String, Object> map = new HashMap<>();
        String startTime = stringg.get(0);
        String endTime = stringg.get(1);
        Connection connection = DaoUtil.getConnection();
        String sql = "SELECT ((SELECT count(*) FROM t_card_recharge where convert(varchar(10),pay_time,120) between ? and ?) + (SELECT COUNT(*) FROM t_payment_message  where convert(varchar(10),pay_time,120) between ? and ?)) as outpatientTotalTrans,\n" +
                "(SELECT ISNULL(sum(pay_amount), 0) FROM t_card_recharge  where convert(varchar(10),pay_time,120) between ? and ?) + (SELECT isnull(sum(amount), 0) from t_payment_message where convert(varchar(10),pay_time,120) between ? and ?)  as outpatientTotalAmount,\n" +
                "(SELECT ISNULL(COUNT(*), 0) from t_payment_pledge where convert(varchar(10),pay_time,120) between ? and ?) as inpatientTotalTrans,\n" +
                "(select ISNULL(sum(amount), 0) from t_payment_pledge where convert(varchar(10),pay_time,120) between ? and ?) as inpatientTotalAmount,\n" +
                "(select sum(a.shuliang) from (select convert(varchar(10),danjurq,120)as ab, count(distinct medicalCardNumber) as shuliang from out_recipe a,out_invoice b,pa_patientinfo c where a.jiuzhenid=b.jiuzhenid and b.jiesuanzt=1 and a.binglihao=c.binglihao and convert(varchar(10),danjurq,120) between ? and ? group by convert(varchar(10),danjurq,120)) as a) as medicalTotal,\n" +
                "(select ISNULL(COUNT(*), 0) from PA_PatientInfo where len(JiuZhenKH)>10 and convert(varchar(10),serialno,120) between ? and ?) as regCardAmount,\n" +
                "(select ISNULL(COUNT(*), 0) from PA_PatientInfo where len(JiuZhenKH)>10 and convert(varchar(10),serialno,120) between ? and ?) as totalCardAmount";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, startTime);
            preparedStatement.setString(2, endTime);
            preparedStatement.setString(3, startTime);
            preparedStatement.setString(4, endTime);
            preparedStatement.setString(5, startTime);
            preparedStatement.setString(6, endTime);
            preparedStatement.setString(7, startTime);
            preparedStatement.setString(8, endTime);
            preparedStatement.setString(9, startTime);
            preparedStatement.setString(10, endTime);
            preparedStatement.setString(11, startTime);
            preparedStatement.setString(12, endTime);
            preparedStatement.setString(13, startTime);
            preparedStatement.setString(14, endTime);
            preparedStatement.setString(15, startTime);
            preparedStatement.setString(16, endTime);
            preparedStatement.setString(17, startTime);
            preparedStatement.setString(18, endTime);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> mapList = DaoUtil.getresultSet(resultSet);
            if (mapList.size() == 0){
                map.put("success", false);
                map.put("msg", "查询成功但是没有记录");
                map.put("code", "10002");
                return map;
            } else {
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("outpatientTotalAmount", mapList.get(0).get("outpatientTotalAmount"));
                map.put("outpatientTotalTrans", mapList.get(0).get("outpatientTotalTrans"));
                map.put("inpatientTotalAmount", mapList.get(0).get("inpatientTotalAmount"));
                map.put("inpatientTotalTrans", mapList.get(0).get("inpatientTotalTrans"));
                map.put("regCardAmount", mapList.get(0).get("regCardAmount"));
                map.put("totalCardAmount", mapList.get(0).get("totalCardAmount"));
                map.put("medicalTotal", mapList.get(0).get("medicalTotal"));
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
     * 住院费用明细
     * @param stringg
     * @return
     */
    @Override
    public Map<String, Object> queryHospitalizationDetail(List<String> stringg) {
        System.out.println(stringg);
        Connection connection = DaoUtil.getConnection();
        Map<String, Object> map = new HashMap<>();
        String id = stringg.get(0);

        System.out.println("id:" + id);
        String sql = "select a.XingMing as name, a.XingBie as sex, c.XingMing as doctor, a.JiZhangKuan as total,a.ZhuYuanHao as admissionNumber,a.RuYuanZD as diagnosis, CONVERT(varchar(19),b.jiesuanrq,121) as time, b.YaoMing as item, d.HeSuanMC as type, b.DanJia as price, b.ShuLiang as quantity, b.JinE as amount  From LD_Register a,LD_ChargeDetail b,Dict_Personnel c,Dict_CheckItem d,pa_patientinfo e where a.ZhuYuanID=b.ZhuYuanID and b.JieSuanPC=a.JieZhangPC and b.HeSuanBM=d.HeSuanBM and a.YiShengBM =c.RenYuanBM and a.zhuyuanhao=e.zhuyuanhao and (e.medicalcardnumber=? or a.ZhuYuanID=?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Map<String, Object>> list = DaoUtil.getresultSet(resultSet);

            if (list.size() == 0) {
                map.put("success", false);
                map.put("msg", "没有记录");
                map.put("code", "10002");
                System.out.println("住院费用明细返回值：" + map);
                return map;
            } else {
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
                map.put("success", true);
                map.put("msg", "成功");
                map.put("code", "10001");
                map.put("data", map1);
                System.out.println("住院费用明细返回值：" + map);
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
}
