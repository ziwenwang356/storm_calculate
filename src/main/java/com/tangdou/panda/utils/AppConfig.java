package com.tangdou.panda.utils;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

public class AppConfig {

    public static final String KAFKA_ZOOKEEPER_HOSTS = "kafka.zookeeper.hosts";
    public static final String KAFKA_BROKER_HOSTS = "kafka.broker.hosts";
    public static final String KAFKA_BROKER_PARTITIONS = "kafka.broker.partitions";
    public static final String KAFKA_TOPIC = "kafka.topic";
    public static final String KAFKA_CLIENT_ID = "kafka.client.id";
    public static final String KAFKA_FETCH_FROM_BEGINNING = "kafka.fetch.from.beginning";

    public static final String REDIS_WATCHED_VIDEO = "redis.watched-video";
    public static final String REDIS_EXPOSED_VIDEO = "redis.exposed-video";
    public static final String REDIS_FAVORED_VIDEO_SHORT = "redis.favored-video.short";
    public static final String REDIS_FAVORED_VIDEO_LONG = "redis.favored-video.long";
    public static final String REDIS_VIDEO_SIMILARITY_SHORT = "redis.video-similarity.short";
    public static final String REDIS_VIDEO_SIMILARITY_LONG = "redis.video-similarity.long";
    public static final String REDIS_USER_SIMILARITY = "redis.user-similarity";
    public static final String REDIS_RECOMMEND_FEED_SHORT = "redis.recommend-feed.short";
    public static final String REDIS_RECOMMEND_FEED_LONG = "redis.recommend-feed.long";
    public static final String REDIS_CTR = "redis.ctr";
    public static final String REDIS_MAB = "redis.mab";
    public static final String REDIS_FEATURE_SET = "redis.feature-set";
    public static final String REDIS_COMMON_STATS = "redis.common-stats";

    public static final String MYSQL_URL = "mysql.url";
    public static final String MYSQL_USERNAME = "mysql.username";
    public static final String MYSQL_PASSWORD = "mysql.password";

    public static final String DRUID_INITIAL_SIZE = "druid.initial-size";
    public static final String DRUID_MIN_IDLE = "druid.min-idle";
    public static final String DRUID_MAX_ACTIVE = "druid.max-active";
    public static final String DRUID_POOL_PREPARED_STATEMENTS = "druid.pool-prepared-statements";

    public static final String SALMON_PARALLELISM_HINT_KAFKA_SPOUT = "salmon.parallelism-hint.kafka-spout";
    public static final String SALMON_PARALLELISM_HINT_KV_BOLT = "salmon.parallelism-hint.kv-bolt";
    public static final String SALMON_PARALLELISM_HINT_REC_BOLT = "salmon.parallelism-hint.rec-bolt";
    public static final String SALMON_PARALLELISM_HINT_CTR_BOLT = "salmon.parallelism-hint.ctr-bolt";
    public static final String SALMON_NUM_TASKS_KV_BOLT = "salmon.num-tasks.kv-bolt";
    public static final String SALMON_NUM_TASKS_REC_BOLT = "salmon.num-tasks.rec-bolt";
    public static final String SALMON_NUM_TASKS_CTR_BOLT = "salmon.num-tasks.ctr-bolt";

    public static final String REDIS_CLICK_DISPLAY_SET = "redis.click-display-set";

    private static final String CONFIG_NAMESPACE = "com.tangdou.salmon";
    private static final Config CONFIG;

    static {
//        Config systemProperties = ConfigFactory.systemProperties();
//        String env = systemProperties.hasPath("env") ? systemProperties.getString("env") : "dev";
//        String envFile = "application." + env + ".conf";
        String envFile = "application.conf";
        CONFIG = ConfigFactory.parseResources(envFile).resolve().getConfig(CONFIG_NAMESPACE);
    }

    public static Config getConfig() {
        return CONFIG;
    }

    public static String getString(String path) {
        return CONFIG.getString(path);
    }

    public static List<String> getStringList(String path) {
        return CONFIG.getStringList(path);
    }

    public static int getInt(String path) {
        return CONFIG.getInt(path);
    }

    public static boolean getBoolean(String path) {
        return CONFIG.getBoolean(path);
    }
}
