package org.digijava.module.dataExchange.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.ActivityUtil;

public class SimpleExportAction extends Action {

	private static Logger log = Logger.getLogger(SimpleExportAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//
		List<AmpActivity> list = ActivityUtil.getAllActivitiesByName("");
		AmpActivity activity = null;
		for (Iterator<AmpActivity> it = list.iterator(); it.hasNext();) {
			activity = it.next();
		}
		//
		response.setContentType("text/plain");
		response.setHeader("content-disposition",
				"attachment;filename=export.csv");
		//
		return mapping.findForward("forward");
	}

}
