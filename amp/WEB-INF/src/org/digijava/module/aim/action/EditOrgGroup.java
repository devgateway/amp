package org.digijava.module.aim.action ;

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
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.form.AddOrgGroupForm;
import org.digijava.module.aim.util.DbUtil;


public class EditOrgGroup extends Action {

		  private static Logger logger = Logger.getLogger(EditOrgGroup.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {

					 HttpSession session = request.getSession();
					 if (session.getAttribute("ampAdmin") == null) {
						return mapping.findForward("index");
					 } 
					 else {
							String str = (String)session.getAttribute("ampAdmin");
							if (str.equals("no")) {
								return mapping.findForward("index");
							}
					 }
					 
					 logger.debug("In edit organization group");

					 AddOrgGroupForm editForm = (AddOrgGroupForm) form;
					 String action = request.getParameter("action");
					 logger.debug("action : " + action);
					 editForm.setAction(action);
					 Collection col = DbUtil.getAllOrgTypes();
					 editForm.setOrgTypeColl(col);
					 //Collection col = DbUtil.getAllLevels();
					 //editForm.setLevel(col);
					 
					 if ("create".equals(action) || ("createGroup".equals(action))) {
					 	if (("createGroup".equals(action))) {
					 		if (request.getParameter("ampOrgId") == null 
					 				|| request.getParameter("ampOrgId").trim().length() < 1 
					 				|| "0".equals(request.getParameter("ampOrgId")))
					 			editForm.setAmpOrgId(null);
					 		else
					 			editForm.setAmpOrgId(new Long(Integer.parseInt(request.getParameter("ampOrgId"))));
					 	}
					 	if (editForm.getOrgGrpName() == null) {
						 	logger.debug("Inside IF [CREATE]");
						 	if (("create".equals(action)))
						 		return mapping.findForward("forward");
						 	else 
						 		return mapping.findForward("popup");
						 }
						 else {
						 	if (session.getAttribute("ampOrgGrp") != null) {
								session.removeAttribute("ampOrgGrp");
							}
							
							AmpOrgGroup ampGrp = new AmpOrgGroup();
							ampGrp.setOrgGrpName(editForm.getOrgGrpName());
							ampGrp.setOrgGrpCode(editForm.getOrgGrpCode());
							/*
							if (!editForm.getLevelId().equals(new Long(-1))) {
								AmpLevel al = DbUtil.getAmpLevel(editForm.getLevelId());
								ampGrp.setLevelId(al);
							}
							else
								ampGrp.setLevelId(null); */
							if (null == editForm.getOrgTypeId() || editForm.getOrgTypeId().intValue() < 1) {
								ampGrp.setOrgType(null);
							}
							else {
								AmpOrgType ot = DbUtil.getOrgType(editForm.getOrgTypeId());
								ampGrp.setOrgType(ot);
							}
							
							DbUtil.add(ampGrp);
							logger.debug("Group added");
							
							logger.debug("action : " + action);
							logger.debug("editForm.getAmpOrgId() : " + editForm.getAmpOrgId());
							
							if (("create".equals(action)))
								return mapping.findForward("added");
							else {
								if (editForm.getAmpOrgId() != null)
									return mapping.findForward("groupedited");
								else
									return mapping.findForward("groupadded");
							}
						 }
				 
						} else if ("edit".equals(action)){
							editForm.setAmpOrgGrpId(new Long(Integer.parseInt(request.getParameter("ampOrgGrpId"))));
							AmpOrgGroup ampGrp = DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId());
							if (ampGrp == null) {
								if (session.getAttribute("ampOrgGrp") != null) {
									session.removeAttribute("ampOrgGrp");
								}
								return mapping.findForward("added");
							}
							editForm.setFlag("delete");
							
							if (editForm.getOrgGrpName() == null) {
								logger.debug("Inside IF [EDIT]");
								
								if (ampGrp.getOrgGrpName() != null)
									editForm.setOrgGrpName(ampGrp.getOrgGrpName());
								else
									editForm.setOrgGrpName("");
								if (ampGrp.getOrgGrpCode() != null)
									editForm.setOrgGrpCode(ampGrp.getOrgGrpCode());
								else
									editForm.setOrgGrpCode("");
								if (ampGrp.getOrgType() != null)
									editForm.setOrgTypeId(ampGrp.getOrgType().getAmpOrgTypeId());
								else
									editForm.setOrgTypeId(new Long(-1));
								/*
								if (ampGrp.getLevelId() != null)
									editForm.setLevelId(ampGrp.getLevelId().getAmpLevelId());
								else
									editForm.setLevelId(new Long(-1));
								*/
								return mapping.findForward("forward");
							 }
							 else {
							 	if (session.getAttribute("ampOrgGrp") != null) {
									session.removeAttribute("ampOrgGrp");
								}
								
								ampGrp.setOrgGrpName(editForm.getOrgGrpName());
								ampGrp.setOrgGrpCode(editForm.getOrgGrpCode());
								if (null != editForm.getOrgTypeId() && editForm.getOrgTypeId().intValue() < 1)
									ampGrp.setOrgType(null);
								else {
									AmpOrgType ot = DbUtil.getOrgType(editForm.getOrgTypeId());
									ampGrp.setOrgType(ot);
								}
								/*
								if (editForm.getLevelId().equals(new Long(-1)))
									ampGrp.setLevelId(null);
								else {
									AmpLevel al = DbUtil.getAmpLevel(editForm.getLevelId());
									ampGrp.setLevelId(al);
								} */
								
								DbUtil.update(ampGrp);
								logger.debug("Organization Group updated");
								return mapping.findForward("added");
							 }
					    } else if ("delete".equals(action)){
					    	
					    	Iterator itr1 = DbUtil.getOrgByGroup(editForm.getAmpOrgGrpId()).iterator();
					    	
					    	if (itr1.hasNext()) {
					    		editForm.setFlag("orgReferences");
								editForm.setAction("edit");
								return mapping.findForward("forward");
							} else {
								if (session.getAttribute("ampOrgGrp") != null) {
									session.removeAttribute("ampOrgGrp");
								}
						    	
						    	AmpOrgGroup ampGrp = DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId());
								if (ampGrp != null) {
									DbUtil.delete(ampGrp);
									logger.debug("Organization Group deleted");
								}
						    	return mapping.findForward("added");
							}
					    }
						
					 return mapping.findForward("index");
				}
}