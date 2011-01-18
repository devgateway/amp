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

		HttpSession session = request.getSession();
		PledgeForm plForm = (PledgeForm) form;
		
		if (plForm.getPledgeSectors() != null) {
			selectedSectors = new ArrayList<ActivitySector>(plForm.getPledgeSectors());
			String deleteList[] = request.getParameter("deleteSect").split("_");
			Iterator <ActivitySector> iter=selectedSectors.iterator();
			ArrayList<ActivitySector> sectorsToDelete = new ArrayList<ActivitySector>();
			Integer i = 1;
			while(iter.hasNext()){
				ActivitySector del = iter.next();
            	for (int j = 0; j < deleteList.length; j++) {
					if (deleteList[j].equals(i.toString())){
						sectorsToDelete.add(del);
					}
				}	
            	i++;
            }
			if (sectorsToDelete.size()!=0) {
				selectedSectors.removeAll(sectorsToDelete);
			}
			plForm.setPledgeSectors(selectedSectors);
		}
		
		request.getSession().removeAttribute("deleteSect");
		return mapping.findForward("forward");
	}
}
