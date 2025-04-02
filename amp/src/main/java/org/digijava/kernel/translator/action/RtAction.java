/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.translator.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.form.TranslatorForm;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.I18NHelper;
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
                logger.debug("ActionClass.MethodEnter.db { \"TranslatorAction\", \"execute()\" }");
            }
                TranslatorForm tForm = (TranslatorForm)form;


                if(request.getParameter("key") != null && request.getParameter("type") != null){
                        Long siteId;

                        if(request.getParameter("type").equalsIgnoreCase("local")){

                                siteId = RequestUtils.getSiteDomain(request).getSite().getId();
                        }else{
                                siteId = DgUtil.getRootSite(RequestUtils.getSiteDomain(request).getSite()).getId();
                        }

                        tForm.setType(request.getParameter("type"));


                        Message msg = new TranslatorWorker().getByKey(request.getParameter("key"),"en",siteId);
                        if (msg.getMessage() != null){
                            tForm.setMessageEn(msg.getMessage());
                                //System.out.println("Data " + msg.getMessage());
                        }else{
                            tForm.setMessageEn("");
                        }

                        //dtermine target locale

                        tForm.setDestLocale(RequestUtils.getNavigationLanguage(request).getCode());
                }


        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"TranslatorLocaleUpdate\", \"execute()\" }");
        }


        return mapping.findForward("success");
    }

}
