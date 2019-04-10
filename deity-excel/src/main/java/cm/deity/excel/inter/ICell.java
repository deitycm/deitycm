package cm.deity.excel.inter;

import java.util.Date;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/6/26 9:39
 * @VERSION : 1.0
 */
public interface ICell {
     /**
      * 获取当前单元格行号
      * @return
      */
     int getRowIndex();

     /**
      * 获取当前列号
      * @return
      */
     int getColumnIndex();

     /**
      * 获取单元格值
      * @return
      */
     Object getICellValue();

     /**
      * 设置单元格值
      * @param cellValue
      */
     void setICellValue(Object cellValue);

     /**
      * 获取字符串单元格值
      * @return
      */
     String getStringCellValue();

     /**
      * 获取日期单元格值
      * @return
      */
     Date getDateCellValue();

     /**
      * 获取布尔单元格值
      * @return
      */
     boolean getBooleanCellValue();
}
