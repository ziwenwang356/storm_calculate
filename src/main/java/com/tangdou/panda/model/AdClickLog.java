package com.tangdou.panda.model;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import shade.storm.com.google.common.base.Strings;

import java.util.Map;

public class AdClickLog {

    public Long u_ad_uid;
    public Integer u_campaign_id;
    public Integer u_subscribe_id;
    public String u_gid;
    public String u_position_id;
    public Integer u_ad_rank;
    public Integer u_price;
    public String u_ad_code;
    public Integer u_frank;
    public String u_adtype;
    public Integer u_ad_vid;
    public Integer u_channel_id;
    public String u_mod;//服务端接口模块
    public String u_ac;//服务端具体接口


    public static AdClickLog createFromMap(Map<String, String> uaDict) {
        AdClickLog lmsg = new AdClickLog();
        lmsg.u_ad_uid = Long.valueOf(uaDict.get("u_ad_uid"));
        lmsg.u_campaign_id = Integer.valueOf(uaDict.get("u_campaign_id"));
        lmsg.u_subscribe_id = Integer.valueOf(uaDict.get("u_subscribe_id"));
        lmsg.u_gid = uaDict.get("u_gid");
        lmsg.u_position_id = uaDict.get("u_position_id");
        lmsg.u_ad_rank = Integer.valueOf(uaDict.get("u_ad_rank"));
        lmsg.u_price = Integer.valueOf(uaDict.get("u_price"));
        lmsg.u_ad_code = uaDict.get("u_ad_code");
        lmsg.u_frank = Integer.valueOf(uaDict.get("u_frank"));
        lmsg.u_adtype = uaDict.get("u_adtype");
        if (!Strings.isNullOrEmpty(uaDict.get("u_ad_vid"))) {
            lmsg.u_ad_vid = Integer.valueOf(uaDict.get("u_ad_vid"));
        }
        lmsg.u_channel_id = Integer.valueOf(uaDict.get("u_channel_id"));
        lmsg.u_mod = uaDict.get("u_mod");
        lmsg.u_ac = uaDict.get("u_ac");
        return lmsg;
    }


    public Values toValues() {
        return new Values(u_ad_uid, u_campaign_id, u_subscribe_id, u_gid, u_position_id, u_ad_rank, u_price, u_ad_code, u_frank, u_adtype, u_ad_vid, u_channel_id, u_mod, u_ac);
    }


    public static Fields getFields() {
        return new Fields("u_ad_uid", "u_campaign_id", "u_subscribe_id", "u_gid", "u_position_id", "u_ad_rank", "u_price",
                "u_ad_code", "u_frank", "u_adtype", "u_ad_vid", "u_channel_id", "u_mod", "u_ac");
    }


    public static AdClickLog from(Tuple tuple) {
        AdClickLog lmsg = new AdClickLog();
        lmsg.u_ad_uid = tuple.getLong(0);
        lmsg.u_campaign_id = tuple.getInteger(1);
        lmsg.u_subscribe_id = tuple.getInteger(2);
        lmsg.u_gid = tuple.getString(3);
        lmsg.u_position_id = tuple.getString(4);
        lmsg.u_ad_rank = tuple.getInteger(5);
        lmsg.u_price = tuple.getInteger(6);
        lmsg.u_ad_code = tuple.getString(7);
        lmsg.u_frank = tuple.getInteger(8);
        lmsg.u_adtype = tuple.getString(9);
        lmsg.u_ad_vid = tuple.getInteger(10);
        lmsg.u_channel_id = tuple.getInteger(11);
        lmsg.u_mod = tuple.getString(12);
        lmsg.u_ac = tuple.getString(13);
        return lmsg;
    }

    @Override
    public String toString() {
        return "AdClickLog{" +
                "u_ad_uid=" + u_ad_uid +
                ", u_campaign_id=" + u_campaign_id +
                ", u_subscribe_id=" + u_subscribe_id +
                ", u_gid='" + u_gid + '\'' +
                ", u_position_id='" + u_position_id + '\'' +
                ", u_ad_rank=" + u_ad_rank +
                ", u_price=" + u_price +
                ", u_ad_code='" + u_ad_code + '\'' +
                ", u_frank=" + u_frank +
                ", u_adtype='" + u_adtype + '\'' +
                ", u_ad_vid=" + u_ad_vid +
                ", u_channel_id=" + u_channel_id +
                ", u_mod='" + u_mod + '\'' +
                ", u_ac='" + u_ac + '\'' +
                '}';
    }
}
