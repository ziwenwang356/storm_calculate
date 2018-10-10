package com.tangdou.panda.entity;

import java.util.Date;

public class AdUser {
	private String user_id;//
	private String username;// '用户名',
	private String password;// '密码',
	private String encrypt;// '加密因子',
	private String mobile;// '联系方式',
	private String email;// 'Email',
	private String realname;// '真实名字',
	private Integer amount;// '余额，单位分',
	private String card;// '密保卡',
	private Integer iscompany;
	private String lastloginip;// '最后登录ip',
	private Integer lastlogintime;// '最后登录时间',
	private Date add_time;// 添加时间,

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	public Integer getIscompany() {
		return iscompany;
	}

	public void setIscompany(Integer iscompany) {
		this.iscompany = iscompany;
	}

	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	public Integer getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Integer lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

}