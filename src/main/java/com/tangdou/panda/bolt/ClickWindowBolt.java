package com.tangdou.panda.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseWindowedBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.windowing.TupleWindow;
import com.alibaba.fastjson.JSON;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tangdou.panda.entity.*;
import com.tangdou.panda.meta.BudgetTypeEnum;
import com.tangdou.panda.meta.PriceUnitEnum;
import com.tangdou.panda.model.AdClickAndDisplayLog;
import com.tangdou.panda.model.AdClickLog;
import com.tangdou.panda.model.ModelAndAction;
import com.tangdou.panda.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shade.storm.org.apache.commons.lang.StringUtils;

import java.util.*;

public class ClickWindowBolt extends BaseWindowedBolt {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger billing_logger = LoggerFactory.getLogger("billing");
    //    ModelAndAction.of("log", "ad_display");
    private static final ModelAndAction CLICK_MODEL_ACTION = ModelAndAction.of("log", "ad_click");
    private MetricRegistry metrics;
    private Timer executeTimer;
    private Meter outputMeter;
    private Meter exceptionMeter;


    MetricRegistryUtils metricRegistryUtils = new MetricRegistryUtils();
    //    @Value("#{properties['redis.cluster.nodes']}")
    String redis_cluster_nodes = "10.19.42.12:6379";
    protected static RedisShuffler redis = null;
    MessagesUtils messagesUtils = new MessagesUtils();

    // 将如下的内容放到redis中，整点失效？不需要做，因为window已经帮助我们实现了.(key/value,key我们目前放置的是ad_uid+"_"+campaign_id_click/ad_uid+"_"+campaign_id+"_"+subscribe_id_click/ad_uid+"_"+campaign_id+"_price",但是我觉得这个再重新设计，因为需要保证它的唯一)
    HashMap<String, Integer> ad_click_campaign_maps = new HashMap<String, Integer>();
    HashMap<String, Integer> ad_click_subscribe_maps = new HashMap<String, Integer>();
    HashMap<String, Integer> ad_click_price_maps = new HashMap<String, Integer>();
    private Integer initPrice = 0;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        logger.info("billing - nodes :" + redis_cluster_nodes);
        redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));
        metrics = MetricRegistryUtils.getMetricRegistry();
        executeTimer = metrics.timer(metricRegistryUtils.name("click-window-bolt", "execute"));
        outputMeter = metrics.meter(metricRegistryUtils.name("click-window-bolt", "output"));
        exceptionMeter = metrics.meter(metricRegistryUtils.name("click-window-bolt", "exceptions-all"));
    }

    @Override
    public void execute(TupleWindow tupleWindow) {

        for (Tuple input : tupleWindow.get()) {

            Timer.Context context = executeTimer.time();
            try {
                AdClickAndDisplayLog adClickAndDisplayLog = AdClickAndDisplayLog.from(input);
                ModelAndAction modelAndAction = ModelAndAction.of(adClickAndDisplayLog);
                if (modelAndAction.equals(CLICK_MODEL_ACTION)) {
                    String ad_uid = input.getStringByField("u_ad_uid");
                    String campaign_id = input.getStringByField("u_campaign_id");
                    String subscribe_id = input.getStringByField("u_subscribe_id");
                    Integer price = input.getIntegerByField("u_price");

                    Integer count_campaign = ad_click_campaign_maps.get(ad_uid + "_" + campaign_id + "_click");
                    if (null == count_campaign) {
                        count_campaign = 0;
                    }
                    ++count_campaign;
                    ad_click_campaign_maps.put(ad_uid + "_" + campaign_id + "_click", count_campaign);


                    Integer count_subscribe = ad_click_subscribe_maps.get(ad_uid + "_" + campaign_id + "_" + subscribe_id + "_click");
                    if (null == count_subscribe) {
                        count_subscribe = 0;
                    }
                    ++count_subscribe;
                    ad_click_subscribe_maps.put(ad_uid + "_" + campaign_id + "_" + subscribe_id + "_click", count_subscribe);


                    initPrice += price;
                    ad_click_price_maps.put(ad_uid + "_" + campaign_id + "_price", initPrice);


                    logger.info("ClickWindowBolt is: " + adClickAndDisplayLog.toString());
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
                    processClick("", uaDict);
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


        // 把 ad_click_campaign_maps，ad_click_subscribe_maps,ad_click_price_maps 中的key/value放置到redis中，整点失效？我们不需要做，因为window已经帮我们做了

        updateToRedis(ad_click_campaign_maps);
        updateToRedis(ad_click_subscribe_maps);
        updateToRedis(ad_click_price_maps);
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(AdClickLog.getFields());
    }


    public void updateToRedis(HashMap<String, Integer> map) {
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            redis.safeAccess(x -> {
                x.set(entry.getKey(), String.valueOf(entry.getValue()));
                logger.info("the calculation result is:" + entry.getKey() + "," + String.valueOf(entry.getValue()) + " in ClickWindowBolt.");
            });
        }
    }

    public int processClick(String message, Map<String, String> dic) {
        int row = 0;
//		logger.info("process click - dic :"+dic);
        long start = System.currentTimeMillis();
        try {
            String creative_id = dic.get("u_gid");
            String channel_id = dic.get("u_channel_id");
            String position_id = dic.get("u_position_id");
            String subscribe_id = dic.get("u_subscribe_id");
            String campaign_id = dic.get("u_campaign_id");
            String user_id = dic.get("u_ad_uid");
            String auction_time = dic.get("u_timestamp");
            String price = dic.get("u_price");
            String result = dic.get("u_status");
            String action = dic.get("u_action");
            String uuid = dic.get("u_uuid");
            String diu = dic.get("u_diu");
            String device_id = StringUtils.defaultString(uuid, diu);

            int basePrice = 60;
            Price obPrice = RedisTablesUtils.getPriceByPositionAndChannelAndUnit(position_id, channel_id, PriceUnitEnum.点击);
            logger.info("getPrice - diu :" + diu + ", obPrice :" + JSON.toJSONString(obPrice));
            if (obPrice != null) {
                basePrice = Convert.toInt(obPrice.getBase_price(), 60);
            }

            int state = -1, position = 0;
            if ("200".equals(result)) state = 0;
            int _price = Convert.toInt(price, basePrice);
            if (_price < basePrice) {
                String msg = "price less than base_price :" + message;
                logger.error(msg);
                billing_logger.error(msg);
            }
            final int bid = _price < basePrice ? basePrice : _price;

            int tabIndex = Convert.toInt(user_id, 1) % 8;
            String timeStr = TimeHelper.formatTimeStr(auction_time, "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd");

//			// 累计点击和消耗
            String click_key = String.join("_", "ad", "click", creative_id);
            String click_daily_key = String.join("_", "ad", "click", creative_id, timeStr);

            String cam_click_key = String.join("_", "ad", "cam", "click", campaign_id);
            String cam_click_daily_key = String.join("_", "cam", "ad", "click", creative_id, timeStr);

            String consume_key = String.join("_", "ad", "consume", creative_id);
            String consume_daily_key = String.join("_", "ad", "consume", creative_id, timeStr);

            String cam_consume_key = String.join("_", "ad", "cam", "consume", campaign_id);
            String cam_consume_daily_key = String.join("_", "ad", "cam", "consume", campaign_id, timeStr);

            String user_consume_key = String.join("_", "ad", "user", "consume", user_id);
            String user_consume_daily_key = String.join("_", "ad", "user", "consume", user_id, timeStr);

            String watchset_key = String.join("_", "ad", "watch", device_id);
            Map<String, Long> map = new HashMap<>();
            redis.safeAccess(x -> {
                map.put("click", x.incrBy(click_key, 1));
                map.put("clickDaily", x.incrBy(click_daily_key, 1));

                map.put("camClick", x.incrBy(cam_click_key, 1));
                map.put("camClickDaily", x.incrBy(cam_click_daily_key, 1));

                map.put("consume", x.incrBy(consume_key, bid));
                map.put("consumeDaily", x.incrBy(consume_daily_key, bid));

                map.put("camConsume", x.incrBy(cam_consume_key, bid));
                map.put("camConsumeDaily", x.incrBy(cam_consume_daily_key, bid));

                map.put("userConsume", x.incrBy(user_consume_key, bid));
                map.put("userConsumeDaily", x.incrBy(user_consume_daily_key, bid));
                x.zadd(watchset_key, Convert.toDouble(System.currentTimeMillis()), creative_id);
            });

            logger.info("process - user_id :" + user_id + ", map :" + JSON.toJSONString(map));
            boolean downYN = false;
//			logger.info("subscribe_id = " + subscribe_id);
            Subscribe subscribe = RedisTablesUtils.getSubscribeById(subscribe_id);
            if (null == subscribe) {
                logger.error("subscribe is null , subscribe_id :" + subscribe_id);
                return row;
            }
            Campaign campaign = RedisTablesUtils.getCampaignById(campaign_id);
            if (null == campaign) {
                logger.error("campaign is null , campaign_id :" + campaign_id);
                return row;
            }
            CreativeInfo creativeInfo = RedisTablesUtils.getCreativeInfoById(creative_id);
            if (null == creativeInfo) {
                logger.error("creativeInfo is null , creative_id :" + creative_id);
                return row;
            }
            int budgetover = 0;
            if (subscribe.getAd_type().intValue() == 2) {//广告类型为品牌，则只考虑广告单元开始结束时间
                if (null != subscribe.getStart_time() && !TimeHelper.isEffectiveDate(new Date(), subscribe.getStart_time(), subscribe.getEnd_time())) {
                    downYN = true;
                    logger.info("click - ad_type :2, downYN : true , time is not effective , subscribe :" + JSON.toJSONString(subscribe));
                }
                if (subscribe.getState().intValue() != 0) {//3、是否满足广告单元状态
                    downYN = true;
                    logger.info("click - ad_type :2, downYN : true , subscribe state not equals 0 , subscribe :" + JSON.toJSONString(subscribe));
                } else if (creativeInfo.getState().intValue() != 0) {//4、是否满足广告创意状态
                    downYN = true;
                    logger.info("click - ad_type :2, downYN : true , creative state not equals 0 , creative :" + JSON.toJSONString(creativeInfo));
                }
            } else {//广告类型为竞价
                Integer budgetType = campaign.getBudget_type();
                //1、是否满足计划时间范围
                if (null != campaign.getStart_time() && !TimeHelper.isEffectiveDate(new Date(), campaign.getStart_time(), campaign.getEnd_time())) {
                    downYN = true;
                    logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , time is not effective, campagin :" + JSON.toJSONString(campaign));
                } else if (campaign.getState().intValue() != 0) {//2、是否满足计划状态
                    downYN = true;
                    logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , campaign state not equals 0, campagin :" + JSON.toJSONString(campaign));
                } else if (subscribe.getState().intValue() != 0) {//3、是否满足广告单元状态
                    downYN = true;
                    logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , subscribe state not equals 0, subscribe :" + JSON.toJSONString(subscribe));
                } else if (creativeInfo.getState().intValue() != 0) {//4、是否满足广告创意状态
                    downYN = true;
                    logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , creative state not equals 0, creative :" + JSON.toJSONString(creativeInfo));
                } else if (BudgetTypeEnum.CONSUME.id().equals(budgetType)) {//5、消耗是否超出
                    if (getUserAmount(user_id, map.get("userConsume")) <= 2000) {
                        budgetover = 1;
                        downYN = true;
                        logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , userAmount less than zero ");
                    } else if (map.get("camConsumeDaily") >= campaign.getBudget()) {
                        budgetover = 1;
                        downYN = true;
                        logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , consume > budget ");
                    }
                } else if (BudgetTypeEnum.CLICK.id().equals(budgetType)) {//6、点击量是否超出
                    int budget = Convert.toInt(campaign.getBudget(), 0);
                    if (budget > 0 && map.get("click") >= budget) {
                        budgetover = 1;
                        downYN = true;
                        logger.info("click - ad_type :" + subscribe.getAd_type() + ", downYN : true , click > budget ");
                    }
                }
            }
            if (downYN) {
                try {
                    position = 1;
                    downSubscribe(campaign_id, subscribe_id, creative_id, position_id, budgetover);//下线广告
                } catch (Exception e) {
                    billing_logger.error(message);
                    logger.error("down subscribe error, campaignId :" + campaign_id + ", creativeId:" + creative_id + ", subscribe_id :" + subscribe_id + ", positionId :" + position_id, e);
                }
            }

//            row = recardDayAuction(tabIndex, subscribe_id, channel_id, campaign_id, user_id, timeStr, bid, state, position);
            if (row <= 0) {
                logger.error("process click - recard day auction failed, row :" + ", message :" + message + ", dic :" + dic);
            }
//            row = recardStat(tabIndex, creative_id, channel_id, subscribe_id, campaign_id, user_id, position, timeStr, 0, 1);
            if (row <= 0) {
                logger.error("process click - recard stat failed ,row :" + row + ", message :" + message + ", dic :" + dic);
            }
            logger.info("process click - tabIndex :" + tabIndex + ", map :" + map + ", key :" + click_key + ", timestamp :" + auction_time + ", result :" + result + ", row :" + row + ", invoke time :" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            row = -1;
            logger.error("process click error , message :" + message + " <==> dic :" + dic, e);
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


    public double getUserAmount(String userId, Long userConsume) {

        AdUser user = RedisTablesUtils.getUserById(userId);
        if (null == user) {
            return 0.0;
        }

        return user.getAmount() > userConsume ? user.getAmount() - userConsume : 0;
    }

}
