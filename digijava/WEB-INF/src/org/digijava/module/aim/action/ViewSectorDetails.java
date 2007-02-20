
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
import org.digijava.module.aim.dbentity.AmpSectorScheme;
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
					 session.setAttribute("moreThanLevelOne","yes");
					 String Level = new String();
					 logger.info("In ViewSectorDetails action");
					 String event = request.getParameter("event");
					 String level = request.getParameter("level");
					 logger.info("LEVEL======================"+level);
					 logger.info("event================"+event);
					 if(event==null && level==null){
						 viewSectorForm.setJspFlag(true);
						 Level = "three";
					 }
					 else if(event.equalsIgnoreCase("enough") && level.equalsIgnoreCase("three")){
						 Level="three";
						 event = "edit";
						 viewSectorForm.setJspFlag(true);
					 }
					 String secId = "";
					 if(session.getAttribute("Id")==null)
					    	secId = request.getParameter("ampSectorId");
					 else
					 {
						 	secId = (String)session.getAttribute("Id");
						 	level = "two";
						 	if(event!=null)
						 	if(event.equalsIgnoreCase("addSector"))
								 level = "three";
						 	event = "edit";
						 	
					 }
					 
					 logger.info(request.getParameter("ampSectorId"));
					 logger.info(session.getAttribute("id"));
					 logger.info(secId);
					 if(session.getAttribute("LEVEL")!=null)
					 if(((String)session.getAttribute("LEVEL")).equalsIgnoreCase("1"))
						 level = "one";
					 else if(((String)session.getAttribute("LEVEL")).equalsIgnoreCase("2"))
						 level = "two";
					 else if(((String)session.getAttribute("LEVEL")).equalsIgnoreCase("3"))
						 level = "three";
					 
					 session.setAttribute("LEVEL",null);
						Long parentId = new Long(secId);
						viewSectorForm.setSubSectors(SectorUtil.getAllChildSectors(parentId));
						Collection _subSectors = viewSectorForm.getSubSectors();
						Iterator itr = _subSectors.iterator();
						while (itr.hasNext()) {
							AmpSector ampScheme = (AmpSector) itr.next();
							viewSectorForm.setAmpSectorId(ampScheme.getAmpSectorId());
							viewSectorForm.setParentId(ampScheme.getAmpSectorId());
							viewSectorForm.setParentSectorId(ampScheme.getParentSectorId().getAmpSectorId());
						}
						
						if(event!=null)
						{
							if(event.equals("edit"))
							{
								logger.info(" inside editing the sector     poi   event === edit...");
								AmpSector editSector= new AmpSector();
								editSector = SectorUtil.getAmpSector(parentId);
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
								if(level.equals("three")|| Level.equalsIgnoreCase("three"))
								{
									
									return mapping.findForward("levelThree");
								}
							}							
						}
					
					 return mapping.findForward("levelOne");
		  }
		  
		  
		  
}

