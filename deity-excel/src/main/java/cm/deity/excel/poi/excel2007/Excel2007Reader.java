package cm.deity.excel.poi.excel2007;

import cm.deity.excel.exception.NonePrintException;
import cm.deity.excel.poi.abstractvo.IExcelReader;
import cm.deity.excel.poi.util.ExcelDBConfig;
import cm.deity.excel.poi.vo.*;
import cm.deity.excel.util.ConstantUtil;
import cm.deity.excel.util.ErrorCodeDesc;
import cm.deity.excel.util.ExcelReaderConfig;
import cm.deity.excel.util.ExcelReaderUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author : chenming
 * @Description :抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析xml
 * 需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低内存的耗费，特别使用于大数据量的文件
 * @Date : 2018/7/10
 * @VERSION : 1.0
 */
public class Excel2007Reader extends DefaultHandler implements IExcelReader {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(Excel2007Reader.class);
    /**
     * excel配置类
     */
    private ExcelReaderConfig excelCheckConfig;
    
    /**
     * 合并单元格
     */
    List<CmCellRangeAddress> cellRangeAddresses;
    
    /**
     * 自建excel对象
     */
    private CmWorkBook qwWorkBook;
    private CmSheet nowSheet;
    private CmRow nowRow = new CmRow();
    private CmCell nowCell = new CmCell();
    //当前sheet的行分组，本程序使用组保存对应的row
    private List<CmRow> groupRows;
    //转换excel值为java对象后的cell值
    private Object dataValue;

    //当前sheet最大行
    private int maxRow = 0;
    //当前sheet最大列
    private int maxColumn = 0;
    //当前最大空行
    private int nowNullRowNum = 0;
    //当前最大空列
    private int nowNullCellNum = 0;
    //当前行
    private int curRow = 0;
    //记录当前是否是tab元素
    private boolean isTElement;
    //记录上一次的内容
    private String lastContents;
    //记录下一次是否是字符串
    private boolean nextIsString;
    //记录富文本字段
    private XSSFRichTextString rtsi;

    //是否是检测excel容量
    private boolean checkContent = false;
    //是否是因为检测excel容量而抛出错误信息
    private boolean checkRight = false;

    //是否使用redis
    private boolean isUsedRedis;

    //redis 客戶端
//    private IJedis jedis;

    //row的分组key
    private String rowKey;
    //用于处理String的容器
    private StringBuilder sb = new StringBuilder();
    //共享字符串表
    private SharedStringsTable sst;
    //单元格样式
    private StylesTable stylesTable;

    //记录当前单元格的上一个可读位置
    private String preRef = null;
    //记录当前单元格位置
    private String ref = null;
    //单元格类型
    private CellDataType nextDataType = CellDataType.SSTINDEX;
    //数据转换类
    private final DataFormatter formatter = new DataFormatter();
    //当前数字类型对应的文字索引
    private short formatIndex;
    //当前数据对应的格式
    private String formatString;
    //单元格解析类型
    private String cellType;
    //单元格解析格式
    private String cellStyleStr;
    //t类型的值
    private String tValue;
    //cell读取标志，兼容cell 空 和cell不存在的问题
    private Set<String> cellFlag = new HashSet<String>();

    /**
     * excel sheet大小检测方法，此方法将获取到当前workSheet最大行列
     * @param excelCheckConfig excel配置类
     * @return
     * @throws OpenXML4JException
     * @throws SAXException
     * @throws NonePrintException
     * @throws IOException
     * @Author : chenming
     */
    @Override
    public ExcelReaderConfig checkExcelContent(ExcelReaderConfig excelCheckConfig) throws OpenXML4JException, SAXException, NonePrintException, IOException {
        InputStream is = null;
        checkContent = true;
        OPCPackage pkg = null;
        excelCheckConfig = excelCheckConfig;
        try {
            /**
             * 读取excel文件
             */
            is = new FileInputStream(excelCheckConfig.getFile());
            pkg = OPCPackage.open(is);
            XSSFReader xSSFReader = new XSSFReader(pkg);
            XMLReader parser = fetchSheetParser(null);
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator)xSSFReader.getSheetsData();
            InputStream sheet = null;
            while (sheets.hasNext()) {
                try {
                    //重置check标识
                    checkRight = false;
                    sheet = sheets.next();
                    curRow = 0;
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                } catch (Exception e) {
                    //如果是正常检测结束不需要处理异常
                    if (!checkRight) {
                        LOGGER.error("导入excel发生异常", e);
                       // //ExceptionCenter.addException(e, "导入excel发生异常", excelCheckConfig.getFileName());
                        throw new NonePrintException(ErrorCodeDesc.EXCEL_READER_ERROR.getCode(), ErrorCodeDesc.EXCEL_READER_ERROR.getDesc());
                    }
                }finally {
                    if(sheet != null){
                        sheet.close();
                    }
                }
            }
        }catch (InvalidFormatException e) {
            throw new NonePrintException(ErrorCodeDesc.EXCEL_LOCKED.getCode(), ErrorCodeDesc.EXCEL_LOCKED.getDesc());
        } finally {
            excelCheckConfig.setActualMaxRow(maxRow);
            excelCheckConfig.setActualMaxColumn(maxColumn);
            closeIO(is, pkg);
        }
        return excelCheckConfig;
    }

    @Override
    public CmWorkBook readExcelContent(ExcelReaderConfig excelCheckConfig) throws IOException, OpenXML4JException, SAXException, NonePrintException {
        //初始化对象
        qwWorkBook = new CmWorkBook();
        InputStream is = null;
        OPCPackage pkg = null;
        try {
            //是否使用redis
            isUsedRedis = ExcelReaderUtil.isUseRedis();
            /**
             * 读取excel文件
             */
            is = new FileInputStream(excelCheckConfig.getFile());
            excelCheckConfig = excelCheckConfig;
            pkg = OPCPackage.open(is);
            XSSFReader r = new XSSFReader(pkg);
            sst = r.getSharedStringsTable();
            stylesTable = r.getStylesTable();
            XMLReader parser = fetchSheetParser(sst);
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator)r.getSheetsData();
            CmSheet qwSheet;
            InputStream sheet = null;
            while (sheets.hasNext()) {
                try {
                    /**
                     * 循环sheet
                     */
                    qwSheet = new CmSheet();
                    qwWorkBook.addSheet(qwSheet);
                    nowSheet = qwSheet;
                    cellRangeAddresses = new ArrayList<CmCellRangeAddress>();
                    nowSheet.setMergedRegions(cellRangeAddresses);
                    curRow = 0;

                    sheet = sheets.next();
                    //必须在next方法后才能获取到sheet名称
                    nowSheet.setSheetName(sheets.getSheetName());
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                }finally {
                    if(sheet != null){
                        sheet.close();
                    }
                }
            }
        } catch (InvalidFormatException e) {
            throw new NonePrintException(ErrorCodeDesc.EXCEL_LOCKED.getCode(), ErrorCodeDesc.EXCEL_LOCKED.getDesc());
        } catch (Exception e) {
            //行列超限检测
            if(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getCode().equals(e.getMessage())){
                throw new NonePrintException(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getCode(), String.format(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getDesc(), String.valueOf(ExcelDBConfig.EXCEL_ROW_NULL_NUM)));
            }else if(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getCode().equals(e.getMessage())){
                throw new NonePrintException(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getCode(), String.format(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getDesc(), String.valueOf(ExcelDBConfig.EXCEL_COLUMN_NULL_NUM)));
            }else{
                LOGGER.error("导入excel发生异常", e);
                throw new NonePrintException(ErrorCodeDesc.EXCEL_READER_ERROR.getCode(), ErrorCodeDesc.EXCEL_READER_ERROR.getDesc());
            }
        } finally {
            closeIO(is, pkg);
        }
        return qwWorkBook;
    }

    /**
     * 关闭流
     * @param is
     * @param pkg
     */
    public void closeIO(InputStream is, OPCPackage pkg) {
        try {
            if(is != null){
                is.close();
            }
            if(pkg!=null){
                pkg.close();
            }
        } catch (IOException e) {
            //ExceptionCenter.addException(e, "关闭流失败", excelCheckConfig.getFileName());
        }
    }

    /**
     * 初始化下xml读取器
     * @param sst
     * @return
     * @throws SAXException
     */
    public XMLReader fetchSheetParser(SharedStringsTable sst)
            throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    @Override
    public void startDocument() throws SAXException {
        //初始化位置
        preRef = null;
        ref = null;
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        if(nowSheet != null){
            nowSheet.clearRows();
        }

        super.endDocument();
    }

    /**
     * 开始处理一个元素
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        /**
         * 如果是检测数据，并且不是dimension的情况下，不需要做业务处理
         */
        if(checkContent && !"dimension".equals(qName)){
            return;
        }

        isTElement = false;
        switch (qName){
            //处理dimension,改元素储存了sheet的基本信息.此处使用来记录当前sheet最大行列
            case "dimension":
                getSheetDimension(attributes);
                break;
            //获取一行，并记录当前行的行号
            case "row":
                getNowRow();
                break;
            //获取一个cell的位置和类型
            case "c":
                getCellInfo(attributes);
                break;
            //获取一个合并单元格信息
            case "mergeCell":
                getMergeAddress(attributes);
                break;
            //判断tab
            case "t":
                isTElement = true;
                break;
            //case "f": break; 公式表达式标签
            //case "is": break; 内联字符串外部标签
            //case "col": break; 处理隐藏列
            default:
            break;
        }
        //置空当前记录的cell值
        lastContents = "";
    }

    /**
     * 读取元素结束，这里可以处理元素值
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        //如果是excel检测直接返回
        if(checkContent) {
            return;
        }

        //如果是字符串则获取字符串信息
        if(nextIsString) {
            lastContents = new XSSFRichTextString(sst.getEntryAt(Integer.parseInt(lastContents))).toString();
            nextIsString = false;
        }

        // t元素也包含字符串
        if (isTElement) {
            //在这之前先去掉字符串前后的空白符
            tValue = lastContents.trim();
            nowCell = nowCell.clone();
            nowCell.setICellValue(tValue);
            nowCell.setIndex(nowRow.getILastCellNum()+1);
            nowCell.setRowIndex(nowRow.getRowNum());
            nowRow.addCell(nowCell);
            isTElement = false;
            return;
        }

        switch (qName){
            //读取一行结束
            case Excel2007Config.ROW:
                //行读取结束，将row存入row group中，并验证行列是否超限
                addSheetRows();
                break;
            //读取一个单元格结束
            case Excel2007Config.CELL:
                //如果单元格没有值，但单元格存在，则标识符应当存在，如果存在，需要往导入结果中手动添加一个null cell
                addNullValueCell();
                break;
            //读取一个单元格的值
            case Excel2007Config.CELL_VALUE:
                getCellValue();
                break;
            default:
                break;
        }
    }


    /**
     * 验证并添加一个空cell
     */
    private void addNullValueCell() {
        if(cellFlag.remove(preRef+ref)){
            checkNullCellForJump();
            nowCell = nowCell.clone();
            nowCell.setICellValue(ConstantUtil.CELL_NULL_STR);
            nowCell.setIndex(nowRow.getILastCellNum()+1);
            nowCell.setRowIndex(nowRow.getRowNum());
            nowRow.addCell(nowCell);
        }
    }

    /**
     * 检测两个跳跃的cell中间的cell大小，并赋值
     */
    private void checkNullCellForJump() {
        if (!ref.equals(preRef)) {
            int len = countNullCell(ref, preRef);
            for (int i = 0; i < len; i++) {
                nowCell = nowCell.clone();
                nowCell.setICellValue(ConstantUtil.CELL_NULL_STR);
                nowCell.setIndex(nowRow.getILastCellNum()+1);
                nowCell.setRowIndex(nowRow.getRowNum());
                nowRow.addCell(nowCell);
            }
        }
    }

    /**
     * 行读取结束，将row存入row group中，并验证行列是否超限
     * @throws SAXException
     */
    private void addSheetRows() throws SAXException {
        if(curRow == 0){
           //todo
            // rowKey = UUID32.getID();
            nowSheet.getRowKeys().add(rowKey);
            groupRows = new ArrayList<CmRow>(ConstantUtil.GROUP_ROW_SIZE);
            groupRows.add(nowRow);
        }else if(curRow % ConstantUtil.GROUP_ROW_SIZE == 0){
            //添加数据到内存或者redis中
            addRowForRedisOrCache();
            //todo
            //rowKey = UUID32.getID();
            nowSheet.getRowKeys().add(rowKey);
            groupRows = new ArrayList<>(ConstantUtil.GROUP_ROW_SIZE);
            groupRows.add(nowRow);
        }else if(curRow == maxRow-1){
            groupRows.add(nowRow);
            //添加数据到内存或者redis中
            addRowForRedisOrCache();
        }else {
            groupRows.add(nowRow);
            //判断是否存在大量空行
            if(nowRow.getILastCellNum() == -1){
                nowNullRowNum++;
            }else{
                nowNullRowNum = 0;
            }

            /**
             * 超过预设空行，自动抛出
             */
            if(nowNullRowNum >= ExcelDBConfig.EXCEL_ROW_NULL_NUM){
                throw new SAXException(String.format(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getCode(), String.format(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getDesc(), String.valueOf(ExcelDBConfig.EXCEL_ROW_NULL_NUM))));
            }
        }

        ExcelReaderUtil.addForRowLast(nowRow, nowCell, maxColumn);
        curRow++;
        //初始化当前空列号
        nowNullCellNum = 0;
    }

    /**
     * 添加数据到内存或者redis中
     */
    private void addRowForRedisOrCache() {
        if(isUsedRedis){
            //todo
//            jedis = new JedisCacheServiceImpl();
//            jedis.hset(rowKey,  ConstantUtil.REDIS_FIELD, groupRows);
//            jedis.expire(rowKey, ConstantUtil.REDIS_OUT_TIME);
        }else{
            ExcelReaderConfig.rowMap.put(rowKey, groupRows);
        }
    }

    /**
     * 读取excel cell的文本值
     * @throws SAXException
     */
    private void getCellValue() throws SAXException {
        //移除cell标识
        cellFlag.remove(preRef+ref);
        //解析cell value值并转换成java对象
        dataValue = getDataValue(lastContents.trim());
        //补全单元格之间的空单元格
        checkNullCellForJump();
        nowCell = nowCell.clone();
        nowCell.setICellValue(dataValue);
        nowCell.setIndex(nowRow.getILastCellNum()+1);
        nowCell.setRowIndex(nowRow.getRowNum());
        nowRow.addCell(nowCell);
        if(dataValue == null && "".equals(dataValue)){
            nowNullCellNum++;
        }else{
            nowNullCellNum = 0;
        }

        /**
         * 超过预设空列，自动抛出
         */
        if(nowNullCellNum >= ExcelDBConfig.EXCEL_COLUMN_NULL_NUM){
            throw new SAXException(String.format(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getCode()));
        }
    }

    /**
     * 根据数据类型获取数据
     * @param value
     * @return
     */

    private Object getDataValue(String value){
        dataValue = ConstantUtil.CELL_NULL_STR;
        switch (nextDataType) {
            //这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL:
                char first = value.charAt(0);
                dataValue = (first == '0' ? false : true);
                break;
            case ERROR:
                dataValue = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA:
                dataValue = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                rtsi = new XSSFRichTextString(value.toString());
                dataValue = rtsi.toString();
                break;
            case SSTINDEX:
                dataValue = value.toString();
                break;
            case NUMBER:
                if (formatString != null){
                    dataValue = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                }else{
                    dataValue = value;
                }
                break;
            case DATE:
                try{
                    dataValue = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                    if(!ObjectUtils.isEmpty(dataValue)){
                        //对日期字符串作特殊处理
                        dataValue = dataValue.toString().trim();
                        if(!ConstantUtil.JAVA_TIME_FPRMAT.equals(formatString)){
                            //todo
                            //dataValue = DateUtil.parse(dataValue.toString(), formatString);
                        }
                    }
                }catch(NumberFormatException ex){
                    dataValue = ConstantUtil.CELL_NULL_STR;
                }
                break;
            default:
                dataValue = ConstantUtil.CELL_NULL_STR;
                break;
        }
        return dataValue;
    }

    /**
     * 获取一个合并单元格信息，此处记录了单元格信息  A1:A2
     * @param attributes
     */
    private void getMergeAddress(Attributes attributes) {
        String mergerInfo = attributes.getValue("ref");
        CmCellRangeAddress qwCellRangeAddress = new CmCellRangeAddress(0,0,0,0);
        if(!ObjectUtils.isEmpty(mergerInfo)){
            String[] rangeAddressArray = mergerInfo.split(":");
            if(!ObjectUtils.isEmpty(rangeAddressArray) && rangeAddressArray.length == 2){
                qwCellRangeAddress = qwCellRangeAddress.clone();
                qwCellRangeAddress.setFirstRow(getRowNumber(rangeAddressArray[0]));
                qwCellRangeAddress.setLastRow(getRowNumber(rangeAddressArray[1]));
                qwCellRangeAddress.setFirstColumn(getColumnNumber(rangeAddressArray[0]));
                qwCellRangeAddress.setLastColumn(getColumnNumber(rangeAddressArray[1]));
                cellRangeAddresses.add(qwCellRangeAddress);
            }
        }
    }

    /**
     * 获取一个cell的位置和类型
     * @param attributes
     */
    private void getCellInfo(Attributes attributes) throws SAXException {
        //前一个单元格的位置
        if(preRef == null){
            preRef = attributes.getValue("r");
        }else{
            preRef = ref;
        }
        //当前单元格的位置
        ref = attributes.getValue("r");
        setNextDataType(attributes);
        // Figure out if the value is an index in the SST
        cellType = attributes.getValue("t");
        if(cellType != null && cellType.equals("s")) {
            nextIsString = true;
        } else {
            nextIsString = false;
        }

        //处理跳cell
        cellFlag.add(preRef+ref);

        //判断是否存在空行
        int rowGap = getRowNumber(ref) - getRowNumber(preRef)-1;
        if(rowGap > 0){
            /**
             * 超过预设空行，自动抛出
             */
            if(rowGap >= ExcelDBConfig.EXCEL_ROW_NULL_NUM){
                throw new SAXException(String.format(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getCode()));
            }

            for(int i = 0; i < rowGap ; i++){
                curRow++;
                if(curRow % ConstantUtil.GROUP_ROW_SIZE == 0){
                    //添加数据到内存或者redis中
                    addRowForRedisOrCache();
                    //todo
                    //rowKey = UUID32.getID();
                    nowSheet.getRowKeys().add(rowKey);
                    groupRows = new ArrayList<CmRow>(ConstantUtil.GROUP_ROW_SIZE);
                    groupRows.add(null);
                }else if(curRow == maxRow-1){
                    groupRows.add(null);
                    //添加数据到内存或者redis中
                    addRowForRedisOrCache();
                }else{
                    groupRows.add(null);
                }
            }
            getNowRow();
        }
    }

    /**
     * 获取一行，并记录当前行的行号
     * @Author : chenming
     */
    private void getNowRow() {
        nowRow = nowRow.clone();
        nowRow.setCells(null);
        nowRow.setRowNum(curRow);
    }

    /**
     * 一个sheet对应一个dimension
     * 处理dimension,该元素储存了sheet的基本信息.此处使用来记录当前sheet最大行列
     * @param attributes
     * @throws SAXException
     */
    private void getSheetDimension(Attributes attributes) throws SAXException {
        checkRight = false;
        //获得总计录数
        String[] dimensions = attributes.getValue("ref").split(":");
        if (ObjectUtils.isEmpty(dimensions)) {
            throw new SAXException(ErrorCodeDesc.EXCEL_SHEET_DIMENSION_ERROR.getDesc());
        }

        //dimensions 例原始数据为 A1:B30   最小A1代表第一行第一列  最大B30代表第30行2列  字母代表列数字代表行
        //如果dimensions只有一个长度，表示该sheet页没有值或者只有一个单元格
        String excelSheetCellNum;
        if(dimensions.length == 1){
            excelSheetCellNum = dimensions[0];
        }else{
            excelSheetCellNum = dimensions[1];
        }

        //获取到当前行数  行号+1 列号+1
        int nowSheetRow = getRowNumber(excelSheetCellNum)+1;
        int nowSheetColumn = getColumnNumber(excelSheetCellNum)+1;
        if (maxRow < nowSheetRow) {
            maxRow = nowSheetRow;
        }
        if (maxColumn < nowSheetColumn) {
            maxColumn = nowSheetColumn;
        }

        if (checkContent) {
            checkRight = true;
            throw new SAXException(ErrorCodeDesc.EXCEL_CHECK_THROW.getDesc());
        } else {
            nowSheet.setTotalRow(nowSheetRow);
            nowSheet.setTotalColumn(nowSheetColumn);
        }
    }


    /**
     * 根据element属性设置数据类型
     * @param attributes
     */
    private void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        cellType = attributes.getValue("t");
        cellStyleStr = attributes.getValue("s");
        if(cellType != null){
            switch (cellType){
                case Excel2007Config.CELL_VALUE_BOOLEAN:
                    nextDataType = CellDataType.BOOL;
                    break;
                case Excel2007Config.CELL_VALUE_ERROR:
                    nextDataType = CellDataType.ERROR;
                    break;
                case Excel2007Config.CELL_VALUE_INLINE:
                    nextDataType = CellDataType.INLINESTR;
                    break;
                case Excel2007Config.CELL_VALUE_SSTINDEX:
                    nextDataType = CellDataType.SSTINDEX;
                    break;
                case Excel2007Config.CELL_VALUE_FORMULA:
                    nextDataType = CellDataType.FORMULA;
                    break;
                default:
                    break;
            }
        }

        //获取excel的内容对应的格式
        if(cellStyleStr != null) {
            XSSFCellStyle style = stylesTable.getStyleAt(Integer.parseInt(cellStyleStr));
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();

            //时间日期等内容的excel格式与java格式转换
            if (ConstantUtil.EXCEL_DATA_FPRMAT.equals(formatString)) {
                nextDataType = CellDataType.DATE;
                formatString = ConstantUtil.JAVA_DATA_FPRMAT;
            }else if (ConstantUtil.EXCEL_TIME_FPRMAT.equals(formatString)) {
                nextDataType = CellDataType.DATE;
                formatString = ConstantUtil.JAVA_TIME_FPRMAT;
            }if (ConstantUtil.EXCEL_DATE_TIME_FPRMAT.equals(formatString)) {
                nextDataType = CellDataType.DATE;
                formatString = ConstantUtil.JAVA_DATE_TIME_FPRMAT;
            }

            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }


    /**
     * 字符串的填充
     * @param str
     * @param len
     * @param let
     * @param isPre
     * @return
     */
    private String fillChar(String str, int len, char let, boolean isPre){
        int len_1 = str.length();
        if(len_1 <len){
            if(isPre){
                for(int i=0;i<(len-len_1);i++){
                    str = let+str;
                }
            }else{
                for(int i=0;i<(len-len_1);i++){
                    str = str+let;
                }
            }
        }
        return str;
    }

    /**
     * 获取字符串内容
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        //得到单元格内容的值
        //ch字符数组如果超出了ch的长度，则会分开多次调用，此时需要经行组装
        lastContents = lastContents + sb.delete(0, sb.length()).append(ch, start, length).toString();
    }

    /**
     * 获取行号
     * @param column
     * @return
     */
    private static int getRowNumber(String column) {
        return Integer.parseInt(column.toUpperCase().replaceAll("[A-Z]", ""))-1;
    }

    /**
     * 获取列号
     * @param column
     * @return
     */
    private static int getColumnNumber(String column) {
        char[] strArr = column.replaceAll("[0-9]", "").toCharArray();
        int exp = 0;
        int sum = 0;
        int num;
        for (int i = strArr.length - 1; i >= 0; i--) {
            num = strArr[i] - 'A' + 1;
            sum += num * ((int) Math.pow(26, exp));
            exp++;
        }
        return sum-1;
    }

    /**
     * 计算两个单元格之间的单元格数目(同一行)
     * @param ref
     * @param preRef
     * @return
     */
    int addForBegin = 0;
    private int countNullCell(String ref, String preRef){
        //如果读取的单元格和上一个单元格换行，则上一个单元格要换算为本行第一格
        int rowGap = getRowNumber(ref) - getRowNumber(preRef);
        //换行，初始化多一列
        if(rowGap > 0){
            preRef = "A";
            addForBegin = 1;
        }
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");
        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);
        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0]-letter_1[0])*26*26 + (letter[1]-letter_1[1])*26 + (letter[2]-letter_1[2]+addForBegin);
        addForBegin = 0;
        return res-1;
    }
}