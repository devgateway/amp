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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class GetTeamActivitiesDocuments extends Action {

	private static Logger logger = Logger
			.getLogger(GetTeamActivitiesDocuments.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		TeamActivitiesForm taForm = (TeamActivitiesForm) form;

		Long id = null;

		try {
			boolean permitted = false;
			HttpSession session = request.getSession();
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
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
			int numRecords = Constants.NUM_RECORDS;
			int page = 0;
			//
			RepairDbUtil.repairDb();
			//
			if (request.getParameter("id") != null) {
				id = new Long(Long.parseLong(request.getParameter("id")));
				AmpApplicationSettings appSettings = DbUtil
						.getTeamAppSettings(id);
				if (appSettings != null) {
					numRecords = appSettings.getDefaultRecordsPerPage()
							.intValue();
				}
			} else if (request.getAttribute("teamId") != null) {
				id = (Long) request.getAttribute("teamId");
				AmpApplicationSettings appSettings = DbUtil
						.getTeamAppSettings(id);
				if (appSettings != null) {
					numRecords = appSettings.getDefaultRecordsPerPage()
							.intValue();
				}
			} else if (tm != null) {

				id = tm.getTeamId();
				if (tm.getAppSettings() != null)
					numRecords = tm.getAppSettings().getDefRecsPerPage();
			}
			if (id != null) {
				if (request.getParameter("page") == null) {
					page = 1;
				} else {
					page = Integer.parseInt(request.getParameter("page"));
				}
				//
				page = (page < 0) ? 1 : page;
				taForm.setPage(page);
				AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
				taForm.setTeamId(id);
				taForm.setTeamName(ampTeam.getName());
				//
				if (taForm.getAllDocuments() == null) {
					Collection col = null;
					Collection collection = new ArrayList();
					Site site = RequestUtils.getSite(request);
					if (tm.getTeamType() == null) {
						if (ampTeam.getAccessType().equalsIgnoreCase(
								Constants.ACCESS_TYPE_MNGMT)) {
							col = TeamUtil.getManagementTeamActivities(id);
							Iterator it = col.iterator();
							AmpActivity activity = null;
							Collection<String> uuids = null;
							while (it.hasNext()) {
								activity = (AmpActivity) it.next();
								uuids = new ArrayList<String>();
								Iterator i = activity.getActivityDocuments()
										.iterator();
								while (i.hasNext()) {
									uuids.add(((AmpActivityDocument) i.next())
											.getUuid());
								}
								collection.addAll(getDocuments(getDocuments(uuids, request), activity));
							}
						} else if (ampTeam.getTeamCategory() != null) {
							col = TeamUtil.getAllTeamActivities(id);
							Iterator it = col.iterator();
							AmpActivity activity = null;
							Collection<String> uuids = null;
							while (it.hasNext()) {
								activity = (AmpActivity) it.next();
								uuids = new ArrayList<String>();
								Iterator i = activity.getActivityDocuments()
										.iterator();
								while (i.hasNext()) {
									uuids.add(((AmpActivityDocument) i.next())
											.getUuid());
								}
								collection.addAll(getDocuments(getDocuments(uuids, request), activity));
							}
						} else {
							col = TeamUtil.getAllTeamActivities(id);
							Iterator it = col.iterator();
							AmpActivity activity = null;
							Collection<String> uuids = null;
							while (it.hasNext()) {
								activity = (AmpActivity) it.next();
								uuids = new ArrayList<String>();
								Iterator i = activity.getActivityDocuments()
										.iterator();
								while (i.hasNext()) {
									uuids.add(((AmpActivityDocument) i.next())
											.getUuid());
								}
								collection.addAll(getDocuments(getDocuments(uuids, request), activity));
							}
						}
					} else {
						col = TeamUtil.getAllTeamActivities(id);
						Iterator it = col.iterator();
						AmpActivity activity = null;
						Collection<String> uuids = null;
						while (it.hasNext()) {
							activity = (AmpActivity) it.next();
							uuids = new ArrayList<String>();
							Iterator i = activity.getActivityDocuments()
									.iterator();
							while (i.hasNext()) {
								uuids.add(((AmpActivityDocument) i.next())
										.getUuid());
							}
							collection.addAll(getDocuments(getDocuments(uuids, request), activity));
						}
					}
					logger.info("Loaded " + collection.size()
							+ " documents for the team " + ampTeam.getName());
					taForm.setAllDocuments(collection);
				}
				//
				Comparator acronymComp = new Comparator() {
					public int compare(Object o1, Object o2) {
						Documents r1 = (Documents) o1;
						Documents r2 = (Documents) o2;
						return r1.getTitle().trim().toLowerCase().compareTo(
								r2.getTitle().trim().toLowerCase());
					}
				};
				Comparator racronymComp = new Comparator() {
					public int compare(Object o1, Object o2) {
						Documents r1 = (Documents) o1;
						Documents r2 = (Documents) o2;
						return -(r1.getTitle().trim().toLowerCase()
								.compareTo(r2.getTitle().trim().toLowerCase()));
					}
				};
				//
				List temp = (List) taForm.getAllDocuments();
				String sort = (taForm.getSort() == null) ? null : taForm
						.getSort().trim();
				String sortOrder = (taForm.getSortOrder() == null) ? null
						: taForm.getSortOrder().trim();
				if (sort == null || "".equals(sort) || sortOrder == null
						|| "".equals(sortOrder)) {
					Collections.sort(temp);
					taForm.setSort("title");
					taForm.setSortOrder("asc");
				} else {
					if ("activity".equals(sort)) {
						if ("asc".equals(sortOrder))
							Collections.sort(temp);
						else
							Collections.sort(temp, Collections.reverseOrder());
					} else if ("title".equals(sort)) {
						if ("asc".equals(sortOrder))
							Collections.sort(temp, acronymComp);
						else
							Collections.sort(temp, racronymComp);
					}
				}
				taForm.setAllDocuments(temp);
				int totActDocuments = taForm.getAllDocuments().size();
				int stIndex = ((page - 1) * numRecords) + 1;
				int edIndex = 1;
				if (page != 0)
					edIndex = page * numRecords;
				else
					edIndex = numRecords;
				edIndex = (edIndex > totActDocuments) ? totActDocuments
						: edIndex;
				if (stIndex < 1)
					stIndex = 1;
				Vector vect = new Vector();
				vect.addAll(taForm.getAllDocuments());
				taForm.setDocuments(new ArrayList());
				for (int i = (stIndex - 1); i < edIndex; i++) {
					taForm.getDocuments().add(vect.get(i));
				}
				int numPages = totActDocuments / numRecords;
				numPages += (totActDocuments % numRecords != 0) ? 1 : 0;
				Collection pages = null;
				if (numPages > 1) {
					pages = new ArrayList();
					for (int i = 0; i < numPages; i++) {
						Integer pageNum = new Integer(i + 1);
						pages.add(pageNum);
					}
				}
				taForm.setCurrentPage(new Integer(page));
				taForm.setPages(pages);
				session.setAttribute("pageno", new Integer(page));
				taForm.setSelActDocuments(new Long[0]);
				return mapping.findForward("forward");
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	private Collection<Documents> getDocuments(Collection<DocumentData> dd, AmpActivity activity) {
		Collection<Documents> collection = new ArrayList<Documents>();
		Documents documents = null;
		DocumentData data = null;
		Iterator it = dd.iterator();
		while (it.hasNext()) {
			data = (DocumentData) it.next();
			documents = new Documents();
			//
			documents.setAmpActivityId(activity.getAmpId());
			documents.setActivityId(activity.getAmpActivityId());
			documents.setActivityName(activity.getName());
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

	private Collection<DocumentData> getDocuments(Collection<String> UUIDs,
			HttpServletRequest myRequest) {
		ArrayList<Node> documents = new ArrayList<Node>();
		Iterator<String> iter = UUIDs.iterator();
		while (iter.hasNext()) {
			String uuid = iter.next();
			Node documentNode = DocumentManagerUtil
					.getReadNode(uuid, myRequest);

			/**
			 * If documentNode is null it means that there is no node with the
			 * specified uuid in the repository but the application still has
			 * some information about that node. It means that there is a
			 * problem in the logic of the application so we need to throw an
			 * exception.
			 */
			if (documentNode == null) {
				try {
					throw new Exception("Document with uuid '" + uuid
							+ "' not found !");
				} catch (Exception e) {
					e.printStackTrace();
					RepairDbUtil.repairDocumentNoLongerInContentRepository(
							uuid, CrDocumentNodeAttributes.class.getName());
					RepairDbUtil.repairDocumentNoLongerInContentRepository(
							uuid, AmpActivityDocument.class.getName());
				}

			} else {
				documents.add(documentNode);
			}
		}
		return getDocuments(documents.iterator(), myRequest);
	}

	private Collection<DocumentData> getDocuments(Iterator nodeIterator,
			HttpServletRequest myRequest) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		HashMap<String, CrDocumentNodeAttributes> uuidMapOrg = CrDocumentNodeAttributes
				.getPublicDocumentsMap(false);
		HashMap<String, CrDocumentNodeAttributes> uuidMapVer = CrDocumentNodeAttributes
				.getPublicDocumentsMap(true);
		try {
			while (nodeIterator.hasNext()) {
				Node documentNode = (Node) nodeIterator.next();
				NodeWrapper nodeWrapper = new NodeWrapper(documentNode);

				// if (nodeWrapper.getWebLink() != null && showOnlyDocs)
				// continue;
				// if (nodeWrapper.getWebLink() == null && showOnlyLinks)
				// continue;

				Boolean hasViewRights = false;
				Boolean hasShowVersionsRights = false;
				Boolean hasVersioningRights = false;
				Boolean hasDeleteRights = false;
				Boolean hasMakePublicRights = false;
				Boolean hasDeleteRightsOnPublicVersion = false;

				String uuid = documentNode.getUUID();
				boolean isPublicVersion = uuidMapVer.containsKey(uuid);

				if (isPublicVersion) { // This document is public and exactly
					// this version is the public one
					hasViewRights = true;
				} else
					hasViewRights = DocumentManagerRights.hasViewRights(
							documentNode, myRequest);

				if (hasViewRights == null || !hasViewRights.booleanValue()) {
					continue;
				}

				String fileName = nodeWrapper.getName();
				if (fileName == null && nodeWrapper.getWebLink() == null)
					continue;

				DocumentData documentData = new DocumentData();
				documentData.setName(fileName);
				documentData.setUuid(nodeWrapper.getUuid());
				documentData.setTitle(nodeWrapper.getTitle());
				documentData.setDescription(nodeWrapper.getDescription());
				documentData.setNotes(nodeWrapper.getNotes());
				documentData.setFileSize(nodeWrapper.getFileSizeInMegabytes());
				documentData.setCalendar(nodeWrapper.getDate());
				documentData.setVersionNumber(nodeWrapper.getVersionNumber());
				documentData.setContentType(nodeWrapper.getContentType());
				documentData.setWebLink(nodeWrapper.getWebLink());
				documentData.setCmDocTypeId(nodeWrapper.getCmDocTypeId());

				documentData.process(myRequest);
				documentData.computeIconPath(true);

				if (!isPublicVersion) {
					hasShowVersionsRights = DocumentManagerRights
							.hasShowVersionsRights(documentNode, myRequest);
					if (hasShowVersionsRights != null)
						documentData
								.setHasShowVersionsRights(hasShowVersionsRights);

					hasVersioningRights = DocumentManagerRights
							.hasVersioningRights(documentNode, myRequest);
					if (hasVersioningRights != null) {
						documentData.setHasVersioningRights(hasVersioningRights
								.booleanValue());
					}
					hasDeleteRights = DocumentManagerRights.hasDeleteRights(
							documentNode, myRequest);
					if (hasDeleteRights != null) {
						documentData.setHasDeleteRights(hasDeleteRights
								.booleanValue());
					}
					hasMakePublicRights = DocumentManagerRights
							.hasMakePublicRights(documentNode, myRequest);
					if (hasMakePublicRights != null) {
						documentData.setHasMakePublicRights(hasMakePublicRights
								.booleanValue());
					}

					hasDeleteRightsOnPublicVersion = DocumentManagerRights
							.hasDeleteRightsOnPublicVersion(documentNode,
									myRequest);
					if (hasDeleteRightsOnPublicVersion != null) {
						documentData
								.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion
										.booleanValue());
					}

					if (uuidMapOrg.containsKey(uuid)) {
						documentData.setIsPublic(true);

						// Verify if the last (current) version is the public
						// one.
						Node lastVersion = DocumentManagerUtil
								.getNodeOfLastVersion(uuid, myRequest);
						String lastVerUUID = lastVersion.getUUID();
						if (uuidMapVer.containsKey(lastVerUUID)) {
							documentData.setLastVersionIsPublic(true);
						}

					} else
						documentData.setIsPublic(false);

				}
				// This is not the actual document node. It is the node of the
				// public version. That's why one shouldn't have
				// the above rights.
				else {
					documentData.setShowVersionHistory(false);
				}
				documents.add(documentData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return documents;
	}

}
