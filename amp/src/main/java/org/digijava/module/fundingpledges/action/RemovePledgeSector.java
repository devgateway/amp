package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class RemovePledgeSector extends Action {

    private static Logger logger = Logger.getLogger(RemovePledgeSector.class);

    private ArrayList<ActivitySector> selectedSectors = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        // TODO: DELETE, NOT USED ANYMORE
        return mapping.findForward("forward");
    }
}
