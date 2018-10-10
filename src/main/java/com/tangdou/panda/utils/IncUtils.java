package com.tangdou.panda.utils;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;

public class IncUtils {
    private static final Logger LOG = LoggerFactory.getLogger(IncUtils.class);
    private static final List<String> redisList = ImmutableList.of("10.19.78.74");

    private static ExecutorService exec = new ThreadPoolExecutor(8, 15, 60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(500000), MyThreadFactoryBuilder.buildThreadFactory("Inc"),
            new DiscardOldestPolicy());

    static class IncThread implements Runnable {
        private final String module;
        private final String timestamp;
        private final String[] vids;

        IncThread(String module, String timestamp, String... vids) {
            this.module = module;
            this.timestamp = timestamp;
            this.vids = vids;
        }

        @Override
        public void run() {
            String address = redisList.get(new Random().nextInt(redisList.size()));

            JedisFactory jf = JedisFactory.getInstance(address);
            if (jf == null) {
                LOG.error("IncUtils: JedisFactory.getInstance failed, ip = {}", address);
                return;
            }
            Jedis rc = null;
            try {
                rc = jf.getConnection();
                if (rc == null) {
                    LOG.error("IncUtils: jf.getConnection failed, ip = {}", address);
                    return;
                }

                Pipeline pipeline = rc.pipelined();
                Map<String, Response<Long>> responseMap;

                responseMap = new HashMap<>();
                long timeIndex = TimeUtils.getTimeIndex(timestamp, 30 * 60 * 1000);
                for (String vid : vids) {
                    String key = "CTR_" + module + "_" + timeIndex + "_" + vid;
                    responseMap.put(vid, pipeline.incr(key));
                    pipeline.expire(key, 60 * 60 * 24 * 3);
                }
                pipeline.sync();

                for (String vid : responseMap.keySet()) {
                    Response<Long> res = responseMap.get(vid);
                    long cnt = null == res.get() ? 0 : res.get();
                    pipeline.hset("CTR_" + module + "_" + vid, String.valueOf(timeIndex), String.valueOf(cnt));
                    LOG.info(module + "," + vid + "," + timeIndex + "," + cnt + "," + timestamp);
                }
                pipeline.sync();

                responseMap = new HashMap<>();
                long timePiece = TimeUtils.getTimePiece(timestamp);
                for (String vid : vids) {
                    String key = "CTR_sliding_" + module + "_" + timePiece + "_" + vid;
                    responseMap.put(vid, pipeline.incr(key));
                    pipeline.expire(key, 60 * 60 * 24 * 3);
                }
                pipeline.sync();

                for (String vid : responseMap.keySet()) {
                    Response<Long> res = responseMap.get(vid);
                    long cnt = null == res.get() ? 0 : res.get();
                    pipeline.hset("CTR_sliding_" + module + "_" + vid, String.valueOf(timePiece), String.valueOf(cnt));
                    LOG.info(module + "," + vid + "," + timePiece + "," + cnt + "," + timestamp);
                }
                pipeline.sync();
            } finally {
                jf.returnResource(rc);
            }
        }

    }

    public static void increment(String module, String timestamp, String... videoIds) {
        exec.execute(new IncThread(module, timestamp, videoIds));
    }
}
