package org.peng;

import org.peng.utils.ExcelUtil;

import java.io.File;

/**
 * @Author sp
 * @Description
 * @create 2021-01-28 14:46
 * @Modified By:
 */
public class OrdinaryMain01 {

  public static void main(String[] args) {
    File file = new File("C:\\Users\\bangcle\\Desktop\\1.xls");
    ExcelUtil excelUtil = new ExcelUtil();
    excelUtil.xlsToJsonArray(file);

    System.out.println(1111111);
  }

}
