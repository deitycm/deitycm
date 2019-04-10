package cm.deity.base.utils.entrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/4/20 19:18
 * @VERSION : 1.0
 */
public class AESUtil {
    private static String aesPassword = MD5Util.encrypt32("deitycm");
    public static void main(String[] args) throws UnsupportedEncodingException {
        //模拟获取敏感数据
        System.out.println("加密前：cmas123452136");
        System.out.println("密文：" + encrypt("cmas123452136"));
        //解密
        System.out.println("解密后：" + decrypt(encrypt("cmas123452136")));
    }

    /**
     * 加密并返回16进制字符串
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return HexadecimalUtil.parseByte2HexStr(encrypt(content, aesPassword));
    }

    public static String decrypt(String content) {
        return new String(decrypt(content, aesPassword));
    }


    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String content, String password) {
       return decrypt(HexadecimalUtil.hexStringToByteArray(content), password);
    }

    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}