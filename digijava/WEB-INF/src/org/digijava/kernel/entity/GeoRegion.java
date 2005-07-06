/*
 *   GeoRegion.java
 *   @Author Irakli Nadareishvili inadareishvili@worldbank.org
 * 	 Created: Jul 3, 2003
 *   CVS-ID: $Id: GeoRegion.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.kernel.entity;

import java.security.acl.Group;
import java.security.Principal;
import java.util.Enumeration;
import java.io.Serializable;

public class GeoRegion implements Group, Serializable {

	private String id;
	private String name;
	private String type;
	private String active;

	public GeoRegion() {
	}

	public GeoRegion(String name, String id, String type) {
		this.name = name;
		this.id = id;
		this.type = type;
		this.active = "true";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static String getNameByISO2(String ISO2) {
		/*
		 * Uses the DgGeoRegionsCollection map
		 */
		return null;
	}


	public String isActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getActive(String active) {
			return this.active;
	}

	public boolean addMember(Principal member) {
		return true;
	}

	public boolean removeMember(Principal member) {
		return true;
	}

	public boolean isMember(Principal member) {
		return true;
	}

	public Enumeration members() {
		return null;
	}

}
