/*
 *   Organization.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 7, 2003
 *   CVS-ID: $Id: Organization.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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


public class Organization {

	private String name;
	private String type;

	public Organization() {
	}

	 Organization(String name, String type) {
		this.name = name;
		this.type = type;
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
		/*
		 * Should throw InvalidOrganizationTypeException if not accurate type is provided
		 * The types are provided by DgOrganizationTypes persisted collection
		 */
		this.type = type;
	}

}
