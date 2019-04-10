package cm.deity.base.utils.entrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha 哈希摘要
 * @Author : chenming
 * @Description :
 * @Date : 2018/4/20 18:48
 * @VERSION : 1.0
 */
public class SHAUtil {
    public static void main(String[] args) {
        System.out.println(entryPtSHA("a"));
        System.out.println(entryPtSHA224("a"));
        System.out.println(entryPtSHA256("a"));
        System.out.println(entryPtSHA384("a"));
        System.out.println(entryPtSHA512("a"));
        System.out.println(entryPtSHA("a").length());
        System.out.println(entryPtSHA224("a").length());
        System.out.println(entryPtSHA256("a").length());
        System.out.println(entryPtSHA384("a").length());
        System.out.println(entryPtSHA512("a").length());
    }

    /**
     * 40位长度sha
     * @param str
     * @return
     */
    public static String entryPtSHA(String str){
        return entryPtSHA(str, "SHA");
        //return entryptSHA(str, "SHA-1");
    }

    /**
     * 56位长度sha
     * @param str
     * @return
     */
    public static String entryPtSHA224(String str){
        return entryPtSHA(str,  "SHA-224");
    }

    /**
     * 64位长度sha
     * @param str
     * @return
     */
    public static String entryPtSHA256(String str){
        return entryPtSHA(str, "SHA-256");
    }

    /**
     * 96位长度sha
     * @param str
     * @return
     */
    public static String entryPtSHA384(String str){
        return entryPtSHA(str, "SHA-384");
    }

    /**
     * 128位长度sha
     * @param str
     * @return
     */
    public static String entryPtSHA512(String str){
        return entryPtSHA(str, "SHA-512");
    }

    /**
     *  利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
    private static String entryPtSHA(String str, String provider){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance(provider);
            messageDigest.update(str.getBytes());
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}