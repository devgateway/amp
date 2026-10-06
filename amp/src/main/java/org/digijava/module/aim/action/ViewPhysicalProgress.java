package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.PhysicalProgressForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.gateperm.core.GatePermConst;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewPhysicalProgress extends TilesAction {

    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {


        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session
                .getAttribute("currentMember");
        request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
        PhysicalProgressForm formBean = (PhysicalProgressForm) form;

        if (teamMember == null) {
            formBean.setValidLogin(false);
        } else {
            formBean.setValidLogin(true);
                    if (teamMember.getAppSettings() != null) {
                      ApplicationSettings appSettings = teamMember.
                          getAppSettings();
                    }

            String compId = request.getParameter("compId");
            if (compId != null) {
                long cId = Long.parseLong(compId);
                Iterator itr = formBean.getComponents().iterator();
                while (itr.hasNext()) {
                    Components comp = (Components) itr.next();
                    if (comp.getComponentId().longValue() == cId) {
                        formBean.setComponent(comp);
                        break;
                    }
                }
                return null;
            }
            Long ampActivityId = new Long(request.getParameter("ampActivityId"));
            formBean.setComponents(ActivityUtil.getAllComponents(ampActivityId));
            formBean.setIssues(new ArrayList()); //dummy value
        }
        return null;
    }
}
