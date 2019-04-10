package cm.deity.excel.poi.abstractvo;

import cm.deity.excel.inter.IRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/25 15:37
 * @VERSION : 1.0
 */
public abstract class AbstractCmRow implements Row, IRow {
    @Override
    public Cell createCell(int i) {
        return null;
    }

    @Override
    public Cell createCell(int i, int i1) {
        return null;
    }

    @Override
    public void removeCell(Cell cell) {

    }

    @Override
    public void setRowNum(int i) {

    }

    @Override
    public int getRowNum() {
        return 0;
    }

    @Override
    public Cell getCell(int i) {
        return null;
    }

    @Override
    public Cell getCell(int i, MissingCellPolicy missingCellPolicy) {
        return null;
    }

    @Override
    public short getFirstCellNum() {
        return 0;
    }

    @Override
    public short getLastCellNum() {
        return 0;
    }

    @Override
    public int getPhysicalNumberOfCells() {
        return 0;
    }

    @Override
    public void setHeight(short i) {

    }

    @Override
    public void setZeroHeight(boolean b) {

    }

    @Override
    public boolean getZeroHeight() {
        return false;
    }

    @Override
    public void setHeightInPoints(float v) {

    }

    @Override
    public short getHeight() {
        return 0;
    }

    @Override
    public float getHeightInPoints() {
        return 0;
    }

    @Override
    public boolean isFormatted() {
        return false;
    }

    @Override
    public CellStyle getRowStyle() {
        return null;
    }

    @Override
    public void setRowStyle(CellStyle cellStyle) {

    }

    @Override
    public Iterator<Cell> cellIterator() {
        return null;
    }

    @Override
    public Sheet getSheet() {
        return null;
    }

    @Override
    public Iterator<Cell> iterator() {
        return null;
    }
}
