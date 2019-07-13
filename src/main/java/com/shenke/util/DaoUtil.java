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
                list.add(map1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    /***
     * 获取查询的结果集List
     * @param resultSet
     * @return
     */
    public static List<Map<String, Object>> getresultSetList(ResultSet resultSet) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Map<String, Object> map1 = new HashMap<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    map1.put(metaData.getColumnName(i), resultSet.getString(i));
                    list.add(map1);
                }
            }
            System.out.println(list);
            return list;
        } catch (Exception e) {
            System.out.println(list);
            e.printStackTrace();
            return list;
        }
    }

}
