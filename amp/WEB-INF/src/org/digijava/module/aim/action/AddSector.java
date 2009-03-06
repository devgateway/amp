
package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;


public class AddSector extends Action {

	private static Logger logger = Logger.getLogger(GetSectors.class);
	
	public static final String DEFAULT_VALUE_SECTOR = "101";
	public static final String DEFAULT_VALUE_SUB_SECTOR = "1001";
	public static final String DEFAULT_VALUE_SUB_SUB_SECTOR = "10001";

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
		if(schemeId==null || schemeId.equals("") || schemeId.length()<=0){
			schemeId = (String)session.getAttribute("Id");			
		}
		else{
			session.setAttribute("Id",schemeId);
			logger.debug("setting the session::::::::::::::::::::");
		}
		if(parent!=null)
		{
			 addSectorForm.setLevelType(parent);
		}
		logger.debug(addSectorForm.getLevelType());
		logger.debug("Add===================================================Sector:;:::::::"+session.getAttribute("Id"));
		logger.debug("outside event if    value of schemeId============="+schemeId);
		
		if(event!=null)
		{
			if(event.equals("addSector"))
			{
				   if(addSectorForm.getLevelType().equals("scheme"))
				{
					   logger.debug("level 1inside event if    value of schemeId============="+schemeId);
					if(!checkSectorName(addSectorForm))
							{
						Collection schemeGot = SectorUtil.getEditScheme(
								new Integer((String)session.getAttribute("Id")));
								addSectorForm.setFormFirstLevelSectors(
										SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
									Iterator itr = schemeGot.iterator();
									while (itr.hasNext()) {
										AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
										addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
										addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
										addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
										addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
									}
								return mapping.findForward("levelFirstSectorAdded");
							}
					addSectorForm.setParentId(new Long(schemeId));
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(null);
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(SectorUtil.getAmpSectorScheme(addSectorForm.getParentId()));
					newSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);
					newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setDescription(addSectorForm.getDescription());
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
					Collection schemeGot = SectorUtil.getEditScheme(
							new Integer((String)session.getAttribute("Id")));
					addSectorForm.setFormFirstLevelSectors(
							SectorUtil.getSectorLevel1(new Integer((String)session.getAttribute("Id"))));
						Iterator itr = schemeGot.iterator();
						while (itr.hasNext()) {
							AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
							addSectorForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
							addSectorForm.setSecSchemeName(ampScheme.getSecSchemeName());
							addSectorForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
							addSectorForm.setParentId(ampScheme.getAmpSecSchemeId());
						}
					
					logger.debug("level one sector added");
					session.setAttribute("Id",null);
					return mapping.findForward("levelFirstSectorAdded");
				}
				if(addSectorForm.getLevelType().equals("sector"))
				{
					logger.debug("level 2 inside event if    value of schemeId============="+schemeId);
					if(!checkSectorName(addSectorForm))
						return mapping.findForward("levelSecondSectorAdded");
					Long id = new Long(schemeId);
					addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
					AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(SectorUtil.getAmpSector(id));
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(user);
					newSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SECTOR);
					newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setDescription(addSectorForm.getDescription());
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
					
					Long parentId = id;//new Long(id);
					addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
					Collection _subSectors = addSectorForm.getSubSectors();
					Iterator itr = _subSectors.iterator();
					while (itr.hasNext()) {
						AmpSector ampScheme = (AmpSector) itr.next();
						addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
						addSectorForm.setParentId(ampScheme.getAmpSectorId());
						addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
					}
					
					AmpSector editSector= new AmpSector();
					editSector = SectorUtil.getAmpSector(parentId);
					addSectorForm.setSectorCode(editSector.getSectorCode());
					addSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
					addSectorForm.setSectorName(editSector.getName());
					addSectorForm.setSectorId(editSector.getAmpSectorId());
					addSectorForm.setDescription(editSector.getDescription());
					logger.debug("level 2  sector added");
					session.setAttribute("Id",null);
					return mapping.findForward("levelSecondSectorAdded");
				}
				if(addSectorForm.getLevelType().equals("sector3"))
				{
					logger.debug("level 3 inside event if    value of schemeId============="+schemeId);
					Long id = new Long(schemeId);
					addSectorForm.setParentId(new Long((String)session.getAttribute("Id")));
					if(!checkSectorName(addSectorForm))
						return mapping.findForward("levelThirdSectorAdded");
					AmpSectorScheme user = SectorUtil.getParentSchemeId(id);
					AmpSector newSector = new AmpSector();
					newSector.setAmpSectorId(null);
					newSector.setParentSectorId(SectorUtil.getAmpSector(id));
					newSector.setAmpOrgId(null);
					newSector.setAmpSecSchemeId(user);
					newSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SUB_SECTOR);
					newSector.setSectorCodeOfficial(addSectorForm.getSectorCodeOfficial());
					newSector.setName(addSectorForm.getSectorName());
					newSector.setDescription(addSectorForm.getDescription());
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
					
					Long parentId = id;//new Long(id);
					addSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
					Collection _subSectors = addSectorForm.getSubSectors();
					Iterator itr = _subSectors.iterator();
					while (itr.hasNext()) {
						AmpSector ampScheme = (AmpSector) itr.next();
						addSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
						addSectorForm.setParentId(ampScheme.getAmpSectorId());
						addSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
					}
					
					AmpSector editSector= new AmpSector();
					editSector = SectorUtil.getAmpSector(parentId);
					addSectorForm.setSectorCode(editSector.getSectorCode());
					addSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
					addSectorForm.setSectorName(editSector.getName());
					addSectorForm.setSectorId(editSector.getAmpSectorId());
					addSectorForm.setDescription(editSector.getDescription());
					
					
					
					logger.debug("level Third Sector added");
					session.setAttribute("Id",null);
					return mapping.findForward("levelThirdSectorAdded");
				}
			}
		}
			
		
		
		if(parent.equalsIgnoreCase("scheme"))		
		return mapping.findForward("forwardScheme");
		else
			if(parent.equalsIgnoreCase("sector")|| parent.equalsIgnoreCase("sector3"))
				return mapping.findForward("forwardSector");
			else return null;
	}

	private boolean checkSectorNameAndCode(AddSectorForm addSectorForm){
		if(addSectorForm.getSectorCode() == null || "".equals(addSectorForm.getSectorCode()) ||
				addSectorForm.getSectorName() == null || "".equals(addSectorForm.getSectorName()))
			return false;
		return true;
	}
	
	private boolean checkSectorName(AddSectorForm addSectorForm){
		if(	addSectorForm.getSectorName() == null || "".equals(addSectorForm.getSectorName()))
			return false;
		return true;
	}
	
}
