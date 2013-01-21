package org.digijava.module.visualization.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.visualization.form.SaveInfoForm;
import org.digijava.module.visualization.util.DbUtil;

public class SaveAdditionalInformation  extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		SaveInfoForm infoForm = (SaveInfoForm)form;
		if (infoForm.getType() != null) 
			DbUtil.saveAdditionalInfo(infoForm.getId(), infoForm.getType(), infoForm.getBackground(), infoForm.getDescription(), infoForm.getKeyAreas());
		return null;
	}

}
