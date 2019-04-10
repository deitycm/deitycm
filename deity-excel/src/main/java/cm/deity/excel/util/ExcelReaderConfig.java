package cm.deity.excel.util;

import cm.deity.excel.poi.util.ExcelDBConfig;
import cm.deity.excel.poi.vo.CmRow;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/20 9:58
 * @VERSION : 1.0
 */
public class ExcelReaderConfig {
    public static Map<String, List<CmRow>> rowMap = new HashMap<>();
    /**
     * 文件
     */
    private File file;
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 模块允许最大行
     */
    private int moduleMaxRow;

    /**
     * 模块允许最大列
     */
    private int moduleMaxColumn;

    /**
     * 系统允许excel导入最大行
     */
    private int excelMaxRow;

    /**
     * 系统允许excel导入最大列
     */
    private int excelMaxColumn;

    /**
     * 用户模式与事件模式的行
     */
    private int readTypeRow;

    /**
     * 用户模式与事件模式的列
     */
    private int readTypeColumn;

    /**
     * 当前实际最大行
     */
    private int actualMaxRow;

    /**
     * 当前实际最大列
     */
    private int actualMaxColumn;

    /**
     * 读取模式  系统推荐（用户模式，事件驱动模式）
     */
    private String readerModel;

    /**
     * excel版本
     */
    private String excelModel;

    /**
     * 配置excel读取的默认构造方法
     * @param file
     */
    public ExcelReaderConfig(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;

        //如果模块不设置最大允许行列，则默认使用系统最大行列
        this.moduleMaxRow = ExcelDBConfig.MAX_EXCEL_ROW;
        this.moduleMaxColumn = ExcelDBConfig.MAX_EXCEL_COLUMN;

        this.excelMaxRow = ExcelDBConfig.MAX_EXCEL_ROW;
        this.excelMaxColumn = ExcelDBConfig.MAX_EXCEL_COLUMN;

        this.readTypeRow = ExcelDBConfig.READ_TYPE_ROW;
        this.readTypeColumn = ExcelDBConfig.READ_TYPE_COLUMN;

        if (fileName.toLowerCase().endsWith(ConstantUtil.EXCEL03_EXTENSION)) {
            this.excelModel = ConstantUtil.EXCEL03_EXTENSION;
        } else if (fileName.toLowerCase().endsWith(ConstantUtil.EXCEL07_EXTENSION)) {
            this.excelModel = ConstantUtil.EXCEL07_EXTENSION;
        }
    }

    /**
     * 配置excel读取的构造方法，可以设置各个模块可支持的最大行列，如果excel超出最大行列将抛出异常，并停止读取
     * @param file 文件
     * @param fileName 文件名称
     * @param moduleMaxRow 各个模块可以预设最大读取行
     * @param moduleMaxColumn 各个模块可以预设最大读取列
     */
    public ExcelReaderConfig(File file, String fileName, int moduleMaxRow, int moduleMaxColumn) {
        this.file = file;
        this.fileName = fileName;

        this.moduleMaxRow = moduleMaxRow;
        this.moduleMaxColumn = moduleMaxColumn;

        this.excelMaxRow = ExcelDBConfig.MAX_EXCEL_ROW;
        this.excelMaxColumn = ExcelDBConfig.MAX_EXCEL_COLUMN;

        this.readTypeRow = ExcelDBConfig.READ_TYPE_ROW;
        this.readTypeColumn = ExcelDBConfig.READ_TYPE_COLUMN;

        if (fileName.toLowerCase().endsWith(ConstantUtil.EXCEL03_EXTENSION)) {
            this.excelModel = ConstantUtil.EXCEL03_EXTENSION;
        } else if (fileName.toLowerCase().endsWith(ConstantUtil.EXCEL07_EXTENSION)) {
            this.excelModel = ConstantUtil.EXCEL07_EXTENSION;
        }
    }

    public void setReaderModel(String readerModel) {
        this.readerModel = readerModel;
    }

    public void setExcelModel(String excelModel) {
        this.excelModel = excelModel;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public int getModuleMaxRow() {
        return moduleMaxRow;
    }

    public int getModuleMaxColumn() {
        return moduleMaxColumn;
    }

    public int getExcelMaxRow() {
        return excelMaxRow;
    }

    public int getExcelMaxColumn() {
        return excelMaxColumn;
    }

    public int getReadTypeRow() {
        return readTypeRow;
    }

    public int getReadTypeColumn() {
        return readTypeColumn;
    }

    public int getActualMaxRow() {
        return actualMaxRow;
    }

    public int getActualMaxColumn() {
        return actualMaxColumn;
    }

    public String getReaderModel() {
        return readerModel;
    }

    public String getExcelModel() {
        return excelModel;
    }

    public void setActualMaxRow(int actualMaxRow) {
        this.actualMaxRow = actualMaxRow;
    }

    public void setActualMaxColumn(int actualMaxColumn) {
        this.actualMaxColumn = actualMaxColumn;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
