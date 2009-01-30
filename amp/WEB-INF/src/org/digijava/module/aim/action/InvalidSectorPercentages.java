package org.digijava.module.aim.action;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.InvalidDataListsForm;
import org.digijava.module.aim.util.InvalidDataUtil;

/**
 * Shows list of activities with invalid percentage data.
 * @author Irakli Kobiashvili
 *
 */
public class InvalidSectorPercentages extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InvalidDataListsForm listsForm = (InvalidDataListsForm)form;
		List<InvalidDataUtil.ActivitySectorPercentages> result = null;
		result = InvalidDataUtil.getActivitiesWithIncorrectSectorPersentage(null);
		listsForm.setInvalidSectorpercentages(result);
		return mapping.findForward("forward");
	}

}