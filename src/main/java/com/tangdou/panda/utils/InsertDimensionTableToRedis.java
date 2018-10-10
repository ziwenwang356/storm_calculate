package com.tangdou.panda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class InsertDimensionTableToRedis {

    Logger logger = LoggerFactory.getLogger(SubscribeMsgHelper.class);
    protected static String redis_cluster_nodes = "10.19.42.12:6379";
    protected static RedisShuffler redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));
    SubscribeMsgHelper subscribeMsgHelper=new SubscribeMsgHelper();



}
