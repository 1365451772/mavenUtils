package org.peng;

import org.peng.Reponese.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author sp
 * @Description
 * @create 2021-01-28 14:46
 * @Modified By:
 */
public class OrdinaryMain01 {

    public static void main(String[] args) throws IOException {
//    String json = "{\n" +
//            "  \"data\": [\n" +
//            "    {\n" +
//            "      \"error\": \"Unknown Type: error\",\n" +
//            "      \"platform_resp\": {},\n" +
//            "      \"push_result\": \"string\",\n" +
//            "      \"push_status\": 0,\n" +
//            "      \"token\": \"string\",\n" +
//            "      \"user_id\": \"string\"\n" +
//            "    }\n" +
//            "  ],\n" +
//            "  \"message\": \"string\",\n" +
//            "  \"status\": 0,\n" +
//            "  \"timestamp\": 0\n" +
//            "}";
//
//
//    GoPushResponse goPushResponse = JSON.parseObject(json, GoPushResponse.class);
//    System.out.println(goPushResponse);
//
//    String data = "{\n" +
//            " \"action_id\": \"111111232323\",\n" +
//            " \"app_ids\":[\n" +
//            "  \"com.dream.novel.best.romance.read.app\",\n" +
//            "  \"com.novel.werewolf\",\n" +
//            "  \"com.xing.hotnovel\"\n" +
//            " ],\n" +
//            " \"message\": {\n" +
//            " \"title\": \"global test\",\n" +
//            "    \"body\": \"global body\",\n" +
//            "    \"data\": {\n" +
//            "   \"type\": \"1\",\n" +
//            "   \"bookId\": \"510003275\"\n" +
//            "  }\n" +
//            "  }\n" +
//            "}";
//
//    GoAppPushParam goAppPushParam = JSON.parseObject(data, GoAppPushParam.class);
//
//    System.out.println(goAppPushParam);
//
//
//    String s = JSON.toJSONString(goAppPushParam);
//    System.out.println(s);
        String str = "AB";
        String pattern = "A^A";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        boolean b = m.find();
        if (b) {
            System.out.println(m.group());
        }
//        System.out.println(m.matches());
        Test test = new Test();
        test.setAge(1);
        test.setName("test");

        Test test1 = new Test();
        test1.setName("ss");
        test1.setAge(2);

        HashMap<String, Test> stringTestHashMap = new HashMap<>();

        stringTestHashMap.put("1", test);
        stringTestHashMap.put("2", test1);

        System.out.println(stringTestHashMap.get("1"));

        test.setAge(2);
        System.out.println(stringTestHashMap.get("1"));


    }


}