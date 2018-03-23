/*
 */
package com.joymeter.util;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
/**
 * AES-128-CBC加密模式
 * 填充方式 PKCS5Padding
 * @author yinhf
 */
public class AesCBC {

    private static final String KEY = "1234567890123456";
    private static final String IVPARAMETER= "0123456789ABCDEF";
    private static AesCBC instance = null;

    private AesCBC() {

    }

    /**
     *
     * @return
     */
    public static AesCBC getInstance() {
        if (instance == null) {
            instance = new AesCBC();
        }
        return instance;
    }

    /**
     * 
     * @param inputStr 需要加密的明文
     * @param encodingFormat 编码格式
     * @param key 密钥
     * @param ivParameter 偏移量
     * @return 
     * @throws Exception
     */
    public String encrypt(String context, String encodingFormat) throws Exception {
        //使用cipher 实例化对象，参数格式为“算法/工作模式/填充模式”
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //获取密钥字节信息
        byte[] raw = KEY.getBytes();
        //创建密钥空间
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //获取偏移量字节信息
        IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        //加密
        byte[] encrypted = cipher.doFinal(context.getBytes(encodingFormat));
        return URLEncoder.encode(Base64.encodeBase64String(encrypted),"utf-8") ;
    }

    /**
     *
     * @param inputStr 需要解密的密文
     * @param encodingFormat 编码格式
     * @param key 密钥
     * @param ivParameter 偏移量
     * @return
     * @throws Exception
     */
    public String decrypt(String context, String encodingFormat) throws Exception {
        try {
            byte[] raw = KEY.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IVPARAMETER.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            context= URLDecoder.decode(context, "utf-8");
            byte[] encrypted =Base64.decodeBase64(context);       
            byte[] original = cipher.doFinal(encrypted);     
            return new String(original, encodingFormat);
        } catch (Exception ex) {
            Logger.getLogger(RSACoder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }   
    }
    /**
     *
     * @param argString
     * @throws Exception
     */
    public static void main(String argString[]) throws Exception {
        String inputStr = "01234567890123456456456";
        System.err.println("加密前: " + inputStr);
        AesCBC as = new AesCBC();
        String after = as.encrypt(inputStr, "utf-8");
        System.out.println("加密数据: " + after);
        String decrypt =  as.decrypt(after, "utf-8");
        System.out.println("解密数据: " + decrypt);
    }
}
