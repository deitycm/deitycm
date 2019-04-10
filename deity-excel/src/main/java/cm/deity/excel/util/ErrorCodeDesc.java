package cm.deity.excel.util;

/**
 * @Author : chenming
 * @Description :
 * @Date : "2018/7/11 14:07
 * @VERSION : 1.0
 */
public enum ErrorCodeDesc {
    EXCEL_READER_ERROR("201001","excel读取出现异常，请稍后再试"),
    EXCEL_LOCKED("201002","excel文件已加锁，请解除密码后重试"),
    EXCEL_ROW_NULL_LIMIT("201003","excel文件连续空行超限，最大允许%s行"),
    EXCEL_COLUMN_NULL_LIMIT("201004","excel文件连续空列超限，最大允许值%s列"),
    EXCEL_SHEET_DIMENSION_ERROR("201005","excel文件sheet行列信息异常"),
    EXCEL_CHECK_THROW("201006","检测excel文件主动抛出异常，不需要处理"),
    EXCEL_SYSTEM_READ_NUM("201007","超出系统读取excel最大行：%s 列：%s，当前行：%s 列：%s"),
    EXCEL_CONFIG_IS_NULL("201008","读取excel的配置不能为空"),
    EXCEL_FILE_IS_NULL("201009","excel文件为空"),
    EXCEL_FILE_NAME_IS_NULL("201010","excel文件名称为空"),
    EXCEL_MODULE_CONFIG_IS_OUT_RANGE("201011","当前模块配置超过系统读取excel最大行：%s 列：%s，当前行：%s 列：%s"),
    EXCEL_MODULE_ROW_COLUMN_IS_OUT_RANGE("201012","超出模块读取excel最大行：%s 列：%s，当前行：%s 列：%s"),
    EXCEL_SIZE_IS_OUT_RANGE("201013","当前excel大小超限，最大支持%sM")
    ;

    // 定义私有变量
    private String code ;
    private String desc ;

    // 构造函数，枚举类型只能为私有
    private ErrorCodeDesc( String _nCode, String _nDesc) {
        this.code = _nCode;
        this.desc = _nDesc;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code 要设置的 code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc 要设置的 desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
