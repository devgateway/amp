/*
 *   TranslatorNavForm.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created:
 *   CVS-ID: $Id: TranslatorNavForm.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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

package org.digijava.kernel.translator.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author shamanth.murthy@mphasis.com
 * @version
 */

public final class TranslatorNavForm extends ActionForm {
	private Collection navigation = null;

	private String strSelectedData = null;

	/**
	* Default Constructor
	*
	*/
	public TranslatorNavForm() {
	}
	public Collection getLocales() {
		return navigation;
	}

	public void setLocales(Collection col) {
		this.navigation = col;
	}

	public String getLocalesSelected() {
		return strSelectedData;
	}

	public void setLocalesSelected(String strSelectedData) {
		this.strSelectedData = strSelectedData;
	}

	/**
	* Reset all properties to their default values.
	*
	* @param mapping The mapping used to select this instance
	* @param request The servlet request we are processing
	*/

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	/**
	* Validate inputs
	*
	* @param mapping
	* @param request
	* @return
	*/

	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {
		return null;

	}

}