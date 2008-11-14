
package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;

public class UpdateSectorSchemes extends Action {

	private static Logger logger = Logger.getLogger(UpdateSectorSchemes.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
		logger.debug("Comes into the  UpdateSectorSchemes action");
		AddSectorForm sectorsForm = (AddSectorForm) form;
		//sectorsForm.setDeleteScheme("");
		String event = request.getParameter("event");
		String sId = request.getParameter("ampSecSchemeId");
		Integer id = new Integer(0);
		if(sId!=null)
	    id = new Integer(sId);
		/*Collection scheme = null;
		
		
		
		scheme = SectorUtil.getSectorSchemes();
		//String Sevent = (String)session.getAttribute("Event");
		boolean case1,case2,case3,case4;
		case1=case2=case3=case4=false;
		if (event!=null)
			if(event.equalsIgnoreCase("Edit") && Sevent==null)
				case1=true;
		
		 if(Sevent!=null)
				if(Sevent.equalsIgnoreCase("Edit")&& event==null)
					case2=true;
		 
		 if(Sevent!=null)
			 if(Sevent.equalsIgnoreCase("Edit"))
				 if(event!=null)
					 if(event.equalsIgnoreCase("Edit"))
						 case4 = true;
		 if(event!=null)
			 if(event.equalsIgnoreCase("Delete") && Sevent!=null)
				 case3 = true;	
		 String resetId = (String)session.getAttribute("resetId");
		 Integer id = new Integer(0);
		 
		if (case1 || case2 || case4 || case3)
			
			{
				
				boolean flag = false;
				if(resetId!=null)
					if(resetId.equalsIgnoreCase("no"))
						flag = true;
						
				 if((session.getAttribute("Id"))==null && request.getParameter("ampSecSchemeId")!=null ){
				 id = new Integer(request.getParameter("ampSecSchemeId"));
				 session.setAttribute("moreThanLevelOne",null);
				}
				 if (session.getAttribute("Id")!=null){
				 id = new Integer((String)session.getAttribute("Id"));
				 if(session.getAttribute("moreThanLevelOne")!=null)
				 return mapping.findForward("toViewSectorDetails");
				}
				if(flag){
					if(request.getParameter("ampSecSchemeId")!=null)
					id = new Integer(request.getParameter("ampSecSchemeId"));
					session.setAttribute("Id",null);
				}
				else
				{
					if(request.getParameter("ampSecSchemeId")!=null)
						id = new Integer(request.getParameter("ampSecSchemeId"));
						session.setAttribute("Id",null);
				}
				*/
				logger.info("FinalID=============================="+id);
				if(event!=null){
					if(event.equalsIgnoreCase("edit")){
				Collection schemeGot = SectorUtil.getEditScheme(id);
				sectorsForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(id));
				Iterator itr = schemeGot.iterator();
				while (itr.hasNext()) {
					AmpSectorScheme ampScheme = (AmpSectorScheme) itr.next();
					sectorsForm.setSecSchemeId(ampScheme.getAmpSecSchemeId());
					sectorsForm.setSecSchemeName(ampScheme.getSecSchemeName());
					sectorsForm.setSecSchemeCode(ampScheme.getSecSchemeCode());
					sectorsForm.setParentId(ampScheme.getAmpSecSchemeId());
				}

				//session.setAttribute("Id",null);
				return mapping.findForward("viewSectorSchemeLevel1");
			}
			
		else if (event.equals("addscheme")) {
				logger.debug("now add a new  scheme");
				return mapping.findForward("addSectorScheme");
			}
		else if (event.equals("saveScheme")) {
				logger.debug("saving the scheme");
				AmpSectorScheme ampscheme = new AmpSectorScheme();
				logger
						.debug(" the name is...."
								+ sectorsForm.getSecSchemeName());
				logger.debug(" the code is ...."
						+ sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				DbUtil.add(ampscheme);
				request.setAttribute("event", "view");

				logger.debug("done kutte");
				//scheme = SectorUtil.getSectorSchemes();
				//sectorsForm.setFormSectorSchemes(scheme);
				return mapping.findForward("viewSectorSchemes");
			}
		else if (event.equals("updateScheme")) {
				logger.debug(" updating Scheme");
				String editId = (String) request.getParameter("editSchemeId");
				AmpSectorScheme ampscheme = new AmpSectorScheme();

				Long Id = new Long(editId);
				logger.debug(" the name is...."	+ sectorsForm.getSecSchemeName());
				logger.debug(" the code is ...."+ sectorsForm.getSecSchemeCode());
				logger.debug(" this is the id......" + Id);
				ampscheme.setSecSchemeCode(sectorsForm.getSecSchemeCode());
				ampscheme.setSecSchemeName(sectorsForm.getSecSchemeName());
				ampscheme.setAmpSecSchemeId(Id);
				DbUtil.update(ampscheme);
				logger.debug(" updated!!");
				return mapping.findForward("viewSectorSchemes");
			}
		else if(event.equalsIgnoreCase("deleteScheme"))
		{
			logger.debug("in the delete Scheme");
			Long id1 = new Long(request.getParameter("ampSecSchemeId"));
			Integer a = new Integer(request.getParameter("ampSecSchemeId"));
			sectorsForm.setFormFirstLevelSectors(SectorUtil.getSectorLevel1(a));
			if (sectorsForm.getFormFirstLevelSectors().size() >= 1) {
				logger.debug("no deletion");
				//ActionErrors errors = new ActionErrors();
				//errors.add("title", new ActionError("error.aim.deleteScheme.schemeSelected"));
				//saveErrors(request, errors);
				session.setAttribute("schemeDeletedError", "true");
				return mapping.findForward("viewSectorSchemes");
			}
			else
				SectorUtil.deleteScheme(id1);
				return mapping.findForward("viewSectorSchemes");
				
		}
}
		

		//scheme = SectorUtil.getSectorSchemes();
		//sectorsForm.setFormSectorSchemes(scheme);

		return mapping.findForward("viewSectorSchemes");
	}
}


