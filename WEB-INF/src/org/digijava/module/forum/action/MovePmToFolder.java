/*
 *   MovePmToFolder.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created:
 *   CVS-ID: $Id$
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

package org.digijava.module.forum.action;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.common.exception.BBCodeException;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.module.forum.dbentity.ForumPrivateMessage;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import java.util.List;
import java.util.ArrayList;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class MovePmToFolder
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        ForumPageForm forumPageForm = (ForumPageForm) form;

        String forward = "showFolder";
        ActionErrors errors = new ActionErrors();

        if (forumPageForm.getCheckboxList() != null &&
            forumPageForm.getCheckboxList().length > 0) {
            String[] selPmsIds = forumPageForm.getCheckboxList();

            List pmList = new ArrayList();
            try {
                for (int idIndex = 0; idIndex < selPmsIds.length; idIndex++) {
                    long longId = Long.parseLong(selPmsIds[idIndex]);

                    ForumPrivateMessage pm =
                        DbUtil.getPrivateMsgItem(longId);
                    pm.setFolderName(forumPageForm.getMoveToFolderName());
                    pmList.add(pm);
                }
                DbUtil.updatePrivateMsgList(pmList);
            }
            catch (Exception ex) {
                errors.add("forumGlobalError",
                           new ActionError("error.forum.pm.movePm"));
            }

        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }

        return mapping.findForward(forward);
    }

}