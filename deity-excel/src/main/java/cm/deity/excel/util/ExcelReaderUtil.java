package cm.deity.excel.util;

import cm.deity.excel.exception.NonePrintException;
import cm.deity.excel.poi.abstractvo.IExcelReader;
import cm.deity.excel.poi.excel2003.Excel2003Reader;
import cm.deity.excel.poi.excel2007.Excel2007Reader;
import cm.deity.excel.poi.vo.CmCell;
import cm.deity.excel.poi.vo.CmRow;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/14 10:08
 * @VERSION : 1.0
 */
public class ExcelReaderUtil {
    //获取excel读取器
    public static IExcelReader getIExcelReader(ExcelReaderConfig excelCheckConfig) throws NonePrintException {
        /**
         * 获取读取策略
         */
        if(excelCheckConfig == null){
            throw new NonePrintException(ErrorCodeDesc.EXCEL_CONFIG_IS_NULL.getCode(), ErrorCodeDesc.EXCEL_CONFIG_IS_NULL.getDesc());
        }

        if(ObjectUtils.isEmpty(excelCheckConfig.getFile())){
            throw new NonePrintException(ErrorCodeDesc.EXCEL_FILE_IS_NULL.getCode(), ErrorCodeDesc.EXCEL_FILE_IS_NULL.getDesc());
        }

        if(StringUtils.isEmpty(excelCheckConfig.getFileName())){
            throw new NonePrintException(ErrorCodeDesc.EXCEL_FILE_NAME_IS_NULL.getCode(), ErrorCodeDesc.EXCEL_FILE_NAME_IS_NULL.getDesc());
        }

        IExcelReader excelReader;
        if(ConstantUtil.EXCEL07_EXTENSION.equals(excelCheckConfig.getExcelModel())){
            excelReader = new Excel2007Reader();
        }else{
            excelReader = new Excel2003Reader();
        }
        return excelReader;
    }

    public static void validateSheetIndex(int listSize, int index) {
        int lastSheetIx = listSize - 1;
        if (index < 0 || index > lastSheetIx) {
            throw new IllegalArgumentException("Sheet index (" + index + ") is out of range (0.." + lastSheetIx + ")");
        }
    }

    /**
     * 补全缺失的尾列
     */
    public static void addForRowLast(CmRow nowRow, CmCell nowCell, int maxColumn) {
        /**
         * 补全缺失的尾列
         */
        if(nowRow!=null){
            int lastCellNum = nowRow.getILastCellNum();
            if(lastCellNum != -1 && maxColumn-(lastCellNum+1) > 0){
                for(int i = 0; i < maxColumn-(lastCellNum+1); i++){
                    CmCell cell = nowCell.clone();
                    cell.setICellValue(ConstantUtil.CELL_NULL_STR);
                    nowRow.addCell(cell);
                }
            }
        }
    }

    /**
     * 判断是否使用redis
     * @return
     */
    public static boolean isUseRedis(){
        //todo
        //return !StringUtils.isEmpty(ConfigMgr.getStringCfg(ConstantUtil.REDIS_CP_NAME, ConstantUtil.REDIS_ADDRESS,null));
        return false;
    }
}