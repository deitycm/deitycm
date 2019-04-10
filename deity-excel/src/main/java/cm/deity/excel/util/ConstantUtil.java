package cm.deity.excel.util;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/15 18:05
 * @VERSION : 1.0
 */
public class ConstantUtil {
    /**
     * excel2003扩展名
     */
    public static final String EXCEL03_EXTENSION = ".xls";

    /**
     * excel2007扩展名
     */
    public static final String EXCEL07_EXTENSION = ".xlsx";

    /**
     * excel控制默认 "" NULL
     */
    public static final String CELL_NULL_STR = "";

    /**
     * 用户驱动模式
     */
    public static final String USERMODEL = "USER_MODEL";

    /**
     * 事件驱动模式
     */
    public static final String EVENTMODEL = "EVENT_MODEL";
    /**
     * EXCEL日期格式
     */
    public static final String EXCEL_DATA_FPRMAT = "m/d/yy";

    /**
     * EXCEL日期格式,校验
     */
    public static final String JAVA_DATA_FPRMAT_EXCEL = "M/d/yy";

    /**
     * JAVA 日期格式
     */
    public static final String JAVA_DATA_FPRMAT = "yyyy-MM-dd";

    /**
     * EXCEL 时间格式
     */
    public static final String EXCEL_TIME_FPRMAT = "h:mm";

    /**
     * JAVA 时间格式
     */
    public static final String JAVA_TIME_FPRMAT = "HH:mm";

    /**
     * EXCEL 年月日时分 格式
     */
    public static final String EXCEL_DATE_TIME_FPRMAT = "m/d/yy h:mm";

    /**
     * JAVA 年月日时分 格式
     */
    public static final String JAVA_DATE_TIME_FPRMAT_EXCEL = "M/d/yy h:mm";

    /**
     * JAVA 年月日时分 格式
     */
    public static final String JAVA_DATE_TIME_FPRMAT = "yyyy-MM-dd HH:mm";

    /**
     * redis field
     */
    public static final String REDIS_FIELD = "excelRow";

    /**
     * redis 模块名称cpName
     */
    public static final String REDIS_CP_NAME = "wxqyhutil";


    /**
     * redis 地址
     */
    public static final String REDIS_ADDRESS = "jedis-addr";

    /**
     * redis过期时间
     */
    public static final int REDIS_OUT_TIME = 30*60;

    /**
     * 事件驱动分组 组长度
     */
    public static final int GROUP_ROW_SIZE = 2000;
}
