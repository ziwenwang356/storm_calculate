package com.tangdou.panda.entity;

import java.util.Date;

public class Campaign {
	private Integer campaign_id;
	private String campaign_name;
	private Long user_id;
	private Date start_time;
	private Date end_time;
	private Integer budget;
	private Integer budget_type;
	private Integer budgetover;
	private Integer state;
	private Integer ad_type;
	private Date add_time;
	private String add_user_id;
	private Date mod_time;
	private String mod_user_id;

	public Integer getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}

	public String getCampaign_name() {
		return campaign_name;
	}

	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
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

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	public Integer getBudget_type() {
		return budget_type;
	}

	public void setBudget_type(Integer budget_type) {
		this.budget_type = budget_type;
	}

	public Integer getBudgetover() {
		return budgetover;
	}

	public void setBudgetover(Integer budgetover) {
		this.budgetover = budgetover;
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

}