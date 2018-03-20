package com.joymeter.information;

import java.io.FileInputStream;
import java.io.IOException;
/**
 * 字节流的读取
 * @author joymeter
 *
 */
public class InPutStream {
    public static void main(String[] args ) {  
        
        String path = "D:\\IO\\demo.txt";  
           
        FileInputStream i = null;  
           
        try {  
            i = new FileInputStream(path);  
               
            //方式一：单个字符读取  
            //需要注意的是，此处我用英文文本测试效果良好  
            //但中文就悲剧了，不过下面两个方法效果良好  
           /* int ch = 0;  
            while((ch=i.read()) != -1){  
                System.out.print((char)ch);  
            }  
               */
            //方式二：数组循环读取  
            
            byte[] buf = new byte[1024]; 
            int len = 0; 
            while((len = i.read(buf)) != -1) { 
                System.out.println(new String(buf,0,len)); 
            } 
             
               
               
            //方式三：标准大小的数组读取  
            /* 
            //定一个一个刚好大小的数组 
            //available()方法返回文件的字节数 
            //但是，如果文件过大，内存溢出，那就悲剧了 
            //所以，亲们要慎用！！！上面那个方法就不错 
            byte[] buf = new byte[i.available()]; 
            i.read(buf); 
            //因为数组大小刚好，所以转换为字符串时无需在构造函数中设置起始点 
            System.out.println(new String(buf)); 
            */  
               
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if(i != null) {  
                try {  
                    i.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
      
    }  
}
