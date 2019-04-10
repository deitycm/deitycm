package cm.deity.excel.poi.abstractvo;

import cm.deity.excel.inter.IWorkBook;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/22 16:06
 * @VERSION : 1.0
 */
public abstract class AbstractCmWorkBook implements Workbook, IWorkBook {
    @Override
    public int getActiveSheetIndex() {
        return 0;
    }

    @Override
    public void setActiveSheet(int i) {}

    @Override
    public int getFirstVisibleTab() {
        return 0;
    }

    @Override
    public void setFirstVisibleTab(int i) {}

    @Override
    public void setSheetOrder(String s, int i) {}

    @Override
    public void setSelectedTab(int i) {}

    @Override
    public void setSheetName(int i, String s) {}

    @Override
    public String getSheetName(int i) {
        return null;
    }

    @Override
    public int getSheetIndex(String s) {
        return 0;
    }

    @Override
    public int getSheetIndex(Sheet sheet) {
        return 0;
    }

    @Override
    public Sheet createSheet() {
        return null;
    }

    @Override
    public Sheet createSheet(String s) {
        return null;
    }

    @Override
    public Sheet cloneSheet(int i) {
        return null;
    }

    @Override
    public int getNumberOfSheets() {
        return 0;
    }

    @Override
    public Sheet getSheetAt(int i) {
        return null;
    }

    @Override
    public Sheet getSheet(String s) {
        return null;
    }

    @Override
    public void removeSheetAt(int i) {

    }

    @Override
    public void setRepeatingRowsAndColumns(int i, int i1, int i2, int i3, int i4) {

    }

    @Override
    public Font createFont() {
        return null;
    }

    @Override
    public Font findFont(short i, short i1, short i2, String s, boolean b, boolean b1, short i3, byte b2) {
        return null;
    }

    @Override
    public short getNumberOfFonts() {
        return 0;
    }

    @Override
    public Font getFontAt(short i) {
        return null;
    }

    @Override
    public CellStyle createCellStyle() {
        return null;
    }

    @Override
    public short getNumCellStyles() {
        return 0;
    }

    @Override
    public CellStyle getCellStyleAt(short i) {
        return null;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {

    }

    @Override
    public int getNumberOfNames() {
        return 0;
    }

    @Override
    public Name getName(String s) {
        return null;
    }

    @Override
    public Name getNameAt(int i) {
        return null;
    }

    @Override
    public Name createName() {
        return null;
    }

    @Override
    public int getNameIndex(String s) {
        return 0;
    }

    @Override
    public void removeName(int i) {

    }

    @Override
    public void removeName(String s) {

    }

    @Override
    public void setPrintArea(int i, String s) {

    }

    @Override
    public void setPrintArea(int i, int i1, int i2, int i3, int i4) {

    }

    @Override
    public String getPrintArea(int i) {
        return null;
    }

    @Override
    public void removePrintArea(int i) {

    }

    @Override
    public Row.MissingCellPolicy getMissingCellPolicy() {
        return null;
    }

    @Override
    public void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy) {

    }

    @Override
    public DataFormat createDataFormat() {
        return null;
    }

    @Override
    public int addPicture(byte[] bytes, int i) {
        return 0;
    }

    @Override
    public List<? extends PictureData> getAllPictures() {
        return null;
    }

    @Override
    public CreationHelper getCreationHelper() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void setHidden(boolean b) {

    }

    @Override
    public boolean isSheetHidden(int i) {
        return false;
    }

    @Override
    public boolean isSheetVeryHidden(int i) {
        return false;
    }

    @Override
    public void setSheetHidden(int i, boolean b) {

    }

    @Override
    public void setSheetHidden(int i, int i1) {

    }

    @Override
    public void addToolPack(UDFFinder udfFinder) {

    }

    @Override
    public void setForceFormulaRecalculation(boolean b) {

    }

    @Override
    public boolean getForceFormulaRecalculation() {
        return false;
    }
}
