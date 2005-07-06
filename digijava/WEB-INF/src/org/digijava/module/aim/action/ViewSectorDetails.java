package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.AddSectorForm;
import javax.servlet.http.*;

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

					 if (request.getParameter("id") != null) {
								Long secId = new Long(Long.parseLong(request.getParameter("id")));
								AmpSector ampSector = DbUtil.getAmpSector(secId);

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
					 }
					 return mapping.findForward("forward");
		  }
		  
		  
		  
}


