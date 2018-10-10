package com.tangdou.panda.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tangdou.panda.model.AdClickAndDisplayLog;
import com.tangdou.panda.utils.Const;
import com.tangdou.panda.utils.LogDecoder;
import com.tangdou.panda.utils.MetricRegistryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Created by jiangtao on 2017/6/7.
 * 解析、清洗、转换日志
 */
public class KVBolt extends BaseBasicBolt {
    private static final Logger LOG = LoggerFactory.getLogger(KVBolt.class);

    private MetricRegistry metrics;
    private Timer executeTimer;
    private Meter outputMeter;
    private Meter exceptionMeter;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        metrics = MetricRegistryUtils.getMetricRegistry();
        executeTimer = metrics.timer(name("kv-bolt", "execute"));
        outputMeter = metrics.meter(name("kv-bolt", "output"));
        exceptionMeter = metrics.meter(name("kv-bolt", "exceptions-all"));
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Timer.Context context = executeTimer.time();
        try {
            String line = input.getStringByField("bytes");
            int partition = input.getIntegerByField("partition");
            JSONObject data = JSON.parseObject(line);
            LOG.info(partition + "#" + LogDecoder.isoStr2utc8Str(data.getString("@timestamp")));
            Map<String, String> uaDict = readJson(line);
            AdClickAndDisplayLog lmsg = AdClickAndDisplayLog.createFromMap(uaDict);
            collector.emit(lmsg.toValues());
            outputMeter.mark();
        } catch (Exception e) {
            LOG.error("Caught exception in kv-bolt", e);
            metrics.meter(name("kv-bolt", "exceptions", e.getClass().getSimpleName())).mark();
            exceptionMeter.mark();
        } finally {
            context.stop();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(AdClickAndDisplayLog.getFields());
    }

    // 读入JSON到Dict
    private Map<String, String> readJson(String line) {
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
    private void etlOther(final JSONObject other, Map<String, String> uaDict) {
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
    private void etlRequest(String jstr, Map<String, String> uaDict) {
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
