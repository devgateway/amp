/**
 * 
 */
package org.digijava.module.fundingpledges.action;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.form.PledgeForm;


/**
 * NOT USED ANYMORE
 *
 */
public class PledgeLocationSelected extends Action {
    private static Logger logger = Logger.getLogger(PledgeLocationSelected.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        
        // TODO: DELETE, NOT USED ANYMORE

        return mapping.findForward("forward");
    }
}
