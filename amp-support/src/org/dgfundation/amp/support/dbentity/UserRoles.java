package org.dgfundation.amp.support.dbentity;

public class UserRoles {
	private String name;
	private int roleid;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public UserRoles(String name, int roleid) {
		super();
		this.name = name;
		this.roleid = roleid;
	}

}


