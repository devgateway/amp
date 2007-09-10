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
public class PermissionMap implements Serializable {
	
	private static final long serialVersionUID = -7294750950373378335L;
	protected Long id;
	protected String categoryName;
	protected String objectIdentifier;
	protected Permission permission;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String className) {
		this.categoryName = className;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getObjectIdentifier() {
		return objectIdentifier;
	}
	public void setObjectIdentifier(String objectId) {
		this.objectIdentifier = objectId;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
