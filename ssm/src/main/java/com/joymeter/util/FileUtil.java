package com.joymeter.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static final String REALNAME = "realName";
    public static final String STORENAME = "storeName";
    public static final String SIZE = "size";
    public static final String SUFFIX = "suffix";
    public static final String CONTENTTYPE = "contentType";
    public static final String CREATETIME = "createTime";
    public static final String UPLOADDIR = "uploadDir/";

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
