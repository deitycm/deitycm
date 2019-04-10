package cm.deity.excel.poi.abstractvo;

import cm.deity.excel.exception.NonePrintException;
import cm.deity.excel.poi.vo.CmWorkBook;
import cm.deity.excel.util.ExcelReaderConfig;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/20 9:53
 * @VERSION : 1.0
 */
public interface IExcelReader {
    //用一个enum表示单元格可能的数据类型
    enum CellDataType{BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL}

    /**
     * 检测excel容量
     * @param excelCheckConfig
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws NonePrintException
     */
    ExcelReaderConfig checkExcelContent(ExcelReaderConfig excelCheckConfig) throws IOException, OpenXML4JException, SAXException, NonePrintException;

    /**
     * 获取excel内容
     * @param excelCheckConfig
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws NonePrintException
     */
    CmWorkBook readExcelContent(ExcelReaderConfig excelCheckConfig) throws IOException, OpenXML4JException, SAXException, NonePrintException;
}
