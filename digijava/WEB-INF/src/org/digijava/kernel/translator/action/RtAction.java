/*
 *   RtAction.java
 * 	 @Author Shamanth Murthy shamanth.murthy@mphasis.com
 *   Created: Sep 24, 2003
 *   CVS-ID: $Id: RtAction.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
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

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.util.DgUtil;

import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.form.TranslatorForm;
import org.digijava.kernel.util.I18NHelper;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.util.RequestUtils;


/* Controller Class that's called by Struts or Tiles ActionServlet... See the definitions in
 * struts-config.xml under /Web-INF/
 */

public class RtAction extends Action {


private static Logger logger =
		I18NHelper.getKernelLogger(org.digijava.kernel.translator.action.RtAction.class);


	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 *
	 * @param servlet The ActionServlet making this request
	 * @param mapping The ActionMapping used to select this instance
	 * @param actionForm The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			if (logger.isDebugEnabled()) {
				Object[] param = { "TranslatorAction", "execute()" };
				logger.l7dlog(
					Level.DEBUG,
					"ActionClass.MethodEnter.db",
					param,
					null);
			}
				TranslatorForm tForm = (TranslatorForm)form;


				if(request.getParameter("key") != null && request.getParameter("type") != null){
						String siteId="";

						if(request.getParameter("type").equalsIgnoreCase("local")){

								siteId= RequestUtils.getSiteDomain(request).getSite().getId().toString();
						}else{
								siteId= DgUtil.getRootSite(RequestUtils.getSiteDomain(request).getSite()).getId().toString();
						}

						tForm.setType(request.getParameter("type"));

						try{
							Message msg = new TranslatorWorker().get(request.getParameter("key"),"en",siteId);
							if(msg.getMessage() != null){
								tForm.setMessageEn(msg.getMessage());
								System.out.println("Data " + msg.getMessage());
							}else{
								tForm.setMessageEn("");
								}

						}catch(WorkerException we){
								Object[] param = { "RtAction" };
								logger.l7dlog(Level.ERROR, "ActionClass.Exception.err", param, we);
							}


						//dtermine target locale

						tForm.setDestLocale(RequestUtils.getNavigationLanguage(request).getCode());

				}


		if (logger.isDebugEnabled()) {
			Object[] param = { "TranslatorLocaleUpdate", "execute()" };
			logger.l7dlog(
				Level.DEBUG,
				"ActionClass.MethodReturn.db",
				param,
				null);
		}


		return mapping.findForward("success");
	}

}
