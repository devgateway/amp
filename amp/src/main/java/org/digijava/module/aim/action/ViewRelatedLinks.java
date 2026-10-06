/***
 * ViewRelatedLinks.java
 * @author Priyajith 
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.form.RelatedLinksForm;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

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
        
        List<Documents> pagedCol = new ArrayList<>();
        
        {   
            //TeamUtil.getAllTeamAmpActivitiesResume(teamId,true,null, "ampActivityId", "name");
            Map<Long, Object[]> collectionActivities = TeamUtil.getAllTeamAmpActivitiesResume(teamId, true, null, "ampActivityId", "name");
            Map<Long, List<AmpActivityDocument>> documentsByAmpActivityId = TeamUtil.getDocumentsByActivityIds(collectionActivities.keySet());
            
            for(Long ampActivityId:documentsByAmpActivityId.keySet())
            {
                String activityName = (String) collectionActivities.get(ampActivityId)[1];
                List<AmpActivityDocument> activityDocuments = documentsByAmpActivityId.get(ampActivityId);
                
                /* Injecting documents into session */
                SelectDocumentDM.clearContentRepositoryHashMap(request);
                if (activityDocuments != null && activityDocuments.size() > 0 )
                        ActivityDocumentsUtil.injectActivityDocuments(request, new HashSet<AmpActivityDocument>(activityDocuments));

                Collection<DocumentData> docCollection = DocumentManagerUtil.createDocumentDataCollectionFromSession(request);
                if(docCollection != null )
                {
                    for (DocumentData documentData : docCollection) {
                        Documents document = new Documents();
                        document.setTitle(documentData.getTitle());
                        document.setUuid(documentData.getUuid());
                        document.setFile((documentData.getWebLink() == null) ? true : false);
                        document.setFileName(documentData.getName());
                        document.setActivityName(activityName);
                        document.setActivityId(ampActivityId);
                        document.setUrl(documentData.getWebLink());
                        pagedCol.add(document);
                    }
                }
                DocumentManagerUtil.logoutJcrSessions(request);
            }
            rlForm.setAllDocuments(pagedCol);
        }
    
        if (request.getParameter("removeFields") != null){
            throw new RuntimeException("not implemented!");
//          ArrayList list = new ArrayList();
//          list.addAll(pagedCol);
//          for(int i=0;i<rlForm.getDeleteLinks().length;i++) {
//              int n = Integer.parseInt(rlForm.getDeleteLinks()[i]);
//              Documents doc = (Documents)list.get(n);
//              
//              Long actId = doc.getActivityId();
//              Long docId = doc.getDocId();
//              AmpActivityVersion  activity = ActivityUtil.getProjectChannelOverview(actId);
//              Set docList = new HashSet();
//              if (activity != null) {
//                  Iterator itr = DbUtil.getActivityDocuments(actId).iterator();
//                  boolean found = false;
//                  while (itr.hasNext()) {
//                      CMSContentItem cmsItem = (CMSContentItem) itr.next();
//                      if (found)
//                          docList.add(cmsItem);
//                      else 
//                          if (docId.equals(new Long(cmsItem.getId()))){
//                              found = true;
//                              //we've found it so we don't add it to the new set
//                          }
//                          else 
//                              docList.add(cmsItem);
//                  }
//                  
//
//                  ActivityUtil.updateActivityDocuments(activity.getAmpActivityId(), docList);
//              }
//          }
//          pagedCol = new ArrayList();
//          pagedCol =DbUtil.getAllDocuments(teamId);
//          rlForm.setAllDocuments(pagedCol);
        }

        rlForm.setRelatedLinks(pagedCol);
        return mapping.findForward("forward");
    }
}
 
