package org.digijava.module.aim.dbentity;

import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.query.Query;
import org.hibernate.Session;

import javax.persistence.*;

@Entity
@Table(name = "AMP_ORGANISATION_DOCUMENT")
@Cacheable
public class AmpOrganisationDocument extends ObjectReferringDocument {
    public static final String SESSION_NAME = "ORGANISATION_DOCUMENTS";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORGANISATION_DOCUMENT_seq")
    @SequenceGenerator(name = "AMP_ORGANISATION_DOCUMENT_seq", sequenceName = "AMP_ORGANISATION_DOCUMENT_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "selected_version_uuid")
    private String selectedVersionUUID;

    @ManyToOne
    @JoinColumn(name = "amp_org_id")
    private AmpOrganisation ampOrganisation;

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
