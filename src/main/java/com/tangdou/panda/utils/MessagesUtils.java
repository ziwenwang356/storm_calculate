package com.tangdou.panda.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesUtils {


    // 读入JSON到Dict
    public  Map<String, String> readJson(String line) {
        Map<String, String> uaDict = new HashMap<>();
        for (String str : Const.UA_LIST) {
            uaDict.put(str, "");
        }

        try {
            JSONObject data = JSON.parseObject(line);

            if (data.get("tags") != null) {

                String regEx = "\\\\(?![/u\"])";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);

                Matcher matcher = pattern.matcher(data.getString("message"));
                data = JSON.parseObject(matcher.group());
            }
            data.remove("path");
            data.remove("@version");
            String reqStr = (String) data.remove("request");

            etlOther(data, uaDict);

            etlRequest(reqStr, uaDict);
        } catch (Exception e) {
            return null;
        }
        return uaDict;
    }


    // 解析、清洗、转换日志的非request部分
    public void etlOther(final JSONObject other, Map<String, String> uaDict) {
        try {
            other.put("timestamp", LogDecoder.isoStr2utc8Str((String) other.remove("@timestamp")));

            if (!other.getString("backtime").equals("-")) {
                other.put("backtime", String.valueOf(other.getFloat("backtime") * 1000));
                other.put("responsetime", String.valueOf(other.getFloat("responsetime") * 1000));
            } else {
                other.put("backtime", "0.0");
            }

            for (Map.Entry<String, Object> it : other.entrySet()) {
                String newKey = "u_" + it.getKey();
                uaDict.put(newKey, it.getValue().toString());
            }
            other.clear();
        } catch (Exception e) {
        }
    }

    // 解析、清洗、转换日志的request部分
    public void etlRequest(String jstr, Map<String, String> uaDict) {
        JSONObject bigjsonDict = new JSONObject();

        try {
            // (GET|POST)\s(\/.*)\?(.*)\s(HTTP\/1\.1)
            String regEx = "(GET|POST)\\s(/.*)\\?(.*)\\s(HTTP/1\\.1)";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(jstr);
            if (matcher.matches()) {
                uaDict.put("u_method", matcher.group(1));
                uaDict.put("u_url", matcher.group(2));
                uaDict.put("u_verb", matcher.group(4));

                String[] reqList = matcher.group(3).split("&");
                for (String reqEle : reqList) {
                    String[] kvList = reqEle.split("=");
                    if (kvList.length < 2) {
                        String v1 = kvList[0];
                        kvList = new String[2];
                        kvList[0] = v1;
                        kvList[1] = "";
                    }
                    String uaEle = "u_" + kvList[0].toLowerCase();

                    String cleanEle = Const.RF.getOrDefault(uaEle, uaEle);
                    if (uaDict.containsKey(cleanEle)) {
                        uaDict.put(cleanEle, kvList[1]);
                    } else {
                        bigjsonDict.put(cleanEle, kvList[1]);
                    }
                }
                uaDict.put("u_bigger_json", bigjsonDict.toJSONString());
            }
        } catch (Exception e) {
        }
    }

}
