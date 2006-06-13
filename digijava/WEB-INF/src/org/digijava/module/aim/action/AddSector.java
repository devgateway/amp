package org.digijava.module.aim.action;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;


public class AddSector extends Action {

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

		logger.debug("In add sector action");

		if (request.getParameter("parSecId") != null
				&& addSectorForm.getParentSectorId() == null) {

			/*
			 * check whether parSecId is Long else give error.
			 */
			Long parSecId = new Long(Long.parseLong(request
					.getParameter("parSecId")));

			/*
			 * check whether there exist a sector with the value parSecId else
			 * give error.
			 */
			addSectorForm.setParentSectorId(parSecId);

			HashMap map = null;

			if (parSecId.intValue() == 0) {
				Iterator itr = DbUtil.getAllOrganisation().iterator();
				map = new HashMap();

				while (itr.hasNext()) {
					AmpOrganisation ampOrg = (AmpOrganisation) itr.next();
					Long orgId = ampOrg.getAmpOrgId();
					String name = ampOrg.getName();
					map.put(orgId, name);
				}
				addSectorForm.setAmpOrganisation(null);
			} else {
				AmpSector ampSector = SectorUtil.getAmpSector(parSecId);
				AmpOrganisation ampOrg = DbUtil.getOrganisation(ampSector
						.getAmpOrgId().getAmpOrgId());
				addSectorForm.setAmpOrganisation(ampOrg.getName());
				addSectorForm.setAmpOrganisationId(ampOrg.getAmpOrgId());
			}
			addSectorForm.setOrganisationList(map);
		} else if (addSectorForm.getParentSectorId() != null) {
			// add the sector details to the databse
			logger.debug("Adding the sector");
			AmpSector ampSector = new AmpSector();

			if (addSectorForm.getParentSectorId().intValue() == 0) {
				ampSector.setParentSectorId(null);
			} else {
				ampSector.setParentSectorId(SectorUtil.getAmpSector(addSectorForm
						.getParentSectorId()));
			}
			ampSector.setAmpOrgId(DbUtil.getOrganisation(addSectorForm
					.getAmpOrganisationId()));
			ampSector.setSectorCode(addSectorForm.getSectorCode());
			ampSector.setName(addSectorForm.getSectorName());
			ampSector.setType(null);
			if (addSectorForm.getDescription() == null
					|| addSectorForm.getDescription().trim().equals("")) {

				ampSector.setDescription(new String(" "));
			} else {
				ampSector.setDescription(addSectorForm.getDescription());
			}
			ampSector.setLanguage(null);
			ampSector.setVersion(null);

			DbUtil.add(ampSector);
			logger.debug("Sector added");

			return mapping.findForward("added");
		} else {
			logger.debug("No action selected");
		}

		return mapping.findForward("forward");
	}

}
