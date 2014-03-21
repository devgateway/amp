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
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class RemovePledgeLocation extends Action {

	private static Logger logger = Logger.getLogger(RemovePledgeLocation.class);

	private ArrayList<FundingPledgesLocation> selectedLocs = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// TODO: NOT USED, TO DELETE
//		HttpSession session = request.getSession();
//		PledgeForm plForm = (PledgeForm) form;
//		
//		if (plForm.getSelectedLocs() != null) {
//			selectedLocs = new ArrayList<FundingPledgesLocation>(plForm.getSelectedLocs());
//			String deleteList[] = request.getParameter("deleteLocs").split("_");
//			Iterator <FundingPledgesLocation> iter=selectedLocs.iterator();
//			ArrayList<FundingPledgesLocation> locationsToDelete = new ArrayList<FundingPledgesLocation>();
//			Integer i = 1;
//			while(iter.hasNext()){
//				FundingPledgesLocation del = iter.next();
//            	for (int j = 0; j < deleteList.length; j++) {
//					if (deleteList[j].equals(i.toString())){
//						locationsToDelete.add(del);
//					}
//				}	
//            	i++;
//            }
//			if (locationsToDelete.size()!=0) {
//				selectedLocs.removeAll(locationsToDelete);
//			}
//			plForm.setSelectedLocs(selectedLocs);
//		}
//		
//		request.getSession().removeAttribute("deleteLocs");
		return mapping.findForward("forward");
	}
}
