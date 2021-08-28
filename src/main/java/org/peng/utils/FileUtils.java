package org.peng.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author sp
 * @date 2021-08-18 12:12
 */
public class FileUtils {

    private static String url = "https://readcms.oss-us-west-1.aliyuncs.com/20210816/939a0058d884498792f668c249cd64a2.jpg";
    private static String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
    private static Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");//正则判断

    public static String getfileNameByUrl(String url) {
        Matcher mc = pat.matcher(url);//条件匹配
        while (mc.find()) {
            String substring = mc.group();//截取文件名后缀名
           return substring;
        }

        return "";
    }


    public static String getAfterDomainByUrl(String url){
        Pattern pat= Pattern.compile("[\\d]+\\/[\\w]+[\\.]("+suffixes+")");//正则判断
        Matcher mc=pat.matcher(url);//条件匹配
        while(mc.find()){
            String substring = mc.group();//截取文件名后缀名
         return substring;
        }
        return "";
    }

    public static  void testZip(File file) {
        InputStream in = null;
        ZipInputStream zin = null;
        ZipFile zipFile = null;
        ZipEntry ze = null;
        String line;
        File temp = null;
        try {
            zipFile = new ZipFile(file, Charset.forName("GBK"));
            in = new BufferedInputStream(new FileInputStream(file));
            zin = new ZipInputStream(in,Charset.forName("GBK"));
            while ((ze = zin.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze),Charset.forName("GBK")));
                        while ((line = br.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        br.close();
                        System.out.println(stringBuilder.toString());
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zin != null) {
                try {
                    zin.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

}
