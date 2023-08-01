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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
