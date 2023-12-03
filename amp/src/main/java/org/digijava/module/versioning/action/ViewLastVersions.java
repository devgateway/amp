package org.digijava.module.versioning.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ViewLastVersions extends TilesAction {

    public ActionForward execute(ComponentContext context, ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        
        List<AmpActivityFake> updatedAcitvities = ActivityUtil.getLastUpdatedActivities();
        session.setAttribute(Constants.MY_LAST_VERSIONS, updatedAcitvities);
        return null;
    }
}
