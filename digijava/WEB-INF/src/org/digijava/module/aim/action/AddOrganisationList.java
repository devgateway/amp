package org.digijava.module.aim.action;

import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.AmpActivityForm;

public class AddOrganisationList
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        AmpActivityForm formBean = (AmpActivityForm) form;
        AmpActivityForm.OrgList list = new AmpActivityForm.OrgList();
//		formBean.setSectorList(new ArrayList());

		Iterator iter = formBean.getOrgList().iterator();
        while (iter.hasNext()) {
            AmpActivityForm.OrgList list1=(AmpActivityForm.OrgList)iter.next();
			//System.out.println("Amp ID in List" + list1.getAmpOrgId());
		}
		//System.out.println("Amp Org Id" + formBean.getAmpOrgId());
		//System.out.println("Amp Org Id" + formBean.getAmpRoleId());
	//	System.out.println("Is Empty" + formBean.getSectorList().isEmpty());
		list.setAmpOrgId(formBean.getAmpOrgId());
		list.setAmpRoleId(formBean.getAmpRoleId());
		formBean.getOrgList().add(list);
		return mapping.findForward("forward") ;
	}
}

