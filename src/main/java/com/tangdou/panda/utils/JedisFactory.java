package com.tangdou.panda.utils;

/**
 * Created by jiangtao on 2017/6/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;


public class JedisFactory {
    // 最大可用连接数，默认值为8，如果赋值为-1则表示不限制
    private static int MAX_TOTAL = 2000;
    // 最大空闲连接数，默认值为8
    private static int MAX_IDLE = 500;
    // 最小空闲连接数
    private static int MIN_IDLE = 200;
    // 最大等待连接毫秒数，默认值为-1表示永不超时
    private static int MAX_WAIT = 30000;
    // 连接redis超时时间
    private static int TIMEOUT = 45000;
    // true表示验证连接
    private static boolean TEST_ON_BORROW = true;

    private String IP;
    //连接池
    private JedisPool jedisPool = null;

    private static Map<String, JedisFactory> JedisFactorys = new HashMap<String, JedisFactory>();

    protected final Logger logger = LoggerFactory.getLogger(JedisFactory.class);

    //constractor
    public JedisFactory(String ip, int port, String password) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setMaxIdle(MAX_IDLE);
        config.setMinIdle(MIN_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);

        jedisPool = new JedisPool(config, ip, port, TIMEOUT);


        IP = ip;

    }

    public static synchronized JedisFactory getInstance(String ip) {
        if (JedisFactorys.get(ip) == null) {
            try {
                JedisFactorys.put(ip, new JedisFactory(ip, 6379, ""));
            } catch (Exception e) {
                JedisFactorys.remove(ip);
            }
        }
        return JedisFactorys.get(ip);
    }

    public synchronized Jedis getConnection() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                logger.error("jedisPool is null");
                return null;
            }
        } catch (Exception e) {
            JedisFactorys.remove(IP);
            logger.error("exception in getResource", e);
            return null;
        }
    }

    public synchronized void printPoolStatus() {
        logger.info("active:" + jedisPool.getNumActive() + ",idle:" + jedisPool.getNumIdle());
    }

    public synchronized void returnResource(Jedis redis) {
        if (redis != null) {
            jedisPool.returnResource(redis);
        }
    }

    public static void main(String[] args) {

    }
}
