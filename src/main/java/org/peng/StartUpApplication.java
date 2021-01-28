package org.peng;

import java.io.File;
import org.peng.utils.ExcelUtil;

/**
 * @Author sp
 * @Description
 * @create 2021-01-28 14:46
 * @Modified By:
 */
public class StartUpApplication {

  public static void main(String[] args) {
    File file = new File("C:\\Users\\bangcle\\Desktop\\1.xls");
    ExcelUtil excelUtil = new ExcelUtil();
    excelUtil.xlsToJsonArray(file);
  }

}
