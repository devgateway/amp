/*
 *   ShowNavigationBar.java
 * 	 @Author George Kvizhinadze gio@digijava.org
 * 	 Created:
 *   CVS-ID: $Id: ShowNavigationBar.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

package org.digijava.module.editor.action;

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
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;
import java.util.List;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.tiles.actions.TilesAction;
import org.apache.struts.tiles.ComponentContext;
import java.io.IOException;
import javax.servlet.ServletException;
import org.digijava.kernel.util.RequestUtils;

public class ShowNavigationBar
    extends TilesAction {

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException,
        ServletException {

        EditorForm editorForm = (EditorForm) form;
        String forward = "showNavBar";
        ActionErrors errors = new ActionErrors();
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(
            request);

        String currentLang = RequestUtils.getNavigationLanguage(request).getCode();
        List groupEditorList = null;

        try {
            groupEditorList = DbUtil.getSiteEditorList(moduleInstance.getSite().
                getSiteId(),
                currentLang,
                Constants.GROUP_MAIN_NAVIGATION_MENU);
        }
        catch (EditorException ex) {
            errors.add("editorNavigationBar",
                       new ActionError(
                "error.editorerror.editor.errorGettingEditorsForSite"));
        }

        editorForm.setNavBarEditorList(groupEditorList);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return null;
    }

}