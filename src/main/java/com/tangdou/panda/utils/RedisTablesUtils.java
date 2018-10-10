package com.tangdou.panda.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tangdou.panda.entity.*;
import com.tangdou.panda.meta.PriceUnitEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;

public class RedisTablesUtils {

    private static Logger logger= LoggerFactory.getLogger(RedisTablesUtils.class);
    private static String redis_cluster_nodes = "127.0.0.1:6379";
    private static RedisShuffler redis = new RedisShuffler(Arrays.asList(redis_cluster_nodes.split("\\,")));

    public static Campaign getCampaignById(String campaignId) {
        Campaign campaign = new Campaign();
        redis.safeAccess(x -> {
            String key = "ad_campaign_" + campaignId;
            String campaignStr = x.get(key);
            JSONObject object = JSON.parseObject(campaignStr);
            campaign.setCampaign_id(object.getInteger("campaign_id"));
            if (isValidated(object, "campaign_name")) {
                campaign.setCampaign_name(object.getString("campaign_name"));
            }
            if (isValidated(object, "user_id")) {
                campaign.setUser_id(object.getLong("user_id"));
            }
            if (isValidated(object, "start_time")) {
                campaign.setStart_time(object.getDate("start_time"));
            }
            if (isValidated(object, "end_time")) {
                campaign.setEnd_time(object.getDate("end_time"));
            }
            if (isValidated(object, "budget")) {
                campaign.setBudget(object.getInteger("budget"));
            }
            if (isValidated(object, "budget_type")) {
                campaign.setBudget_type(object.getInteger("budget_type"));
            }
            if (isValidated(object, "budgetover")) {
                campaign.setBudgetover(object.getInteger("budgetover"));
            }
            if (isValidated(object, "state")) {
                campaign.setState(object.getInteger("state"));
            }
            if (isValidated(object, "ad_type")) {
                campaign.setAd_type(object.getInteger("ad_type"));
            }
            if (isValidated(object, "add_time")) {
                campaign.setAdd_time(object.getDate("add_time"));
            }
            if (isValidated(object, "add_user_id")) {
                campaign.setAdd_user_id(object.getString("add_user_id"));
            }
            if (isValidated(object, "mod_time")) {
                campaign.setMod_time(object.getDate("mod_time"));
            }
            if (isValidated(object, "mod_user_id")) {
                campaign.setMod_user_id(object.getString("mod_user_id"));
            }
        });
        return campaign;
    }


    public static Subscribe getSubscribeById(String subscribeId) {
        Subscribe subscribe = new Subscribe();
        redis.safeAccess(x -> {
            String subscribeStr = x.get("ad_subscribe_" + subscribeId);
            JSONObject object = JSONObject.parseObject(subscribeStr);
            subscribe.setSubscribe_id(object.getInteger("subscribe_id"));
            if (isValidated(object, "channel_id")) {
                subscribe.setChannel_id(object.getInteger("channel_id"));
            }
            if (isValidated(object, "campaign_id")) {
                subscribe.setCampaign_id(object.getInteger("campaign_id"));
            }
            if (isValidated(object, "user_id")) {
                subscribe.setUser_id(object.getLong("user_id"));
            }
            if (isValidated(object, "start_time")) {
                subscribe.setStart_time(object.getDate("start_time"));
            }
            if (isValidated(object, "end_time")) {
                subscribe.setEnd_time(object.getDate("end_time"));
            }
            if (isValidated(object, "bid")) {
                subscribe.setBid(object.getInteger("bid"));
            }
            if (isValidated(object, "state")) {
                subscribe.setState(object.getInteger("state"));
            }
            if (isValidated(object, "ad_type")) {
                subscribe.setAd_type(object.getInteger("ad_type"));
            }
            if (isValidated(object, "add_time")) {
                subscribe.setAdd_time(object.getDate("add_time"));
            }
            if (isValidated(object, "add_user_id")) {
                subscribe.setAdd_user_id(object.getString("add_user_id"));
            }
            if (isValidated(object, "mod_time")) {
                subscribe.setMod_time(object.getDate("mod_time"));
            }
            if (isValidated(object, "mod_user_id")) {
                subscribe.setMod_user_id(object.getString("mod_user_id"));
            }
        });
        return subscribe;
    }


    public static CreativeInfo getCreativeInfoById(String creativeInfoId) {
        CreativeInfo creativeInfo = new CreativeInfo();
        redis.safeAccess(x -> {
            String creativeInfoStr = x.get("ad_creativeinfo_" + creativeInfoId);
            JSONObject object = JSONObject.parseObject(creativeInfoStr);
            creativeInfo.setCreative_id(object.getLong("creative_id"));
            if (isValidated(object, "subscribe_id")) {
                creativeInfo.setSubscribe_id(object.getInteger("subscribe_id"));
            }
            if (isValidated(object, "channel_id")) {
                creativeInfo.setChannel_id(object.getInteger("channel_id"));
            }
            if (isValidated(object, "campaign_id")) {
                creativeInfo.setCampaign_id(object.getInteger("campaign_id"));
            }
            if (isValidated(object, "user_id")) {
                creativeInfo.setUser_id(object.getLong("user_id"));
            }
            if (isValidated(object, "title")) {
                creativeInfo.setTitle(object.getString("title"));
            }
            if (isValidated(object, "describe")) {
                creativeInfo.setDescribe(object.getString("describe"));
            }
            if (isValidated(object, "target_url")) {
                creativeInfo.setTarget_url(object.getString("target_url"));
            }
            if (isValidated(object, "open_url")) {
                creativeInfo.setOpen_url(object.getString("open_url"));
            }
            if (isValidated(object, "pic_url")) {
                creativeInfo.setPic_url(object.getString("pic_url"));
            }
            if (isValidated(object, "show_time")) {
                creativeInfo.setShow_time(object.getInteger("show_time"));
            }
            if (isValidated(object, "video_url")) {
                creativeInfo.setVideo_url(object.getString("video_url"));
            }
            if (isValidated(object, "video_duration")) {
                creativeInfo.setVideo_duration(object.getInteger("video_duration"));
            }
            if (isValidated(object, "appinfo")) {
                creativeInfo.setAppinfo(object.getString("appinfo"));
            }
            if (isValidated(object, "appid")) {
                creativeInfo.setAppid(object.getString("appid"));
            }
            if (isValidated(object, "vid")) {
                creativeInfo.setVid(object.getString("vid"));
            }
            if (isValidated(object, "action")) {
                creativeInfo.setAction(object.getInteger("action"));
            }
            if (isValidated(object, "state")) {
                creativeInfo.setState(object.getInteger("state"));
            }
            if (isValidated(object, "repeat")) {
                creativeInfo.setRepeat(object.getInteger("repeat"));
            }
            if (isValidated(object, "ad_type")) {
                creativeInfo.setAd_type(object.getInteger("ad_type"));
            }
            if (isValidated(object, "creative_type")) {
                creativeInfo.setCreative_type(object.getInteger("creative_type"));
            }
            if (isValidated(object, "position_type")) {
                creativeInfo.setPosition_type(object.getInteger("position_type"));
            }
            if (isValidated(object, "pic_type")) {
                creativeInfo.setPic_type(object.getInteger("pic_type"));
            }
            if (isValidated(object, "local_industry")) {
                creativeInfo.setLocal_industry(object.getInteger("local_industry"));
            }
            if (isValidated(object, "refuse_reason")) {
                creativeInfo.setRefuse_reason(object.getString("refuse_reason"));
            }
            if (isValidated(object, "mod_info")) {
                creativeInfo.setMod_info(object.getString("mod_info"));
            }
            if (isValidated(object, "add_time")) {
                creativeInfo.setAdd_time(object.getDate("add_time"));
            }
            if (isValidated(object, "add_user_id")) {
                creativeInfo.setAdd_user_id(object.getString("add_user_id"));
            }
            if (isValidated(object, "mod_time")) {
                creativeInfo.setMod_time(object.getDate("mod_time"));
            }
            if (isValidated(object, "mod_user_id")) {
                creativeInfo.setMod_user_id(object.getString("mod_user_id"));
            }

        });
        return creativeInfo;
    }


    public static AdUser getUserById(String userId) {
        if (StringUtil.isEmpty(userId)) {
            System.out.println("userId is null or its length is 0.");
            return null;
        }
        AdUser adUser = new AdUser();
        redis.safeAccess(x -> {
            String adUserStr = x.get("ad_user_" + userId);
            JSONObject object = JSONObject.parseObject(adUserStr);
            adUser.setUser_id(object.getString("user_id"));
            if (isValidated(object, "username")) {
                adUser.setUsername(object.getString("username"));
            }
            if (isValidated(object, "password")) {
                adUser.setPassword(object.getString("password"));
            }
            if (isValidated(object, "encrypt")) {
                adUser.setEncrypt(object.getString("encrypt"));
            }
            if (isValidated(object, "mobile")) {
                adUser.setMobile(object.getString("mobile"));
            }
            if (isValidated(object, "email")) {
                adUser.setEmail(object.getString("email"));
            }
            if (isValidated(object, "realname")) {
                adUser.setRealname(object.getString("realname"));
            }
            if (isValidated(object, "amount")) {
                adUser.setAmount(object.getInteger("amount"));
            }
            if (isValidated(object, "card")) {
                adUser.setCard(object.getString("card"));
            }
            if (isValidated(object, "iscompany")) {
                adUser.setIscompany(object.getInteger("iscompany"));
            }
            if (isValidated(object, "lastloginip")) {
                adUser.setLastloginip(object.getString("lastloginip"));
            }
            if (isValidated(object, "lastlogintime")) {
                adUser.setLastlogintime(object.getInteger("lastlogintime"));
            }
            if (isValidated(object, "add_time")) {
                adUser.setAdd_time(object.getDate("add_time"));
            }
        });
        return adUser;
    }

    public static Price getPriceByPositionAndChannelAndUnit(String position_id, String channel_id, PriceUnitEnum unit) {

        if (StringUtil.isEmpty(position_id) || StringUtil.isEmpty(channel_id)) {
            return null;
        }
        Price price = new Price();
        redis.safeAccess(x -> {
            Set<String> priceStrSet = x.keys("ad_price_*");
            for (String priceStr : priceStrSet) {
                String result=x.get(priceStr);
                JSONObject object = JSONObject.parseObject(result);
                Integer posi = null;
                Integer chann = null;
                Integer t_unit = null;
                if (isValidated(object, "position_id")) {
                    posi = object.getInteger("position_id");
                }
                if (isValidated(object, "channel_id")) {
                    chann = object.getInteger("channel_id");
                }
                if (isValidated(object, "time_unit")) {
                    t_unit = object.getInteger("time_unit");
                }
                if (!StringUtil.isEmpty(String.valueOf(posi))) {
                    if ((posi == Integer.parseInt(position_id)) && (chann == Integer.parseInt(channel_id)) && (t_unit == unit.getUnit())) {
                        price.setPrice_id(object.getInteger("price_id"));
                        price.setChannel_id(object.getInteger("channel_id"));
                        price.setPosition_id(object.getInteger("position_id"));
                        if (isValidated(object, "type")) {
                            price.setType(object.getInteger("type"));
                        }
                        if (isValidated(object, "base_price")) {
                            price.setBase_price(object.getInteger("base_price"));
                        }
                        if (isValidated(object, "suggest_price")) {
                            price.setSuggest_price(object.getInteger("suggest_price"));
                        }
                        price.setTime_unit(object.getInteger("time_unit"));
                        if (isValidated(object, "add_time")) {
                            price.setAdd_time(object.getDate("add_time"));
                        }
                        if (isValidated(object, "add_user_id")) {
                            price.setAdd_user_id(object.getString("add_user_id"));
                        }
                        if (isValidated(object, "mod_time")) {
                            price.setMod_time(object.getDate("mod_time"));
                        }
                        if (isValidated(object, "mod_user_id")) {
                            price.setMod_user_id(object.getString("mod_user_id"));
                        }
                        break;
                    }
                }


            }
        });
        return price;
    }

    public static Boolean isValidated(JSONObject object, String field) {
        if (!"null".equalsIgnoreCase(String.valueOf(object.get(field)))) {
            return true;
        }
        return false;
    }


    public static int updateByCustom(String updateStatement, String tableName, String condition) {
        int num = 0;
        if (condition == null || condition.trim().equals("")) {
            condition = "1=2";
        }
        String sqlStr = "UPDATE " + tableName + " SET " + updateStatement + " WHERE " + condition;
        redis.safeAccess(x -> x.publish("synUpdateDimensionTable", sqlStr));
        logger.info("channel:synUpdateDimensionTable,published a message:"+sqlStr);
        num = 1;
        return num;
    }

}
