package com.tangdou.panda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;

public class SubscribeMsgHelper extends JedisPubSub {

    Logger logger = LoggerFactory.getLogger(SubscribeMsgHelper.class);
    protected static String redis_cluster_nodes = "10.19.42.12:6379";
    protected static RedisShuffler redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));

    public SubscribeMsgHelper() {
    }

    public void onMessage(String channel, String message) {
        System.out.println(String.format("received redis published message, channel %s, message %s", channel, message));
        logger.info("received redis published message, channel %s, message %s", channel, message);
    }

    public void onSubscribe(String channel, int subscribedChannels) {

        //这个是在springboot中实现的,在该项目中把update语句取出来然后执行sql
        System.out.println(String.format("subscribed redis channel success, channel %s, subscribedChannels %d", channel, subscribedChannels));
        logger.info("subscribed redis channel success, channel %s, subscribedChannels %d", channel, subscribedChannels);
        ;
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d", channel, subscribedChannels));

    }


}
