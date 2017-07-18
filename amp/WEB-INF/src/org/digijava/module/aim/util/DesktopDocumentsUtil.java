package org.digijava.module.aim.util;

import java.util.*;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dgfoundation.amp.utils.BoundedList;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.apache.log4j.Logger;

public class DesktopDocumentsUtil {

	protected static final Logger logger = Logger.getLogger(DesktopDocumentsUtil.class);

	public Collection<DocumentData> getLatestDesktopLinks(HttpServletRequest request, int top) {
		ArrayList<DocumentData> reducedList = null;

        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);

        Session readSession = DocumentManagerUtil.getReadSession(request);

        DesktopDocumentsUtil desktopDocumentUtils = new DesktopDocumentsUtil();

        Node rootNode = null;
        try {
            rootNode = readSession.getRootNode();
        } catch (RepositoryException ex) {
            logger.warn("Failed to read documents from repository", ex);
            return Collections.emptyList();
        }

        List<DocumentData> allDocuments = new ArrayList<>();
        List<DocumentData> privateDocs = desktopDocumentUtils.getPrivateDocuments(tm, rootNode, request);
        List<DocumentData> teamDocs = desktopDocumentUtils.getTeamDocuments(tm, rootNode, request);
        BoundedList<DocumentData> downloadedDocs =
                (BoundedList<DocumentData>)(request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES));

        allDocuments.addAll(privateDocs);
        allDocuments.addAll(teamDocs);
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
        Iterator<DocumentData> it = allDocuments.listIterator();
        Set<String> uidList = new HashSet<String>();
        while (it.hasNext() && reducedList.size() <= top) {
            DocumentData document = it.next();
            // Checking to skip URLs and duplicates
            if (!document.getContentType().equalsIgnoreCase("URL") && !uidList.contains(document.getUuid())) {
                reducedList.add(document);
                uidList.add(document.getUuid());
            }
        }
        DocumentManagerUtil.logoutJcrSessions(request);

		return reducedList;
	}

	public List<DocumentData> getPrivateDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node userNode;
		try {
			userNode = DocumentManagerUtil.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (RepositoryException e) {
			logger.warn("Failed to read user private documents from the Repository", e);
			return Collections.emptyList();
		}
		return getDocuments(userNode, request);
	}

	public List<DocumentData> getTeamDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node teamNode;
		try {
			teamNode = DocumentManagerUtil.getTeamNode(rootNode.getSession(), teamMember.getTeamId());
		} catch (RepositoryException e) {
            logger.warn("Failed to read team documents from the Repository", e);
            return Collections.emptyList();
		}
		return getDocuments(teamNode, request);
	}

	private List<DocumentData> getDocuments(Node node, HttpServletRequest request) {
		try {
			NodeIterator nodeIterator = node.getNodes();
			return getDocuments(nodeIterator, request);
		} catch (RepositoryException e) {
            logger.warn("Failed to read documents from the Repository", e);
            return Collections.emptyList();
		}
	}

	private List<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest request) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		HashMap<String, CrDocumentNodeAttributes> uuidMapOrg = CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String, CrDocumentNodeAttributes> uuidMapVer = CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		Boolean hasMakePublicRights = DocumentManagerRights.hasMakePublicRights(request);
		
		List<String> gpiSupportiveDocuments = DocumentUtil.getAllSupportiveDocumentsUUID();

		try {
			while (nodeIterator.hasNext()) {
				Node documentNode = (Node) nodeIterator.next();
				
				// hide gpi supportive documents
				if (gpiSupportiveDocuments.contains(documentNode.getIdentifier())) {
					continue;
				}
				
				NodeWrapper nodeWrapper = new NodeWrapper(documentNode);
				Boolean hasViewRights = false;
				Boolean hasShowVersionsRights = false;
				Boolean hasVersioningRights = false;
				Boolean hasDeleteRights = false;
				Boolean hasDeleteRightsOnPublicVersion = false;

				String uuid = documentNode.getIdentifier();
				boolean isPublicVersion = uuidMapVer.containsKey(uuid);

				if (isPublicVersion) { // This document is public and exactly
					// this version is the public one
					hasViewRights = true;
				} else
					hasViewRights = DocumentManagerRights.hasViewRights(documentNode, request);

				if (hasViewRights == null || !hasViewRights) {
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
						documentData.setHasVersioningRights(hasVersioningRights);
					}

					hasDeleteRights = DocumentManagerRights.hasDeleteRights(documentNode, request);
					if (hasDeleteRights != null) {
						documentData.setHasDeleteRights(hasDeleteRights);
					}

					if (hasMakePublicRights != null) {
						documentData.setHasMakePublicRights(hasMakePublicRights);
					}

					hasDeleteRightsOnPublicVersion = DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, request);
					if (hasDeleteRightsOnPublicVersion != null) {
						documentData.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion);
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

		} catch (RepositoryException | CrException e) {
            logger.warn("Failed to read documents from the Repository", e);
			return Collections.emptyList();
		}

		return documents;
	}
	
}
