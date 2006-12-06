package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class RemoveSelSectors extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		Long selSectors[] = eaForm.getSelActivitySectors();
		Collection prevSelSectors = eaForm.getActivitySectors();
		Collection newSectors = new ArrayList();

		Iterator itr = prevSelSectors.iterator();

		while (itr.hasNext()) {
			ActivitySector asec = (ActivitySector) itr.next();
			for (int i = 0; i < selSectors.length; i++) {
				if (!asec.getId().equals(selSectors[i])) {
					newSectors.add(asec);
					break;
				}
			}
		}

		eaForm.setActivitySectors(newSectors);
		eaForm.setStep("2");
		return mapping.findForward("forward");
	}
}
