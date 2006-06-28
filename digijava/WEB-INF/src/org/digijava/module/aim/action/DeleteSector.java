package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.form.AddSectorForm;
import javax.servlet.http.*;
import java.util.*;

public class DeleteSector extends Action {

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

		AddSectorForm deleteSectorForm = (AddSectorForm) form;

		if (request.getParameter("ampSectorId") != null) {

			/*
			 * check whether the id is a valid long value
			 */
			Long secId = new Long(Long.parseLong(request
					.getParameter("ampSectorId")));

			AmpSector ampSector = SectorUtil.getAmpSector(secId);
			
			logger.info("Getting subsectors for " + secId);
			Collection amp = SectorUtil.getSubSectors(secId);
			/*
			 * check whether ampsector is null if yes return error.
			 */
			logger.info("Collection amp.size = " + amp.size());
			if (amp.size() > 0) {
				logger.info("cant delete..................");
				ActionErrors errors = new ActionErrors();
				errors.add("title", new ActionError(
						"error.aim.deleteScheme.sectorSelected"));
				saveErrors(request,errors);
				return mapping.findForward("cantDelete");
			} else {
				logger.info("cannn delete..................finally.....");
				SectorUtil.deleteSector(secId);
				/*deleteSectorForm.setSectorId(secId);
				deleteSectorForm.setSectorCode(ampSector.getSectorCode());
				deleteSectorForm.setSectorName(ampSector.getName());
				// deleteSectorForm.setAmpOrganisation(ampSector.getAmpOrgId().getName());
				deleteSectorForm.setDescription(ampSector.getDescription());

				Iterator itr = SectorUtil.getSubSectors(secId).iterator();
				Iterator actItr = SectorUtil.getSectorActivities(secId).iterator();

				if (itr.hasNext()) {
					logger.info("cant delete 1");
					deleteSectorForm.setFlag("subSectorExist");
				} else if (actItr.hasNext()) {
					logger.info("cant delete 2");
					deleteSectorForm.setFlag("activityExist");
				} else {
					logger.info("cannnnnnnnnn delete");
					deleteSectorForm.setFlag("delete");
				}*/
				
				ActionErrors errors = new ActionErrors();
				errors.add("title", new ActionError(
						"error.aim.deleteSector.sectorDeleted"));
				saveErrors(request,errors);
				return mapping.findForward("cantDelete");
			}
		} /*else if (request.getParameter("id") == null
				&& deleteSectorForm.getSectorId() != null) {

			logger.debug("deleting the sector");
			AmpSector ampSector = SectorUtil.getAmpSector(deleteSectorForm
					.getSectorId());
			// DbUtil.delete(ampSector);
			logger.debug("Sector deleted");
			return mapping.findForward("deleted");
		} else {
			logger.debug("No action selected");
		}*/

		return null;
	}

}
