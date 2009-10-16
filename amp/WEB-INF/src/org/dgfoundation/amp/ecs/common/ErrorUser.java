package org.dgfoundation.amp.ecs.common;

import java.io.Serializable;

public class ErrorUser implements Comparable<ErrorUser>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fullName;
	private String login;
	private String password;
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
}
