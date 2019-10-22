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

package org.digijava.module.editor.action.admin;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.form.EditorAdminForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

public class AddEditor
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        EditorAdminForm editorAdminForm = (EditorAdminForm) form;
        String forward = "showAdmin";

        User user = RequestUtils.getUser(request);
        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
        String refUrl = RequestUtils.getSourceURL(request);

        String key = editorAdminForm.getKey().trim();

        if (key != null &&
            key.length()>0) {

            try {
//                key = URLDecoder.decode(key, "UTF-8");

                Editor ed = DbUtil.createEditor(user,
                    currentLang,
                    refUrl,
                    key,
                    editorAdminForm.getKey(),
                    editorAdminForm.getValue(),
                    null,
                    request);

                ed.setLastModDate(new Date());
                ed.setGroupName(Constants.GROUP_OTHER);

                    DbUtil.saveEditor(ed);

            } catch (Exception ex1) {
                /**@todo exception handling */
            }
        }
        return mapping.findForward(forward);
    }
}
