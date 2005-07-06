/*
 *   SaveText.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Dec 17, 2003
 * 	 CVS-ID: $Id: SaveText.java,v 1.1 2005-07-06 10:34:12 rahul Exp $
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


import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.DbUtil;
import net.sf.hibernate.Hibernate;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.digijava.kernel.util.RequestUtils;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SaveText
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        EditorForm formBean = (EditorForm) form;

        User user = RequestUtils.getUser(request);
        Editor editor = formBean.getEditor();

        String returnUrl = null;

        if (editor != null) {
            if (formBean.getContent() != null) {
                editor.setBody(formBean.getContent());
            }
            if (formBean.getTitle() != null){
                editor.setTitle(formBean.getTitle());
            }
            editor.setLanguage(formBean.getLang());
            editor.setLastModDate(new Date());
            editor.setUser(user);
            editor.setTitle(formBean.getTitle());

            returnUrl = editor.getUrl();

            DbUtil.updateEditor(editor);
        }
        response.setContentType("text/html; charset=UTF-8");
        response.sendRedirect(returnUrl);

        return null;
    }
}
