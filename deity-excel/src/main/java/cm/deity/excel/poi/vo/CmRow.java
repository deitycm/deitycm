package cm.deity.excel.poi.vo;

import cm.deity.excel.inter.ICell;
import cm.deity.excel.poi.abstractvo.AbstractCmRow;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/22 16:47
 * @VERSION : 1.0
 */
public class CmRow extends AbstractCmRow implements Cloneable, Serializable {
    private int rowNum;
    private List<ICell> cells;

    @Override
    public ICell getICell(int i) {
        if(!ObjectUtils.isEmpty(this.cells)){
            //校验当前下标是否越界
            if(i >= this.cells.size() ){
                return null;
            }else{
                return this.cells.get(i);
            }
        }else{
            return null;
        }
    }

    @Override
    public Cell getCell(int i) {
        if(!StringUtils.isEmpty(this.cells)){
            //校验当前下标是否越界
            if(i >= this.cells.size() ){
                return null;
            }else{
                return (Cell) this.cells.get(i);
            }
        }else{
            return null;
        }
    }

    @Override
    public short getFirstCellNum() {
        return 0;
    }

    @Override
    public int getIFirstCellNum() {
        return 0;
    }

    @Override
    public int getILastCellNum() {
        if(ObjectUtils.isEmpty(cells)){
            return -1;
        }
        return this.cells.size()-1;
    }

    @Override
    public short getLastCellNum() {
        if(ObjectUtils.isEmpty(cells)){
            return (short)-1;
        }
        return (short) (this.cells.size());
    }

    @Override
    public void addCell(ICell cell) {
        if(this.cells == null){
            this.cells = new ArrayList<ICell>();
        }
        this.cells.add(cell);
    }

    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public int getRowNum() {
        return rowNum;
    }

    public List<ICell> getCells() {
        return cells;
    }

    public void setCells(List<ICell> cells) {
        this.cells = cells;
    }

    @Override
    public CmRow clone(){
        try {
            return (CmRow)super.clone();
        } catch (CloneNotSupportedException e) {
            return new CmRow();
        }
    }
}
