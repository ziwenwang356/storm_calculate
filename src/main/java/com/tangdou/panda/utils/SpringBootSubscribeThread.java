package com.tangdou.panda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SpringBootSubscribeThread extends Thread {

    Logger logger = LoggerFactory.getLogger(SubscribeMsgHelper.class);
    protected static String redis_cluster_nodes = "10.19.42.12:6379";
    protected static RedisShuffler redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));
    SubscribeMsgHelper subscribeMsgHelper=new SubscribeMsgHelper();

    //如下是我们需要订阅的channel名字
    private final String channel = "synUpdateDimensionTable";


    @Override
    public void run(){
        redis.safeAccess(x->x.subscribe(subscribeMsgHelper,channel));
    }
}
