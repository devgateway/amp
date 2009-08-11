
package org.digijava.module.aim.action ;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.DbUtil;

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
		String action = request.getParameter("action");
		if(action!=null && action.equals("update")){
			String secUpdate[]=request.getParameterValues("secSchemeShowName");			
			Collection<AmpSectorScheme> schemes = SectorUtil.getSectorSchemes();
			for(AmpSectorScheme mysch:schemes){
				boolean showName = mysch.getSecSchemeShowName()==null?false:mysch.getSecSchemeShowName();
				if(showName){
					if(!isInList(secUpdate, mysch.getAmpSecSchemeId())){
						mysch.setSecSchemeShowName(false);
						DbUtil.update(mysch);
					}
				}
				else{
					if(isInList(secUpdate, mysch.getAmpSecSchemeId())){
						mysch.setSecSchemeShowName(true);
						DbUtil.update(mysch);
					}
					
				}
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
					 
					 ActionErrors errors = (ActionErrors) session.getAttribute("managingSchemes");
					 if(errors != null && errors.size() > 0)
					 {
						 saveErrors(request, errors);
						 session.setAttribute("managingSchemes",null);
	        		 }
					 
					 return mapping.findForward("viewSectorSchemes");
		  }

		private boolean isInList(String[] secUpdate, Long ampSecSchemeId) {
			if(secUpdate!=null){
				for(String st : secUpdate){
					if(Long.parseLong(st)==ampSecSchemeId)
						return true;
				}
			}
			return false;
		}
}


