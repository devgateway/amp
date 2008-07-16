/***
 * GetDocumentDetails.java
 * @author Priyajith 
 */
  
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.RelatedLinksForm;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

/***
 * Fetch the Document and activity details provided the document Id and activity Id 
 */

public class GetDocumentDetails
extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("currentMember") == null) // User not logged in
			return mapping.findForward("index");
		
		if (request.getParameter("docId") == null || 
				request.getParameter("actId") == null) // invalid url parameters specified 
			return mapping.findForward("index");
		
		Long dId = null;
		Long aId = null;
		int pageId = 0;
		try {
			String docId = request.getParameter("docId");
			dId = new Long(Long.parseLong(docId));
			String actId = request.getParameter("actId");
			aId = new Long(Long.parseLong(actId));
			pageId = Integer.parseInt(request.getParameter("pageId"));
		} catch (NumberFormatException ex) {
			return mapping.findForward("index");
		}
		
		RelatedLinksForm rlForm = (RelatedLinksForm) form;
		
		if (dId != null) {
			Documents document = new Documents();
			CMSContentItem cmsItem = DbUtil.getCMSContentItem(dId);
			AmpActivity activity = ActivityUtil.getProjectChannelOverview(aId);
			document.setActivityId(activity.getAmpActivityId());
			document.setActivityName(activity.getName());
			document.setTitle(cmsItem.getTitle());
			document.setDocDescription(cmsItem.getDescription());
			document.setDate(cmsItem.getDate());
			document.setDocId(new Long(cmsItem.getId()));
			document.setIsFile(cmsItem.getIsFile());
			document.setFileName(cmsItem.getFileName());
			document.setUrl(cmsItem.getUrl());
			rlForm.setDocument(document);
		}

		rlForm.setPageId(pageId);	  
		if (pageId == 0) {
			return mapping.findForward("forward1");
		} else {
			return mapping.findForward("forward2");	
		}
	}
}
