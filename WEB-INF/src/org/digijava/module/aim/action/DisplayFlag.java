package org.digijava.module.aim.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class DisplayFlag extends Action {
	
	private static Logger logger = Logger.getLogger(DisplayFlag.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		if (id != null) {
			try {
				long temp = Long.parseLong(id);
				Long cId = new Long(temp);
				ServletOutputStream os = response.getOutputStream();
				os.write(FeaturesUtil.getFlag(cId));
				os.flush();									
			} catch (NumberFormatException nfe) {
				logger.error("Trying to parse " + id + " to long");
			}
			
		} else {
			ServletContext ampContext = getServlet().getServletContext();
			Boolean defFalgExist = (Boolean) ampContext.getAttribute(Constants.DEF_FLAG_EXIST);
			if (defFalgExist.booleanValue() == true) {
				ServletOutputStream os = response.getOutputStream();
				os.write(FeaturesUtil.getDefaultFlag());
				os.flush();									
			}
		}
		
		return null;
	}
	

}