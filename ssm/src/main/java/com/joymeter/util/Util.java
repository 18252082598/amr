package com.joymeter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class Util {

    public static String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bt = md.digest(password.getBytes());
            return Base64.encodeBase64String(bt);
        } catch (NoSuchAlgorithmException e) {
            java.util.logging.Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public static List<String> traverseFolder(String path) {
        List<String> fileNames = new ArrayList();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (file.length() != 0) {
                for (File file2 : files) {
                    String fileName = file2.getName();
                    fileNames.add(fileName);
                }
            }
        }
        return fileNames;
    }

    /**
     *
     * @return
     */
    public static SimpleDateFormat getDf() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df;
    }

    /**
     *
     * @param path
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Connection getConnection(String path) throws ClassNotFoundException,
            SQLException, FileNotFoundException, IOException {
        Properties pps = new Properties();
        pps.load(new FileInputStream(path + "/WEB-INF/classes/jdbc.properties"));
        Class.forName(pps.getProperty("driverClassName"));
        Connection conn = DriverManager.getConnection(
                pps.getProperty("url"),
                pps.getProperty("username"),
                pps.getProperty("password"));
        return conn;
    }

    /**
     * 将所有用户请求的参数以参数名（区分大小写）按 ASCII码从小到大排序排序，如果参数的值为空则不参与排序。
     *
     * @param urlEncode 是否需要URLENCODE true：需要
     * @param params 需要排序的参数
     * @return
     */
    public static String increasByASCII(Map<String, String> params, boolean urlEncode) {
        String buff = "";
        try {
            List<Map.Entry<String, String>> list = new ArrayList<>(params.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
            Collections.sort(list, (Map.Entry<String, String> o1, Map.Entry<String, String> o2) -> (o1.getKey()).compareTo(o2.getKey()));
            // 构造URL 键值对的格式  
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : list) {
                if (StringUtils.isNotBlank(item.getValue())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buf.append(key).append("=").append(val);
                    buf.append("&");
                }
            }
            buff = buf.toString();
            if (!buff.isEmpty()) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, e);
        }
        return buff;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("abc12345:" + md5("abc12345"));
        System.out.println("admin:" + md5("admin"));
        System.out.println(Math.round(3.1415 * 1000) / 1000d);
        System.out.println(new java.text.DecimalFormat("0.000").format(3.1415));
    }
}
