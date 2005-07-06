package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.SectorsForm;
import org.digijava.module.aim.helper.Sector;
import javax.servlet.http.*;
import java.util.*;

public class GetSectors extends Action {

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
					 
					 Collection sectors = null;
					 SectorsForm sectorsForm = (SectorsForm) form;
					
					 logger.debug("In get sector action using info");					
					 logger.debug("In get sector action using debug");
					 if (request.getParameter("sectorId") == null || request.getParameter("sectorId").equals("0")) {
								// access the top level sectors
								sectors = DbUtil.getSubSectors(new Long(0));
								sectorsForm.setParentSector(null);
								sectorsForm.setPrevViewedSectorId(null);
								
					 } else {
								// access the sub sectors of the sector with id 'sectorId'
								Long parentSecId = new Long(Long.parseLong(request.getParameter("sectorId")));
								sectors = DbUtil.getSubSectors(parentSecId);

								Sector parentSector = DbUtil.getSector(parentSecId);
								sectorsForm.setParentSectorId(parentSecId);
								sectorsForm.setParentSector(parentSector.getSectorName());
								
								AmpSector ampSector = DbUtil.getAmpSector(parentSecId);
								if (ampSector == null || ampSector.getParentSectorId() == null) {
										  sectorsForm.setPrevViewedSectorId("0");
								} else {
										  sectorsForm.setPrevViewedSectorId("" + ampSector.getParentSectorId().getAmpSectorId());
								}
										  
					 }
					 sectorsForm.setSectors(sectors);

					 return mapping.findForward("forward");
		  }
}
