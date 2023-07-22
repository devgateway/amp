/**
 * 
 */
package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Alex Gartner
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "CR_DOCUMENTS_TO_ORGANISATIONS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CrDocumentsToOrganisations extends ObjectReferringDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "amp_organisation_id")
    private AmpOrganisation ampOrganisation;

    @Column(name = "document_uuid")
    private String uuid;

    
    public CrDocumentsToOrganisations(){
        ;
    }

    public CrDocumentsToOrganisations(String uuid,
            AmpOrganisation ampOrganisation) {
        super(uuid);
        this.ampOrganisation = ampOrganisation;
    }

    /**
     * @return the ampOrganisation
     */
    public AmpOrganisation getAmpOrganisation() {
        return ampOrganisation;
    }

    /**
     * @param ampOrganisation the ampOrganisation to set
     */
    public void setAmpOrganisation(AmpOrganisation ampOrganisation) {
        this.ampOrganisation = ampOrganisation;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    
}
