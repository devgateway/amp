
package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
					 //session.setAttribute("moreThanLevelOne","yes");
					 //String Level = new String();
					 logger.debug("In ViewSectorDetails action");
					 String event = request.getParameter("event");
					 String level = request.getParameter("level");
					 String secId = request.getParameter("ampSectorId");
					 if(secId==null)
						secId = (String)session.getAttribute("Id");
					 /*
					 logger.info("LEVEL======================"+level);
					 logger.info("event================"+event);
					
					 logger.debug(request.getParameter("ampSectorId"));
					 logger.debug(session.getAttribute("Id"));
					 logger.debug(secId);
					 */
					 	String sortByColumn = request.getParameter("sortByColumn");
						Long parentId = new Long(secId);

						Collection<AmpSector> sectors = SectorUtil.getAllChildSectors(parentId);
						if(sortByColumn==null || sortByColumn.compareTo("sectorCode")==0){
							List<AmpSector> sec = new ArrayList<AmpSector>(sectors);
							Collections.sort(sec, new Comparator<AmpSector>(){
								public int compare(AmpSector a1, AmpSector a2) {
									String s1	= a1.getSectorCodeOfficial();
									String s2	= a2.getSectorCodeOfficial();
									if ( s1 == null )
										s1	= "";
									if ( s2 == null )
										s2	= "";
								
									return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
								}
							});
						
							viewSectorForm.setSubSectors(sec);
						}
						else if(sortByColumn!=null && sortByColumn.compareTo("sectorName")==0){
							viewSectorForm.setSubSectors(sectors);
						}
						
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
								logger.debug("Inside view sector details--------------");
								AmpSector editSector= new AmpSector();
								editSector = SectorUtil.getAmpSector(parentId);
								viewSectorForm.setSectorCode(editSector.getSectorCode());
								viewSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
								viewSectorForm.setSectorName(editSector.getName());
								viewSectorForm.setDescription(editSector.getDescription());
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
							else if (event.equalsIgnoreCase("enough")){
								AmpSector editSector= new AmpSector();
								editSector = SectorUtil.getAmpSector(parentId);
								viewSectorForm.setSectorCode(editSector.getSectorCode());
								viewSectorForm.setSectorCodeOfficial(editSector.getSectorCodeOfficial());
								viewSectorForm.setSectorName(editSector.getName());
								viewSectorForm.setDescription(editSector.getDescription());
								viewSectorForm.setSectorId(editSector.getAmpSectorId());
								logger.debug("Setting jsp Flag==================(true)================");
								viewSectorForm.setJspFlag(true);
								if(level.equals("three")){
									return mapping.findForward("levelEnough");	
								}								
								return mapping.findForward("levelThree");
							}
						}
					
					 return mapping.findForward("levelOne");
		  }
		  
		  
		  
}

