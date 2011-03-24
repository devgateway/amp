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
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class RemovePledgeProgram extends Action {

	private static Logger logger = Logger.getLogger(RemovePledgeProgram.class);

	private ArrayList<FundingPledgesProgram> selectedProgs = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		PledgeForm plForm = (PledgeForm) form;
		
		if (plForm.getSelectedProgs() != null) {
			selectedProgs = new ArrayList<FundingPledgesProgram>(plForm.getSelectedProgs());
			String deleteList[] = request.getParameter("deleteProgs").split("_");
			Iterator <FundingPledgesProgram> iter=selectedProgs.iterator();
			ArrayList<FundingPledgesProgram> progsToDelete = new ArrayList<FundingPledgesProgram>();
			Integer i = 1;
			while(iter.hasNext()){
				FundingPledgesProgram del = iter.next();
            	for (int j = 0; j < deleteList.length; j++) {
					if (deleteList[j].equals(i.toString())){
						progsToDelete.add(del);
					}
				}	
            	i++;
            }
			if (progsToDelete.size()!=0) {
				selectedProgs.removeAll(progsToDelete);
			}
			plForm.setSelectedProgs(selectedProgs);
		}
		
		request.getSession().removeAttribute("deleteProgs");
		return mapping.findForward("forward");
	}
}
