package com.tangdou.panda.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangtao on 2017/7/10.
 */
public class Const {

    public static final List<String> UA_LIST = ImmutableList.of(
            "u_timestamp",
            "u_backtime",
            "u_responsetime",
            "u_host",
            "u_http_host",
            "u_clientip",
            "u_referer",
            "u_xff",
            "u_status",
            "u_url",
            "u_verb",
            "u_size",
            "u_div",
            "u_dic",
            "u_diu",
            "u_diu2",
            "u_diu3",
            "u_uid",
            "u_startid",
            "u_stepid",
            "u_time",
            "u_mod",
            "u_ac",
            "u_client",
            "u_ver",
            "u_uuid",
            "u_hash",
            "u_xinge",
            "u_token",
            "u_agent",
            "u_method",
            "u_new_activity",
            "u_old_activity",
            "u_key",
            "u_client_module",
            "u_source",
            "u_page",
            "u_position",
            "u_vid",
            "u_type",
            "u_playtime",
            "u_percent",
            "u_rsource",
            "u_rate",
            "u_user_role",
            "u_isnew_user",
            "u_isdownload",
            "u_isonline",
            "u_buffertime",
            "u_action",
            "u_ishigh",
            "u_cdn_source",
            "u_download_start",
            "u_download_stop",
            "u_fail_cdn_source",
            "u_new_cdn_source",
            "u_width",
            "u_height",
            "u_lon",
            "u_lat",
            "u_province",
            "u_city",
            "u_netop",
            "u_nettype",
            "u_sdkversion",
            "u_model",
            "u_device",
            "u_manufacture",
            "u_bigger_json"
    );

    public static final Map<String, String> RF = ImmutableMap.of(
            "u_channel", "u_dic",
            "u_version", "u_div",
            "u_setpid", "u_stepid"
    );
}
