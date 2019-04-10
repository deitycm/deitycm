package cm.deity.excel.poi.excel2003;

import cm.deity.excel.exception.ExcelNullRowColumnException;
import cm.deity.excel.exception.NonePrintException;
import cm.deity.excel.poi.abstractvo.IExcelReader;
import cm.deity.excel.poi.util.ExcelDBConfig;
import cm.deity.excel.poi.vo.*;
import cm.deity.excel.util.ConstantUtil;
import cm.deity.excel.util.ErrorCodeDesc;
import cm.deity.excel.util.ExcelReaderConfig;
import cm.deity.excel.util.ExcelReaderUtil;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.*;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象Excel2003读取器，通过实现HSSFListener监听器，采用事件驱动模式解析excel2003
 * 中的内容，遇到特定事件才会触发，大大减少了内存的使用。
 * @Description :
 * @Date : 2018/6/14 10:04
 * @VERSION : 1.0
 * @Author : chenming
 */
public  class Excel2003Reader implements HSSFListener, IExcelReader {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(Excel2003Reader.class);

    //自建excel对象
    private CmWorkBook qwWorkBook;
    private CmSheet nowSheet;
    private CmRow nowRow = new CmRow();
    private CmCell nowCell = new CmCell();
    //cell java值
    private Object cellValue;
    //当前行
    private int curRow = 0;
    //当前sheet编号，用于从workBook中获取对应sheet
    private int thisSheetDemonNum = 0;
    //当前的行标
    private String rowKey;
    //记录当前的连续空row的长度
    int nowNullRowNum = 0;
    //记录当前的连续空cell的长度
    int nowCellNumForRow = 0;
    //是否使用redis
    boolean isUsedRedis;
    //redis 客戶端
    //todo
    //private IJedis jedis;

    //当前sheet的行分组，本程序使用组保存对应的row
    private List<CmRow> groupRows;
    //记录行的列信息
    private Map<Integer, Integer> rowCluNum = new HashMap<Integer, Integer>();
    //当前sheet的总行数
    private int maxRow;
    //当前sheet的总列数
    private int maxColumn;
    //是否是检测excel容量
    private boolean checkContent = false;
    //格式监听器
    private FormatTrackingHSSFListener formatListener;

    //poi 自建对象
    private DimensionsRecord dimensionsRecord;
    private BoundSheetRecord boundSheetRecord;
    private RowRecord rowRecord;
    private BoolErrRecord boolErrRecord;
    private FormulaRecord formulaRecord;
    private LabelRecord labelRecord;
    private LabelSSTRecord labelSSTRecord;
    private NumberRecord numberRecord;
    private MergeCellsRecord mergeCellsRecord;
    private MissingCellDummyRecord mc;
    private String formatString;
    private SSTRecord sstRecord;
    //poi 自建对象



    /**
     * excel sheet大小检测方法，此方法将获取到当前workSheet最大行列
     * @param excelCheckConfig excel配置类
     * @return
     * @throws IOException
     * @throws NonePrintException
     */
    @Override
    public ExcelReaderConfig checkExcelContent(ExcelReaderConfig excelCheckConfig) throws IOException, NonePrintException {
        FileInputStream fin = null;
        POIFSFileSystem poiFs;
        InputStream workBookInputStream = null;
        try {
            checkContent = true;
            fin = new FileInputStream(excelCheckConfig.getFile());
            poiFs = new POIFSFileSystem(fin);
            //从流中获取Excel的WorkBook流
            workBookInputStream = poiFs.createDocumentInputStream("Workbook");
            HSSFRequest hssfRequest = new HSSFRequest();
            //为所有的record注册一个监听器
            hssfRequest.addListenerForAllRecords(this);
            //创建事件工厂
            HSSFEventFactory factory = new HSSFEventFactory();
            //根据WorkBook输入流处理所有事件
            factory.processEvents(hssfRequest, workBookInputStream);

            //设置当前最大行列信息
            excelCheckConfig.setActualMaxRow(maxRow);
            excelCheckConfig.setActualMaxColumn(maxColumn);
        }finally{
            // 一旦所有的监听器处理完成，关闭文件输入流
            if(workBookInputStream != null) {
                workBookInputStream.close();
            }
            if(fin != null){
                fin.close();
            }
        }
        return excelCheckConfig;
    }

    @Override
    public CmWorkBook readExcelContent(ExcelReaderConfig excelCheckConfig) throws IOException, OpenXML4JException, SAXException, NonePrintException {
        FileInputStream fin = null;
        POIFSFileSystem poiFs;
        InputStream workBookInputStream = null;
        //初始化对象
        qwWorkBook = new CmWorkBook();
        try {
            //是否使用redis
            isUsedRedis = ExcelReaderUtil.isUseRedis();
            /**
             * 读取excel文件
             */
            fin = new FileInputStream(excelCheckConfig.getFile());
            poiFs = new POIFSFileSystem(fin);
            //创建类型转换监听
            formatListener = new FormatTrackingHSSFListener(new MissingRecordAwareHSSFListener(this));
            //从流中获取Excel的WorkBook流
            workBookInputStream = poiFs.createDocumentInputStream("Workbook");
            HSSFRequest hssfRequest = new HSSFRequest();
            //为所有的record注册一个监听器
            hssfRequest.addListenerForAllRecords(formatListener);
            //创建事件工厂
            HSSFEventFactory factory = new HSSFEventFactory();
            //根据WorkBook输入流处理所有事件
            factory.processEvents(hssfRequest, workBookInputStream);

            //设置当前最大行列信息
            excelCheckConfig.setActualMaxRow(maxRow);
            excelCheckConfig.setActualMaxColumn(maxColumn);
        }finally{
            // 一旦所有的监听器处理完成，关闭文件输入流
            if(workBookInputStream != null) {
                IOUtils.closeQuietly(workBookInputStream);
            }
            if(fin != null){
                fin.close();
            }
        }
        return qwWorkBook;
    }

    /**
     * HSSFListener 监听方法，处理 Record
     * excel2003使用流读取excel，所有的元素都在这个方法中解析
     */
    @SuppressWarnings("unchecked")
    boolean hasCheckBatch = false;
    int beginRowNum = 0;
    public void processRecord(Record record) {
        /**
         * 校验sheet 行列
          */
        if(checkContent){
            //获取当前sheet的行列信息
            getNowSheetRowAndColumn (record);
            return;
        }

        /**
         * 如果当前非检测逻辑，则需要对每一个record进行解析
         */
        switch (record.getSid()) {
            //流程第一次读取sheet信息，此处可获取到sheet名称，此时不会读取sheet详情
            case BoundSheetRecord.sid:
                boundSheetRecord = (BoundSheetRecord) record;
                addSheet();
                break;
            //流程开始循环读取excel每一个sheet信息，最开始都会生成DimensionsRecord，此处可初始化sheet内明细
            case DimensionsRecord.sid:
                dimensionsRecord = (DimensionsRecord)record;
                initSheetInfo();
                break;
            case TableRecord.sid:
                break;
            //流程开始循环每一个行
            //这里每次会读取一批row，然后就会循环cell
            case RowRecord.sid:
                rowRecord = (RowRecord)record;
                addSheetRows();

                if(hasCheckBatch){
                    beginRowNum = rowRecord.getRowNumber();
                    hasCheckBatch = false;
                }
                break;
            //文档循环开始  或者  每一个sheet的循环开始
            case BOFRecord.sid:
                break;
            //文档循环结束 或者 每一个sheet的循环结束
            case EOFRecord.sid:
                beginRowNum = 0;
                //清理最后一组在sheet中的数据
                if(nowSheet!=null){
                    if(isUsedRedis){
                        List<String> rowKeys = nowSheet.getRowKeys();
                        groupRows = ExcelReaderConfig.rowMap.get(rowKeys.get(rowKeys.size()-1));
//                        jedis = new JedisCacheServiceImpl();
//                        jedis.hset(rowKey, ConstantUtil.REDIS_FIELD, groupRows);
//                        jedis.expire(rowKey, ConstantUtil.REDIS_OUT_TIME);
                    }
                    nowSheet.clearRows();
                    ExcelReaderUtil.addForRowLast(nowRow, nowCell, maxColumn);
                }
                break;
            //文字记录
            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;
            //空格类型
            case BlankRecord.sid:
                cellValue = "";
                instanceNowRow(((BlankRecord) record).getRow());
                break;
            //布尔类型
            case BoolErrRecord.sid:
                boolErrRecord = (BoolErrRecord) record;
                cellValue = boolErrRecord.getBooleanValue();
                instanceNowRow(boolErrRecord.getRow());
                break;
            //公式类型
            case FormulaRecord.sid:
//                formulaRecord = (FormulaRecord) record;
//                if (outputFormulaValues) {
//                    if (Double.isNaN(formulaRecord.getValue())) {
//                        outputNextStringRecord = true;
//                        nextRow = formulaRecord.getRow();
//                        nextColumn = formulaRecord.getColumn();
//                    } else {
//                        cellValue = formatListener.formatNumberDateCell(formulaRecord);
//                    }
//                } else {
//                    cellValue = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, formulaRecord.getParsedExpression()) + '"';
//                }
                cellValue = "";
                instanceNowRow(formulaRecord.getRow());
                break;
            //单元格中公式的字符串
            case StringRecord.sid://
//              cellValue = "";
//                if (outputNextStringRecord) {
//                    // String for formula
//                    StringRecord srec = (StringRecord) record;
//                    outputNextStringRecord = false;
//                }
                break;
            //标签类型
            case LabelRecord.sid:
                labelRecord = (LabelRecord) record;
                cellValue = ObjectUtils.isEmpty(labelRecord.getValue())?" ":cellValue;
                instanceNowRow(labelRecord.getRow());
                break;
            //字符串类型
            case LabelSSTRecord.sid:
                cellValue = "";
                labelSSTRecord = (LabelSSTRecord) record;
                if (sstRecord != null) {
                    cellValue = sstRecord.getString(labelSSTRecord.getSSTIndex()).toString().trim();
                }
                instanceNowRow(labelSSTRecord.getRow());
                break;
            //数字类型
            case NumberRecord.sid:
                numberRecord = (NumberRecord) record;
                getNumberRecord();
                break;
            //合并单元格信息  一个sheet只会读取一次
            case MergeCellsRecord.sid:
                mergeCellsRecord = (MergeCellsRecord) record;
                //存储
                addSheetRangeAddress();
                break;
            default:
                break;
        }

        // 空值的操作
        if (record instanceof MissingCellDummyRecord) {
            mc = (MissingCellDummyRecord) record;
            cellValue = ConstantUtil.CELL_NULL_STR;
            instanceNowRow(mc.getRow());
        }
    }

    /**
     * 添加合并单元格信息
     */
    public void addSheetRangeAddress() {
        short mergeCellsNum = mergeCellsRecord.getNumAreas();
        List<CmCellRangeAddress> cellRangeAddresses = new ArrayList<>(mergeCellsNum);
        CellRangeAddress cellRangeAddress;
        CmCellRangeAddress qwCellRangeAddress = new CmCellRangeAddress(0,0,0,0);
        for(int i = 0; i<mergeCellsNum; i++){
            cellRangeAddress = mergeCellsRecord.getAreaAt(i);
            qwCellRangeAddress = qwCellRangeAddress.clone();
            qwCellRangeAddress.setFirstRow(cellRangeAddress.getFirstRow());
            qwCellRangeAddress.setLastRow(cellRangeAddress.getLastRow());
            qwCellRangeAddress.setFirstColumn(cellRangeAddress.getFirstColumn());
            qwCellRangeAddress.setLastColumn(cellRangeAddress.getLastColumn());
            cellRangeAddresses.add(qwCellRangeAddress);
        }
        nowSheet.setMergedRegions(cellRangeAddresses);
    }

    /**
     * 获取数字类型对应的内容，需要转换字典
     */
    public void getNumberRecord() {
        formatString = formatListener.getFormatString(numberRecord);
        cellValue = formatListener.formatNumberDateCell(numberRecord).trim();
        //excel 时间格式转换成java可支持格式
        if (ConstantUtil.EXCEL_DATA_FPRMAT.equals(formatString)) {
            formatString = ConstantUtil.JAVA_DATA_FPRMAT_EXCEL;
            //todo
            //cellValue = DateUtil.parse(cellValue.toString(), formatString);
        }else if(ConstantUtil.EXCEL_DATE_TIME_FPRMAT.equals(formatString)){
            formatString = ConstantUtil.JAVA_DATE_TIME_FPRMAT_EXCEL;
            //todo
            //cellValue = DateUtil.parse(cellValue.toString(), formatString);
        }
        instanceNowRow(numberRecord.getRow());
    }


    /**
     * 获取一行，并记录当前行的行号
     * @Author : chenming
     */
    private void getNowRow() {
        if(nowRow != null){
            int rowNum = nowRow.getRowNum();
            nowRow = nowRow.clone();
            nowRow.setCells(null);
            nowRow.setRowNum(rowRecord.getRowNumber());

        }else{
            nowRow = new CmRow();
            nowRow.setCells(null);
            nowRow.setRowNum(rowRecord.getRowNumber());
        }
    }

    /**
     * 行读取结束，将row存入row group中，并验证行列是否超限
     */
    public void addSheetRows() {
        /**
         * 添加excel中跳过的行
         */
        addLoseRow();
        /**
         * 添加当前行
         */
        getNowRow();
        if(curRow == 0 || curRow % ConstantUtil.GROUP_ROW_SIZE == 0){
            //todo
            //rowKey = UUID32.getID();
            nowSheet.getRowKeys().add(rowKey);
            groupRows = new ArrayList<>(ConstantUtil.GROUP_ROW_SIZE);
            groupRows.add(nowRow);
            ExcelReaderConfig.rowMap.put(rowKey, groupRows);
        }else{
            groupRows.add(nowRow);
        }
        rowCluNum.put(rowRecord.getRowNumber(), rowRecord.getLastCol());
        curRow++;
    }

    /**
     * 添加excel中跳过的行
     */
    private void addLoseRow() {
        if(nowRow!=null){
            if(rowRecord.getRowNumber() - nowRow.getRowNum() > 1){
                int rowNum = nowRow.getRowNum();
                for(int i = 0; i< rowRecord.getRowNumber() - rowNum - 1 ;i++){
                    nowRow = nowRow.clone();
                    nowRow.setCells(null);
                    nowRow.setRowNum(rowNum+1);
                    groupRows.add(nowRow);
                }
                nowNullRowNum = nowNullRowNum+rowRecord.getRowNumber() - rowNum - 1;
            }
        }
    }

    /**
     * 初始化流程明细
     */
    public void initSheetInfo() {
        maxRow = dimensionsRecord.getLastRow();
        maxColumn = dimensionsRecord.getLastCol();
        nowSheet = qwWorkBook.getCmSheetAt(thisSheetDemonNum);
        nowSheet.setTotalRow(maxRow);
        nowSheet.setTotalColumn(maxColumn);
        /**
         * 初始化数据计数器
         */
        thisSheetDemonNum++;
        curRow = 0;
        nowRow = new CmRow();
    }

    /**
     * 添加sheet
     */
    public void addSheet() {
        CmSheet qwSheet = new CmSheet();
        qwSheet.setSheetName(boundSheetRecord.getSheetname());
        qwWorkBook.addSheet(qwSheet);
    }

    /**
     * 获取当前最大行与最大列
     */
    public void getNowSheetRowAndColumn(Record record) {
        if(record.getSid() == DimensionsRecord.sid){
            dimensionsRecord = (DimensionsRecord) record;
            if(dimensionsRecord.getLastRow() > maxRow){
                maxRow = dimensionsRecord.getLastRow();
            }

            if(dimensionsRecord.getLastCol() > maxColumn){
                maxColumn = dimensionsRecord.getLastCol();
            }
        }
    }


    /**
     * 初始化各种类型的cell
     * @param rowNum
     */
    boolean isNullRow = true;
    private void instanceNowRow(int rowNum) {
        if(!hasCheckBatch){
            nowRow = groupRows.get(beginRowNum);
            hasCheckBatch = true;
        }

        if(nowRow == null || nowRow.getRowNum() != rowNum){
            if(isNullRow){
                nowNullRowNum++;
            }
            isNullRow = true;
            nowCellNumForRow = 0;
            //必须在初始化新行前补全旧行
            ExcelReaderUtil.addForRowLast(nowRow, nowCell, maxColumn);
            //判断是否需要刷新map到redis
            if(isUsedRedis){
                if(rowNum != 0 && rowNum != maxRow-1 && rowNum % ConstantUtil.GROUP_ROW_SIZE == 0){
                    int nowGroupIndex = rowNum/ConstantUtil.GROUP_ROW_SIZE;
                    groupRows = ExcelReaderConfig.rowMap.get(nowSheet.getRowKeys().get(nowGroupIndex));
//                    jedis = new JedisCacheServiceImpl();
//                    jedis.hset(rowKey, ConstantUtil.REDIS_FIELD, groupRows);
//                    jedis.expire(rowKey, ConstantUtil.REDIS_OUT_TIME);
                    ExcelReaderConfig.rowMap.remove(nowSheet.getRowKeys().get(nowGroupIndex));
                }
            }
            nowRow = nowSheet.getRowForExcelMap(rowNum);
            //读完一行后刷新列号
            if(nowRow == null){
                nowNullRowNum++;
            }

            if(nowNullRowNum >= ExcelDBConfig.EXCEL_ROW_NULL_NUM){
                throw new ExcelNullRowColumnException(String.format(ErrorCodeDesc.EXCEL_ROW_NULL_LIMIT.getDesc(), String.valueOf(ExcelDBConfig.EXCEL_ROW_NULL_NUM)));
            }
        }

        if(nowRow != null){
            nowCell = nowCell.clone();
            if(!ObjectUtils.isEmpty(cellValue)){
                nowNullRowNum = 0;
                isNullRow = false;
                nowCellNumForRow = 0;
            }else{
                nowCellNumForRow++;
                if(nowCellNumForRow >= ExcelDBConfig.EXCEL_COLUMN_NULL_NUM){
                    throw new RuntimeException(String.format(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getCode(), String.format(ErrorCodeDesc.EXCEL_COLUMN_NULL_LIMIT.getDesc(), String.valueOf(ExcelDBConfig.EXCEL_COLUMN_NULL_NUM))));
                }
            }
            nowCell.setICellValue(cellValue);
            nowCell.setIndex(nowRow.getILastCellNum()+1);
            nowCell.setRowIndex(nowRow.getRowNum());
            nowRow.addCell(nowCell);
        }
    }
}
