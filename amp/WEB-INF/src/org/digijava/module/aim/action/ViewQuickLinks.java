package org.digijava.module.aim.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ViewQuickLinksForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

public class ViewQuickLinks
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewQuickLinksForm vlForm = (ViewQuickLinksForm) form;
        HttpSession session = request.getSession();

        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
        if(tm != null) {
            Collection links = TeamMemberUtil.getMemberLinks(tm.getMemberId());
            vlForm.setMemberLinks(links);
        }

        return mapping.findForward("forward");
    }
}
