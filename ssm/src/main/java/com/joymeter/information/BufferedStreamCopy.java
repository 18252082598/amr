package com.joymeter.information;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 利用字节流的缓冲区进行二进制文件的复制 
 * @author joymeter
 *
 */
public class BufferedStreamCopy {
    public static void main(String[] args ) {  
        
        String bin = "D:\\IO\\1.jpg";    
        
        String copy ="D:\\IO\\2.jpg";  
           
        FileInputStream i = null;  
        FileOutputStream o = null;  
        BufferedInputStream bi = null;  
        BufferedOutputStream bo = null;  
           
        try {  
            i = new FileInputStream(bin);  
            o = new FileOutputStream(copy);  
            bi = new BufferedInputStream(i);  
            bo = new BufferedOutputStream(o);  
               
            byte[] buf = new byte[1024];  
            int temp = 0;  
            while((temp = bi.read(buf)) != -1) {  
                bo.write(buf,0,temp);  
            }  
               
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if(bi != null) {  
                try {  
                    i.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if(bo != null) {  
                try {  
                    o.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}
