package org.digijava.module.aim.action ;

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

public class EditSector extends Action {

		  private static Logger logger = Logger.getLogger(GetSectors.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}					 

					 AddSectorForm editSectorForm = (AddSectorForm) form;

					 logger.debug("In edit sector action");

					 if (request.getParameter("id") != null) {
								/*
								 * check whether the id is a valid long value
								 */
								Long secId = new Long(Long.parseLong(request.getParameter("id")));
								AmpSector ampSector = SectorUtil.getAmpSector(secId);

								/*
								 * check whether ampSector is null if yes return error
								 */

								editSectorForm.setSectorId(secId);
								HashMap map = null;
								
								if (ampSector.getParentSectorId() == null) {
										  editSectorForm.setParentSectorId(null);
										  Iterator itr = DbUtil.getAllOrganisation().iterator();
										  map = new HashMap();

										  while (itr.hasNext()) {
													 AmpOrganisation ampOrg = (AmpOrganisation) itr.next();
													 Long orgId = ampOrg.getAmpOrgId();
													 String name = ampOrg.getName();
													 map.put(orgId,name);
										  }
								} else {
										  editSectorForm.setParentSectorId(ampSector.getParentSectorId().getAmpSectorId());
								}
								editSectorForm.setSectorCode(ampSector.getSectorCode());
								editSectorForm.setSectorName(ampSector.getName());
								editSectorForm.setAmpOrganisationId(ampSector.getAmpOrgId().getAmpOrgId());
								editSectorForm.setDescription(ampSector.getDescription());
								editSectorForm.setOrganisationList(map);
					 }
					 else if (request.getParameter("id") == null && editSectorForm.getSectorId() != null) {
								// update the sector details to the database
								
								logger.debug("Updating the sector");
								AmpSector ampSector = new AmpSector();
								ampSector.setAmpSectorId(editSectorForm.getSectorId());
								ampSector.setParentSectorId(SectorUtil.getAmpSector(editSectorForm.getParentSectorId()));
								ampSector.setAmpOrgId(DbUtil.getOrganisation(editSectorForm.getAmpOrganisationId()));
								ampSector.setSectorCode(editSectorForm.getSectorCode());
								ampSector.setName(editSectorForm.getSectorName());
								if (editSectorForm.getDescription() == null
													 || editSectorForm.getDescription().trim().equals("")) {
										  ampSector.setDescription(new String(" "));
								} else {
										  ampSector.setDescription(editSectorForm.getDescription());
								}

								SectorUtil.updateSector(ampSector);
								logger.debug("Sector updated");
								return mapping.findForward("edited");
					 } else {
								logger.debug("No action selected");
					 }

					 return mapping.findForward("forward");
		  }
		  
		  
		  
}

