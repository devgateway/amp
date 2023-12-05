package org.digijava.module.fundingpledges.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class RemovePledgeProgram extends Action {

    private static Logger logger = Logger.getLogger(RemovePledgeProgram.class);

    private ArrayList<FundingPledgesProgram> selectedProgs = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

// TODO: DELETE, NOT USED ANYMORE
        return mapping.findForward("forward");
    }
}
