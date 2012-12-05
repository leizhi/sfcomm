package com.mooo.sfwine;

public class Card {
	private Long id; // 主键 卡唯一ID
	
	private String uuid;//序列号
	private String rfidcode;//序列号
	private String winery;//酒厂定义
	private Integer orgId;//机构
	private String cardTypeName;//卡类型 6
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRfidcode() {
		return rfidcode;
	}
	public void setRfidcode(String rfidcode) {
		this.rfidcode = rfidcode;
	}
	public String getWinery() {
		return winery;
	}
	public void setWinery(String winery) {
		this.winery = winery;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
