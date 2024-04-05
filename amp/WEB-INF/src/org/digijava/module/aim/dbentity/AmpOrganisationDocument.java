package org.digijava.module.aim.dbentity;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AmpOrganisationDocument extends ObjectReferringDocument {
    public static final String SESSION_NAME = "ORGANISATION_DOCUMENTS";
    
    private Long id;
    private AmpOrganisation ampOrganisation;
    private String selectedVersionUUID;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AmpOrganisation getAmpOrganisation() {
        return ampOrganisation;
    }
    public void setAmpOrganisation(AmpOrganisation ampOrganisation) {
        this.ampOrganisation = ampOrganisation;
    }
    public String getSelectedVersionUUID() {
        return selectedVersionUUID;
    }
    public void setSelectedVersionUUID(String selectedVersionUUID) {
        this.selectedVersionUUID = selectedVersionUUID;
    }
    
    
    @Override
    protected void detach() {
        this.ampOrganisation.getDocuments().remove(this);
        this.ampOrganisation    = null;
    }
    
    public static String getSingleDocumentUuidByOrganisation (Long ampOrgId) {
        List<AmpOrganisationDocument> list  = AmpOrganisationDocument.findDocumentsByOrganisation(ampOrgId);
        if ( list!=null && list.size()==1 )
            return list.get(0).getUuid();
        
        return null;
        
    }
    
    public static List<AmpOrganisationDocument> findDocumentsByOrganisation (Long ampOrgId) {
        Session session;
        try{
            session             = PersistenceManager.getRequestDBSession();
            String queryString  = "select doc from " + AmpOrganisationDocument.class.getName() + " doc " +
                    "where doc.ampOrganisation=:ampOrgId";
            Query query         = session.createQuery(queryString);
            query.setLong("ampOrgId", ampOrgId);
            List<AmpOrganisationDocument> res       = query.list();
            
            return res;
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return null;
        
    }
    
}
