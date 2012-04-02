package com.mooo.sfwine;

/**

 */
public class User {

	private Long id;
	private String name;
	private String password;
	private String alias;
	private String active;
	
	public Long getId() {
	return id;
	}
	public void setId(Long cardId) {
	 this.id = cardId;
	}
	public String getName() {
	return name;
	}
	public void setName(String name) {
	 this.name = name;
	}
	public String getPassword() {
	return password;
	}
	public void setPassword(String password) {
	 this.password = password;
	}
	public String getAlias() {
	return alias;
	}
	public void setAlias(String alias) {
	 this.alias = alias;
	}
	public String getActive() {
	return active;
	}
	public void setActive(String active) {
	 this.active = active;
	}
}
