package com.mooo.sfwine;

import com.mooo.mycoz.common.StringUtils;

public class LoginSession {

	public static User user = new User();
	
	public static boolean allow = false;
	
	public static boolean staffSignal = false;

	public static boolean isAllow() {
		UserAction userAction = new UserAction();

		if(!StringUtils.isNull(user.getName()))
			user.setName(user.getName().trim());
		
		if(!StringUtils.isNull(user.getPassword()))
			user.setPassword(user.getPassword().trim());

		allow = userAction.processLogin();
		
		return allow;
	}
	
}