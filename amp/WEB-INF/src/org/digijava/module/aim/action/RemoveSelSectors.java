package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;

public class RemoveSelSectors extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		Long selSectors[] = eaForm.getSectors().getSelActivitySectors();
		Collection prevSelSectors = eaForm.getSectors().getActivitySectors();
		Collection newSectors = new ArrayList();

		Iterator itr = prevSelSectors.iterator();

        boolean flag =false;

		while (itr.hasNext()) {
			ActivitySector asec = (ActivitySector) itr.next();
            flag=false;
			for (int i = 0; i < selSectors.length; i++) {
				if (asec.getSectorId().equals(selSectors[i])) {
					flag=true;
                    break;
				}
			}

            if(!flag){
                newSectors.add(asec);
            }
		}

		eaForm.getSectors().setActivitySectors(newSectors);
		return mapping.findForward("forward");
	}
}
