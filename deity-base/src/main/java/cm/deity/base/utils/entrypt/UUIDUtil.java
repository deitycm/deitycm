package cm.deity.base.utils.entrypt;

import java.util.UUID;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/3/30 9:30
 * @VERSION : 1.0
 */
public class UUIDUtil {
    public static String getUUID32(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}