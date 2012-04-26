package com.mooo.sfwine;

public class Card {
	private Long id; // 主键 卡唯一ID
	
	private String rfidcode;//序列号
	private String jobTypeName;
	private String zipCode;//邮编 6
	private String wineryKey;//酒厂编号3
	private String wineryName;//酒厂编号3
	private String wineryAddress;//酒厂地址
	private String wineJarKey;//酒罐编号 4
	
	private String operationDate;//操作日期 10
	private String wineJarVolume;//酒罐容积 8
	private String wineVolume;//源酒容积 8
	private String volumeUnit;//容积单位 2
	
	private Integer orgId;//机构
	
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
	public String getJobTypeName() {
		return jobTypeName;
	}
	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getWineryKey() {
		return wineryKey;
	}
	public void setWineryKey(String wineryKey) {
		this.wineryKey = wineryKey;
	}
	public String getWineryName() {
		return wineryName;
	}
	public void setWineryName(String wineryName) {
		this.wineryName = wineryName;
	}
	public String getWineryAddress() {
		return wineryAddress;
	}
	public void setWineryAddress(String wineryAddress) {
		this.wineryAddress = wineryAddress;
	}
	public String getWineJarKey() {
		return wineJarKey;
	}
	public void setWineJarKey(String wineJarKey) {
		this.wineJarKey = wineJarKey;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public String getWineJarVolume() {
		return wineJarVolume;
	}
	public void setWineJarVolume(String wineJarVolume) {
		this.wineJarVolume = wineJarVolume;
	}
	public String getWineVolume() {
		return wineVolume;
	}
	public void setWineVolume(String wineVolume) {
		this.wineVolume = wineVolume;
	}
	public String getVolumeUnit() {
		return volumeUnit;
	}
	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

}
