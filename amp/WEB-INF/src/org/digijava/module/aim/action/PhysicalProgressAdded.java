
/***
 * @author Priyajith
 * @version 0.1
 * Created on 19/01/2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.PhysicalProgress;

/**
 * This action class will add the physical progress entries from the
 * "add physical progress" pop-up to the "add activity" form
 */

public class PhysicalProgressAdded extends Action {

	private static Logger logger = Logger.getLogger(PhysicalProgressAdded.class);

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		if ((eaForm.getPhisycalProgress().getPhyProgTitle() == null ||
				eaForm.getPhisycalProgress().getPhyProgTitle().trim().length() == 0) ||
				eaForm.getPhisycalProgress().getPhyProgRepDate() == null)
			return mapping.findForward("forward");

		PhysicalProgress phyProg = new PhysicalProgress();
		phyProg.setTitle(eaForm.getPhisycalProgress().getPhyProgTitle());
		phyProg.setDescription(eaForm.getPhisycalProgress().getPhyProgDesc());
		phyProg.setReportingDate(eaForm.getPhisycalProgress().getPhyProgRepDate());

		Components comp = null;

		if (eaForm.getComponents().getSelectedComponents() != null) {
			Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
			while (itr.hasNext()) {
				comp = (Components) itr.next();
				if (comp.getComponentId().equals(eaForm.getComponents().getComponentId()))
					break;
			}

			if (eaForm.getPhisycalProgress().getPhyProgId() != null &&
					eaForm.getPhisycalProgress().getPhyProgId().intValue() != 0) {
				phyProg.setPid(eaForm.getPhisycalProgress().getPhyProgId());
				if (comp.getPhyProgress() != null && comp.getPhyProgress().size() > 0) {
					comp.getPhyProgress().remove(new PhysicalProgress(eaForm.getPhisycalProgress().getPhyProgId()));
				}
			} else {
				phyProg.setPid(new Long(System.currentTimeMillis()));
                                phyProg.setNewProgress(true);
			}
			if (comp.getPhyProgress() == null) {
				comp.setPhyProgress(new ArrayList());
			}
			comp.getPhyProgress().add(phyProg);
			List temp = (List) comp.getPhyProgress();
			Collections.sort(temp);
			comp.setPhyProgress(temp);

			eaForm.getComponents().getSelectedComponents().remove(new Components(comp.getComponentId()));
			if (eaForm.getComponents().getSelectedComponents() == null) {
				eaForm.getComponents().setSelectedComponents(new ArrayList());
			}
			eaForm.getComponents().getSelectedComponents().add(comp);
			temp = (List) eaForm.getComponents().getSelectedComponents();
			Collections.sort(temp);
			eaForm.getComponents().setSelectedComponents(temp);
			eaForm.getPhisycalProgress().setSelPhyProg(null);
			eaForm.getPhisycalProgress().setPhyProgId(null);
			eaForm.getComponents().setComponentId(null);
		}

		return mapping.findForward("forward");
	}
}
