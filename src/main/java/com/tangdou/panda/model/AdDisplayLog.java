package com.tangdou.panda.model;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import shade.storm.com.google.common.base.Strings;

import java.util.Map;

public class AdDisplayLog {

    public String u_guuid;
    public String u_position_id;
    public Long u_ad_uid;
    public Integer u_campaign_id;
    public Integer u_subscribe_id;
    public String u_gid;
    public Integer u_ad_rank;
    public Integer u_price;
    public Long u_frequency;
    public String u_crowd;
    public Integer u_frank;
    public String u_adtype;
    public Integer u_ad_vid;
    public Integer u_channel_id;

    public String u_mod;//服务端接口模块
    public String u_ac;//服务端具体接口


    public static AdDisplayLog createFromMap(Map<String, String> uaDict) {
        AdDisplayLog lmsg = new AdDisplayLog();
        lmsg.u_guuid = uaDict.get("u_guuid");
        lmsg.u_position_id = uaDict.get("u_position_id");
        lmsg.u_ad_uid = Long.valueOf(uaDict.get("u_ad_uid"));
        lmsg.u_campaign_id = Integer.valueOf(uaDict.get("u_campaign_id"));
        lmsg.u_subscribe_id = Integer.valueOf(uaDict.get("u_subscribe_id"));
        lmsg.u_gid = uaDict.get("u_gid");
        lmsg.u_ad_rank = Integer.valueOf(uaDict.get("u_ad_rank"));
        lmsg.u_price = Integer.valueOf(uaDict.get("u_price"));
        if (!Strings.isNullOrEmpty(uaDict.get("u_frequency"))){
            lmsg.u_frequency = Long.valueOf(uaDict.get("u_frequency"));
        }
        lmsg.u_crowd = uaDict.get("u_crowd");
        lmsg.u_frank = Integer.valueOf(uaDict.get("u_frank"));
        lmsg.u_adtype = uaDict.get("u_adtype");
        if (!Strings.isNullOrEmpty(uaDict.get("u_ad_vid"))) {
            lmsg.u_ad_vid = Integer.valueOf(uaDict.get("u_ad_vid"));
        }
        lmsg.u_channel_id = Integer.valueOf(uaDict.get("u_channel_id"));
        lmsg.u_mod=uaDict.get("u_mod");
        lmsg.u_ac=uaDict.get("u_ac");
        return lmsg;
    }

    public Values toValues() {
        return new Values(u_guuid, u_position_id, u_ad_uid, u_campaign_id, u_subscribe_id, u_gid, u_ad_rank, u_price, u_frequency, u_crowd, u_frank, u_adtype, u_ad_vid, u_channel_id,
                u_mod,u_ac);
    }


    public static Fields getFields() {
        return new Fields("u_guuid", "u_position_id", "u_ad_uid", "u_campaign_id", "u_subscribe_id", "u_gid", "u_ad_rank", "u_price",
                "u_frequency", "u_crowd", "u_frank", "u_adtype", "u_ad_vid", "u_channel_id","u_mod","u_ac");
    }


    public static AdDisplayLog from(Tuple tuple) {
        AdDisplayLog lmsg = new AdDisplayLog();
        lmsg.u_guuid = tuple.getString(0);
        lmsg.u_position_id = tuple.getString(1);
        lmsg.u_ad_uid = tuple.getLong(2);
        lmsg.u_campaign_id = tuple.getInteger(3);
        lmsg.u_subscribe_id = tuple.getInteger(4);
        lmsg.u_gid = tuple.getString(5);
        lmsg.u_ad_rank = tuple.getInteger(6);
        lmsg.u_price = tuple.getInteger(7);
        lmsg.u_frequency = tuple.getLong(8);
        lmsg.u_crowd = tuple.getString(9);
        lmsg.u_frank = tuple.getInteger(10);
        lmsg.u_adtype = tuple.getString(11);
        lmsg.u_ad_vid = tuple.getInteger(12);
        lmsg.u_channel_id = tuple.getInteger(13);
        lmsg.u_mod=tuple.getString(14);
        lmsg.u_ac=tuple.getString(15);
        return lmsg;
    }

    @Override
    public String toString() {
        return "AdDisplayLog{" +
                "u_guuid='" + u_guuid + '\'' +
                ", u_position_id='" + u_position_id + '\'' +
                ", u_ad_uid=" + u_ad_uid +
                ", u_campaign_id=" + u_campaign_id +
                ", u_subscribe_id=" + u_subscribe_id +
                ", u_gid='" + u_gid + '\'' +
                ", u_ad_rank=" + u_ad_rank +
                ", u_price=" + u_price +
                ", u_frequency=" + u_frequency +
                ", u_crowd='" + u_crowd + '\'' +
                ", u_frank=" + u_frank +
                ", u_adtype='" + u_adtype + '\'' +
                ", u_ad_vid=" + u_ad_vid +
                ", u_channel_id=" + u_channel_id +
                ", u_mod='" + u_mod + '\'' +
                ", u_ac='" + u_ac + '\'' +
                '}';
    }
}
