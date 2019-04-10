package cm.deity.excel.inter;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/26 9:38
 * @VERSION : 1.0
 */
public interface IWorkBook {
    /**
     * 获取sheet页
     * @param i
     * @return
     */
    ISheet getISheetAt(int i);

    /**
     * 获取sheet页数
     * @return
     */
    int getNumberOfSheets();

    /**
     * 添加sheet页
     * @param sheet
     */
    void addSheet(ISheet sheet);
}
