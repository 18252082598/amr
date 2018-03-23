package com.joymeter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

public class DownloadUtil {

    /**
     * 
     * @param inStream
     * @param os 
     */
    public static void downloadLocal(InputStream inStream, OutputStream os) {
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0) {
                os.write(b, 0, len);
            }
            inStream.close();
            os.close();
        } catch (IOException e) {
            java.util.logging.Logger.getLogger(DownloadUtil.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
