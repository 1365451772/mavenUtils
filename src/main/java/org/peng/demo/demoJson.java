package org.peng.demo;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.peng.utils.JsonUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sp
 * @date 2021-08-28 17:09
 */
public class demoJson {

    public static void resultsTransformation(String engineResultPath) {

//        String engineResultPath = "D:\\Ruifeng\\firmwareAnalysis\\fact结果比对\\AmbaSysFW.bin\\firmware_engine_result";

        JSONObject totalObj = new JSONObject();

        List<String> engineResulList = FileUtil.readUtf8Lines(engineResultPath);

        for (String result : engineResulList) {

            JSONObject parseObject = JSONObject.parseObject(result);

            String filePath = parseObject.getString("file_path");
            JSONObject fileHashObj = parseObject.getJSONObject("file_hash");
            String fileName = parseObject.getString("file_name");
            JSONArray fileTypeArr = parseObject.getJSONArray("file_type");

            JSONObject fileBasicObj = new JSONObject();
            fileBasicObj.put("file_path", filePath);
            fileBasicObj.put("file_name", fileName);
            fileBasicObj.put("file_type", fileTypeArr);
            fileBasicObj.put("file_hash", fileHashObj);

            String cveLookup = parseObject.getString("cve_lookup");
            if (StringUtils.isNotEmpty(cveLookup) && !cveLookup.equals("[]")) {

                Set<String> softwareDescSet = new HashSet<>();

                JSONObject softwareComponentsObj = parseObject.getJSONObject("software_components");
                if (softwareComponentsObj != null) {
                    Iterator<String> iterator = softwareComponentsObj.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONObject softwareDetailObj = softwareComponentsObj.getJSONObject(key);
                        JSONArray stringsArray = softwareDetailObj.getJSONArray("strings");
                        if (stringsArray != null && stringsArray.size() > 0) {
                            for (int x = 0; x < stringsArray.size(); x++) {
                                String softwareStr = stringsArray.getJSONArray(x).getString(2).toLowerCase();
                                if (softwareStr.startsWith("\\x00")) {
                                    softwareStr = softwareStr.replace("\\x00", "");
                                }
                                softwareDescSet.add(softwareStr);
                            }
                        }
                    }
                }

                Iterator<String> iterator = softwareDescSet.iterator();
                while (iterator.hasNext()) {
                    String softwareDesc = iterator.next();
                    JSONArray cvelookupArray = JSONArray.parseArray(cveLookup);
                    for (int x = 0; x < cvelookupArray.size(); x++) {
                        String cveNo = cvelookupArray.getString(x);
                        JSONObject cve = JSONObject.parseObject(cveNo);
                        String splicing = softwareDesc + "##" + cve.getString("cve_id");
                        JSONObject cveLookupTotalObj = totalObj.getJSONObject("cve_lookup");
                        if (cveLookupTotalObj == null) {
                            cveLookupTotalObj = new JSONObject();
                            JSONArray list = new JSONArray();
                            list.add(fileBasicObj);
                            cveLookupTotalObj.put(splicing, list);
                        } else {
                            JSONArray list = cveLookupTotalObj.getJSONArray(splicing);
                            if (list == null) {
                                list = new JSONArray();
                            }
                            list.add(fileBasicObj);
                            cveLookupTotalObj.put(splicing, list);
                        }

                        totalObj.put("cve_lookup", cveLookupTotalObj);
                    }
                }

            }

            String cryptoMaterial = parseObject.getString("crypto_material");
            if (StringUtils.isNotEmpty(cryptoMaterial) && !cryptoMaterial.equals("{}")) {
                JSONObject cryptoMaterialObj = JSONObject.parseObject(cryptoMaterial);
                Iterator<String> iterator = cryptoMaterialObj.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject cryptoMaterialTotalObj = totalObj.getJSONObject("crypto_material");
                    if (cryptoMaterialTotalObj == null) {


                        cryptoMaterialTotalObj = new JSONObject();
                        JSONArray list = new JSONArray();
                        list.add(fileBasicObj);
                        cryptoMaterialTotalObj.put(key, list);
                    } else {
                        JSONArray list = cryptoMaterialTotalObj.getJSONArray(key);
                        if (list == null) {
                            list = new JSONArray();
                        }
                        list.add(fileBasicObj);
                        cryptoMaterialTotalObj.put(key, list);
                    }
                    totalObj.put("crypto_material", cryptoMaterialTotalObj);
                }
            }

            String cweChecker = parseObject.getString("cwe_checker");
            if (StringUtils.isNotEmpty(cweChecker)) {
                JSONObject cweCheckerObj = JSONObject.parseObject(cweChecker);
                Iterator<String> iterator = cweCheckerObj.keySet().iterator();
                while (iterator.hasNext()) {
                    String cweNo = iterator.next();
                    JSONObject cweCheckerTotalObj = totalObj.getJSONObject("cwe_checker");
                    if (cweCheckerTotalObj == null) {
                        cweCheckerTotalObj = new JSONObject();
                        JSONArray list = new JSONArray();
                        list.add(fileBasicObj);
                        cweCheckerTotalObj.put(cweNo, list);
                    } else {
                        JSONArray list = cweCheckerTotalObj.getJSONArray(cweNo);
                        if (list == null) {
                            list = new JSONArray();
                        }
                        list.add(fileBasicObj);
                        cweCheckerTotalObj.put(cweNo, list);
                    }
                    totalObj.put("cwe_checker", cweCheckerTotalObj);
                }
            }

            String cpuArchitectureStr = parseObject.getString("cpu_architecture");
            if (StringUtils.isNotEmpty(cpuArchitectureStr)) {
                JSONObject cpuArchitectureObj = totalObj.getJSONObject("cpu_architecture");
                if (cpuArchitectureObj == null) {
                    cpuArchitectureObj = new JSONObject();
                    JSONArray list = new JSONArray();
                    list.add(fileBasicObj);
                    cpuArchitectureObj.put(cpuArchitectureStr, list);
                } else {
                    JSONArray list = cpuArchitectureObj.getJSONArray(cpuArchitectureStr);
                    if (list == null) {
                        list = new JSONArray();
                    }
                    list.add(fileBasicObj);
                    cpuArchitectureObj.put(cpuArchitectureStr, list);
                }
                totalObj.put("cpu_architecture", cpuArchitectureObj);
            }

            JSONObject softwareComponentsObj = parseObject.getJSONObject("software_components");
            if (softwareComponentsObj != null) {
                Iterator<String> iterator = softwareComponentsObj.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject softwareDetailObj = softwareComponentsObj.getJSONObject(key);
                    JSONArray stringsArray = softwareDetailObj.getJSONArray("strings");
                    if (stringsArray != null && stringsArray.size() > 0) {
                        for (int x = 0; x < stringsArray.size(); x++) {
                            String softwareStr = stringsArray.getJSONArray(x).getString(2).toLowerCase();
                            if (softwareStr.startsWith("\\x00")) {
                                softwareStr = softwareStr.replace("\\x00", "");
                            }

                            JSONObject softwareComponentsTotalObj = totalObj.getJSONObject("software_components");
                            if (softwareComponentsTotalObj == null) {
                                softwareComponentsTotalObj = new JSONObject();
                                JSONArray list = new JSONArray();
                                list.add(fileBasicObj);
                                softwareComponentsTotalObj.put(softwareStr, list);
                            } else {
                                JSONArray list = softwareComponentsTotalObj.getJSONArray(softwareStr);
                                if (list == null) {
                                    list = new JSONArray();
                                    list.add(fileBasicObj);

                                }
                                for (int j = 0; j < list.size(); j++) {
                                    JSONObject hadJsobject = list.getJSONObject(j);
                                    if (!hadJsobject.getString("file_path").equals(fileBasicObj.getString("file_path"))) {
                                        list.add(fileBasicObj);
                                    }
                                }
//                                list.add(fileBasicObj);
                                softwareComponentsTotalObj.put(softwareStr, list);
                            }
                            totalObj.put("software_components", softwareComponentsTotalObj);
                        }
                    }
                }
            }


            String hardcode = parseObject.getString("hardcode");
            if (StringUtils.isNotEmpty(hardcode)) {
                JSONArray hardcodeArr = JSONObject.parseArray(hardcode);

                for (int x = 0; x < hardcodeArr.size(); x++) {
                    String hardcodeString = hardcodeArr.getString(x);
                    JSONObject hardcodeTotalObj = totalObj.getJSONObject("hardcode");
                    if (hardcodeTotalObj == null) {
                        hardcodeTotalObj = new JSONObject();
                        JSONArray list = new JSONArray();
                        list.add(fileBasicObj);
                        hardcodeTotalObj.put(hardcodeString, list);
                    } else {
                        JSONArray list = hardcodeTotalObj.getJSONArray(hardcodeString);
                        if (list == null) {
                            list = new JSONArray();
                        }
                        list.add(fileBasicObj);
                        hardcodeTotalObj.put(hardcodeString, list);
                    }
                    totalObj.put("hardcode", hardcodeTotalObj);
                }
            }

            String userspasswords = parseObject.getString("users_passwords");
            if (StringUtils.isNotEmpty(userspasswords)) {
                JSONObject userspasswordsObj = JSONObject.parseObject(userspasswords);
                if (userspasswordsObj != null) {
                    Iterator<String> iterator = userspasswordsObj.keySet().iterator();
                    while (iterator.hasNext()) {
                        String userspasswordsString = iterator.next();
                        JSONObject userspasswordsTotalObj = totalObj.getJSONObject("users_passwords");
                        if (userspasswordsTotalObj == null) {
                            userspasswordsTotalObj = new JSONObject();
                            JSONArray list = new JSONArray();
                            list.add(fileBasicObj);
                            userspasswordsTotalObj.put(userspasswordsString, list);
                        } else {
                            JSONArray list = userspasswordsTotalObj.getJSONArray(userspasswordsString);
                            if (list == null) {
                                list = new JSONArray();
                            }
                            list.add(fileBasicObj);
                            userspasswordsTotalObj.put(userspasswordsString, list);
                        }
                        totalObj.put("users_passwords", userspasswordsTotalObj);
                    }
                }
            }

            String exploitMitigations = parseObject.getString("exploit_mitigations");
            if (StringUtils.isNotEmpty(exploitMitigations)) {
                JSONObject exploitMitigationsObj = JSONObject.parseObject(exploitMitigations);
                if (exploitMitigationsObj != null) {
                    Iterator<String> iterator = exploitMitigationsObj.keySet().iterator();
                    while (iterator.hasNext()) {
                        String exploitMitigationsString = iterator.next();
                        JSONObject exploitMitigationsTotalObj = totalObj.getJSONObject("exploit_mitigations");
                        if (exploitMitigationsTotalObj == null) {
                            exploitMitigationsTotalObj = new JSONObject();
                            JSONArray list = new JSONArray();
                            list.add(fileBasicObj);
                            exploitMitigationsTotalObj.put(exploitMitigationsString, list);
                        } else {
                            JSONArray list = exploitMitigationsTotalObj.getJSONArray(exploitMitigationsString);
                            if (list == null) {
                                list = new JSONArray();
                            }
                            list.add(fileBasicObj);
                            exploitMitigationsTotalObj.put(exploitMitigationsString, list);
                        }
                        totalObj.put("exploit_mitigations", exploitMitigationsTotalObj);
                    }
                }
            }


            String operatingSystemStr = parseObject.getString("operating_system");
            if (StringUtils.isNotEmpty(operatingSystemStr)) {
                JSONObject operatingSystemObj = totalObj.getJSONObject("operating_system");
                if (operatingSystemObj == null) {
                    operatingSystemObj = new JSONObject();
                    JSONArray list = new JSONArray();
                    list.add(fileBasicObj);
                    operatingSystemObj.put(operatingSystemStr, list);
                } else {
                    JSONArray list = operatingSystemObj.getJSONArray(operatingSystemStr);
                    if (list == null) {
                        list = new JSONArray();
                    }
                    list.add(fileBasicObj);
                    operatingSystemObj.put(operatingSystemStr, list);
                }
                totalObj.put("operating_system", operatingSystemObj);
            }
            String configset = parseObject.getString("configset");
            if (StringUtils.isNotEmpty(configset)) {
                JSONObject configsetObj = JSONObject.parseObject(configset);
                Iterator<String> iterator = configsetObj.keySet().iterator();
                while (iterator.hasNext()) {
                    String configsetNo = iterator.next();
                    JSONObject cweCheckerTotalObj = totalObj.getJSONObject("configset");
                    if (cweCheckerTotalObj == null) {
                        cweCheckerTotalObj = new JSONObject();
                        JSONArray list = new JSONArray();
                        list.add(fileBasicObj);
                        cweCheckerTotalObj.put(configsetNo, list);
                    } else {
                        JSONArray list = cweCheckerTotalObj.getJSONArray(configsetNo);
                        if (list == null) {
                            list = new JSONArray();
                        }
                        list.add(fileBasicObj);
                        cweCheckerTotalObj.put(configsetNo, list);
                    }
                    totalObj.put("configset", cweCheckerTotalObj);
                }
            }


        }

        //        String engineResultPath = "D:\\Ruifeng\\firmwareAnalysis\\fact结果比对\\AmbaSysFW.bin\\firmware_engine_result";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        String outPath = engineResultPath.substring(0,engineResultPath.lastIndexOf("\\"))+ File.separator+"firmware_result_json";
        String outPath = engineResultPath.substring(0, engineResultPath.lastIndexOf("\\")) + File.separator + dateFormat.format(date) + "-firmware_result_json-" + System.currentTimeMillis();

        FileUtil.writeUtf8String(JsonUtils.toJson(totalObj), outPath);

//        FileUtil.writeUtf8String(JacksonUtil.toJson(totalObj), "D:\\Ruifeng\\firmwareAnalysis\\fact结果比对\\AmbaSysFW.bin\\firmware_result_json");
    }
}



