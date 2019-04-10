package cm.deity.excel.poi.abstractvo;

import cm.deity.excel.inter.ICellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/7/10 10:53
 * @VERSION : 1.0
 */
public abstract class AbstractCmCellRangeAddress extends CellRangeAddress implements ICellRangeAddress {
    public AbstractCmCellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
    }
}
