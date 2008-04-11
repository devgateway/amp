package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import javax.servlet.http.*;

import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.form.ComponentsForm;


public class GetComponents extends Action{
		private static Logger logger = Logger.getLogger(GetSectorSchemes.class);
	
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
							logger.info("came into the components manager");
							List<AmpComponent> com = new ArrayList<AmpComponent>(ComponentsUtil.getAmpComponents());
							ComponentsForm compForm = (ComponentsForm) form;
							Collections.sort(com);
							compForm.setComponents(com);
		return mapping.findForward("default");
	  }
	
}