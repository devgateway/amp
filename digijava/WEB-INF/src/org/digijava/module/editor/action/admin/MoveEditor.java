/*
 *   MoveEditor.java
 *   @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Apr 8, 2004
 *   CVS-ID: $Id: MoveEditor.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
package org.digijava.module.editor.action.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.form.EditorAdminForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;

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
        String siteId = moduleInstance.getSite().getSiteId();

        //temporary
        try {
            key = URLDecoder.decode(key, "UTF-8");
        }
        catch (UnsupportedEncodingException ex2) {
        }


        Editor editor = null;

        try {

            editor = DbUtil.getEditor(siteId, key, lang);
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
                swapEd = DbUtil.getEditor(siteId, editorIndex);
                if (swapEd == null) {
                    swapEd = DbUtil.getEditor(siteId, 0);
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