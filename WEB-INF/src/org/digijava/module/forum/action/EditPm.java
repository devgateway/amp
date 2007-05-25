package org.digijava.module.forum.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPrivateMessage;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;
import org.digijava.module.forum.form.ForumPageForm;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.util.ForumAction;
import org.digijava.module.forum.util.ForumBaseForm;
import org.digijava.module.forum.util.ForumConstants;

public class EditPm
    extends ForumAction {
    public ActionForward process(ActionMapping mapping,
                                 ForumBaseForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Forum forum,
                                 ForumUserSettings forumUser) {
        ForumPageForm forumPageForm = (ForumPageForm) form;

        ForumPrivateMessage pm = null;
        try {
            pm = DbUtil.getPrivateMsgItem(forumPageForm.getPmId());
            if (pm != null) {
                forumPageForm.setPMParams(pm);
                ForumUserSettings toUser =
                    DbUtil.getForumUserItem(new Long(pm.getToUserId()),
                                            new Long(forum.getId()));
                forumPageForm.setUserSettingParams(toUser);
                forumPageForm.setUserId(toUser.getId());
            }

        }
        catch (ForumException ex) {
        }

        String forward = "showPmDetails";
        return mapping.findForward(forward);
    }

}