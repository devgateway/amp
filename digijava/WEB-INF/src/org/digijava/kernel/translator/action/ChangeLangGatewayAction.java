/*
 *   ChangeLangGatewayAction.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created:
 *   CVS-ID: $Id: ChangeLangGatewayAction.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

package org.digijava.kernel.translator.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.kernel.translator.form.TranslatorNavForm;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

import org.apache.log4j.Logger;

/* Controller Class that's called by Struts or Tiles ActionServlet... See the definitions in
 * struts-config.xml under /Web-INF/
 */

public final class ChangeLangGatewayAction extends Action {

	private static Logger logger =
		Logger.getLogger(ChangeLangGatewayAction.class);

	/* This method overrides the Action classes execute method. This is the function called by the
	 * controller servlet
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

		//TranslatorNavForm nav = (TranslatorNavForm) form;

		//Set locale
		org.digijava.kernel.entity.Locale locale = new org.digijava.kernel.entity.Locale();
		String selectedLocale = request.getParameter("lang");
		if(selectedLocale == null)
			selectedLocale = java.util.Locale.ENGLISH.getLanguage();

		if(logger.isDebugEnabled()){
			logger.debug(" Selected locale " + selectedLocale);
		}

		locale.setCode(selectedLocale);

		DgUtil.switchLanguage(locale,request,response);

		String localeUrl = "";

		//first check for site-domain/site-path mapping to the destination locale...
		java.util.Collection col = RequestUtils.getSite(request).getSiteDomains();
		java.util.Iterator it = col.iterator();

		while(it.hasNext()){
			org.digijava.kernel.request.SiteDomain domain =(org.digijava.kernel.request.SiteDomain)it.next();
			if(domain.getLanguage() != null && domain.getLanguage().getCode().equals(selectedLocale)){
				localeUrl = DgUtil.getSiteUrl(domain, request);
				break;
			}

			if(logger.isDebugEnabled())
				logger.debug(" domain object got " + domain);
		}



		if(localeUrl.equals("")){
			it = col.iterator();
			while(it.hasNext()){
				org.digijava.kernel.request.SiteDomain domain1 = (org.digijava.kernel.request.SiteDomain)it.next();
				if(domain1.getLanguage() == null){

					if(localeUrl.equals("")){
						localeUrl = DgUtil.getSiteUrl(domain1, request);
						break;
					}
				}
			}
		}

		String url = localeUrl + request.getParameter("back_url");

		if(logger.isDebugEnabled())
			logger.debug(" Url being redirected to " + url);
		return new ActionForward(url,true);

	}
}
