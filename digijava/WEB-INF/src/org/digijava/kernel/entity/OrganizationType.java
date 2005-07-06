/*
 *   OrganizationType.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 7, 2003
 *   CVS-ID: $Id: OrganizationType.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.io.Serializable;

public class OrganizationType implements Serializable{

    private String id;
	private String type;

    public OrganizationType() {}

    public OrganizationType( String id, String type ) {
        this.id = id;
        this.type = type;
    }

    public OrganizationType( String id ) {
        this.id = id;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
