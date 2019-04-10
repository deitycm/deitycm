package cm.deity.excel.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelWriteUtil {
    /**
     * 判断单元格个数并添加到对应的工作簿表格
     * @param writeSheet
     * @param firstRow
     * @param lastRow
     * @param firstColumn
     * @param lastColumn
     */
    public static void addMergedRegion(Sheet writeSheet, int firstRow, int lastRow, int firstColumn, int lastColumn){
        if(writeSheet == null){
            return;
        }


        //单元格只有一个，不添加合并
        if(firstRow == lastRow && firstColumn == lastColumn){
            return;
        }
        writeSheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
    }
}
