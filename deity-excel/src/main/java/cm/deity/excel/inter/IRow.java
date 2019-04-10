package cm.deity.excel.inter;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/26 9:39
 * @VERSION : 1.0
 */
public interface IRow {
    /**
     * 获取cell
     * @param i
     * @return
     */
    ICell getICell(int i);

    /**
     * 获取第一个cell列号
     * @return
     */
    int getIFirstCellNum();

    /**
     * 获取当前行最后一个cell的列号
     * @return
     */
    int getILastCellNum();

    /**
     * 添加单元格
     * @param cell
     */
    void addCell(ICell cell);

    /**
     * 设置行号
     * @param rowNum
     */
    void setRowNum(int rowNum);

    /**
     * 获取行号
     * @return
     */
    int getRowNum();
}
