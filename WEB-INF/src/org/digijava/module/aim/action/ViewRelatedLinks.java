/***
 * ViewRelatedLinks.java
 * @author Priyajith 
 */

package org.digijava.module.aim.action;
import java.util.ArrayList;
import java.util.Collection;
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
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class ViewRelatedLinks extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		RelatedLinksForm rlForm = (RelatedLinksForm) form;
		
		boolean permitted = false;
		HttpSession session = request.getSession();
		
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				TeamMember tmem = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
				if (tmem != null && tmem.getTeamId() != null) {
					permitted = true;
				}
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("index");
		}

		int page = 0;
		int numRecords = Constants.NUM_RECORDS;
		Long teamId = null;

		if (session.getAttribute("currentMember") != null) {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			teamId = tm.getTeamId();
			numRecords = tm.getAppSettings().getDefRecsPerPage();
		}
		
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;
		}
		
		Collection pagedCol = null;

		if (pagedCol == null || pagedCol.size() == 0) {
			pagedCol = new ArrayList();
			
			Collection collectionActivities = TeamUtil.getAllTeamAmpActivities(teamId,true);
			Iterator activitiesIterator = collectionActivities.iterator();
			while(activitiesIterator.hasNext())
			{
				AmpActivity currentActivity = (AmpActivity)activitiesIterator.next();
		        /* Injecting documents into session */
		        SelectDocumentDM.clearContentRepositoryHashMap(request);
		        if (currentActivity.getActivityDocuments() != null && currentActivity.getActivityDocuments().size() > 0 )
		        		ActivityDocumentsUtil.injectActivityDocuments(request, currentActivity.getActivityDocuments());

		        Collection docCollection = DocumentManagerUtil.createDocumentDataCollectionFromSession(request);
		        if(docCollection != null )
		        {
		        	Iterator docIterator = docCollection.iterator();
			        while(docIterator.hasNext()){
			        	 org.digijava.module.contentrepository.helper.DocumentData documentData = (org.digijava.module.contentrepository.helper.DocumentData)docIterator.next();
			        	 Documents document = new Documents();
			        	 document.setTitle(documentData.getTitle());
			        	 document.setUuid(documentData.getUuid());
			        	 document.setFile((documentData.getWebLink() == null)?true:false);
			        	 document.setFileName(documentData.getName());
			        	 document.setActivityName(currentActivity.getName());
			        	 document.setActivityId(currentActivity.getAmpActivityId());
			        	 document.setUrl(documentData.getWebLink());
			        	 pagedCol.add(document);
			        }
		        }
			}
			rlForm.setAllDocuments(pagedCol);
		}
	
		if (request.getParameter("removeFields") != null){
			ArrayList list = new ArrayList();
			list.addAll(pagedCol);
			for(int i=0;i<rlForm.getDeleteLinks().length;i++) {
				int n = Integer.parseInt(rlForm.getDeleteLinks()[i]);
				Documents doc = (Documents)list.get(n);
				
				Long actId = doc.getActivityId();
				Long docId = doc.getDocId();
				AmpActivity activity = ActivityUtil.getProjectChannelOverview(actId);
				Set docList = new HashSet();
				if (activity != null) {
					Iterator itr = DbUtil.getActivityDocuments(actId).iterator();
					boolean found = false;
					while (itr.hasNext()) {
						CMSContentItem cmsItem = (CMSContentItem) itr.next();
						if (found)
							docList.add(cmsItem);
						else 
							if (docId.equals(new Long(cmsItem.getId()))){
								found = true;
								//we've found it so we don't add it to the new set
							}
							else 
								docList.add(cmsItem);
					}
					

					ActivityUtil.updateActivityDocuments(activity.getAmpActivityId(), docList);
				}
			}
			pagedCol = new ArrayList();
			pagedCol =DbUtil.getAllDocuments(teamId);
			rlForm.setAllDocuments(pagedCol);
		}

		rlForm.setRelatedLinks(pagedCol);
		return mapping.findForward("forward");
	}
}
 
