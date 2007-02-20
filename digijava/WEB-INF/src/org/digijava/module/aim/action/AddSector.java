
package org.digijava.module.aim.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTeamMember;
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

		String event = request.getParameter("event");
		String parent = request.getParameter("parent");
		String schemeId = request.getParameter("ampSecSchemeId");
		if(schemeId==null){
			schemeId = (String)session.getAttribute("Id");
			parent = "sector";
		}
		if(session.getAttribute("Id")==null){
			session.setAttribute("Id",schemeId);
		}
		if(parent!=null)
		{
			 addSectorForm.setLevelType(parent);
		}
		
		if(event!=null)
		{
			if(event.equals("addSector"))
			{
				   if(addSectorForm.getLevelType().equals("scheme"))
				{
					addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(null);
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(SectorUtil.getAmpSectorScheme(addSectorForm.getParentId()));
					newSector.setSectorCode(addSectorForm.getSectorCode());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setType(null);
					newSector.setAmpSectorId(null);
					if (addSectorForm.getDescription() == null
							|| addSectorForm.getDescription().trim().equals("")) {

						newSector.setDescription(new String(" "));
					} else {
						newSector.setDescription(addSectorForm.getDescription());
					}
					newSector.setLanguage(null);
					newSector.setVersion(null);
					DbUtil.add(newSector);
					session.setAttribute("Event","Edit");
					session.setAttribute("LEVEL","1");
					logger.info("level one sector added");
					return mapping.findForward("levelFirstSectorAdded");
				}
				if(addSectorForm.getLevelType().equals("sector"))
				{
					Long id = new Long((String)session.getAttribute("Id"));
					addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
					AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(SectorUtil.getAmpSector(id));
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(user);
					newSector.setSectorCode(addSectorForm.getSectorCode());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setType(null);
					if (addSectorForm.getDescription() == null
							|| addSectorForm.getDescription().trim().equals("")) {

						newSector.setDescription(new String(" "));
					} else {
						newSector.setDescription(addSectorForm.getDescription());
					}
					newSector.setLanguage(null);
					newSector.setVersion(null);
					DbUtil.add(newSector);
					session.setAttribute("LEVEL","2");
					logger.info("level 2 sector added");
					return mapping.findForward("levelSecondSectorAdded");
				}
				if(addSectorForm.getLevelType().equals("sector3"))
				{
					Long id = new Long((String)session.getAttribute("Id"));
					addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
					AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(SectorUtil.getAmpSector(id));
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(user);
					newSector.setSectorCode(addSectorForm.getSectorCode());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setType(null);
					if (addSectorForm.getDescription() == null
							|| addSectorForm.getDescription().trim().equals("")) {

						newSector.setDescription(new String(" "));
					} else {
						newSector.setDescription(addSectorForm.getDescription());
					}
					newSector.setLanguage(null);
					newSector.setVersion(null);
					DbUtil.add(newSector);
					session.setAttribute("LEVEL","3");
					logger.info("level Third Sector added");
					return mapping.findForward("levelThirdSectorAdded");
				}
			}
		}
			
		
		/*if (request.getParameter("parSecId") != null
				&& addSectorForm.getParentSectorId() == null) {

			/*
			 * check whether parSecId is Long else give error.
			 
			Long parSecId = new Long(Long.parseLong(request
					.getParameter("parSecId")));

			/*
			 * check whether there exist a sector with the value parSecId else
			 * give error.
			 
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
*/
		if(parent.equalsIgnoreCase("scheme"))		
		return mapping.findForward("forwardScheme");
		else
			if(parent.equalsIgnoreCase("sector")|| parent.equalsIgnoreCase("sector3"))
				return mapping.findForward("forwardSector");
		return null;
	}

}
