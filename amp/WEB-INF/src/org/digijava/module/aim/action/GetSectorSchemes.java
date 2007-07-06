
package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.helper.Sector;
import javax.servlet.http.*;
import java.util.*;

public class GetSectorSchemes extends Action {

		  private static Logger logger = Logger.getLogger(GetSectorSchemes.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		//session.setAttribute("moreThanLevelOne",null);
		session.setAttribute("Id",null);
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
					 logger.info("came into the sector schemes manager");
					 Collection scheme = null;
					 AddSectorForm sectorsForm = (AddSectorForm) form;
					 logger.info("in the get sector scheme's action");
					 /*
					 scheme = SectorUtil.getSectorSchemes();
					 sectorsForm.setFormSectorSchemes(scheme);
					 */
					 //String event = request.getParameter("event");
					 //String schemeId = (String)request.getParameter("ampSecSchemeId");
					 
					 //logger.info(" this is the event got!!....."+event+"  id is "+schemeId);
					
					 scheme = SectorUtil.getSectorSchemes();
					 sectorsForm.setFormSectorSchemes(scheme);
					 if("true".equals(session.getAttribute("schemeDeletedError")))
					 {
						 	ActionErrors errors = new ActionErrors();
						 	errors.add("title", new ActionError("error.aim.deleteScheme.schemeSelected"));
						 	saveErrors(request, errors);
						 	session.setAttribute("schemeDeletedError",null);
					 }
			
					 return mapping.findForward("viewSectorSchemes");
		  }
}


