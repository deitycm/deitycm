package cm.deity.excel.poi.util;

import org.springframework.stereotype.Component;

/**
 * excel 导入配置类
 */
@Component
public class ExcelDBConfig {
    /**
     * 系统支持最大行
     */
    //@DBConfig(configName = "maxExcelRow",defaultValue = "10000",describe = "excel导入最大允许行",componentName = "excel")
    public static Integer MAX_EXCEL_ROW = 10000;
    /**
     * 系统支持最大列
     */
    //@DBConfig(configName = "maxExcelColumn",defaultValue = "100",describe = "excel导入最大允许列",componentName = "excel")
    public static Integer MAX_EXCEL_COLUMN = 100;

    /**
     * 用户模式支持最大文件大小，默认2M
     */
    //@DBConfig(configName = "userMaxExcelLength",defaultValue = "2097152",describe = "excel用户模式导入最大支持文件大小",componentName = "excel")
    public static Long USER_MAX_EXCEL_LENGTH = 2097152L;

    /**
     * 事件模式支持最大文件大小，默认10M
     */
    //@DBConfig(configName = "eventMaxExcelLength",defaultValue = "10485760",describe = "excel事件模式导入最大支持文件大小",componentName = "excel")
    public static Long EVENT_MAX_EXCEL_LENGTH = 10485760L;

    /**
     * 用户模式支持最大行
     */
    //@DBConfig(configName = "readTypeRow",defaultValue = "2000",describe = "excel导入用户模式支持最大行",componentName = "excel")
    public static Integer READ_TYPE_ROW = 2000;
    /**
     * 用户模式支持最大列
     */
    //@DBConfig(configName = "readTypeColumn",defaultValue = "50",describe = "excel导入用户模式支持最列",componentName = "excel")
    public static Integer READ_TYPE_COLUMN = 50;

    /**
     * 允许的excel连续空行
     */
    //@DBConfig(configName = "excelRowNullNum",defaultValue = "20",describe = "excel最大空行",componentName = "excel")
    public static Integer EXCEL_ROW_NULL_NUM = 20;

    /**
     * 允许的excel连续空列
     */
    //@DBConfig(configName = "excelColumnNullNum",defaultValue = "150",describe = "excel最大空列",componentName = "excel")
    public static Integer EXCEL_COLUMN_NULL_NUM = 150;
}
