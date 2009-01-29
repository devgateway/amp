package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class LocationLevelChanged extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		eaForm.getLocation().setSelectedLocs(null);
		eaForm.getLocation().setSelLocs(null);
		eaForm.getLocation().setCols(null);
		eaForm.getLocation().setNumResults(0);
		
		eaForm.setStep("2");
		return mapping.findForward("forward");
    }
}
