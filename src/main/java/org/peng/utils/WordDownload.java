package org.peng.utils;


import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author sp
 * @date 2021-08-28 11:42
 */
public class WordDownload {


 /**
  * 读取doc，docx
  *
  * @author sp
  * @date 2021-08-28 17:03
  * @param filePath
  * @return java.lang.String
  * @throws Exception
  * @since
 */
    public static String readWord(String filePath) throws Exception{

        File file = new File(filePath);
        if(file.length()==0) return ""; // 需要操作原因是可能会空文件问题，如果不做处理，在下面读取中会报错
        StringBuffer sb = new StringBuffer();
        String buffer = "";
        try {
            if (filePath.endsWith(".doc")) {
                InputStream is = new FileInputStream(file);
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                if(buffer.length() > 0){
                    //使用回车换行符分割字符串
                    String [] arry = buffer.split(System.getProperty("line.separator"));
                    for (String string : arry) {
                        sb.append("<p>"+string.trim()+"</p></br>");
                    }
                }
            } else if (filePath.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                buffer = extractor.getText();
                if(buffer.length() > 0){
                    //使用换行符分割字符串
                    String [] arry = buffer.split(System.getProperty("line.separator"));
                    //"<p>" + s + "</p></br>"
                    for (String string : arry) {
                        sb.append("<p>"+string.trim()+"</p></br>");
                    }
                }
            } else {
                return null;
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.print("error---->"+filePath);
            e.printStackTrace();
            return null;
        }
    }

}