/**
 * PermissionMap.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.io.Serializable;

/**
 * PermissionMap.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 29.08.2007
 */
public class PermissionMap implements Serializable,Comparable {

	protected Long id;
	protected String permissibleCategory;
	protected String objectIdentifier;
	protected Permission permission;
	
	public String toString() {
		return permissibleCategory+"("+(objectIdentifier==null?"global":objectIdentifier)+")";
	}
	
	public boolean getDedicated() {
	    if(permission==null) return false;
	    return permission.isDedicated();
	}
	
	//this is a helper property and never persisted:
	protected Long permissionId;
	
	//this is a helper property and never persisted:
	protected String objectLabel;
	
	public String getPermissibleCategory() {
		return permissibleCategory;
	}
	public void setPermissibleCategory(String className) {
		this.permissibleCategory = className;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	public String getObjectLabel() {
	    return objectLabel;
	}
	public void setObjectLabel(String objectLabel) {
	    this.objectLabel = objectLabel;
	}
	public Long getPermissionId() {
	    return permissionId;
	}
	public void setPermissionId(Long permissionId) {
	    this.permissionId = permissionId;
	}
	public int compareTo(Object o) {
	    PermissionMap pm=(PermissionMap) o;
	    if(this.getObjectLabel()!=null && pm.getObjectLabel()!=null) return this.getObjectLabel().compareTo(pm.getObjectLabel());
	    if(this.getObjectIdentifier()!=null && pm.getObjectIdentifier()!=null) return this.getObjectIdentifier().compareTo(pm.getObjectIdentifier());
	    return this.getPermissibleCategory().compareTo(pm.getPermissibleCategory());
	}

	/**
	 * @return the objectIdentifier
	 */
	public String getObjectIdentifier() {
		return objectIdentifier;
	}

	/**
	 * @param objectIdentifier the objectIdentifier to set
	 */
	public void setObjectIdentifier(String objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}
}
