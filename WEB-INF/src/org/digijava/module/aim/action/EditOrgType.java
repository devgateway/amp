package org.digijava.module.aim.action ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.form.AddOrgTypeForm;
import org.digijava.module.aim.util.DbUtil;


public class EditOrgType extends DispatchAction {

		  private static Logger logger = Logger.getLogger(EditOrgType.class);

		  private boolean sessionChk(HttpServletRequest request) {
			  HttpSession session = request.getSession();
				 if (session.getAttribute("ampAdmin") == null) {
					return true;
				 } 
				 else {
					String str = (String)session.getAttribute("ampAdmin");
					if ("no".equals(str)) {
						return true;
					}
				 }
				 return false;
		  }
		  private void removeSessAttribute (HttpServletRequest request) {
			  if (null != request.getSession().getAttribute("ampOrgType"))
					request.getSession().setAttribute("ampOrgType", null);
		  }
		  
		  public ActionForward create(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 if (sessionChk(request))
						 return mapping.findForward("index");
					 
					 AddOrgTypeForm editForm = (AddOrgTypeForm) form;
					 if (null == editForm.getOrgType())
						 return mapping.findForward("forward");
					 AmpOrgType otype = new AmpOrgType();
					 otype.setOrgType(editForm.getOrgType());
					 otype.setOrgTypeCode(editForm.getOrgTypeCode());
					 otype.setClassification(editForm.getClassification());
					 DbUtil.add(otype);
					 removeSessAttribute(request);
					 editForm.setReset(Boolean.TRUE);
					 return mapping.findForward("added");
				}
		  
		  public ActionForward edit(ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response) throws java.lang.Exception {

			  		if (sessionChk(request))
			  			return mapping.findForward("index");
					 
			  		AddOrgTypeForm editForm = (AddOrgTypeForm) form;
			  		try {
			  			editForm.setAmpOrgTypeId(new Long(Integer.parseInt(request.getParameter("ampOrgTypeId"))));
			  		}
			  		catch (NumberFormatException nex) {
			  			logger.debug("Invalid org type-id in request scope");
			  			return mapping.findForward("added");
			  		}
			  		AmpOrgType otype = DbUtil.getOrgType(editForm.getAmpOrgTypeId());
			  		if (null == otype) {
			  			logger.debug("No Object exists corresponding to ampOrgTypeId in request scope!");
			  			return mapping.findForward("added");
			  		}
			  		if (null == editForm.getOrgType()) {
			  			editForm.setOrgType(otype.getOrgType());
					  	editForm.setOrgTypeCode(otype.getOrgTypeCode());
					  	editForm.setClassification(otype.getClassification());
					  	return mapping.findForward("forward");
			  		}
			  		otype.setOrgType(editForm.getOrgType());
			  		otype.setOrgTypeCode(editForm.getOrgTypeCode());
			  		otype.setClassification(editForm.getClassification());
			  		DbUtil.update(otype);
			  		removeSessAttribute(request);
					editForm.setReset(Boolean.TRUE); 
					return mapping.findForward("added");
		  		}
		  
		  public ActionForward delete(ActionMapping mapping,
					ActionForm form,
					HttpServletRequest request,
					HttpServletResponse response) throws java.lang.Exception {

			  		if (sessionChk(request))
			  			return mapping.findForward("index");
					 
					AddOrgTypeForm editForm = (AddOrgTypeForm) form;
					try {
						editForm.setAmpOrgTypeId(new Long(Integer.parseInt(request.getParameter("ampOrgTypeId"))));
                        editForm.setAction("edit");
				  	}
				  	catch (NumberFormatException nex) {
				  		logger.debug("Invalid org type-id in request scope");
				  		return mapping.findForward("added");
				  	}
				  	if (DbUtil.chkOrgTypeReferneces(editForm.getAmpOrgTypeId())) {
				  		editForm.setDeleteFlag("orgReferences");
				  		return mapping.findForward("forward");
			  		}
			  		else {
			  			AmpOrgType otype = DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId());
			  			if (null != otype) {
			  				DbUtil.delete(otype);
			  				removeSessAttribute(request);
			  			}
			  			else
			  				logger.debug("No Object exists corresponding to ampOrgTypeId in request scope!");
			  			editForm.setReset(Boolean.TRUE);
				  		return mapping.findForward("added");
			  		}
		  }
}