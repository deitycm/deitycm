package cm.deity.excel.poi.vo;

import cm.deity.excel.poi.abstractvo.AbstractCmCellRangeAddress;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/7/10 10:52
 * @VERSION : 1.0
 */
public class CmCellRangeAddress extends AbstractCmCellRangeAddress {
    public CmCellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        super(firstRow, lastRow, firstCol, lastCol);
    }

    @Override
    public CmCellRangeAddress clone(){
        try {
            return (CmCellRangeAddress)super.clone();
        } catch (CloneNotSupportedException e) {
            return new CmCellRangeAddress(0,0,0,0);
        }
    }
}
