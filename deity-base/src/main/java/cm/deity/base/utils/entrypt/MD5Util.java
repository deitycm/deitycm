package cm.deity.base.utils.entrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5文本摘要算法
 * @Author : chenming
 * @Description :
 * @Date : 2018/4/20 18:14
 * @VERSION : 1.0
 */
public class MD5Util {
    private static final char[] HEX = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static void main(String[] args) {
        System.out.println(encrypt32("a"));
        System.out.println(encrypt16("a"));
    }


    public static String encrypt32(String message){
        return encrypt(message);
    }

    public static String encrypt16(String message){
        return encrypt(message).substring(8,24);
    }

    private static String encrypt(String message){
        //申明使用MD5算法
        MessageDigest md5 = null;
        String encryptStr = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(message.getBytes());
            encryptStr = byte2str(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * 将字节数组转换成十六进制字符串
     * @param bytes
     * @return
     */
    private static String byte2str(byte []bytes){
        int len = bytes.length;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < len; i++) {
            byte byte0 = bytes[i];
            result.append(HEX[byte0 >>> 4 & 0xf]);
            result.append(HEX[byte0 & 0xf]);
        }
        return result.toString();
    }
}