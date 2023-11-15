package org.digijava.module.contentrepository.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.gis.services.MapTilesService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Public Document Manager
 * @author Viorel Chihai
 */
public class DocumentsNodesAttributeManager {
    
    private static Logger logger = Logger.getLogger(DocumentsNodesAttributeManager.class);
    
    private static DocumentsNodesAttributeManager documentsNodeAttributetManager;
    
    private List<CrDocumentNodeAttributes> cachedDocumentNodesAttributes;
    
    public static DocumentsNodesAttributeManager getInstance() {
        if (documentsNodeAttributetManager == null) {
            documentsNodeAttributetManager = new DocumentsNodesAttributeManager();
        }
        
        return documentsNodeAttributetManager;
    }
    
    protected void invalidateCachedDocumentNodeAttributes() {
        cachedDocumentNodesAttributes = null;
    }
    
    public List<CrDocumentNodeAttributes> getDocumentNodeAttributes() {
        if (cachedDocumentNodesAttributes == null) {
            updateDocumentNodesAttributes();
        }
        
        return cachedDocumentNodesAttributes;
    }
    
    public void makeDocumentPublic(HttpServletRequest request, String uuid) {
        try {
            boolean shouldSaveObject = false;

            CrDocumentNodeAttributes docAttributes = getDocumentNodeAttributes().stream()
                    .filter(d -> d.getUuid().equals(uuid)).findFirst().orElse(null);

            if ((docAttributes == null) || (docAttributes.getUuid() == null)) {
                docAttributes = new CrDocumentNodeAttributes();
                docAttributes.setUuid(uuid);
                shouldSaveObject = true;
            }

            Node lastVersionNode = DocumentManagerUtil.getLastVersionNotWaitingApproval(uuid, request);
            docAttributes.setPublicDocument(true);
            docAttributes.setPublicVersionUUID(lastVersionNode.getIdentifier());

            if (shouldSaveObject) {
                PersistenceManager.getSession().saveOrUpdate(docAttributes);
                invalidateCachedDocumentNodeAttributes();
                
                NodeWrapper nw = new NodeWrapper(lastVersionNode);
                if (nw.getName().equals(MapTilesService.FILE_NAME)) {
                    MapTilesService.getInstance().updateOfflineChangeLog(PersistenceManager.getSession());
                }
            }
        } catch (RepositoryException | CrException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    public void deleteDocumentNodesAttributes(String uuid) {
        Session session = PersistenceManager.getRequestDBSession();
        String queryStr = "SELECT a FROM " + CrDocumentNodeAttributes.class.getName() + " a WHERE uuid=:uuid";
        Query query = session.createQuery(queryStr);
        query.setParameter("uuid", uuid, StringType.INSTANCE);
        CrDocumentNodeAttributes docNodeAtt = (CrDocumentNodeAttributes) query.uniqueResult();
        
        if (docNodeAtt != null) {
            session.delete(docNodeAtt);
            invalidateCachedDocumentNodeAttributes();
        }
    }

    private void updateDocumentNodesAttributes() {
        cachedDocumentNodesAttributes = PersistenceManager.getRequestDBSession()
                .createCriteria(CrDocumentNodeAttributes.class)
                .list();
    }
    
    public Map<String, CrDocumentNodeAttributes> getPublicDocumentsMap(boolean keyIsVersionOfDocument) {
        
        Function<CrDocumentNodeAttributes, String> keyMapper = keyIsVersionOfDocument
                ? CrDocumentNodeAttributes::getPublicVersionUUID : CrDocumentNodeAttributes::getUuid;
        
        return getPublicDocuments().stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }
    
    public List<CrDocumentNodeAttributes> getPublicDocuments() {
        return getDocumentNodeAttributes().stream()
                .filter(d -> Boolean.TRUE.equals(d.getPublicDocument()))
                .collect(Collectors.toList());
    }
    
}
