
package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.SectorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

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

