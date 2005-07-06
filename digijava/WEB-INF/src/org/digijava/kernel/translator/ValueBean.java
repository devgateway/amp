/*
*   ValueBean.java
*   @Author
*   Created:
*   CVS-ID: $Id: ValueBean.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.translator;

import java.io.Serializable;
public class ValueBean implements Serializable {
	private String keyId;
	private String source;
	private String target;
	private String targetSiteId;
	private String sourceSiteId;
	boolean needsUpdate = false;

	public ValueBean() {

	}
	public ValueBean(String key,String message_source,String message_target,String sourceSiteId,String targetSiteId,boolean update) {
		this.keyId = key;
		this.source = message_source;
		this.target = message_target;
		this.needsUpdate = update;
		this.targetSiteId = targetSiteId;
		this.sourceSiteId = sourceSiteId;
	}

	public String getKeyId() {
		return this.keyId;
	}

	public String getSource() {
			return this.source;
	}

	public String getTarget() {
		return this.target;
	}

	public void setKeyId(String key) {
		this.keyId = key;
	}

	public void setSource(String message_source) {
		this.source = message_source;
	}

	public void setTarget(String message_target) {
		this.target = message_target;
	}

	public void setTargetSiteId(String siteId) {
		this.targetSiteId = siteId;
	}

	public String getTargetSiteId() {
		return this.targetSiteId;
	}
	public void setSourceSiteId(String siteId) {
		this.sourceSiteId = siteId;
	}

	public String getSourceSiteId() {
		return this.sourceSiteId;
	}
	public boolean isNeedsUpdate() {
			return this.needsUpdate;
	}

	public void setNeedsUpdate(boolean b) {
			needsUpdate = b;
	}


}