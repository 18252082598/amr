/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Zhong Fuqiang
 * @version 1.0.0
 */
public class ZipUtil {

    /**
     * 输出压缩文件
     *
     * @param request
     * @param fileNames
     * @param out
     * @param title
     */
    public static void fileToZip(HttpServletRequest request, List<String> fileNames, OutputStream out, final String title) {
        try {
            File zip = new File(String.format("%s/%s.zip", request.getSession().getServletContext().getRealPath("/file"), getFileName(title)));// 压缩文件
            File srcfile[] = new File[fileNames.size()];
            for (int i = 0, n1 = fileNames.size(); i < n1; i++) {
                srcfile[i] = new File(fileNames.get(i));
            }
            ZipFiles(srcfile, zip);
            try (FileInputStream inStream = new FileInputStream(zip)) {
                byte[] buf = new byte[4096];
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    out.write(buf, 0, readLength);
                }
            }
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ZipUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 压缩文件
     *
     * @param srcfile 源文件
     * @param zipfile 目标文件
     */
    public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
        byte[] buf = new byte[1024];
        try {
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile))) {
                for (File srcfile1 : srcfile) {
                    try (FileInputStream in = new FileInputStream(srcfile1)) {
                        out.putNextEntry(new ZipEntry(srcfile1.getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ZipUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 输出文件名
     *
     * @param title
     * @return
     */
    public static String getFileName(final String title) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String f = title + format.format(date);
        return f;
    }
}
