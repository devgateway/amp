package org.digijava.module.aim.action;

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.AmpActivityForm;

public class AddLocationList
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        AmpActivityForm formBean = (AmpActivityForm) form;
        AmpActivityForm.LocationList list = new AmpActivityForm.LocationList();
//		formBean.setSectorList(new ArrayList());

		Iterator iter = formBean.getLocationList().iterator();
        while (iter.hasNext()) {
            AmpActivityForm.LocationList list1=(AmpActivityForm.LocationList)iter.next();
			//System.out.println("Amp ID in List" + list1.getAmpLocationId());
		}
		//System.out.println("Amp Region Id" + formBean.getAmpLocationId());
		//System.out.println("Is Empty" + formBean.getLocationList().isEmpty());
		list.setAmpLocationId(formBean.getAmpLocationId());
		formBean.getLocationList().add(list);
		return mapping.findForward("forward") ;
	}
}

