package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.form.AddSectorForm;
import javax.servlet.http.*;
import java.util.*;

public class DeleteSector extends Action {

  private static Logger logger = Logger.getLogger(GetSectors.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    HttpSession session = request.getSession();
    if (session.getAttribute("ampAdmin") == null) {
      return mapping.findForward("index");
    }
    else {
      String str = (String) session.getAttribute("ampAdmin");
      if (str.equals("no")) {
        return mapping.findForward("index");
      }
    }

    /*
     * modyfed by Xaxan
 */
    AddSectorForm deleteSectorForm = (AddSectorForm) form;
    String event = request.getParameter("event");
    String forward=null;
    Long id = null;
    String schemeId = request.getParameter("schemeId");
    AmpSector aSector = new AmpSector();
    aSector = SectorUtil.getAmpSector(deleteSectorForm.getAmpSectorId());
    if (event.equals("delete")) {
      id = deleteSectorForm.getAmpSectorId();
      if(SectorUtil.getAllChildSectors(aSector.getAmpSectorId()).isEmpty()){
    	  logger.info("Sector dont have any child sector:");
    	  session.setAttribute("Id",schemeId);
    	  session.setAttribute("Event","Edit");
    	  session.setAttribute("resetId","no");
    	  SectorUtil.deleteSector(id);
    	  forward="sectorDeleted";
      }

    else {
    	ActionErrors errors = new ActionErrors();
		errors.add("title", new ActionError("error.aim.deleteScheme.schemeSelected"));
		saveErrors(request, errors);
    	forward="cantDelete";
    }
    }
    return mapping.findForward(forward);
   }
}
