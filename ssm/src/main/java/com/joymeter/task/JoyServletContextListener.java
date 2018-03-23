package com.joymeter.task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.joymeter.dao.UserDao;
import com.joymeter.service.CallBackService;
import com.joymeter.service.SaaSService;

public class JoyServletContextListener implements ServletContextListener {
    
    public static int Port = 8080;
    public static String Root = "amr";
    private static CallBackService callbackService = null;
    private static SaaSService saasService = null;
    private static final String CLASSPATH = JoyServletContextListener.class.getResource("/").getPath();
    public static  String mode = "Plus"; 

    
    //初始化
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            StringBuilder fileName = new StringBuilder(CLASSPATH);
            fileName.append("root.properties");
            InputStream inputStream = new FileInputStream(fileName.toString());
            Properties prop = new Properties();
            prop.load(inputStream);
            JoyServletContextListener.Port = Integer.valueOf(prop.getProperty("port", "8787"));
            JoyServletContextListener.Root = prop.getProperty("path", "amr");
            JoyServletContextListener.mode = prop.getProperty("name");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    public static String GetUrl() {
        return String.format("http://%s:%s/%s/user/receive.do",
                "127.0.0.1",
                JoyServletContextListener.Port,
                JoyServletContextListener.Root);
    }

    /**
     * 实例化回调函数
     *
     * @param userDao
     * @return
     */
    public static CallBackService instanceCallback(UserDao userDao) {
        InputStream inputStream = null;
        if (callbackService == null) {
            try {
                StringBuilder fileName = new StringBuilder(CLASSPATH);
                fileName.append("callBack.properties");
                inputStream = new FileInputStream(fileName.toString());
                Properties prop = new Properties();
                prop.load(inputStream);
                Map<String, String> params = new HashMap<>();
                prop.entrySet().stream().forEach(entry -> {
                    params.put((String) entry.getKey(), (String) entry.getValue());
                });
                String package_name = prop.getProperty("package_name", null);
                if (package_name == null || package_name.isEmpty()) {
                    package_name = "com.joymeter.service.callbackImp.CallBackServiceImp";
                }
                Class cls = Class.forName(package_name);
                JoyServletContextListener.callbackService = (CallBackService) cls.newInstance();
                JoyServletContextListener.callbackService.setParams(userDao, params);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return JoyServletContextListener.callbackService;
    }

    
    /**
    *
    * @param userDao
    * @return
    */
   public static SaaSService instanceSaaS(UserDao userDao) {
       InputStream inputStream = null;
       if (JoyServletContextListener.saasService == null) {
           try {
               StringBuilder fileName = new StringBuilder(CLASSPATH);
               fileName.append("saas.properties");
               inputStream = new FileInputStream(fileName.toString());
               Properties prop = new Properties();
               prop.load(inputStream);
               Map<String, String> params = new HashMap<>();
               prop.entrySet().stream().forEach(entry -> {
                   params.put((String) entry.getKey(), (String) entry.getValue());
               });
               String package_name = prop.getProperty("package_name", null);
               if (package_name == null || package_name.isEmpty()) {
                   package_name = "com.joymeter.service.saasImp.SaaSServiceImp";
               }
               Class cls = Class.forName(package_name);
               JoyServletContextListener.saasService = (SaaSService) cls.newInstance();
               JoyServletContextListener.saasService.setParams(userDao, params);
           } catch (FileNotFoundException ex) {
               Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
               Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
           } finally {
               try {
                   if (inputStream != null) {
                       inputStream.close();
                   }
               } catch (IOException ex) {
                   Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
       return JoyServletContextListener.saasService;
   }
   
   /**
    *
    * @param userDao
    * @return
    */
   public static SaaSService instancePayment(UserDao userDao) {
       InputStream inputStream = null;
       if (JoyServletContextListener.saasService == null) {
           try {
               StringBuilder fileName = new StringBuilder(CLASSPATH);
               fileName.append("payment.properties");
               inputStream = new FileInputStream(fileName.toString());
               Properties prop = new Properties();
               prop.load(inputStream);
               Map<String, String> params = new HashMap<>();
               prop.entrySet().stream().forEach(entry -> {
                   params.put((String) entry.getKey(), (String) entry.getValue());
               });
               String package_name = prop.getProperty("package_name", null);
               if (package_name == null || package_name.isEmpty()) {
                   package_name = "com.joymeter.service.saasImp.SaaSServiceImp";
               }
               Class cls = Class.forName(package_name);
               JoyServletContextListener.saasService = (SaaSService) cls.newInstance();
               JoyServletContextListener.saasService.setParams(userDao, params);
           } catch (FileNotFoundException ex) {
               Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
               Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
           } finally {
               try {
                   if (inputStream != null) {
                       inputStream.close();
                   }
               } catch (IOException ex) {
                   Logger.getLogger(JoyServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
       return JoyServletContextListener.saasService;
   }
  

    
}
