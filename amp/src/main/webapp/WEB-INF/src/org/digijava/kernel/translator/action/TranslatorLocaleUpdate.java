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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.form.TranslatorNavForm;
import org.digijava.kernel.util.DgUtil;

/* Controller Class that's called by Struts or Tiles ActionServlet... See the definitions in
 * struts-config.xml under /Web-INF/
 */

public final class TranslatorLocaleUpdate extends Action {

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

        TranslatorNavForm nav = (TranslatorNavForm) form;

        //Set locale
        org.digijava.kernel.entity.Locale locale = new org.digijava.kernel.entity.Locale();
        locale.setCode(nav.getLocalesSelected());


        DgUtil.switchLanguage(locale,request,response);
        //System.out.println("Switch locale completed " + nav.getLocalesSelected());

        return new ActionForward(request.getParameter("back_url"),true);

    }
}
