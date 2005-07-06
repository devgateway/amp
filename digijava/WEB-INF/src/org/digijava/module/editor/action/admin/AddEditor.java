/*
 *   AddEditor.java
 *   @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Apr 8, 2004
 *   CVS-ID: $Id: AddEditor.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

            } catch (EditorException ex1) {
                /**@todo exception handling */
            }
        }
        return mapping.findForward(forward);
    }
}