package org.digijava.module.contentrepository.util;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Document Organization Manager
 * @author Viorel Chihai
 */
public class DocumentOrganizationManager {
    
    private static DocumentOrganizationManager documentOrganizationManager;
    
    private Map<String, List<AmpOrganisation>> cachedUuidToOrganisations;
    
    public static DocumentOrganizationManager getInstance() {
        if (documentOrganizationManager == null) {
            documentOrganizationManager = new DocumentOrganizationManager();
        }
        
        return documentOrganizationManager;
    }
    
    public void saveObject(CrDocumentsToOrganisations obj) {
        PersistenceManager.getRequestDBSession().saveOrUpdate(obj);
        invalidateChachedMap();
    }
    
    /**
     * returns a comma-separated list of the sorted names of the organisations of a Document
     * @param uuid - the document uuid
     * @return "a, b, c"
     */
    public String getOrganisationsAsStringByUUID(String uuid) {
        List<AmpOrganisation> orgs = getOrganizationsByUUID(uuid);
        
        if (orgs == null || orgs.isEmpty()) {
            return "";
        }
        
        SortedSet<String> names = new java.util.TreeSet<String>();
        
        names.addAll(orgs.stream()
                .filter(org -> StringUtils.isNotBlank(org.getName()))
                .map(org -> org.getName())
                .collect(Collectors.toSet()));
        
        return String.join(", ", names);
    }
    
    /**
     * returns the set of all the AmpOrgIds associated with a Document
     * @param uuid - the document UUID
     * @return
     */
    public Set<Long> getDocToOrgIdsByUUID(String uuid) {
        List<AmpOrganisation> orgs = getCachedUuidToOrganisations()
                .getOrDefault(uuid, new ArrayList<AmpOrganisation>());
        
        Set<Long> orgIds = orgs.stream().map(AmpOrganisation::getAmpOrgId).collect(Collectors.toSet());
        
        return orgIds;
    }

    public List<AmpOrganisation> getOrganizationsByUUID(String uuid) {
        String publicVersionUuid = getUuidToPublicVersionUuid().getOrDefault(uuid, uuid);
        
        return getCachedUuidToOrganisations().getOrDefault(uuid, 
                getCachedUuidToOrganisations().getOrDefault(publicVersionUuid, new ArrayList<>()));
    }

    public List<AmpOrganisation> getAllUsedOrganisations() {
        List<AmpOrganisation> orgs = new ArrayList<>();
        
        orgs.addAll(getCachedUuidToOrganisations().values().stream()
                .flatMap(o -> o.stream())
                .collect(Collectors.toSet()));
        
        return orgs;
    }
    
    
    public List<CrDocumentsToOrganisations> getDocToOrgObjsByUuid(String uuid) {
        return PersistenceManager.getRequestDBSession()
                .createCriteria(CrDocumentsToOrganisations.class)
                .add(Restrictions.eq("uuid", uuid))
                .list();
    }
    
    public void deleteDocumentOrganization(String uuid, Long ampOrganisationId) {
        Session hbSession = PersistenceManager.getRequestDBSession();
        String queryString = "DELETE FROM " + CrDocumentsToOrganisations.class.getName() + " dto WHERE "
                + "dto.uuid=:uuid AND dto.ampOrganisation=:ampOrganisationId";
        Query query = hbSession.createQuery(queryString);
        query.setString("uuid", uuid);
        query.setLong("ampOrganisationId", ampOrganisationId);
        query.executeUpdate();

        invalidateChachedMap();
    }
    
    protected void invalidateChachedMap() {
        cachedUuidToOrganisations = null;
    }
    
    private Map<String, List<AmpOrganisation>> getCachedUuidToOrganisations() {
        if (cachedUuidToOrganisations == null) {
            updateCachedUuidToOrganizations();
        }
        
        return cachedUuidToOrganisations;
    }
    
    private void updateCachedUuidToOrganizations() {
        cachedUuidToOrganisations = new HashMap<>();
        
        List<CrDocumentsToOrganisations> docOrgs = PersistenceManager.getRequestDBSession()
                .createCriteria(CrDocumentsToOrganisations.class)
                .list();
        
        for (CrDocumentsToOrganisations docOrg : docOrgs) {
            if (cachedUuidToOrganisations.get(docOrg.getUuid()) == null) {
                cachedUuidToOrganisations.put(docOrg.getUuid(), new ArrayList<>());
            }
            
            cachedUuidToOrganisations.get(docOrg.getUuid()).add(docOrg.getAmpOrganisation());
        }
    }
    
    private Map<String, String> getUuidToPublicVersionUuid() {
        Map<String, String> publicVersionUuidToOringialUuid = new HashMap<>();
        List<CrDocumentNodeAttributes> docNodeAttrs = DocumentsNodesAttributeManager.getInstance()
                .getDocumentNodeAttributes();
        
        for (CrDocumentNodeAttributes docNodeAttr : docNodeAttrs) {
            publicVersionUuidToOringialUuid.put(docNodeAttr.getPublicVersionUUID(), docNodeAttr.getUuid());
        }
        
        return publicVersionUuidToOringialUuid;
    }
}
