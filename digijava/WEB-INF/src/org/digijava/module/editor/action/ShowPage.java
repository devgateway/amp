/*
 *   ShowPage.java
 * 	 @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Apr 8, 2004
 *   CVS-ID: $Id: ShowPage.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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

import java.net.URLDecoder;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
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
    String siteId = moduleInstance.getSite().getSiteId();

    Editor ed = DbUtil.getEditor(siteId, edForm.getKey(), currentLang);

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