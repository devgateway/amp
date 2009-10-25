/**
 * XmlPatchesAction.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatch;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
public class XmlPatchesAction extends MultiAction {

	/**
	 * 
	 */
	public XmlPatchesAction() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return modeSelect(mapping,form,request,response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String mode=request.getParameter("mode");
		if("listPatches".equals(mode)) return modePatchList(mapping,form,request,response);
		if("listPatchLogs".equals(mode)) return modePatchLogs(mapping,form,request,response);
		return null;
	}

	
	public ActionForward modePatchLogs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String patchId=request.getParameter("patchId");
	
		AmpXmlPatch patch=(AmpXmlPatch) DbUtil.get(AmpXmlPatch.class, patchId);
		request.setAttribute("patchLogs", patch.getLogs());
		
		return mapping.findForward("listPatchLogs");
	}
	
	public ActionForward modePatchList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	

		return mapping.findForward("listPatches");
	}

}
