package com.tangdou.panda.entity;

import java.util.Date;

public class Subscribe {
	private Integer subscribe_id;
	private String industry;
	private String network;
	private String region;
	private Integer channel_id;
	private Integer campaign_id;
	private Long user_id;
	private Date start_time;// '开始时间,品牌广告用'
	private Date end_time;// '结束时间,品牌广告用'
	private Integer bid;// '单位分'
	private Integer state;// '-1: 新建 0：正常；1：搁置；2：删除'
	private Integer ad_type;// '类型，1竞价；2品牌'
	private Date add_time;
	private String add_user_id;
	private Date mod_time;
	private String mod_user_id;

	public Integer getSubscribe_id() {
		return subscribe_id;
	}

	public void setSubscribe_id(Integer subscribe_id) {
		this.subscribe_id = subscribe_id;
	}

	public Integer getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}

	public Integer getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getAd_type() {
		return ad_type;
	}

	public void setAd_type(Integer ad_type) {
		this.ad_type = ad_type;
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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
