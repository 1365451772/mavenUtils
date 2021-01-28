package org.peng.utils;


import java.io.File;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author sp
 * @Description
 * @create 2021-01-28 14:55
 * @Modified By:
 */
@Component
public class ExcelUtil {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void xlsToJsonArray(File file) {
    Sheet sheet;
    Workbook book;
    Cell cellId, purpose, name, level, harm, solution;
//    JSONArray array = new JSONArray();
    try {
      //要读取的excel文件
      book = Workbook.getWorkbook(file);
      //获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
      sheet = book.getSheet(1);
      int columns = sheet.getColumns();
      int rows = sheet.getRows();
      for (int i = 0; i < rows; i++) {
        //获取每一行的单元格
        cellId = sheet.getCell(1, i);//（列，行）
        purpose = sheet.getCell(2, i);
        name = sheet.getCell(3, i);
        level = sheet.getCell(4, i);
        harm = sheet.getCell(5, i);
        solution = sheet.getCell(6, i);

        String sql = "INSERT INTO t_cwe_item (cwe_id, harm, level, name, purpose, solution) VALUES "
            + "(" + "'" + cellId.getContents() + "'" + ", " + "'" + harm.getContents() + "'" + ", "
            + "'" + level.getContents() + "'" + ", " + "'" + name.getContents() + "'" + ", "
            + "'" + purpose.getContents() + "'" + ", "
            + "'" + solution.getContents() + "'" + ")";
        jdbcTemplate.execute(sql);

//        JSONObject object = new JSONObject();
//        object.put("姓名", name.getContents());
//        object.put("数量", level.getContents());
//        object.put("住址", harm.getContents());
//        array.add(object);
      }
      book.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }
}


