package cm.deity.excel.exception;

/**
 * excel 空行空列异常
 * @Author : chenming
 * @Description :
 * @Date : 2018/7/19 13:43
 * @VERSION : 1.0
 */
public class ExcelNullRowColumnException extends RuntimeException{
    public ExcelNullRowColumnException(String message) {
        super(message);
    }
}
