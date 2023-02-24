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

import java.net.URLDecoder;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.DbUtil;

public class ShowPage extends Action {

    public ActionForward execute(ActionMapping mapping,
                             ActionForm form,
                             javax.servlet.http.HttpServletRequest request,
                             javax.servlet.http.HttpServletResponse
                             response) throws Exception{
    String forward = "showPage";
    EditorForm edForm = (EditorForm) form;
    edForm.setKey(URLDecoder.decode(edForm.getKey(), "UTF-8"));

    String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
    ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
        request);
    Site site = moduleInstance.getSite();

    Editor ed = DbUtil.getEditor(site, edForm.getKey(), currentLang);

    String tagDefault = null;
    if (ed.getTitle() == null || ed.getTitle().trim().length()==0) {
        tagDefault = URLDecoder.decode(ed.getEditorKey(), "UTF-8");
    } else {
        tagDefault = ed.getTitle();
    }
    edForm.setTitle(tagDefault);
    edForm.setEditor(ed);

    return mapping.findForward(forward);
}
}
