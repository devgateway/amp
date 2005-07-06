/*
*   LabelValueBean.java
*   @Author Shamanth Murthy
*   Created: Shamanth Murthy
*   CVS-ID: $Id: LabelValueBean.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
package org.digijava.kernel.util;

import java.io.Serializable;

/**
 * @author shamanth.murthy
 *
 *
 */
public class LabelValueBean implements Serializable {
	private String Label;
	private String Value;

	/**
	 * @param strLabel
	 * @param strValue
	 */
	public LabelValueBean(String strLabel, String strValue) {

		this.Label = strLabel;
		this.Value = strValue;

	}

	/**
	 * @return
	 */
	public String getLabel() {
		return this.Label;
	}
	/**
	 * @return
	 */
	public String getValue() {
		return this.Value;
	}
	/**
	 * @param strLabel
	 */
	public void setLabel(String strLabel) {
		this.Label = strLabel;
	}
	/**
	 * @param strValue
	 */
	public void setValue(String strValue) {
		this.Value = strValue;
	}
}