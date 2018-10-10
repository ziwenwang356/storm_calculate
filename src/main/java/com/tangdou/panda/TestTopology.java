package com.tangdou.panda;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseWindowedBolt;
import com.tangdou.panda.bolt.ClickWindowBolt;
import com.tangdou.panda.bolt.DisplayWindowBolt;
import com.tangdou.panda.kafka.KafkaSpout;
import com.tangdou.panda.kafka.KafkaSpoutConfig;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestTopology {
    //private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());

    private static KafkaSpoutConfig buildSpoutConfig() {
        String borkers = "ukafka-1qamr5-1-bj04.service.ucloud.cn:9092,ukafka-1qamr5-2-bj04.service.ucloud.cn:9092,ukafka-1qamr5-3-bj04.service.ucloud.cn:9092";
        String topic = "adlog";
        String zookeepers = "10.19.90.47:2181";
        Properties props = new Properties();
        props.setProperty("kafka.topic", topic);
        props.setProperty("kafka.broker.hosts", borkers);
        props.setProperty("kafka.zookeeper.hosts", zookeepers);
        props.setProperty("kafka.client.id", "ad_stat_199");
        props.setProperty("kafka.broker.partitions", "8");
        props.setProperty("kafka.fetch.from.beginning", "false");
        return new KafkaSpoutConfig(props);
    }

    private static Config buildStormConfig() {
        Config conf = new Config();
        conf.setDebug(false);
        //conf.setMaxTaskParallelism(1000);
        conf.put(Config.STORM_ZOOKEEPER_SESSION_TIMEOUT, "100000");
        conf.put(Config.STORM_ZOOKEEPER_RETRY_TIMES, "5");
        conf.put(Config.STORM_ZOOKEEPER_RETRY_INTERVAL, "5000");
        //conf.setMaxSpoutPending(1000);
        conf.setNumAckers(16);
        conf.setNumWorkers(95);
        //conf.setNumWorkers(49);
        return conf;
    }

    public static void main(String[] args) throws Exception {
        String prefix = TestTopology.class.getSimpleName();
        String time = FORMATTER.format(Instant.now());
        String name = prefix + "-" + time;
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(buildSpoutConfig()), 32);
        // kv-bolt
        builder.setBolt("click-bolt", new ClickWindowBolt().withWindow(new BaseWindowedBolt.Duration(30, TimeUnit.MINUTES), new BaseWindowedBolt.Duration(15, TimeUnit.MINUTES)), 64).noneGrouping("kafka-spout");
        builder.setBolt("display-bolt", new DisplayWindowBolt().withWindow(new BaseWindowedBolt.Duration(30, TimeUnit.MINUTES), new BaseWindowedBolt.Duration(15, TimeUnit.MINUTES)), 128).noneGrouping("kafka-spout");
        StormSubmitter.submitTopology(name, buildStormConfig(), builder.createTopology());
    }
}
