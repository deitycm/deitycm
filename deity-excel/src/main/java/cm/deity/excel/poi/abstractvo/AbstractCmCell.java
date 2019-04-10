package cm.deity.excel.poi.abstractvo;

import cm.deity.excel.inter.ICell;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/25 15:54
 * @VERSION : 1.0
 */
public abstract class AbstractCmCell implements Cell, ICell {
    @Override
    public int getColumnIndex() {
        return 0;
    }

    @Override
    public int getRowIndex() {
        return 0;
    }

    @Override
    public Sheet getSheet() {
        return null;
    }

    @Override
    public Row getRow() {
        return null;
    }

    @Override
    public void setCellType(int i) {

    }

    @Override
    public int getCellType() {
        return 0;
    }

    @Override
    public int getCachedFormulaResultType() {
        return 0;
    }

    @Override
    public void setCellValue(double v) {

    }

    @Override
    public void setCellValue(Date date) {

    }

    @Override
    public void setCellValue(Calendar calendar) {

    }

    @Override
    public void setCellValue(RichTextString richTextString) {

    }

    @Override
    public void setCellValue(String s) {

    }

    @Override
    public void setCellFormula(String s) throws FormulaParseException {

    }

    @Override
    public String getCellFormula() {
        return null;
    }

    @Override
    public double getNumericCellValue() {
        return 0;
    }

    @Override
    public Date getDateCellValue() {
        return null;
    }

    @Override
    public RichTextString getRichStringCellValue() {
        return null;
    }

    @Override
    public String getStringCellValue() {
        return null;
    }

    @Override
    public void setCellValue(boolean b) {

    }

    @Override
    public void setCellErrorValue(byte b) {

    }

    @Override
    public boolean getBooleanCellValue() {
        return false;
    }

    @Override
    public byte getErrorCellValue() {
        return 0;
    }

    @Override
    public void setCellStyle(CellStyle cellStyle) {

    }

    @Override
    public CellStyle getCellStyle() {
        return null;
    }

    @Override
    public void setAsActiveCell() {

    }

    @Override
    public void setCellComment(Comment comment) {

    }

    @Override
    public Comment getCellComment() {
        return null;
    }

    @Override
    public void removeCellComment() {

    }

    @Override
    public Hyperlink getHyperlink() {
        return null;
    }

    @Override
    public void setHyperlink(Hyperlink hyperlink) {

    }

    @Override
    public CellRangeAddress getArrayFormulaRange() {
        return null;
    }

    @Override
    public boolean isPartOfArrayFormulaGroup() {
        return false;
    }
}
