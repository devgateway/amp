package org.digijava.module.aim.action ;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpLevel;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.AddOrgGroupForm;
import javax.servlet.http.*;
import java.util.Collection;


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
					 Collection col = DbUtil.getAllLevels();
					 editForm.setLevel(col);
					 
					 if ("create".equals(action) || ("createGroup".equals(action))) {
					 	if (("createGroup".equals(action))) {
					 		if (request.getParameter("ampOrgId") != null && request.getParameter("ampOrgId").trim().length() != 0)
					 			editForm.setAmpOrgId(new Long(Integer.parseInt(request.getParameter("ampOrgId"))));
					 		else
					 			editForm.setAmpOrgId(null);
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
							if (!editForm.getLevelId().equals(new Long(-1))) {
								AmpLevel al = DbUtil.getAmpLevel(editForm.getLevelId());
								ampGrp.setLevelId(al);
							}
							else
								ampGrp.setLevelId(null);
							
							DbUtil.add(ampGrp);
							logger.debug("Group added");
							
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
								if (ampGrp.getLevelId() != null)
									editForm.setLevelId(ampGrp.getLevelId().getAmpLevelId());
								else
									editForm.setLevelId(new Long(-1));
								
								return mapping.findForward("forward");
							 }
							 else {
							 	if (session.getAttribute("ampOrgGrp") != null) {
									session.removeAttribute("ampOrgGrp");
								}
								
								ampGrp.setOrgGrpName(editForm.getOrgGrpName());
								ampGrp.setOrgGrpCode(editForm.getOrgGrpCode());
								if (editForm.getLevelId().equals(new Long(-1)))
									ampGrp.setLevelId(null);
								else {
									AmpLevel al = DbUtil.getAmpLevel(editForm.getLevelId());
									ampGrp.setLevelId(al);
								}
								
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