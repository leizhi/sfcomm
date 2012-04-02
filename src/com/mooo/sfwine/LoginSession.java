package com.mooo.sfwine;

public class LoginSession {

	public static User user = new User();
	
	public static boolean allow = false;
	
	public static boolean staffSignal = false;

	public static boolean isAllow() {
		UserAction userAction = new UserAction();
		user.setName(user.getName().trim());
		user.setPassword(user.getPassword().trim());

		allow = userAction.processLogin();
		
		return allow;
	}
	
}