package com.joymeter.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class PrintWriterUtil {
    public static void printStr(HttpServletResponse resp, String str) {
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.print(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        writer.flush();
        writer.close();
    }
}
