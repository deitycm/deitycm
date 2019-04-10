package cm.deity.excel.poi.vo;

import cm.deity.excel.poi.abstractvo.AbstractCmCell;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/25 15:55
 * @VERSION : 1.0
 */
public class CmCell extends AbstractCmCell implements Cloneable, Serializable {
    private Object cellValue;
    private int index;
    private int rowIndex;
    @Override
    public int getColumnIndex() {
        return index;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    public Object getICellValue() {
        return cellValue;
    }

    public void setICellValue(Object cellValue) {
        this.cellValue = cellValue;
    }

    @Override
    public String getStringCellValue() {
        return cellValue==null ? null : String.valueOf(cellValue);
    }

    @Override
    public Date getDateCellValue() {
        return cellValue==null ? null : (Date)cellValue;
    }

    @Override
    public boolean getBooleanCellValue() {
        return cellValue==null ? null : ((Boolean)cellValue);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public CmCell clone(){
        try {
            return (CmCell)super.clone();
        } catch (CloneNotSupportedException e) {
            return new CmCell();
        }
    }
}
