
package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;


public class ViewSchemeTree extends Action {

	private static Logger logger = Logger.getLogger(GetSectors.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		AddSectorForm addSectorForm = (AddSectorForm) form;

		String event = request.getParameter("event");
		String parent = request.getParameter("parent");
		String schemeId = request.getParameter("ampSecSchemeId");
		logger.debug(request.getParameter("ampSecSchemeId"));

		Long schId = Long.parseLong(schemeId);
		if (schId == null || schId < 0)
			return mapping.findForward("back");
		
		List<AmpSector> sectors = SectorUtil.treeBuildGetAllSectorsFromScheme(schId);
		addSectorForm.setSchemeTree(sectors);
		
		return mapping.findForward("forward");
	}
	
}
