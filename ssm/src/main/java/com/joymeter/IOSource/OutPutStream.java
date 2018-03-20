package com.joymeter.IOSource;

import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 字节流的写入
 * @author joymeter
 *
 */
public class OutPutStream {
    public static void main(String[] args ) {  
        
        String path = "D:\\IO\\demo.txt";   
           
        FileOutputStream o = null;  
           
        try {  
            o = new FileOutputStream(path);  
            String str = "Nerxious is a good boy\r\n";  
            byte[] buf = str.getBytes();  
            //也可以直接使用o.write("String".getBytes());  
            //因为字符串就是一个对象，能直接调用方法  
            o.write(buf);  
               
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if(o != null) {  
                try {  
                    o.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
      
    }  
}
