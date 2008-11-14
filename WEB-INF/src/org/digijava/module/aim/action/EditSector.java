package org.digijava.module.aim.action ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
					 logger.debug("In edit sector action");
					 if (request.getParameter("id") != null) {
						 	String id = (String)request.getParameter("id");
							Long secId = new Long(Long.parseLong(request.getParameter("id")));
							String event = request.getParameter("event");
							String flag = request.getParameter("flag");
							if(request.getParameter("flag")==null)
								flag = "false";
							////System.out.println(flag);
							////System.out.println("FLAG========================"+editSectorForm.getJspFlag());
						 if(flag.equalsIgnoreCase("false"))
						 	{
						 		
								if(event!=null || !event.equals("") || event.length()<=0)
								{
									if(event.equalsIgnoreCase("update2LevelSector"))
								  {
										AmpSector ampSector = SectorUtil.getAmpSector(secId);
										editSectorForm.setSectorId(secId);
										ampSector.setName(editSectorForm.getSectorName());
										ampSector.setDescription(editSectorForm.getDescription());
										ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SECTOR);										
										ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
										logger.debug("Updating.............................................");
										DbUtil.update(ampSector);
										Long schemeId =ampSector.getAmpSecSchemeId().getAmpSecSchemeId();
										Integer schemeID = new Integer(schemeId.intValue());
										editSectorForm.setFormFirstLevelSectors(
												SectorUtil.getSectorLevel1(schemeID));
										editSectorForm.setSecSchemeCode(ampSector.getAmpSecSchemeId().getSecSchemeCode());
										editSectorForm.setSecSchemeName(ampSector.getAmpSecSchemeId().getSecSchemeName());
										logger.debug(" update sector1 Complete");
										return mapping.findForward("editedSecondLevelSector");
								}
								else if(event.equalsIgnoreCase("update3LevelSector"))
								 {
									AmpSector ampSector = SectorUtil.getAmpSector(secId);
									editSectorForm.setSectorId(secId);
									ampSector.setName(editSectorForm.getSectorName());
									ampSector.setDescription(editSectorForm.getDescription());
									ampSector.setSectorCode(AddSector.DEFAULT_VALUE_SUB_SECTOR);
									ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
									logger.debug("Updating.............................................");
									DbUtil.update(ampSector);
									ampSector = SectorUtil.getAmpSector(secId);
									Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
									Integer schemeID = new Integer(sectorId.intValue());
									editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
									editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
									editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
									editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
									editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
									editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
									editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
									logger.debug(" update sector2 Complete");
									return mapping.findForward("editedThirdLevelSector");
								}
					 
							  }
		  		  
						 	}
						 else if(flag.equalsIgnoreCase("true"))
						 	{
							 if(event.equalsIgnoreCase("update3LevelSector"))
							 	{
									AmpSector ampSector = SectorUtil.getAmpSector(secId);
									editSectorForm.setSectorId(secId);
									ampSector.setName(editSectorForm.getSectorName());
									ampSector.setDescription(editSectorForm.getDescription());
									ampSector.setSectorCode(editSectorForm.getSectorCode());
									ampSector.setSectorCodeOfficial(editSectorForm.getSectorCodeOfficial());
									logger.debug("Updating.............................................");
									DbUtil.update(ampSector);
									ampSector = SectorUtil.getAmpSector(secId);
									Long sectorId = ampSector.getParentSectorId().getAmpSectorId();
									Integer schemeID = new Integer(sectorId.intValue());
									editSectorForm.setSubSectors(SectorUtil.getAllChildSectors(sectorId));
									editSectorForm.setSectorCode(ampSector.getParentSectorId().getSectorCode());
									editSectorForm.setSectorCodeOfficial(ampSector.getParentSectorId().getSectorCodeOfficial());
									editSectorForm.setSectorName(ampSector.getParentSectorId().getName());
									editSectorForm.setSecSchemeId(ampSector.getAmpSecSchemeId().getAmpSecSchemeId());
									editSectorForm.setSectorId(ampSector.getParentSectorId().getAmpSectorId());
									editSectorForm.setDescription(ampSector.getParentSectorId().getDescription());
									logger.debug(" update sector3 Complete");
									editSectorForm.setJspFlag(false);
									return mapping.findForward("editedThirdLevelSectorPlusOne");
						 }
						 }
					}
					 return null;
		  }
}

