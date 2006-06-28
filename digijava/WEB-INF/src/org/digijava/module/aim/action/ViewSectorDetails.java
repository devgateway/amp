package org.digijava.module.aim.action ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.SectorUtil;

public class ViewSectorDetails extends Action {

		  private static Logger logger = Logger.getLogger(ViewSectorDetails.class);

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

					 AddSectorForm viewSectorForm = (AddSectorForm) form;

					 logger.debug("In view sector action");
					 logger.debug("In add sector action");
						String sectId = request.getParameter("ampSectorId");
						String event = request.getParameter("event");
						String level = request.getParameter("level");
						Long parentId = new Long(sectId);
						logger.info(" this is the sector id....." +parentId);
						
						viewSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
						if(event!=null)
						{
							if(event.equals("edit"))
							{
								logger.info(" inside editing the sector...");
								AmpSector editSector = new AmpSector();
								editSector = SectorUtil.getAmpSector(parentId);
								logger.info(" this is the name ....."+editSector.getName());
								logger.info(" this is the code ....."+ editSector.getSectorCode());
								logger.info("this is the sector id...."+editSector.getAmpSectorId());
								viewSectorForm.setSectorCode(editSector.getSectorCode());
								viewSectorForm.setSectorName(editSector.getName());
								viewSectorForm.setSectorId(editSector.getAmpSectorId());
								
								if(level.equals("one"))
								{
									return mapping.findForward("levelOne");
								}
								if(level.equals("two"))
								{
									return mapping.findForward("levelTwo");
								}
								if(level.equals("three"))
								{
									return mapping.findForward("levelThree");
								}
									
							}
							
							
						}
					/* if (request.getParameter("id") != null) {
								Long secId = new Long(Long.parseLong(request.getParameter("id")));
								AmpSector ampSector = SectorUtil.getAmpSector(secId);

								if (ampSector.getParentSectorId() != null) {
										  viewSectorForm.setParentSectorId(ampSector.getParentSectorId().getAmpSectorId());
								} else {
										  viewSectorForm.setParentSectorId(new Long(0));
								}
								viewSectorForm.setSectorId(secId);
								viewSectorForm.setSectorCode(ampSector.getSectorCode());
								viewSectorForm.setSectorName(ampSector.getName());
								viewSectorForm.setAmpOrganisation(ampSector.getAmpOrgId().getName());
								viewSectorForm.setDescription(ampSector.getDescription());
								if (ampSector.getParentSectorId() == null) {
										  viewSectorForm.setParentSectorName("");
								} else {
										  viewSectorForm.setParentSectorName(ampSector.getParentSectorId().getName());
								}
					 }*/
					 return mapping.findForward("levelOne");
		  }
		  
		  
		  
}


