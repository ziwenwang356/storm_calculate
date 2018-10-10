package com.tangdou.panda.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseWindowedBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.windowing.TupleWindow;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tangdou.panda.entity.Campaign;
import com.tangdou.panda.meta.BudgetTypeEnum;
import com.tangdou.panda.model.AdClickAndDisplayLog;
import com.tangdou.panda.model.AdDisplayLog;
import com.tangdou.panda.model.ModelAndAction;
import com.tangdou.panda.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shade.storm.org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DisplayWindowBolt extends BaseWindowedBolt {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger billing_logger = LoggerFactory.getLogger("billing");
    //    ModelAndAction.of("log", "ad_display");
    //如下的model and action需要更正
    private static final ModelAndAction DISPLAY_MODEL_ACTION = ModelAndAction.of("log", "ad_display");
    private MetricRegistry metrics;
    private Timer executeTimer;
    private Meter outputMeter;
    private Meter exceptionMeter;


    MetricRegistryUtils metricRegistryUtils = new MetricRegistryUtils();
    //    @Value("#{properties['redis.cluster.nodes']}")
    String redis_cluster_nodes = "10.19.42.12:6379";
    protected static RedisShuffler redis = null;
    MessagesUtils messagesUtils = new MessagesUtils();

    // 将如下的内容放到redis中，整点失效？不需要做，因为window已经帮助我们实现了.(key/value,key我们目前放置的是ad_uid+"_"+campaign_id_display/ad_uid+"_"+campaign_id+"_"+subscribe_id"_display,但是我觉得这个再重新设计，因为需要保证它的唯一)
    HashMap<String, Integer> ad_display_campaign_maps = new HashMap<String, Integer>();
    HashMap<String, Integer> ad_display_subscribe_maps = new HashMap<String, Integer>();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        logger.info("billing - nodes :" + redis_cluster_nodes);
        redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));
        metrics = MetricRegistryUtils.getMetricRegistry();
        executeTimer = metrics.timer(metricRegistryUtils.name("display-window-bolt", "execute"));
        outputMeter = metrics.meter(metricRegistryUtils.name("display-window-bolt", "output"));
        exceptionMeter = metrics.meter(metricRegistryUtils.name("display-window-bolt", "exceptions-all"));
    }

    @Override
    public void execute(TupleWindow tupleWindow) {
        for (Tuple input : tupleWindow.get()) {

            Timer.Context context = executeTimer.time();
            try {
                AdClickAndDisplayLog adClickAndDisplayLog = AdClickAndDisplayLog.from(input);
                ModelAndAction modelAndAction = ModelAndAction.of(adClickAndDisplayLog);
                if (modelAndAction.equals(DISPLAY_MODEL_ACTION)) {
                    String ad_uid = input.getStringByField("u_ad_uid");
                    String campaign_id = input.getStringByField("u_campaign_id");
                    String subscribe_id = input.getStringByField("u_subscribe_id");
                    Integer price = input.getIntegerByField("u_price");
                    Integer count_campaign = ad_display_campaign_maps.get(ad_uid + "_" + campaign_id + "_display");
                    if (null == count_campaign) {
                        count_campaign = 0;
                    }
                    ++count_campaign;
                    ad_display_campaign_maps.put(ad_uid + "_" + campaign_id + "_display", count_campaign);
                    Integer count_subscribe = ad_display_subscribe_maps.get(ad_uid + "_" + campaign_id + "_" + subscribe_id + "_display");
                    if (null == count_subscribe) {
                        count_subscribe = 0;
                    }
                    ++count_subscribe;
                    ad_display_subscribe_maps.put(ad_uid + "_" + campaign_id + "_" + subscribe_id + "_display", count_subscribe);
                    logger.info("DisplayWindowBolt is: " + adClickAndDisplayLog.toString());
                    Map<String, String> uaDict = new HashMap<>();
                    String u_gid = input.getStringByField("u_gid");
                    String u_channel_id = input.getStringByField("u_channel_id");
                    String u_position_id = input.getStringByField("u_position_id");
                    String u_timestamp = input.getStringByField("u_timestamp");
                    String u_status = input.getStringByField("u_status");
                    String u_action = input.getStringByField("u_action");
                    String u_uuid = input.getStringByField("u_uuid");
                    String u_diu = input.getStringByField("u_diu");
                    uaDict.put("u_gid", u_gid);
                    uaDict.put("u_channel_id", u_channel_id);
                    uaDict.put("u_position_id", u_position_id);
                    uaDict.put("u_subscribe_id", subscribe_id);
                    uaDict.put("u_campaign_id", campaign_id);
                    uaDict.put("u_ad_uid", ad_uid);
                    uaDict.put("u_timestamp", u_timestamp);
                    uaDict.put("u_price", String.valueOf(price));
                    uaDict.put("u_status", u_status);
                    uaDict.put("u_action", u_action);
                    uaDict.put("u_uuid", u_uuid);
                    uaDict.put("u_diu", u_diu);
//                    processClick("", uaDict);
                    processDisplay("", uaDict);
                    outputMeter.mark();
                }

            } catch (Exception e) {
                logger.error("Caught exception in click-bolt", e);
                metrics.meter(metricRegistryUtils.name("ClickWindowBolt", "exceptions", e.getClass().getSimpleName())).mark();
                exceptionMeter.mark();
            } finally {
                context.stop();
            }

        }

        updateToRedis(ad_display_campaign_maps);
        updateToRedis(ad_display_subscribe_maps);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(AdDisplayLog.getFields());
    }


    public void updateToRedis(HashMap<String, Integer> map) {
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            redis.safeAccess(x -> {
                x.set(entry.getKey(), String.valueOf(entry.getValue()));
                logger.info("the calculation result is:" + entry.getKey() + "," + String.valueOf(entry.getValue()));
            });
        }
    }


    public int processDisplay(String message, Map<String, String> dic) {
//		logger.info("process display - dic :"+dic);
        int row = 0;
        try {
            long start = System.currentTimeMillis();
            dic = batchDecodeDic(dic);
                String[] creative_id = StringUtil.getArray(dic.get("u_gid"));
                String[] channel_id = StringUtil.getArray(dic.get("u_channel_id"));
                String[] subscribe_id = StringUtil.getArray(dic.get("u_subscribe_id"));
                String[] campaign_id = StringUtil.getArray(dic.get("u_campaign_id"));
                String[] user_id = StringUtil.getArray(dic.get("u_ad_uid"));
                String[] position_id = StringUtil.getArray(dic.get("u_position_id"));
                String uuid = dic.get("u_uuid");
                String diu = dic.get("u_diu");
                String online_date = dic.get("u_timestamp");
                String device_id = StringUtils.defaultString(uuid, diu);
                for (int i = 0; i < creative_id.length; i++) {
                String _creative_id = StringUtil.getOrDefault(creative_id, i, null);
                String _channel_id = StringUtil.getOrDefault(channel_id, i, null);
                String _subscribe_id = StringUtil.getOrDefault(subscribe_id, i, null);
                String _campaign_id = StringUtil.getOrDefault(campaign_id, i, null);
                String _user_id = StringUtil.getOrDefault(user_id, i, null);
                String _position_id = StringUtil.getOrDefault(position_id, i, null);
                row += processDisplayOne(_creative_id, _channel_id, _subscribe_id, _campaign_id, _user_id, online_date, _position_id, device_id, message, dic);
            }

            logger.info("process display - diu :" + device_id + ",row :" + row + ", timestamp :" + online_date + ", invoke time :" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            row = -1;
            logger.error("process display error , message :" + message + " <==> dic :" + dic, e);
        }
        return row;
    }

    public Map<String, String> batchDecodeDic(Map<String, String> dic) {
        Map<String, String> res = new HashMap<>();
        for (Iterator<String> iterator = dic.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = dic.get(key);
            try {
                if (null != value) {
                    value = URLDecoder.decode(value, "UTF-8");
                }
            } catch (Exception e) {
                logger.error("process display: batchDecodeDic error , key :" + key + ", value :" + value);
            }
            res.put(key, value);
        }
        return res;
    }

    public int processDisplayOne(String creative_id, String channel_id, String subscribe_id, String campaign_id, String user_id, String online_date, String position_id, String device_id, String message, Map<String, String> dic) {
        int row = 0;
        try {
            long start = System.currentTimeMillis();
            String timeStr = TimeHelper.formatTimeStr(online_date, "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd");
            int tabIndex = Convert.toInt(user_id, 1) % 8;

            // 累计曝光数
            String pv_key = String.join("_", "ad", "pv", creative_id /**,timeStr*/);
            String pv_daily_key = String.join("_", "ad", "pv", creative_id, timeStr);

            String expose_key = String.join("_", "ad", "ex", device_id, creative_id);
            Map<String, Long> map = new HashMap<>();
            redis.safeAccess(x -> {
                map.put("pv", x.incrBy(pv_key, 1));
                map.put("pvDaily", x.incrBy(pv_daily_key, 1));

                map.put("exposeCount", x.incrBy(expose_key, 1));
                //			x.zadd(expose_key, Convert.toDouble(System.currentTimeMillis()), creative_id);
            });

            int position = 0;
            Campaign campaign = RedisTablesUtils.getCampaignById(campaign_id);
            if (null != campaign) {
                // 上下线
                Integer budgetType = campaign.getBudget_type();
                int budget = Convert.toInt(campaign.getBudget(), 0);
                if (BudgetTypeEnum.DISPLAY.id().equals(budgetType)) {
                    Map<String,Long> m=new HashMap<>();
                    redis.safeAccess(x->{
                        String display=x.get(String.join("_",user_id,campaign_id,"display"));
                        String predicPv=x.get(String.join("_","ad","predict","pv",String.valueOf(campaign_id)));
                        m.put("jstormDisplays",Long.parseLong(display));
                        m.put("adPredicPv",Long.parseLong(predicPv));
                        logger.info("In DisplayWindowBolt,jstormDisplays`display is: "+display+"adPredicPv`s predicPv is: "+predicPv);
                    });
                    Long jstormDisplays=m.get("jstormDisplays");
                    Long adPredicPv=m.get("adPredicPv");
                    if ((budget > 0 && map.get("pv") >= budget)  || (jstormDisplays>adPredicPv)) {
                        position = 1;
                        try {
                            downSubscribe(campaign_id, subscribe_id, creative_id, position_id, 1);//下线广告
                            logger.info("display - downYN : true , pv > budget , creative_id :" + creative_id);
                        } catch (Exception e) {
                            billing_logger.error(message);
                            logger.error("down subscribe error, campaignId :" + campaign_id + ", creativeId:" + creative_id + ", subscribe_id :" + subscribe_id + ", positionId :" + position_id, e);
                        }
                    } else {

                    }
                }
            }
//            row = recardStat(tabIndex, creative_id, channel_id, subscribe_id, campaign_id, user_id, position, timeStr, 1, 0);
            if (row <= 0) {
                logger.error("process display one - recard stat failed , row :" + row + ", message :" + message + ", dic :" + dic);
            }

            logger.info("process display one - value :" + map + ", timestamp :" + online_date + ", invoke time :" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return row;
    }


    //添加事务注解
    //1.使用 propagation 指定事务的传播行为, 即当前的事务方法被另外一个事务方法调用时
    //如何使用事务, 默认取值为 REQUIRED, 即使用调用方法的事务
    //REQUIRES_NEW: 事务自己的事务, 调用的事务方法的事务被挂起.
    //2.使用 isolation 指定事务的隔离级别, 最常用的取值为 READ_COMMITTED
    //3.默认情况下 Spring 的声明式事务对所有的运行时异常进行回滚. 也可以通过对应的
    //属性进行设置. 通常情况下去默认值即可.
    //4.使用 readOnly 指定事务是否为只读. 表示这个事务只读取数据但不更新数据,
    //这样可以帮助数据库引擎优化事务. 若真的只是一个只读取数据库值的方法, 应设置 readOnly=true
    //5.使用 timeout 指定强制回滚之前事务可以占用的时间.
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            readOnly = false,
            timeout = 3,
            rollbackFor = Exception.class
    )
    protected int downSubscribe(String campaignId, String subscribeId, String creativeId, String positionId, int budgetover) throws Exception {
        int row = 0;
        if (budgetover > 0 && StringUtils.isNotBlank(campaignId)) {
            row = RedisTablesUtils.updateByCustom("budgetover=1", "campaign", "campaign_id=" + campaignId);
            logger.info("预算不足下线(更新计划表) campaignId = " + campaignId + "更新数据库行 row = " + row);
            if (row > 0) {
//				row = subscribeRepos.updateByCustom("state=1", "campaign_id=?", campaignId);
                logger.info("预算不足下线(更新单元表) campaignId = " + campaignId + "更新数据库行 row = " + row);
            }
            if (row > 0) {
//				row = creativeRepos.updateByCustom("state=1", "campaign_id=?", campaignId);
                logger.info("预算不足下线(更新创意表) campaignId = " + campaignId + "更新数据库行 row = " + row);
            }
        } else {
            if (StringUtils.isNotBlank(creativeId)) {
                row = RedisTablesUtils.updateByCustom("state=1", "creative_info_0", "creative_id=" + creativeId);
            }
            if (StringUtils.isNotBlank(subscribeId)) {
                row = RedisTablesUtils.updateByCustom("state=1", "subscribe", "subscribe_id=" + subscribeId);
            }
        }
        String tag = redis.safeAccess(x -> x.get(String.join("_", "ad", "p")), "a");
        redis.safeAccess(x -> x.del(String.join("_", "ad", "p", tag, "" + positionId)));//???
        return row;
    }


}
