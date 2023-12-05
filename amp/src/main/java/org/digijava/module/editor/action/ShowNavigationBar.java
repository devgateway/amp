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

package org.digijava.module.editor.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowNavigationBar
    extends TilesAction {

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {

        EditorForm editorForm = (EditorForm) form;

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
            request);
        Site site;
        if (moduleInstance == null) {
            site = RequestUtils.getSite(request);
        }
        else {
            site = moduleInstance.getSite();
        }

        String currentLang = RequestUtils.getNavigationLanguage(request).
            getCode();

        List groupEditorList = DbUtil.getSiteEditorList(site,
            currentLang,
            Constants.GROUP_MAIN_NAVIGATION_MENU);

        editorForm.setNavBarEditorList(groupEditorList);


        return null;
    }

}
