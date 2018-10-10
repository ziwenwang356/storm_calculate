package com.tangdou.panda.model;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import shade.storm.com.google.common.base.Strings;

import java.util.Map;

public class AdClickAndDisplayLog {
    public Long u_ad_uid;//广告主id，click,display
    public Integer u_campaign_id;//ADx推广计划ID,click,display
    public Integer u_subscribe_id;//ADx推广单元ID,click,display
    public String u_gid;//ADx创意ID,click,display
    public String u_position_id;//广告展现的广告位ID,click,display
    public Integer u_ad_rank;//ADx展现时的排序位置,click,display
    public Integer u_price;//实际扣费金额,click,display
    public String u_ad_code;//点击状态码,click
    public Integer u_frank;//前端展现排序,click,display
    public String u_adtype;//广告类型,click,display
    public Integer u_ad_vid;//导流位视频id,click(非必传),display(非必传)
    public Integer u_channel_id;//广告投放频道id,click,display
    public String u_guuid;//广告展现ID,display
    public Long u_frequency;//投放频次,display(非必传)
    public String u_crowd;//人群,display(非必传)

    // ?是否需要加上如下的字段，为了区分点击和曝光
    public String u_mod;//服务端接口模块
    public String u_ac;//服务端具体接口

    //如下是后期加的字段，因为在processClick()中用到了



    public String u_timestamp;
    public String u_status;
    public String u_action;
    public String u_uuid;
    public String u_diu;


    public static AdClickAndDisplayLog createFromMap(Map<String, String> uaDict) {
        AdClickAndDisplayLog lmsg = new AdClickAndDisplayLog();
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
        lmsg.u_guuid = uaDict.get("u_guuid");
        if (!Strings.isNullOrEmpty(uaDict.get("u_frequency"))) {
            lmsg.u_frequency = Long.valueOf(uaDict.get("u_frequency"));
        }
        if (!Strings.isNullOrEmpty(uaDict.get("u_crowd"))) {
            lmsg.u_crowd = uaDict.get("u_ad_vid");
        }
        lmsg.u_mod=uaDict.get("u_mod");
        lmsg.u_ac=uaDict.get("u_ac");
        lmsg.u_timestamp=uaDict.get("u_timestamp");
        lmsg.u_status=uaDict.get("u_status");
        lmsg.u_action=uaDict.get("u_action");
        lmsg.u_uuid=uaDict.get("u_uuid");
        lmsg.u_diu=uaDict.get("u_diu");
        return lmsg;
    }


    public Values toValues() {
        return new Values(u_ad_uid, u_campaign_id, u_subscribe_id, u_gid, u_position_id, u_ad_rank, u_price, u_ad_code, u_frank, u_adtype, u_ad_vid, u_channel_id,u_guuid,u_frequency,u_crowd,u_mod,u_ac,
                u_timestamp,u_status,u_action,u_uuid,u_diu);
    }


    public static Fields getFields() {
        return new Fields("ad_uid", "u_campaign_id", "u_subscribe_id", "u_gid", "u_position_id", "u_ad_rank", "u_price",
                "u_ad_code", "u_frank", "u_adtype", "u_ad_vid", "u_channel_id","u_guuid","u_frequency","u_crowd","u_mod","u_ac","u_timestamp","u_status","u_action","u_uuid","u_diu");
    }


    public static AdClickAndDisplayLog from(Tuple tuple) {
        AdClickAndDisplayLog lmsg = new AdClickAndDisplayLog();
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
        lmsg.u_guuid=tuple.getString(12);
        lmsg.u_frequency=tuple.getLong(13);
        lmsg.u_crowd=tuple.getString(14);
        lmsg.u_mod=tuple.getString(15);
        lmsg.u_ac=tuple.getString(16);
        lmsg.u_timestamp=tuple.getString(17);
        lmsg.u_status=tuple.getString(18);
        lmsg.u_action=tuple.getString(19);
        lmsg.u_uuid=tuple.getString(20);
        lmsg.u_diu=tuple.getString(21);
        return lmsg;
    }


    @Override
    public String toString() {
        return "AdClickAndDisplayLog{" +
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
                ", u_guuid='" + u_guuid + '\'' +
                ", u_frequency=" + u_frequency +
                ", u_crowd='" + u_crowd + '\'' +
                ", u_mod='" + u_mod + '\'' +
                ", u_ac='" + u_ac + '\'' +
                ", u_timestamp='" + u_timestamp + '\'' +
                ", u_status='" + u_status + '\'' +
                ", u_action='" + u_action + '\'' +
                ", u_uuid='" + u_uuid + '\'' +
                ", u_diu='" + u_diu + '\'' +
                '}';
    }
}
