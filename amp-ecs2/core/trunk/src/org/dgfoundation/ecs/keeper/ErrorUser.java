package org.dgfoundation.ecs.keeper;

import java.io.Serializable;

import org.dgfoundation.ecs.core.ECS;

public class ErrorUser implements Comparable<ErrorUser>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fullName;
	private String login;
	private String password;
	
	public ErrorUser() {
		super();
	}
	
	public ErrorUser(String fullName, String login, String password) {
		super();
		this.fullName = fullName;
		this.login = login;
		this.password = password;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int compareTo(ErrorUser o) {
		return this.login.compareTo(o.getLogin());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErrorUser)
			return this.login.compareTo(((ErrorUser)obj).getLogin()) == 0;
		else
			return false;
	}
	
	public String toJson() {
		String result = 
				"{" +
				ECS.t("fullName", fullName) +
				ECS.t("login", login) +
				ECS.t("password", password) +
				ECS.to("fullName", fullName) +
				"}";
		return result;
	}
}
