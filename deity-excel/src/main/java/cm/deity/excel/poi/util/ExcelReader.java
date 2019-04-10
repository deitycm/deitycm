package cm.deity.excel.poi.util;

import cm.deity.excel.exception.NonePrintException;
import cm.deity.excel.util.ConstantUtil;
import cm.deity.excel.util.ExcelReaderConfig;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/20 11:07
 * @VERSION : 1.0
 */
public class ExcelReader {
    public static Workbook excelReader(ExcelReaderConfig excelCheckConfig) throws NonePrintException {
        FileInputStream fis = null;
        Workbook book = null;
        try {
            fis = new FileInputStream(excelCheckConfig.getFile());
            if (excelCheckConfig.getFileName().toLowerCase().endsWith(ConstantUtil.EXCEL03_EXTENSION)) {
                book = new HSSFWorkbook(fis);
            } else if (excelCheckConfig.getFileName().toLowerCase().endsWith(ConstantUtil.EXCEL07_EXTENSION)) {
                book = new XSSFWorkbook(fis);
            }
        } catch(OfficeXmlFileException | POIXMLException e){
           // throw new NonePrintException(ErrorCodeDesc.EXCEL_CONTENT_ERROR.getCode(), ErrorCodeDesc.EXCEL_CONTENT_ERROR.getDesc());
        } catch(EncryptedDocumentException e){
           // throw new NonePrintException(ErrorCodeDesc.EXCEL_PWD_ERROR.getCode(), ErrorCodeDesc.EXCEL_PWD_ERROR.getDesc());
        } catch (IOException e) {
            //throw new NonePrintException(ErrorCodeDesc.EXCEL_IO_ERROR.getCode(), ErrorCodeDesc.EXCEL_IO_ERROR.getDesc());
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                   // throw new NonePrintException(ErrorCodeDesc.EXCEL_IO_ERROR.getCode(), ErrorCodeDesc.EXCEL_IO_ERROR.getDesc());
                }
            }
        }
        return book;
    }
}
