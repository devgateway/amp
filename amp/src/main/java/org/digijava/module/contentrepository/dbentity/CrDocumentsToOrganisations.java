/**
 * 
 */
package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

/**
 * @author Alex Gartner
 *
 */
public class CrDocumentsToOrganisations extends ObjectReferringDocument {
    
    private Long id;
    private AmpOrganisation ampOrganisation;
    
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
