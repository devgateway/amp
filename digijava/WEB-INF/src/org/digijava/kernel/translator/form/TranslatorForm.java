/*
 *   TranslatorForm.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created:
 *   CVS-ID: $Id: TranslatorForm.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author shamanth.murthy@mphasis.com
 * @version
 */

public final class TranslatorForm extends ActionForm {

	public String strData = "";
	public String strKey = "";
	public String strMode = "";
	public String strLocale = "";
	public String strType = "";
	public String strMessageEn = "";
	public String strDestLocale = "";

	/**
	 * Default Constructor
	 *
	 */

	public TranslatorForm() {
		//CHANGE HERE IF NEEDED//

	}

	public String getDestLocale() {

		return this.strDestLocale;
	}

	public void setDestLocale(String strDestLocale) {

		this.strDestLocale = strDestLocale;
	}

	public String getText() {

		return this.strData;
	}

	public void setText(String strTranslatedText) {

		this.strData = strTranslatedText;
	}

	public String getMessageEn() {

		return this.strMessageEn;
	}

	public void setMessageEn(String strMessageEn) {

		this.strMessageEn = strMessageEn;
	}


	public String getLocale() {

		return this.strLocale;
	}

	public void setLocale(String strLocale) {

		this.strLocale = strLocale;
	}

	public String getMode() {

		return this.strMode;
	}

	public void setMode(String strMode) {

		this.strMode = strMode;
	}

	public String getType() {

			return this.strType;
	}

	public void setType(String strType) {

			this.strType = strType;
	}
	public String getKey() {

		return this.strKey;
	}

	public void setKey(String strKey) {

		this.strKey = strKey;
	}

	/*
	 *
	 */

	/*This Collection holds all the URL information,
	 * which will be rendered by the JSP
	 */

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {

		this.strData = "";

	}

	/**
	 *
	 *
	 */

	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		//		System.out.println("into Validate");

		ActionErrors errors = new ActionErrors();

		if (strData.equals("")) {

			//		      System.out.println("Errors Detected");
			errors.add(
				"Translated text ",
				new ActionError("error.username.required"));
		}

		return errors;

		//return null;
	}

}