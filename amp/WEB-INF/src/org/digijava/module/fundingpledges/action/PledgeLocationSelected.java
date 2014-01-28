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


public class PledgeLocationSelected extends Action {
	private static Logger logger = Logger.getLogger(PledgeLocationSelected.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws Exception {
		
		PledgeForm plForm = (PledgeForm) form;
		Long[] userSelectedLocs = plForm.getUserSelectedLocs();
		if (userSelectedLocs != null) {
            List<Long> locsIds = getSelectedLocIds(plForm);
            for (Long userSelectedLoc : userSelectedLocs) {
                if (! locsIds.contains(userSelectedLoc)) {
                    AmpCategoryValueLocations ampCVLocation = DynLocationManagerUtil.getLocation(userSelectedLoc, false);
                    FundingPledgesLocation fpl = new FundingPledgesLocation();
                    fpl.setLocation(ampCVLocation);
                    if (plForm.getSelectedLocs() == null) {
                        plForm.setSelectedLocs(new ArrayList<FundingPledgesLocation>());
                    }
                    if (plForm.getSelectedLocs().size() == 0) {
                        fpl.setLocationpercentage(100f);
                    } else {
                        fpl.setLocationpercentage(0f);
                    }
                    plForm.getSelectedLocs().add(fpl);
                }
            }
		}
		
		return mapping.findForward("forward");
	}

    private List<Long> getSelectedLocIds(PledgeForm plForm) {
        List<Long> selectedLocs = new ArrayList<Long>();
        if (plForm.getSelectedLocs() != null) {
            for (FundingPledgesLocation location : plForm.getSelectedLocs()) {
                selectedLocs.add(location.getLocation().getId());
            }
        }
        return selectedLocs;
    }
}
