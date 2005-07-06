/*
 *   DeleteEditor.java
 *   @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Apr 9, 2004
 *   CVS-ID: $Id: DeleteEditor.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
import org.digijava.kernel.entity.ModuleInstance;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;



public class DeleteEditor extends Action {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        EditorAdminForm editorAdminForm = (EditorAdminForm) form;
        String forward = "showAdmin";
        ActionErrors errors = new ActionErrors();


        String key = editorAdminForm.getKey().trim();
        String lang = editorAdminForm.getLang();
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);
        String siteId = moduleInstance.getSite().getSiteId();
        if (key != null &&
            key.length()>0) {
            try {
                Editor ed = DbUtil.getEditor(siteId, key, lang);
                if (ed != null) {
                    DbUtil.deleteEditor(ed);
                }

            } catch (EditorException ex) {
                logger.debug("Unable to delete editor", ex);
                errors.add("editor" ,
                           new ActionError("error.editor.errorDeletingEditor"));
            }
        }
        return mapping.findForward(forward);
    }

}