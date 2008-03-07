package org.digijava.module.forum.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.forum.form.ForumPageForm;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.forum.util.DbUtil;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.exception.*;
import org.apache.struts.action.ActionError;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumSubsection;

public class DeleteMessage
    extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String forward = "showThread";
        ForumPageForm forumPageForm = (ForumPageForm) form;
        ActionErrors errors = new ActionErrors();

        long delPostId = forumPageForm.getPostId();
        long retThreadId = 0;

        try {
            ForumPost post = DbUtil.getPostItem(delPostId);
            if (post != null) {
                retThreadId = post.getThread().getId();
                ForumThread parentThread = post.getThread();
                ForumSubsection subs = parentThread.getSubsection();
                parentThread.setLastPost(null);
                subs.setLastPost(null);
                DbUtil.updateThread(parentThread);
                DbUtil.updateSubsection(subs);
                DbUtil.deletePost(post);
            }
        }
        catch (ForumException ex) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.deletePost"));
        }

        ActionForward retFwd = mapping.findForward(forward);
        StringBuffer path = new StringBuffer(mapping.findForward(forward).
                                             getPath());
        path.append("?threadId=");
        path.append(retThreadId);
        path.append("&lastPost=true");
        retFwd = new ActionForward(path.toString(), true);

        return retFwd;

    }

}