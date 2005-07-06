package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.AddSectorForm;
import javax.servlet.http.*;
import java.util.*;

public class DeleteSector extends Action {

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
					 
					 AddSectorForm deleteSectorForm = (AddSectorForm) form;

					 logger.debug("In delete sector action");

					 if (request.getParameter("id") != null) {

								/*
								 * check whether the id is a valid long value
								 */
								Long secId = new Long(Long.parseLong(request.getParameter("id")));

								AmpSector ampSector = DbUtil.getAmpSector(secId);

								/*
								 * check whether ampsector is null if yes return error.
								 */
								
								deleteSectorForm.setSectorId(secId);
								deleteSectorForm.setSectorCode(ampSector.getSectorCode());
								deleteSectorForm.setSectorName(ampSector.getName());
								deleteSectorForm.setAmpOrganisation(ampSector.getAmpOrgId().getName());
								deleteSectorForm.setDescription(ampSector.getDescription());

								Iterator itr = DbUtil.getSubSectors(secId).iterator();
								Iterator actItr = DbUtil.getSectorActivities(secId).iterator();
								
								if (itr.hasNext()) {
										  deleteSectorForm.setFlag("subSectorExist");
								} else if (actItr.hasNext()) {
										  deleteSectorForm.setFlag("activityExist");
								} else {
										  deleteSectorForm.setFlag("delete");
								}
					 }
					 else if (request.getParameter("id") == null && deleteSectorForm.getSectorId() != null) {
								
								logger.debug("deleting the sector");
								AmpSector ampSector = DbUtil.getAmpSector(deleteSectorForm.getSectorId());
								DbUtil.delete(ampSector);
								logger.debug("Sector deleted");
								return mapping.findForward("deleted");
					 } else {
								logger.debug("No action selected");
					 }
					
					 
					 return mapping.findForward("forward");
		  }
		  
		  
		  
}


