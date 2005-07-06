/*
*   IsAdmin.java
*   @Author Irakli Nadareishvili inadareishvili@worldbank.org
*   Created:
*   CVS-ID: $Id: IsAdmin.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.kernel.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.tagext.BodyTagSupport;

import javax.security.auth.Subject;
import java.security.Principal;

import org.apache.log4j.*;

import java.util.Iterator;
import java.util.Set;
/**
 * Custom tag that retrieves an internationalized messages string
 *
 * @author inadareishvili@worldbank.org
 * @version $Id: IsAdmin.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
 */

public class IsAdmin extends BodyTagSupport {

	private Logger log =
		Logger.getLogger("org.developmentgateway.core.taglib.AuthorizeTag");
	/**
	 * Process the start tag.
	 *
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() {

		try {
			//set up the logger
			//log.setResourceBundle(I18NHelper.getAiDABundle());

			HttpServletRequest request =
				(HttpServletRequest) pageContext.getRequest();


			boolean showData = false;


				if (request.getParameter("user") == null) {
					return (SKIP_BODY);
				}else{

					if(request.getParameter("user").equalsIgnoreCase("admin")){

							return EVAL_BODY_INCLUDE;

						}else{
							return (SKIP_BODY);

							}

					}

		} catch (Exception exception) {

			//TODO: comment this
            log.error("Could not retrieve an internationalized messages",exception);

			//TODO: UnCOMMENT this
			//if (log.isEnabledFor(Level.ERROR)) {
			//	Object[] obj = { "AuthorizeTag - doStartTag()" };
			//	log.l7dlog(
				//	Level.ERROR,
				//	"TaglibClass.Exception.err",
				//	obj,
				//	exception);
			//}
		}

		// Continue processing this page
		return (SKIP_BODY);
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {

		super.release();

	}

}
