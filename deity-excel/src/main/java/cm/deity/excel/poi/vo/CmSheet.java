package cm.deity.excel.poi.vo;

import cm.deity.excel.inter.IRow;
import cm.deity.excel.poi.abstractvo.AbstractCmSheet;
import cm.deity.excel.util.ConstantUtil;
import cm.deity.excel.util.ExcelReaderConfig;
import cm.deity.excel.util.ExcelReaderUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/22 16:00
 * @VERSION : 1.0
 */
public class CmSheet extends AbstractCmSheet implements Cloneable {
    private String sheetName;
    private List<CmCellRangeAddress> mergedRegions;
    private List<CmRow> rows = new ArrayList<>();
    protected List<String> rowKeys = new ArrayList<>();
    protected int totalRow;
    protected int totalColumn;
    protected int nowGroup;
    public CmRow getRow(int i) {
        /**
         * 查询分组信息，动态从缓存中加载对应分组，并返回数据
         */
        return (CmRow) getRowForExcel(i);
    }

    @Override
    public IRow getIRow(int i) {
        return getRowForExcel(i);
    }

    public CmRow getRowForExcelMap(int i) {
        /**
         * 查询分组信息，动态从缓存中加载对应分组，并返回数据
         */
        if(totalRow > 0){
            ExcelReaderUtil.validateSheetIndex(totalRow, i);
            if(rowKeys.size()>= i/ ConstantUtil.GROUP_ROW_SIZE){
                if(!ObjectUtils.isEmpty(rows) && nowGroup == i/ConstantUtil.GROUP_ROW_SIZE){
                    /**
                     * 计算分组位置
                     */
                    if(rows.size() > i%ConstantUtil.GROUP_ROW_SIZE){
                        return rows.get(i%ConstantUtil.GROUP_ROW_SIZE);
                    }else{
                        return null;
                    }
                }else{
                    /**
                     * 重新获取分组
                     */
                    nowGroup = i/ConstantUtil.GROUP_ROW_SIZE;
                    rows = ExcelReaderConfig.rowMap.get(rowKeys.get(nowGroup));
                    if(rows.size() > i%ConstantUtil.GROUP_ROW_SIZE){
                        return rows.get(i%ConstantUtil.GROUP_ROW_SIZE);
                    }else{
                        return null;
                    }
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }



    private IRow getRowForExcel(int i) {
        /**
         * 查询分组信息，动态从缓存中加载对应分组，并返回数据
         */
        if(totalRow > 0){
            ExcelReaderUtil.validateSheetIndex(totalRow, i);
            if(rowKeys.size()>= i/ ConstantUtil.GROUP_ROW_SIZE){
                if(!ObjectUtils.isEmpty(rows) && nowGroup == i/ConstantUtil.GROUP_ROW_SIZE){
                    /**
                     * 计算分组位置
                     */
                    if(rows.size() > i%ConstantUtil.GROUP_ROW_SIZE){
                        return rows.get(i%ConstantUtil.GROUP_ROW_SIZE);
                    }else{
                        return null;
                    }
                }else{
                    /**
                     * 重新获取分组
                     */
                    nowGroup = i/ConstantUtil.GROUP_ROW_SIZE;
                    //缓存中获取分组row
                    if(ExcelReaderUtil.isUseRedis()){
                        //todo
                        //IJedis jedis = new JedisCacheServiceImpl();
                        //rows = jedis.hget(rowKeys.get(nowGroup), ConstantUtil.REDIS_FIELD, List.class);
                    }else{
                        rows = ExcelReaderConfig.rowMap.get(rowKeys.get(nowGroup));
                    }

                    if(!ObjectUtils.isEmpty(rows) && rows.size() > i%ConstantUtil.GROUP_ROW_SIZE){
                        return rows.get(i%ConstantUtil.GROUP_ROW_SIZE);
                    }else{
                        return null;
                    }
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public int getFirstRowNum() {
        return 0;
    }

    @Override
    public int getLastRowNum() {
        return totalRow-1;
    }

    @Override
    public int getIFirstRowNum() {
        return 0;
    }

    @Override
    public int getILastRowNum() {
        return totalRow-1;
    }

    @Override
    public int getNumMergedRegions() {
        if(mergedRegions == null){
            return 0;
        }else{
            return mergedRegions.size();
        }
    }

    public CellRangeAddress getMergedRegion(int i) {
        if(mergedRegions == null || i > mergedRegions.size()){
            return null;
        }else{
            return mergedRegions.get(i);
        }
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalColumn() {
        return totalColumn;
    }

    public void setTotalColumn(int totalColumn) {
        this.totalColumn = totalColumn;
    }

    @Override
    public List<String> getIRowKeys() {
        return null;
    }

    public List<String> getRowKeys() {
        return rowKeys;
    }

    public void setRowKeys(List<String> rowKeys) {
        this.rowKeys = rowKeys;
    }

    public void setMergedRegions(List<CmCellRangeAddress> mergedRegions) {
        this.mergedRegions = mergedRegions;
    }

    public void clearRows(){
        this.rows = null;
    }

    @Override
    protected CmSheet clone(){
        try {
            return (CmSheet)super.clone();
        } catch (CloneNotSupportedException e) {
            return new CmSheet();
        }
    }
}

