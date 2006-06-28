package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.helper.Sector;
import javax.servlet.http.*;
import java.util.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class UpdateSectorSchemes extends Action {

	private static Logger logger = Logger.getLogger(UpdateSectorSchemes.class);

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
		logger.info("came into the  UPDate sector schemes manager");
		Collection scheme = null;
		AddSectorForm sectorsForm = (AddSectorForm) form;
		logger.info("in the update sector scheme's action");
		String event = request.getParameter("event");

		sectorsForm.setIdGot((String) request
				.getParameter("ampSecSchemeparentId"));
		String schemeId = sectorsForm.getIdGot();
		scheme = SectorUtil.getSectorSchemes();
		logger.info(" this is the event got!!....." + event + "  id is "
				+ schemeId);
		if (event != null) {
			if (event.equals("edit")) {
				Integer a = new Integer(schemeId);
				Collection schemeGot = SectorUtil.getEditScheme(a);

				sectorsForm.setFormFirstLevelSectors(SectorUtil
						.getSectorLevel1(a));
				logger.info("in edit....");
				Iterator itr = schemeGot.iterator();
				while (itr.hasNext()) {
					AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
					sectorsForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
					sectorsForm.setSecSchemeName(ampScheme.getSecSchemeName());
					sectorsForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
				}

				/* scheme = SectorUtil.getSectorSchemes();
				 sectorsForm.setFormSectorSchemes(scheme);*/

				return mapping.findForward("viewSectorSchemeLevel1");
			}
			if (event.equals("addscheme")) {
				logger.info("now add a new  scheme");
				return mapping.findForward("addSectorScheme");
			}
			if (event.equals("saveScheme")) {
				logger.info("saving the scheme");
				AmpSectorScheme ampscheme = new AmpSectorScheme();
				logger
						.info(" the name is...."
								+ sectorsForm.getSecSchemeName());
				logger.info(" the code is ...."
						+ sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				DbUtil.add(ampscheme);
				request.setAttribute("event", "view");

				logger.info("done kutte");
				scheme = SectorUtil.getSectorSchemes();
				sectorsForm.setFormSectorSchemes(scheme);
				return mapping.findForward("viewSectorSchemes");
			}
			if (event.equals("updateScheme")) {
				logger.info(" updating Scheme");
				String editId = (String) request.getParameter("editSchemeId");
				AmpSectorScheme ampscheme = new AmpSectorScheme();

				Long Id = new Long(editId);
				logger
						.info(" the name is...."
								+ sectorsForm.getSecSchemeName());
				logger.info(" the code is ...."
						+ sectorsForm.getSecSchemeCode());
				logger.info(" this is the id......" + Id);
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				ampscheme.setAmpSecSchemeId(Id);
				DbUtil.update(ampscheme);
				logger.info(" updated!!");
				return mapping.findForward("viewSectorSchemes");
			}
			if (event.equals("delete")) {
				logger.info("in the delete Scheme");
				Integer a = new Integer(schemeId);
				sectorsForm.setFormFirstLevelSectors(SectorUtil
						.getSectorLevel1(a));
				Long Id = new Long(schemeId);
				logger.info(" this is the id......" + Id);
				logger.info(" the size is..........."
						+ sectorsForm.getFormFirstLevelSectors().size());
				if (sectorsForm.getFormFirstLevelSectors().size() >= 1) {
					logger.info("no deletion");
					sectorsForm.setDeleteSchemeFlag(false);
					ActionErrors errors = new ActionErrors();
					errors.add("title", new ActionError(
							"error.aim.deleteScheme.schemeSelected"));
					saveErrors(request, errors);
					return mapping.findForward("viewSectorSchemes");

				} else {
					sectorsForm.setDeleteSchemeFlag(true);
					logger.info(" yup!!! delete");
					SectorUtil.deleteScheme(Id);
				}
			}
		}

		scheme = SectorUtil.getSectorSchemes();
		sectorsForm.setFormSectorSchemes(scheme);

		return mapping.findForward("viewSectorSchemes");
	}
}
