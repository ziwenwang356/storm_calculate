package com.tangdou.panda.entity;

import java.util.Date;

public class DayAuctionRecard {

	private Long flow_id;
	private Integer subscribe_id;
	private Integer channel_id;
	private Integer campaign_id;
	private Long user_id;
	private String auction_time;
	private Integer bid;
	private Integer result;
	private Integer position;
	private Date add_time;
	private Date mod_time;

	public Long getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(Long flow_id) {
		this.flow_id = flow_id;
	}

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

	public String getAuction_time() {
		return auction_time;
	}

	public void setAuction_time(String auction_time) {
		this.auction_time = auction_time;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public Date getMod_time() {
		return mod_time;
	}

	public void setMod_time(Date mod_time) {
		this.mod_time = mod_time;
	}

}
