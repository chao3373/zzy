package com.shenke.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/6/3 15:20
 * @Description:
 */
public class DaoUtil {
    static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String dbURL = "jdbc:sqlserver://192.168.56.132:1433;DatabaseName=zyyhis";
    static final String Name = "sa";
    static final String Pwd = "his168?";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbURL, Name, Pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取查询的结果集
     * @param resultSet
     * @return
     */
    public static List<Map<String, Object>> getresultSet(ResultSet resultSet) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Map<String, Object> map1 = new HashMap<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    map1.put(metaData.getColumnName(i), resultSet.getString(i));
                }
                System.out.println(map1);
                list.add(map1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

//    public static PreparedStatement getPreparedStatement(Connection conn, String sql) {
//        try {
//            return conn.prepareStatement(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /***
//     * 调用存储过程
//     * @param conn
//     * @param sql
//     * @return
//     */
//    public static CallableStatement getCallableStatement(Connection conn, String sql) {
//        try {
//            return conn.prepareCall(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    public static void main(String[] args) {

//        PreparedStatement ps = null;
//        Connection conn = null;
//        ResultSet rs = null;
//
//        try {
//            Class.forName(driverName);
//            conn = DriverManager.getConnection(dbURL, Name, Pwd);
//            System.out.println("连接成功");
//            CallableStatement callableStatement = conn.prepareCall("{? = call WeiXin_InsertCard(?, ?, ?, ?, ?, ?, ?)}");
//            callableStatement.registerOutParameter(1, Types.NUMERIC);
//            callableStatement.setObject(2, "bl1009909900211");
//            callableStatement.setObject(3, "158");
//            callableStatement.setObject(4, "156158");
//            callableStatement.setObject(5, "2019-03-01");
//            callableStatement.setObject(6, "地址");
//            callableStatement.setObject(7, "159158");
//            callableStatement.setObject(8, "159158");


//            callableStatement.registerOutParameter();
//            callableStatement.execute();
//            System.out.println(callableStatement.getInt(1));

//            ps = conn.prepareStatement("select * from Sys_Config");
//            rs = ps.executeQuery();
//            List<Map<String, Object>> list = new ArrayList<>();
//            while (rs.next()) {
//                Map<String, Object> map = new HashMap<>();
//                ResultSetMetaData metaData = rs.getMetaData();
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    map.put(metaData.getColumnName(i), rs.getString(i));
//                }
//                list.add(map);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("连接失败");
//        }
//    }
}
