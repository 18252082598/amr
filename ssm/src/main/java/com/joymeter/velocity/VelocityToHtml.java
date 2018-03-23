package com.joymeter.velocity;

import com.joymeter.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityToHtml {

    static String vmHTMLPath = "vm/html/";
    static String vmJSPath = "vm/js/";
    static VelocityContext _Context;
    static VelocityEngine _Ve = new VelocityEngine();

    /**
     *
     * @throws Exception
     */
    public static void initVelocityEngine() throws Exception {
        _Ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        _Ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        _Ve.init();
    }

    /**
     *
     * @param language
     * @return
     */
    public static String changeDir(String language) {
        switch (language) {
            case "Chinese":
                return "cn";
            case "English":
                return "en";
            default:
                return "";
        }
    }

    /**
     *
     * @param language
     * @param reqPath
     */
    public static void createDir(String language, String reqPath) {
        File file = new File(reqPath + changeDir(language));
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     *
     * @param reqPath
     * @param language
     */
    public static void loadProperties(String reqPath, String language) {
        VelocityToHtml._Context = new VelocityContext();
        Properties pps = new Properties();
        try {
            if (language.equals("Chinese")) {
                pps.load(new FileInputStream(reqPath + "language/chinese.properties"));
            } else if (language.equals("English")) {
                pps.load(new FileInputStream(reqPath + "language/english.properties"));
            }
            Enumeration<?> enumer = pps.propertyNames();
            while (enumer.hasMoreElements()) {
                String strKey = (String) enumer.nextElement();
                String strValue = pps.getProperty(strKey);
                VelocityToHtml._Context.put(strKey, strValue);
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     *
     * @param req
     * @param language
     */
    public static void generateAllHtml(HttpServletRequest req, String language) {
        FileOutputStream fos = null;
        PrintWriter pw = null;
        String reqPath = req.getSession().getServletContext().getRealPath("/");
        try {
            initVelocityEngine();
            createDir(language, reqPath);
            List<String> htmlNames = Util.traverseFolder(reqPath + "WEB-INF/classes/" + vmHTMLPath);
            for (String htmlName : htmlNames) {
                if (htmlName == null || htmlName.isEmpty()) {
                    continue;
                }
                Template t = _Ve.getTemplate(vmHTMLPath + htmlName, "UTF-8");
                loadProperties(reqPath, language);
                StringWriter writer = new StringWriter();
                t.merge(VelocityToHtml._Context, writer);
                fos = new FileOutputStream(reqPath + changeDir(language) + "/" + htmlName);
                pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"), true);
                pw.println(writer.toString());
            }
        } catch (Exception ex) {
            Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 根据模板生成JS
     *
     * @param req
     * @param language
     */
    public static void generateAllJs(HttpServletRequest req, String language) {
        FileOutputStream fos = null;
        PrintWriter pw = null;
        String reqPath = req.getSession().getServletContext().getRealPath("/");
        try {
            initVelocityEngine();
            createDir(language, reqPath);
            List<String> jsNames = Util.traverseFolder(reqPath + "WEB-INF/classes/" + vmJSPath);
            for (String jsName : jsNames) {
                if (jsName == null || jsName.isEmpty()) {
                    continue;
                }
                Template t = _Ve.getTemplate(vmJSPath + jsName, "UTF-8");
                loadProperties(reqPath, language);
                StringWriter writer = new StringWriter();
                t.merge(VelocityToHtml._Context, writer);
                fos = new FileOutputStream(reqPath + changeDir(language) + "/" + jsName);
                pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"), true);
                pw.println(writer.toString());
            }
        } catch (Exception ex) {
            Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(VelocityToHtml.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
