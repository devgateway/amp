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

import org.apache.struts.action.*;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.form.EditorAdminForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ShowAdmin
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        EditorAdminForm editorAdminForm = (EditorAdminForm) form;
        String forward = "showAdmin";
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);
        ActionMessages errors = new ActionMessages();

        editorAdminForm.setRefUrl(RequestUtils.getSourceURL(request));

        List siteEditorList = null;
        try {
            siteEditorList = DbUtil.getSiteEditorList(
                moduleInstance.getSite());
            editorAdminForm.setSiteEditors(siteEditorList);
        }
        catch (EditorException ex) {
            errors.add("editor",
                       new ActionMessage("error.editor.errorGettingEditorsForSite"));
        }

        //Check order index duplicates
        boolean hasOrderConflict = false;
        if (siteEditorList != null) {
            Set orderIndexDupChecker = new HashSet();
            Iterator dupIt = siteEditorList.iterator();
            while (dupIt.hasNext()) {
                Editor dupEd = (Editor) dupIt.next();
                if (!orderIndexDupChecker.add(new Integer(dupEd.getOrderIndex())) ||
                    dupEd.getOrderIndex() == 0) {
                    hasOrderConflict = true;
                    break;
                }
            }
        }

        if (hasOrderConflict) {
            Iterator iter = siteEditorList.iterator();
            for (int newIndex = 1; iter.hasNext(); newIndex ++) {
                Editor ed = (Editor) iter.next();
                ed.setOrderIndex(newIndex);
            }
            try {
                DbUtil.updateEditorList(siteEditorList);
            }
            catch (EditorException ex2) {
                /**@todo Exception handling */
            }
        }

        //Assign updated values
        if (request.getParameter("update") != null) {
            List updateEditors = new ArrayList();
            Set keys = new HashSet();
            if (editorAdminForm.getMultiboxList() != null &&
                editorAdminForm.getMultiboxList().length > 0) {

                for (int ind = 0;
                     ind < editorAdminForm.getMultiboxList().length;
                     ind++) {
                    String key = editorAdminForm.getMultiboxList()[ind];
                    keys.add(key);
                }
            }
            if (siteEditorList != null) {
                Iterator it = siteEditorList.iterator();
                while (it.hasNext()) {
                    Editor ed = (Editor) it.next();
                    if (keys.contains(ed.getEditorKey()) &&
                        (ed.getGroupName() == null ||
                         !ed.getGroupName().equals(Constants.
                        GROUP_MAIN_NAVIGATION_MENU))) {
                        ed.setGroupName(Constants.GROUP_MAIN_NAVIGATION_MENU);
                        updateEditors.add(ed);
                    }
                    else if (!keys.contains(ed.getEditorKey()) &&
                             ed.getGroupName() != null &&
                             ed.getGroupName().
                             equals(Constants.GROUP_MAIN_NAVIGATION_MENU)) {
                        ed.setGroupName(Constants.GROUP_OTHER);
                        updateEditors.add(ed);
                    }
                }
            }
            if (!updateEditors.isEmpty()) {
                try {
                    DbUtil.updateEditorList(updateEditors);
                }
                catch (EditorException ex1) {
                    errors.add("editor",
                               new ActionMessage(
                        "error.editor.errorUpdatingEditorList"));
                }
            }
        }


        //Fill selected checkbox list
        if (siteEditorList != null && !siteEditorList.isEmpty()) {
            Set selKeys = new HashSet();
            Iterator selIt = siteEditorList.iterator();
            while (selIt.hasNext()) {
                Editor ed = (Editor) selIt.next();
                if (ed.getGroupName() != null &&
                    ed.getGroupName().equals(Constants.GROUP_MAIN_NAVIGATION_MENU)) {
                    String editorKey = ed.getEditorKey();
                    selKeys.add(editorKey);
                }
            }
            String[] newMultiboxList = new String[selKeys.size()];
            Iterator keySetIt = selKeys.iterator();
            for (int mBoxValueIndex = 0;
                 keySetIt.hasNext();
                 mBoxValueIndex ++) {
                newMultiboxList[mBoxValueIndex] = (String) keySetIt.next();
            }
            editorAdminForm.setMultiboxList(newMultiboxList);
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return mapping.findForward(forward);
    }

}
