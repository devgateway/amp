package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.utils.BoundedList;
import org.digijava.kernel.ampapi.endpoints.documents.Documents;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.apache.log4j.Logger;

public class DesktopDocumentsUtil {

	protected static final Logger logger = Logger.getLogger(DesktopDocumentsUtil.class);

	public Collection<DocumentData> getLatestDesktopLinks(HttpServletRequest request, int top) {
		ArrayList<DocumentData> reducedList = null;
		try {
			HttpSession session = request.getSession();
			TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

			// TODO: Check why this is so slow.
			Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);

			ArrayList<DocumentData> allDocuments = new ArrayList<DocumentData>();

			DesktopDocumentsUtil desktopDocumentUtils = new DesktopDocumentsUtil();
			Collection<DocumentData> tmp = desktopDocumentUtils.getTeamDocuments(tm, jcrWriteSession.getRootNode(), request);
			allDocuments.addAll(tmp);
			
			// just keeps last top
			BoundedList<DocumentData> downloadedDocs =
	                (BoundedList<DocumentData>)(request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES));
			
			if (downloadedDocs != null) {
	            allDocuments.addAll(downloadedDocs);
	        }
	        Collections.sort(allDocuments, new Comparator<DocumentData>() {
	            @Override
	            public int compare(DocumentData o1, DocumentData o2) {
	            	if(o1 == null) {
	            		if (o2 == null)
	            			return 0;
	            		return -1;
	            	} 
	            	
	            	if (o2 == null) 
	            		return 1;
	                
	            	return o2.getDate().compareTo(o1.getDate());
	            }
	        });


			reducedList = new ArrayList<DocumentData>();
			Iterator<DocumentData> it = allDocuments.iterator();
			while (it.hasNext() && reducedList.size() <= top) {
				DocumentData document = it.next();
				// Checking to skip URLs
				if (!document.getContentType().equalsIgnoreCase("URL")) {
					reducedList.add(document);
				}
			}
			DocumentManagerUtil.logoutJcrSessions(request);
		} catch (Exception e) {
			logger.error(e);
		}
		return reducedList;
	}

	public Collection<DocumentData> getPrivateDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node userNode;
		try {

			userNode = DocumentManagerUtil.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(userNode, request);
	}

	public Collection<DocumentData> getTeamDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node teamNode;
		try {
			teamNode = DocumentManagerUtil.getTeamNode(rootNode.getSession(), teamMember.getTeamId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(teamNode, request);
	}

	private Collection<DocumentData> getDocuments(Node node, HttpServletRequest request) {
		try {
			NodeIterator nodeIterator = node.getNodes();
			return getDocuments(nodeIterator, request);
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}

	}

	private Collection<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest request) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		HashMap<String, CrDocumentNodeAttributes> uuidMapOrg = CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String, CrDocumentNodeAttributes> uuidMapVer = CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try {
			while (nodeIterator.hasNext()) {
				Node documentNode = (Node) nodeIterator.next();
				NodeWrapper nodeWrapper = new NodeWrapper(documentNode);
				Boolean hasViewRights = false;
				Boolean hasShowVersionsRights = false;
				Boolean hasVersioningRights = false;
				Boolean hasDeleteRights = false;
				Boolean hasMakePublicRights = false;
				Boolean hasDeleteRightsOnPublicVersion = false;

				String uuid = documentNode.getIdentifier();
				boolean isPublicVersion = uuidMapVer.containsKey(uuid);

				if (isPublicVersion) { // This document is public and exactly
					// this version is the public one
					hasViewRights = true;
				} else
					hasViewRights = DocumentManagerRights.hasViewRights(documentNode, request);

				if (hasViewRights == null || !hasViewRights.booleanValue()) {
					continue;
				}

				String fileName = nodeWrapper.getName();
				if (fileName == null && nodeWrapper.getWebLink() == null)
					continue;

				DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper, fileName, null, null);

				if (!isPublicVersion) {
					hasShowVersionsRights = DocumentManagerRights.hasShowVersionsRights(documentNode, request);
					if (hasShowVersionsRights != null)
						documentData.setHasShowVersionsRights(hasShowVersionsRights);

					hasVersioningRights = DocumentManagerRights.hasVersioningRights(documentNode, request);
					if (hasVersioningRights != null) {
						documentData.setHasVersioningRights(hasVersioningRights.booleanValue());
					}
					hasDeleteRights = DocumentManagerRights.hasDeleteRights(documentNode, request);
					if (hasDeleteRights != null) {
						documentData.setHasDeleteRights(hasDeleteRights.booleanValue());
					}
					hasMakePublicRights = DocumentManagerRights.hasMakePublicRights(documentNode, request);
					if (hasMakePublicRights != null) {
						documentData.setHasMakePublicRights(hasMakePublicRights.booleanValue());
					}

					hasDeleteRightsOnPublicVersion = DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, request);
					if (hasDeleteRightsOnPublicVersion != null) {
						documentData.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion.booleanValue());
					}

					if (uuidMapOrg.containsKey(uuid)) {
						documentData.setIsPublic(true);

						// Verify if the last (current) version is the public
						// one.
						Node lastVersion = DocumentManagerUtil.getNodeOfLastVersion(uuid, request);
						String lastVerUUID = lastVersion.getIdentifier();
						if (uuidMapVer.containsKey(lastVerUUID)) {
							documentData.setLastVersionIsPublic(true);
						}

					} else
						documentData.setIsPublic(false);
				} else {
					// This is not the actual document node. It is the node of the
					// public version. That's why one shouldn't have
					// the above rights.
					documentData.setShowVersionHistory(false);
				}
				
				if (nodeWrapper.getCalendarDate() != null) {
					documentData.setDate(nodeWrapper.getCalendarDate().getTime());
				}
				
				documents.add(documentData);
			}

			/* } */
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return documents;
	}
}
