/*
 *   HowDidYouHear.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 7, 2003
 *   CVS-ID: $Id: HowDidYouHear.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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


public class HowDidYouHear {

    private String id;
	private String referral;

    public HowDidYouHear() {}

    public HowDidYouHear(String id, String referral) {
        this.id = id;
        this.referral = referral;
    }


	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
