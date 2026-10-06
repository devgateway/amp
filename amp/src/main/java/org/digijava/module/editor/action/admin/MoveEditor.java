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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.form.EditorAdminForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;

public class MoveEditor extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        EditorAdminForm editorAdminForm = (EditorAdminForm) form;
        String forward = "showAdmin";

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);

        String lang = editorAdminForm.getLang();
        String key = editorAdminForm.getKey();
        Site site = moduleInstance.getSite();

        //temporary
        try {
            key = URLDecoder.decode(key, "UTF-8");
        }
        catch (UnsupportedEncodingException ex2) {
        }


        Editor editor = null;

        try {

            editor = DbUtil.getEditor(site, key, lang);
        }
        catch (EditorException ex) {
        }

        if (editor != null) {
            Editor swapEd = null;

            int swapIndex = editor.getOrderIndex();
            int editorIndex = 0;
            if (editorAdminForm.getMoveDir().equals(Constants.MOVE_UP)) {
                 editorIndex = swapIndex - 1;
            }
            else if (editorAdminForm.getMoveDir().equals(Constants.MOVE_DOWN)) {
                editorIndex = swapIndex + 1;
            }

            try {
                swapEd = DbUtil.getEditor(site, editorIndex);
                if (swapEd == null) {
                    swapEd = DbUtil.getEditor(site, 0);
                }
                if (swapEd != null) {
                    swapEd.setOrderIndex(swapIndex);
                    editor.setOrderIndex(editorIndex);
                    Collection updateEditors = new ArrayList();
                    updateEditors.add(editor);
                    updateEditors.add(swapEd);
                    DbUtil.updateEditorList(updateEditors);
                }
            }
            catch (EditorException ex1) {
            }

        }


        return mapping.findForward(forward);
    }
}
