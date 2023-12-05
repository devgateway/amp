package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpSession;

public class ConfigureTeam extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws

    java.lang.Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("currentMember") == null) {
            return mapping.findForward("index");
        }

        return mapping.findForward("forward");
    }
}

