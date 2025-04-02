package org.digijava.module.aim.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.service.ServiceManager;
import org.digijava.kernel.services.JCRRepositoryService;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGPINiSurveyResponseDocument;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.ManagedDocument;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;

public class DocumentUtil {

    public static boolean isDMEnabled() {
        try {
            return getRepositoryService() != null;
        } catch(AimException ex) {
            return false;
        }
    }

    private static JCRRepositoryService getRepositoryService() throws
        AimException {
        JCRRepositoryService repositoryService = (JCRRepositoryService)
            ServiceManager.getInstance().getService(JCRRepositoryService.
            SERVICE_ID);
        if(repositoryService == null) {
            throw new AimException(
                "Document management service is not configured/started");
        }
        return repositoryService;
    }

    public static Node createDocumentSpace(Site site, String spaceName) throws
        AimException, RepositoryException, DgException {
        JCRRepositoryService service = getRepositoryService();

        Node siteRoot = service.getRootNode(site);

        Node rootNode =  siteRoot.addNode(spaceName, "cm:folder");
        renameNode(rootNode, spaceName);

        return rootNode;
    }

    public static Node getDocumentSpace(Site site, String spaceName) throws
        AimException, RepositoryException, DgException {
        return getDocumentSpace(site, spaceName, false);
    }

    public static Node getDocumentSpace(Site site, String spaceName,
                                        boolean createIfNotExist) throws
        AimException, RepositoryException, DgException {
        JCRRepositoryService service = getRepositoryService();

        Node siteRoot = service.getRootNode(site);
        try {
            return siteRoot.getNode(spaceName);
        } catch(PathNotFoundException ex) {
            if(createIfNotExist) {
                return siteRoot.addNode(spaceName, "cm:folder");
            } else {
                throw ex;
            }
        }
    }

    public static void renameNode(Node node, String name) throws
        ConstraintViolationException, LockException, VersionException,
        ValueFormatException, RepositoryException {
        node.setProperty("cm:name", name);
    }

    public static Node getActivitySpaceNode(Site site, AmpActivityVersion activity) throws
        DgException, RepositoryException, AimException {
        if(activity.getDocumentSpace() == null ||
           activity.getDocumentSpace().trim().length() == 0) {
            return null;
        }
        Node spaceNode = getDocumentSpace(site, activity.getDocumentSpace(), true);
        if (activity.getName() != null && activity.getName().trim().length()!= 0) {
            renameNode(spaceNode, activity.getName());
        }
        return spaceNode;
    }

    public static Node addDocument(Site site, String activitySpace, String fileName, String name, String description, String date,String docType, byte[] data) throws
        RepositoryException, DgException {
        Node spaceNode = getDocumentSpace(site, activitySpace);
        Node fileNode = spaceNode.addNode(fileName, "cm:content");

        fileNode.setProperty("cm:name", fileName);

        if (name != null && name.trim().length() != 0) {
            fileNode.setProperty("cm:title", name);
        }
        if (description != null && description.trim().length() != 0) {
            fileNode.setProperty("cm:description", description);
        } else {
            fileNode.setProperty("cm:description","");
        }
        if (date != null && date.trim().length() != 0) {
            fileNode.setProperty("cm:date", date);
        } else {
            fileNode.setProperty("cm:date","");
        }
        if (docType != null && docType.trim().length() != 0) {
            fileNode.setProperty("cm:docType", docType);
        } else {
            fileNode.setProperty("cm:docType","");
        }
        
        fileNode.setProperty("cm:content", new ByteArrayInputStream(data));

        return fileNode;
    }

    public static void removeDocument(Site site, String activitySpace,
                                      ManagedDocument document) throws
        RepositoryException, DgException {
    }

    /**
     * removeDocument
     *
     * @param currentSite Site
     * @param string String
     * @param managedDocId String
     * @throws RepositoryException
     * @throws DgException
     */
    public static void removeDocument(Site currentSite, String activitySpace,
                                      String managedDocId)  throws RepositoryException, DgException {
        Node spaceNode = getDocumentSpace(currentSite, activitySpace);
        Node nodeToRemove = spaceNode.getNode(managedDocId);
        nodeToRemove.remove();
    }


    public static List<ManagedDocument> getDocumentsForActivity(Site site,
        AmpActivityVersion activity) throws RepositoryException, DgException {
        Node spaceNode = getActivitySpaceNode(site, activity);
        if (spaceNode == null) {
            // Not configured yet
            return new ArrayList<>();
        }
        return getContentItems(spaceNode);
    }

    public static List<ManagedDocument> getDocumentsForSpace(Site site,
        String activitySpace) throws RepositoryException, DgException {
        Node spaceNode = getDocumentSpace(site, activitySpace);
        if (spaceNode == null) {
            // Not configured yet
            return new ArrayList<>();
        }
        return getContentItems(spaceNode);
    }

    private static List<ManagedDocument> getContentItems(Node node) throws RepositoryException {
        NodeIterator iter = node.getNodes();
        ArrayList<ManagedDocument> nodeList = new ArrayList<>();
        while(iter.hasNext()) {
            Node childItem = (Node) iter.next();

            ManagedDocument contentItem = createDocument(childItem);
            if(contentItem != null) {
                nodeList.add(contentItem);
            }
        }

        return nodeList;
    }

    private static ManagedDocument createDocument(Node childItem) throws
        RepositoryException {
        ManagedDocument contentItem = new ManagedDocument();

        if(!childItem.getPrimaryNodeType().isNodeType("cm:content")) {
            return null;
        }

        Property nameProperty = childItem.getProperty("cm:name");
        if(nameProperty != null) {
            contentItem.setFileName(nameProperty.getString());
        } else {
            contentItem.setFileName("Item #" + childItem.getIdentifier());
        }

        contentItem.setId(contentItem.getFileName());

        Property titleProperty = childItem.getProperty("cm:title");
        if(nameProperty != null) {
            contentItem.setName(titleProperty.getString());
        } else {
            contentItem.setName(contentItem.getFileName());
        }

        Property descriptionProperty = childItem.getProperty("cm:description");
        if(descriptionProperty != null) {
            contentItem.setDescription(descriptionProperty.getString());
        } else {
            contentItem.setDescription(null);
        }
        Property documentType    = childItem.getProperty("cm:docType");
        if(documentType != null) {
            contentItem.setDocType(documentType.getString());
        } else {
            contentItem.setDocType(null);
        }

        contentItem.setVersion("initial");

        return contentItem;
    }
    
    /**
     * Get all uuid of supportive documents (from gpi ni indicators)
     * 
     * @return List<String> list of all supportive documents
     */
    public static List<String> getAllSupportiveDocumentsUUID() {
        List<String> supportiveDocumentsUUID = PersistenceManager.getSession().createQuery("SELECT d.uuid FROM "
                + AmpGPINiSurveyResponseDocument.class.getName() + " d").list();
        
        return supportiveDocumentsUUID;
    }
    
    public static List<CrDocumentNodeAttributes> getAllPublicDocuments() {
        return PersistenceManager.getSession().createCriteria(CrDocumentNodeAttributes.class).list();
    }

}
