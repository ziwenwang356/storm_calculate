package com.tangdou.panda.entity;

import java.util.Date;

public class Price {
	private Integer price_id;// '主键，自增'
	private Integer channel_id;// '外键，对应频道id; 当该值不为0时则为频道价格'
	private Integer position_id;// '外键，对应广告位id; 当该值不为0是则为广告位价格'
	private Integer type;// '类型，1视频，2图文，3文字, 4图片'
	private Integer base_price;// '基础价格，单位分'
	private Integer suggest_price;// '建议价格，一起可跟基础价格保持一致，单位分'
	private Integer time_unit;// '位置基础价格 0：小时；1：天；2：周；3：月; 4: 点击'
	private Date add_time;
	private String add_user_id;
	private Date mod_time;
	private String mod_user_id;

	public Integer getPrice_id() {
		return price_id;
	}

	public void setPrice_id(Integer price_id) {
		this.price_id = price_id;
	}

	public Integer getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}

	public Integer getPosition_id() {
		return position_id;
	}

	public void setPosition_id(Integer position_id) {
		this.position_id = position_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBase_price() {
		return base_price;
	}

	public void setBase_price(Integer base_price) {
		this.base_price = base_price;
	}

	public Integer getSuggest_price() {
		return suggest_price;
	}

	public void setSuggest_price(Integer suggest_price) {
		this.suggest_price = suggest_price;
	}

	public Integer getTime_unit() {
		return time_unit;
	}

	public void setTime_unit(Integer time_unit) {
		this.time_unit = time_unit;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public String getAdd_user_id() {
		return add_user_id;
	}

	public void setAdd_user_id(String add_user_id) {
		this.add_user_id = add_user_id;
	}

	public Date getMod_time() {
		return mod_time;
	}

	public void setMod_time(Date mod_time) {
		this.mod_time = mod_time;
	}

	public String getMod_user_id() {
		return mod_user_id;
	}

	public void setMod_user_id(String mod_user_id) {
		this.mod_user_id = mod_user_id;
	}

}