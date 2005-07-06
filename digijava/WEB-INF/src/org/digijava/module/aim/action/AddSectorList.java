package org.digijava.module.aim.action;

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.AmpActivityForm;

public class AddSectorList
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        AmpActivityForm formBean = (AmpActivityForm) form;
        AmpActivityForm.SectorList list = new AmpActivityForm.SectorList();
//		formBean.setSectorList(new ArrayList());

		Iterator iter = formBean.getSectorList().iterator();
        while (iter.hasNext()) {
            AmpActivityForm.SectorList list1=(AmpActivityForm.SectorList)iter.next();
			//System.out.println("Amp ID in List" + list1.getAmpSectorId());
		}
		//System.out.println("Amp Sector Id" + formBean.getAmpSectorId());
		//System.out.println("Is Empty" + formBean.getSectorList().isEmpty());
		list.setAmpSectorId(formBean.getAmpSectorId());
		formBean.getSectorList().add(list);
		return mapping.findForward("forward") ;
	}
}

