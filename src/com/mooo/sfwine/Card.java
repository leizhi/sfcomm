package com.mooo.sfwine;

public class Card {
	private Long id; // 主键 卡唯一ID
	private String rfidcode;//序列号
	private String jobTypeName;
	private String zipCode;//邮编 6
	private String wineryKey;//酒厂编号3
	private String wineryName;//酒厂编号3

	private String wineJarKey;//酒罐编号 4
	private String wineType;//酒香型 2
	
	private String wineLevel;//酒的品质 2
	private String alcohol;//酒精度 3
	private String operator;//操作人名称 3
	private String supervisorCompanyKey;//监管公司代码 2
	private String supervisorName;//监管人名称 3
	private String brewingDate;//酿造日期 10
	private String operationDate;//操作日期 10
	private String wineJarVolume;//酒罐容积 8
	private String wineVolume;//源酒容积 8
	private String volumeUnit;//容积单位 2
	
	private String material;//原料 2
	
	private String wineName;//酒名
	private long userId;//登陆用户ID
	private Integer orgId;//机构

	
	
	
	public String getWineName() {
		return wineName;
	}
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
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
	
	public String getWineryName() {
		return wineryName;
	}
	public void setWineryName(String wineryName) {
		this.wineryName = wineryName;
	}
	public void setWineryKey(String wineryKey) {
		this.wineryKey = wineryKey;
	}
	public String getWineJarKey() {
		return wineJarKey;
	}
	public void setWineJarKey(String wineJarKey) {
		this.wineJarKey = wineJarKey;
	}
	public String getWineType() {
		return wineType;
	}
	public void setWineType(String wineType) {
		this.wineType = wineType;
	}
	public String getWineLevel() {
		return wineLevel;
	}
	public void setWineLevel(String wineLevel) {
		this.wineLevel = wineLevel;
	}
	public String getAlcohol() {
		return alcohol;
	}
	public void setAlcohol(String alcohol) {
		this.alcohol = alcohol;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSupervisorCompanyKey() {
		return supervisorCompanyKey;
	}
	public void setSupervisorCompanyKey(String supervisorCompanyKey) {
		this.supervisorCompanyKey = supervisorCompanyKey;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getBrewingDate() {
		return brewingDate;
	}
	public void setBrewingDate(String brewingDate) {
		this.brewingDate = brewingDate;
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
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
}
