package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class UpdateTeamActivities extends Action {

	private static Logger logger = Logger.getLogger(UpdateTeamActivities.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		logger.debug("In update team activities");
		
		boolean permitted = false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				permitted = true;
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

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		int numRecords = 0;
		int page = 0;

		if (session.getAttribute("currentMember") != null) {
			id = tm.getTeamId();
			numRecords = tm.getAppSettings().getDefRecsPerPage();
		}
		//
		if ((taForm.getSelActDocuments() != null) && (taForm.getRemoveDocument().equals("remove"))) {
			/* remove all selected document from the activities associated */
			if (taForm.getSelActDocuments() != null) {
				logger.info("In remove documents from activity");
				TeamUtil.removeDocumentsFromActivities(taForm.getSelActDocuments(), taForm.getUuid());
				//
				return mapping.findForward("forward2");
			}
			taForm.setRemoveDocument(null);
			taForm.setSelActDocuments(null);
		}
		//
		if (taForm.getSelActivities() != null && taForm.getRemoveActivity().equals("remove")) {
			/* remove all selected activities */
		    
	        if (taForm.getSelActivities() != null) {
	        	TeamUtil.removeActivitiesFromTeam(taForm.getSelActivities(),taForm.getTeamId());
	        }
	 		    
			Long selActivities[] = taForm.getSelActivities();
			taForm.setSelActivities(null);

			if (session.getAttribute("unassignedActivityList") != null) {
				session.removeAttribute("unassignedActivityList");
			}
			
			if (session.getAttribute("ampProjects") != null) {
				logger.info("removing ampProjects from session..");
				session.removeAttribute("ampProjects");
			}
			if (session.getAttribute("teamActivityList") != null) {
				session.removeAttribute("teamActivityList");
			}
			taForm.setRemoveActivity(null);
			taForm.setSelActivities(null);
			session.removeAttribute("report"); // so that the activity list gets refreshed
			session.removeAttribute("reportMeta"); // so that the activity list gets refreshed
			return mapping.findForward("forward");
		} else if (taForm.getSelActivities() != null && taForm.getRemoveActivity().equals("assign")) {
			/* add the selected activities to the team list */
			logger.info("in assign activity");
			Long selActivities[] = taForm.getSelActivities();
			Long memberId = taForm.getMemberId();

			if (selActivities != null) {
				for (int i = 0; i < selActivities.length; i++) {
					if (selActivities[i] != null) {
						Long actId = selActivities[i];
						AmpActivity activity = ActivityUtil.getProjectChannelOverview(actId);
						
						AmpTeam ampTeam = TeamUtil.getAmpTeam(taForm.getTeamId());
						activity.setTeam(ampTeam);
						AmpTeamMember atm=TeamMemberUtil.getAmpTeamMember(memberId);
//						AmpTeamMemberRoles ampRole = atm.getAmpMemberRole();
//						AmpTeamMemberRoles headRole = TeamMemberUtil.getAmpTeamHeadRole();
						//AMP-3937 - the activities assigned to an user, if that user is team lead then the 
						//activities are approved!
						if(ampTeam.getTeamLead().getAmpTeamMemId().equals(atm.getAmpTeamMemId())){
						//if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) {
							activity.setApprovalStatus(Constants.APPROVED_STATUS);
						}
						activity.setActivityCreator(atm);
						
						if (activity.getActivityCreator() == null) {
							AmpTeamMember thisTeamMember	= TeamUtil.getAmpTeamMember(tm.getMemberId());
							if (thisTeamMember != null) {
								activity.setActivityCreator(thisTeamMember);
							}
						}

						logger.info("updating " + activity.getName());
						DbUtil.update(activity);
						//UpdateDB.updateReportCache(actId);
					}
				}
			} else {
				taForm.setAssignActivity(null);
				taForm.setSelActivities(null);
				return mapping.findForward("forward");				
			}
			taForm.setSelActivities(null);
			if (session.getAttribute("unassignedActivityList") != null) {
				session.removeAttribute("unassignedActivityList");
			}
			if (session.getAttribute("ampProjects") != null) {
				session.removeAttribute("ampProjects");
			}
			if (session.getAttribute("teamActivityList") != null) {
				session.removeAttribute("teamActivityList");
			}
			taForm.setAssignActivity(null);
			return mapping.findForward("forward");
		} else if (taForm.getUuid() != null && taForm.getRemoveDocument().equals("assign")) {
			/* add the selected activities to the team list */
			logger.info("in assign document to activity");
			String selectedUuids[] = taForm.getUuid();
			//Long memberId = taForm.getMemberId();
			Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request);
			
			if (selectedUuids != null) {
				for (int i = 0; i < selectedUuids.length; i++) {
					if (selectedUuids[i] != null) {
						String uuid = selectedUuids[i];
						//Node node = DocumentManagerUtil.getReadNode(uuid, request);
						String actSelected = taForm.getSelectedAct();
			    		String actIdSelected = actSelected.substring(actSelected.lastIndexOf("(") + 1, actSelected.lastIndexOf("") - 1);
			    		
						AmpActivity activity = ActivityUtil.loadActivity(Long.valueOf(actIdSelected));
						AmpActivityDocument document = new AmpActivityDocument();
						document.setAmpActivity(activity);
						document.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
						document.setUuid(uuid);
						//						
						activity.getActivityDocuments().add(document);
						//
						logger.info("updating " + activity.getName());
						DbUtil.update(activity);
					}
				}
			}
			taForm.setSelectedAct("");		
			taForm.setUuid(null);
			taForm.setAssignActivity(null);
			return mapping.findForward("showAddActivityDoc");
		} else if ((request.getParameter("showUnassignedDocs") != null && (request.getParameter("showUnassignedDocs").equals("true")))) {

			/* show all unassigned activities */

			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
				if (page == 0) {
					page = 1;
				}
			}

			AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
			Collection actDocList = new ArrayList(); 
			//
			List<AmpActivity> aal = ActivityUtil.getAllActivitiesList();
			Iterator<AmpActivity> aait = aal.iterator();
			AmpActivity aa = null;
			//
			Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request);
			Collection otherTeamMembers		= TeamMemberUtil.getAllTeamMembers(id);			
			Iterator iterator				= otherTeamMembers.iterator();	
			Node otherHomeNode				= null;
			while ( iterator.hasNext() ) {
				TeamMember someTeamMember	= (TeamMember) iterator.next(); 
				//
				otherHomeNode				= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession , someTeamMember );
				Collection c = getDocuments(otherHomeNode, request);
				Iterator i = c.iterator();
				DocumentData data = null;
				//
				while (aait.hasNext()) {
					aa = aait.next();
					Iterator<AmpActivityDocument> aadit = aa.getActivityDocuments().iterator();
					AmpActivityDocument aad = null;
					while (aadit.hasNext()) {
						aad = aadit.next();
						while (i.hasNext()) {
							data = (DocumentData) i.next();
							if (data.getUuid().equals(aad.getUuid())) {
								c.remove(data);
							}
						}
					}
				}
				actDocList.addAll(getDocuments(c));								
			}
			
			//
			Comparator acronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					Documents r1 = (Documents) o1;
					Documents r2 = (Documents) o2;
					return r1.getDocType().trim().toLowerCase().compareTo(
							r2.getDocType().trim().toLowerCase());
				}
			};
			Comparator racronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					Documents r1 = (Documents) o1;
					Documents r2 = (Documents) o2;
					return -(r1.getDocType().trim().toLowerCase()
							.compareTo(r2.getDocType().trim().toLowerCase()));
				}
			};
			
			List temp = (List) actDocList;
			String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
			String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();
			
			if (sort == null || "".equals(sort) || sortOrder == null
					|| "".equals(sortOrder)) {
				Collections.sort(temp);
				taForm.setSort("type");
				taForm.setSortOrder("asc");
			} else {
				if ("filename".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp);
					else
						Collections.sort(temp, Collections.reverseOrder());
				} else if ("type".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp, acronymComp);
					else
						Collections.sort(temp, racronymComp);
				}
			}		
			
			int stIndex = ((page - 1) * numRecords) + 1;
			int edIndex = page * numRecords;
			if (edIndex > temp.size()) {
				edIndex = temp.size();
			}

			Vector vect = new Vector();
			vect.addAll(temp);

			Collection col = new ArrayList();
			for (int i = (stIndex - 1); i < edIndex; i++) {
				col.add(vect.get(i));
			}

			int numPages = temp.size() / numRecords;
			numPages += (temp.size() % numRecords != 0) ? 1 : 0;

			Collection pages = null;

			if (numPages > 1) {
				pages = new ArrayList();
				for (int i = 0; i < numPages; i++) {
					Integer pageNum = new Integer(i + 1);
					pages.add(pageNum);
				}
			}
			//			
			taForm.setPages(pages);
			taForm.setDocuments(col);
			// load activitities of current member only
			taForm.setRelatedActivities(ActivityUtil.loadActivitiesNamesAndIds(tm));			
			taForm.setTeamId(id);
			taForm.setTeamName(ampTeam.getName());
			taForm.setCurrentPage(new Integer(page));
			taForm.setSelActDocuments(null);
			taForm.setUuid(null);
			taForm.setMembers(TeamMemberUtil.getAllTeamMembers(id));
			if(ampTeam.getTeamLead()!=null) taForm.setMemberId(ampTeam.getTeamLead().getAmpTeamMemId()); else taForm.setMemberId(null);
			session.setAttribute("pageno", new Integer(page));
			return mapping.findForward("showAddActivityDoc");
		} else {
			/* show all unassigned activities */

			if (request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
			}

			AmpTeam ampTeam = TeamUtil.getAmpTeam(id);

			Collection col = null;
			if (session.getAttribute("unassignedActivityList") == null) {
				col = TeamUtil.getAllUnassignedActivities();
				List temp = (List) col;
				Collections.sort(temp);
				col = (Collection) temp;
				session.setAttribute("unassignedActivityList", col);
			}
			
			Collection actList = (Collection) session
					.getAttribute("unassignedActivityList");

			
			Comparator acronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					AmpActivity r1 = (AmpActivity) o1;
					AmpActivity r2 = (AmpActivity) o2;
			        return r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase());
				}
			};
			Comparator racronymComp = new Comparator() {
				public int compare(Object o1, Object o2) {
					AmpActivity r1 = (AmpActivity) o1;
					AmpActivity r2 = (AmpActivity) o2;
					return -(r1.getDonors().trim().toLowerCase().compareTo(r2.getDonors().trim().toLowerCase()));
				}
			};
			
			List temp = (List)actList;
			String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
			String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();
			
			if ( sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
				Collections.sort(temp);
				taForm.setSort("activity");
				taForm.setSortOrder("asc");
			}
			else {
				if ("activity".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp);
					else
						Collections.sort(temp,Collections.reverseOrder());
				}
				else if ("donor".equals(sort)) {
					if ("asc".equals(sortOrder))
						Collections.sort(temp, acronymComp);
					else
						Collections.sort(temp, racronymComp);
				}
			}			
			
			int stIndex = ((page - 1) * numRecords) + 1;
			int edIndex = page * numRecords;
			if (edIndex > temp.size()) {
				edIndex = temp.size();
			}

			Vector vect = new Vector();
			vect.addAll(temp);

			col = new ArrayList();
			for (int i = (stIndex - 1); i < edIndex; i++) {
				col.add(vect.get(i));
			}

			int numPages = temp.size() / numRecords;
			numPages += (temp.size() % numRecords != 0) ? 1 : 0;

			Collection pages = null;

			if (numPages > 1) {
				pages = new ArrayList();
				for (int i = 0; i < numPages; i++) {
					Integer pageNum = new Integer(i + 1);
					pages.add(pageNum);
				}
			}
			taForm.setPages(pages);
			taForm.setActivities(col);
			taForm.setTeamId(id);
			taForm.setTeamName(ampTeam.getName());
			taForm.setCurrentPage(new Integer(page));
			taForm.setSelActivities(null);
			taForm.setMembers(TeamMemberUtil.getAllTeamMembers(id));
			if(ampTeam.getTeamLead()!=null) taForm.setMemberId(ampTeam.getTeamLead().getAmpTeamMemId()); else taForm.setMemberId(null);
			session.setAttribute("pageno", new Integer(page));
			return mapping.findForward("showAddActivity");
			//return mapping.findForward("forward");
		}
	}

	public boolean canDelete(Long actId) {
		logger.debug("In can delete");
		Iterator itr = TeamMemberUtil.getAllMembersUsingActivity(actId).iterator();
		if (itr.hasNext()) {
			logger.debug("return false");
			return false;
		} else {
			logger.debug("return true");
			return true;
		}
	}	

	private Collection<Documents> getDocuments(Collection<DocumentData> dd) {
		Collection<Documents> collection = new ArrayList<Documents>();
		Documents documents = null;
		DocumentData data = null;
		Iterator it = dd.iterator();
		while (it.hasNext()) {
			data = (DocumentData) it.next();
			documents = new Documents();
			//
			//documents.setAmpActivityId();
			//documents.setActivityId();
			//documents.setActivityName();
			documents.setDate(data.getCalendar());
			//documents.setDocComment(data.getUuid());
			documents.setDocDescription(data.getDescription());
			//documents.setDocLanguage(data.getUuid());
			documents.setDocType(data.getCmDocType());
			//documents.setFile(data.getUuid());
			documents.setFileName(data.getName());
			//documents.setIsFile(data.getUuid());
			documents.setTitle(data.getTitle());
			documents.setUrl(data.getWebLink());
			documents.setUuid(data.getUuid());
			//
			collection.add(documents);
		}
		return collection;
	}
	
	private Collection getDocuments(Node node, HttpServletRequest request) {
		try {
			NodeIterator nodeIterator	= node.getNodes();
			return getDocuments(nodeIterator, request);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	private Collection<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest myRequest) {
		ArrayList<DocumentData> documents										= new ArrayList<DocumentData>();
		HashMap<String,CrDocumentNodeAttributes> uuidMapOrg		= CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String,CrDocumentNodeAttributes> uuidMapVer		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try{
			while ( nodeIterator.hasNext() ) {
				Node documentNode		= (Node)nodeIterator.next();
				NodeWrapper nodeWrapper	= new NodeWrapper(documentNode);
				
				Boolean hasViewRights			= false;
				Boolean hasShowVersionsRights	= false;
				Boolean hasVersioningRights		= false;
				Boolean hasDeleteRights			= false;
				Boolean hasMakePublicRights		= false;
				Boolean hasDeleteRightsOnPublicVersion			= false;
				
				String uuid						= documentNode.getUUID();
				boolean isPublicVersion		= uuidMapVer.containsKey(uuid);
				
				if ( isPublicVersion ) { // This document is public and exactly this version is the public one
						hasViewRights			= true;
				}
				else
						hasViewRights			= DocumentManagerRights.hasViewRights(documentNode, myRequest);
				
				if ( hasViewRights == null || !hasViewRights.booleanValue() ) {
					continue;
				}
				
				String fileName		=  nodeWrapper.getName();
				if ( fileName == null && nodeWrapper.getWebLink() == null )
						continue;
				
				DocumentData documentData		= new DocumentData();
				documentData.setName( fileName );
				documentData.setUuid( nodeWrapper.getUuid() );
				documentData.setTitle( nodeWrapper.getTitle() );
				documentData.setDescription( nodeWrapper.getDescription() );
				documentData.setNotes( nodeWrapper.getNotes() );
				documentData.setFileSize( nodeWrapper.getFileSizeInMegabytes() );
				documentData.setCalendar( nodeWrapper.getDate() );
				documentData.setVersionNumber( nodeWrapper.getVersionNumber() );
				documentData.setContentType( nodeWrapper.getContentType() );
				documentData.setWebLink( nodeWrapper.getWebLink() );
				documentData.setCmDocTypeId( nodeWrapper.getCmDocTypeId() );
				
				
					documentData.process(myRequest);
					documentData.computeIconPath(true);
					
					if ( !isPublicVersion ) {
						hasShowVersionsRights	= DocumentManagerRights.hasShowVersionsRights(documentNode, myRequest);
						if ( hasShowVersionsRights != null )
							documentData.setHasShowVersionsRights(hasShowVersionsRights);
						
						hasVersioningRights		= DocumentManagerRights.hasVersioningRights(documentNode, myRequest);
						if ( hasVersioningRights != null ) {
							documentData.setHasVersioningRights( hasVersioningRights.booleanValue() );
						}
						hasDeleteRights			= DocumentManagerRights.hasDeleteRights(documentNode, myRequest);
						if ( hasDeleteRights != null ) {
							documentData.setHasDeleteRights( hasDeleteRights.booleanValue() );
						}
						hasMakePublicRights		= DocumentManagerRights.hasMakePublicRights(documentNode, myRequest);
						if ( hasMakePublicRights != null ) {
							documentData.setHasMakePublicRights( hasMakePublicRights.booleanValue() );
						}
						
						hasDeleteRightsOnPublicVersion			= DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, myRequest);
						if ( hasDeleteRightsOnPublicVersion != null ) {
							documentData.setHasDeleteRightsOnPublicVersion( hasDeleteRightsOnPublicVersion.booleanValue() );
						}
						
						if ( uuidMapOrg.containsKey(uuid) ) {
								documentData.setIsPublic(true);
								
								//Verify if the last (current) version is the public one.
								Node lastVersion	= DocumentManagerUtil.getNodeOfLastVersion(uuid, myRequest);
								String lastVerUUID	= lastVersion.getUUID();
								if ( uuidMapVer.containsKey(lastVerUUID) ) {
									documentData.setLastVersionIsPublic( true );
								}
								
						}
						else
								documentData.setIsPublic(false);
						
												
					}
					// This is not the actual document node. It is the node of the public version. That's why one shouldn't have 
					// the above rights.
					else {
						documentData.setShowVersionHistory(false); 
					}
					documents.add(documentData);
				}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
		return documents;
	}
}
