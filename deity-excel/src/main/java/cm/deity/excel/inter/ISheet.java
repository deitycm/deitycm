package cm.deity.excel.inter;


import cm.deity.excel.poi.vo.CmCellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/26 9:39
 * @VERSION : 1.0
 */
public interface ISheet {
    /**
     * 获取行
     * @param i
     * @return
     */
    IRow getIRow(int i);

    /**
     * 获取最后一行
     * @return
     */
    int getIFirstRowNum();

    /**
     * 获取最后一行
     * @return
     */
    int getILastRowNum();

    /**
     * 获取合并单元格数量
     * @return
     */
    int getNumMergedRegions();

    /**
     * 根据序号获取一个单元格
     * @param i
     * @return
     */
    CellRangeAddress getMergedRegion(int i);

    /**
     * 获取sheetName
     * @return
     */
    String getSheetName();

    /**
     * 设置sheet name
     * @param sheetName
     */
    void setSheetName(String sheetName);

    /**
     * 获取总行数
     * @return
     */
    int getTotalRow();

    /**
     * 设置总行数
     * @param totalRow
     */
    void setTotalRow(int totalRow);

    /**
     * 获取总列数
     * @return
     */
    int getTotalColumn();

    /**
     * 设置总列数
     * @param totalColumn
     */
    void setTotalColumn(int totalColumn);

    /**
     * 获取行key，用于分组保存row
     * @return
     */
    List<String> getIRowKeys();

    /**
     * 设置行key
     * @return
     */
    void setRowKeys(List<String> rowKeys);

    /**
     * 设置单元格
     * @param mergedRegions
     */
    void setMergedRegions(List<CmCellRangeAddress> mergedRegions);

    /**
     * 清空当前sheet下的row
     */
    void clearRows();
}
