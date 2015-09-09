package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.form.AddOrgGroupForm;
import org.digijava.module.aim.util.DbUtil;

public class EditOrgGroup extends Action {

	private static Logger logger = Logger.getLogger(EditOrgGroup.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}

		AddOrgGroupForm editForm = (AddOrgGroupForm) form;
		String action = request.getParameter("action");
		editForm.setAction(action);
		Collection col = DbUtil.getAllOrgTypes();
		editForm.setOrgTypeColl(col);

		if (action.indexOf("create") > -1) {
			editForm.setAction("save");

			if (editForm.getAmpOrgId() != null)
				return mapping.findForward("popup");
			else
				return mapping.findForward("forward");

		}

		if ("edit".equals(action)) {
			editForm.setAmpOrgGrpId(new Long(Integer.parseInt(request.getParameter("ampOrgGrpId"))));
			AmpOrgGroup ampGrp = DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId());
			if (session.getAttribute("ampOrgGrp") != null) {
				session.removeAttribute("ampOrgGrp");
			}
			editForm.setOrgGrpName(ampGrp.getOrgGrpName());
			editForm.setOrgGrpCode(ampGrp.getOrgGrpCode());
			editForm.setOrgTypeId(ampGrp.getOrgType().getAmpOrgTypeId());
			editForm.setAction("save");
			editForm.setFlag("delete");
			return mapping.findForward("forward");
		}

		if ("save".equals(action)) {

			if (session.getAttribute("ampOrgGrp") != null) {
				session.removeAttribute("ampOrgGrp");
			}
			AmpOrgGroup ampGrp = new AmpOrgGroup();
			ampGrp.setOrgGrpName(editForm.getOrgGrpName());
			ampGrp.setOrgGrpCode(editForm.getOrgGrpCode());
			AmpOrgType ot = DbUtil.getAmpOrgType(editForm.getOrgTypeId());
			ampGrp.setOrgType(ot);
			ARUtil.clearOrgGroupTypeDimensions();
			
			if (DbUtil.checkAmpOrgGroupDuplication(ampGrp.getOrgGrpName(), editForm.getAmpOrgGrpId())) {
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationGroupManager.duplicateGroupName"));
				saveErrors(request, errors);
				//if(editForm.getAmpOrgGrpId()!=null && editForm.getAmpOrgGrpId().longValue()!=0) editForm.setAction("edit"); 
				//else editForm.setAction("create");
				if (editForm.getAmpOrgId() != null) return mapping.findForward("popup");
				else return mapping.findForward("forward");
			}
			

			if ((editForm.getAmpOrgGrpId().longValue() ==0)||(editForm.getAmpOrgGrpId().longValue() ==-1)) {
				DbUtil.add(ampGrp);
				if (editForm.getAmpOrgId() != null) {
					editForm.setFlag("refreshParent");
					return mapping.findForward("popup");
					
				} else {
					return mapping.findForward("added");
				}
			} else {
				ampGrp.setAmpOrgGrpId(editForm.getAmpOrgGrpId());
				DbUtil.update(ampGrp);
				if (editForm.getAmpOrgId() != null) {
					editForm.setFlag("refreshParent");
					return mapping.findForward("popup");
				} else {
					return mapping.findForward("added");
				}
			}

		}

		if ("delete".equals(action)) {

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
					ARUtil.clearOrgGroupTypeDimensions();
					DbUtil.deleteUserExt(ampGrp, null, null);
					/*AMP-21078 : replacing hard delete with soft delete*/
//					DbUtil.delete(ampGrp);
					ampGrp.setDeleted(true);
					logger.debug("Organization Group deleted");
				}
				return mapping.findForward("added");
			}
		}

		return mapping.findForward("index");
	}
}