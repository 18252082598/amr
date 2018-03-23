package com.joymeter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    
    public static final String REALNAME = "realName";//实名
    public static final String STORENAME = "storeName";//仓库名
    public static final String SIZE = "size";
    public static final String SUFFIX = "suffix";//后缀
    public static final String CONTENTTYPE = "contentType";//内容类型
    public static final String CREATETIME = "createTime";//创建时间
    public static final String UPLOADDIR = "uploadDir/";//上传目录


    /**
     * @param name
     * @return
     */
    public static String rename(String name) {
        Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        Long random = (long) (Math.random() * now);
        String fileName = now + "" + random;
        if (name.contains(".")) {
            fileName += name.substring(name.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * @param name
     * @return
     */
    public static String excelName(String name) {
        String prefix;
        if (name.contains(".")) {
            prefix = name.substring(0, name.lastIndexOf("."));
        } else {
            prefix = name;
        }
        return prefix + ".xlsx";
    }
}
