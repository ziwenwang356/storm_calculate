package com.tangdou.panda.utils;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;

public class RedisShuffler {
    private static final Logger LOG = LoggerFactory.getLogger(RedisShuffler.class);

    private static final int MAX_TOTAL = 200;
    private static final int MAX_IDLE = 50;
    private static final int MIN_IDLE = 10;
    private static final int MAX_WAIT = 30000;
    private static final boolean TEST_ON_BORROW = true;

    private static final JedisPoolConfig CONFIG;
    private static final int TIMEOUT = 45000;

    private static final Map<HostAndPort, JedisPool> JEDIS_POOL_MAP = Maps.newConcurrentMap();

    static {
        CONFIG = new JedisPoolConfig();
        CONFIG.setMaxTotal(MAX_TOTAL);  // TODO make it configurable
        CONFIG.setMaxIdle(MAX_IDLE);
        CONFIG.setMinIdle(MIN_IDLE);
        CONFIG.setMaxWaitMillis(MAX_WAIT);
        CONFIG.setTestOnBorrow(TEST_ON_BORROW);
    }

    //private List<String> addresses;  // TODO delete this
    private List<JedisPool> jedisPoolList = Lists.newArrayList();

    public RedisShuffler(List<String> addresses) {
        //this.addresses = addresses;  // TODO delete this
        for (String address : addresses) {
            HostAndPort node = parseAddress(address);
            if (!JEDIS_POOL_MAP.containsKey(node)) {
                JEDIS_POOL_MAP.putIfAbsent(node, new JedisPool(CONFIG, node.getHost(), node.getPort(), TIMEOUT));
            }
            jedisPoolList.add(JEDIS_POOL_MAP.get(node));
        }
    }

    private static HostAndPort parseAddress(String address) {
        String[] p = address.split(":");
        switch (p.length) {
            case 1:
                return new HostAndPort(address, 6379);
            case 2:
                return new HostAndPort(p[0], Integer.parseInt(p[1]));
            default:
                LOG.error("Error when parsing redis address: " + address);
                throw new RuntimeException();
        }
    }

    private Jedis getJedis(int index) {
        JedisPool jedisPool = jedisPoolList.get(index);
        try {
            return jedisPool.getResource();
        } catch (JedisConnectionException e) {
            throw e;
        }
    }

    public void safeAccess(Consumer<Jedis> consumer) {
        Jedis jedis = null;
        int index = ThreadLocalRandom.current().nextInt(jedisPoolList.size());
        try {
            jedis = this.getJedis(index);
            consumer.accept(jedis);
        } catch (JedisException e) {
            LOG.error("Error when executing redis operation", e);
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (JedisException e) {
                    LOG.error("Error when executing redis operation", e);
                }
            }
        }
    }

    public <T> T safeAccess(Function<Jedis, T> func, T defaultValue) {
        T t = null;
        Jedis jedis = null;
        int index = ThreadLocalRandom.current().nextInt(jedisPoolList.size());
        try {
            jedis = this.getJedis(index);
            t = func.apply(jedis);
            return (null == t ? defaultValue : t);
        } catch (JedisException e) {
            LOG.error("Error when executing redis operation:", e);
            return defaultValue;
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (JedisException e) {
                    LOG.error("Error when executing redis operation:", e);
                }
            }
        }
    }
}
