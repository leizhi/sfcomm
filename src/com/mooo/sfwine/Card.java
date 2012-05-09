package com.mooo.sfwine;

public class Card {
	private Long id; // 主键 卡唯一ID
	
	private String rfidcode;//序列号
	private String zipCode;//邮编 6
	private Integer orgId;//机构
	private String cardType;//卡类型 6
	
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
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}
