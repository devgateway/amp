/***
 * DeleteDocument.java 
 */

package org.digijava.module.aim.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

public class DeleteDocument 
extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CURRENT_MEMBER) == null) 
			return mapping.findForward("index");
		
		RelatedLinksForm rlForm = (RelatedLinksForm) form;
		Long actId = rlForm.getActivityId();
		Long docId = rlForm.getDocId();
		
		if (actId == null || docId == null) return mapping.findForward("index");
	
		AmpActivity activity = ActivityUtil.getProjectChannelOverview(actId);
		Set docList = new HashSet();
		if (activity != null) {
			Iterator itr = DbUtil.getActivityDocuments(actId).iterator();
			boolean found = false;
			while (itr.hasNext()) {
				CMSContentItem cmsItem = (CMSContentItem) itr.next();
				if (found) docList.add(cmsItem);
				else if (docId.equals(new Long(cmsItem.getId()))) found = true;
				else docList.add(cmsItem);
			}
			
			rlForm.setAllDocuments(null);
			ActivityUtil.updateActivityDocuments(activity.getAmpActivityId(), docList);
		}
		return mapping.findForward("forward");
	}
}
  
