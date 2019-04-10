package cm.deity.excel.poi.vo;

import cm.deity.excel.inter.ISheet;
import cm.deity.excel.poi.abstractvo.AbstractCmWorkBook;
import cm.deity.excel.util.ExcelReaderConfig;
import cm.deity.excel.util.ExcelReaderUtil;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/22 15:59
 * @VERSION : 1.0
 */
public class CmWorkBook extends AbstractCmWorkBook implements Cloneable {
    protected List<CmSheet> qwSheets = new ArrayList<>();

    public CmWorkBook() { }

    public CmWorkBook(List<CmSheet> qwSheets) {
        this.qwSheets = qwSheets;
    }

    @Override
    public int getNumberOfSheets() {
        return !ObjectUtils.isEmpty(qwSheets) ? qwSheets.size() : 0;
    }

    @Override
    public void addSheet(ISheet sheet) {
        this.qwSheets.add((CmSheet) sheet);
    }

    public void addSheet(CmSheet sheet) {
        this.qwSheets.add(sheet);
    }

    @Override
    public ISheet getISheetAt(int i) {
        if(!ObjectUtils.isEmpty(this.qwSheets)){
            ExcelReaderUtil.validateSheetIndex(this.qwSheets.size(), i);
            return this.qwSheets.get(i);
        }else{
            return null;
        }
    }

    @Override
    public CmSheet getSheetAt(int i) {
        if(!ObjectUtils.isEmpty(this.qwSheets)){
            ExcelReaderUtil.validateSheetIndex(this.qwSheets.size(), i);
            return this.qwSheets.get(i);
        }else{
            return null;
        }
    }

    public CmSheet getCmSheetAt(int i) {
        if(!ObjectUtils.isEmpty(this.qwSheets)){
            ExcelReaderUtil.validateSheetIndex(this.qwSheets.size(), i);
            return this.qwSheets.get(i);
        }else{
            return null;
        }
    }

    @Override
    protected CmWorkBook clone(){
        try {
            return (CmWorkBook)super.clone();
        } catch (CloneNotSupportedException e) {
            return new CmWorkBook();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if(ObjectUtils.isEmpty(qwSheets)) {
                return;
            }

            //todo
//            IJedis jedis = null;
//            if (ExcelReaderUtil.isUseRedis()) {
//                jedis = new JedisCacheServiceImpl();
//            }

            for (CmSheet qwSheet : qwSheets) {
                List<String> keys = qwSheet.getRowKeys();
                if (ObjectUtils.isEmpty(keys)) {
                    continue;
                }
                for (String key : keys) {
                    if (ExcelReaderConfig.rowMap.containsKey(key)) {
                        ExcelReaderConfig.rowMap.remove(key);
                    }
                }

//                if (jedis != null) {
//                    for (String key : keys) {
//                        //清除redies缓存
//                        jedis.hdel(key, ConstantUtil.REDIS_FIELD);
//                    }
//                }
            }
        }catch (Exception e){
            //ExceptionCenter.addException(e, "资源回收失败","");
        }
        super.finalize();
    }
}
