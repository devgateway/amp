package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.contentrepository.helper.ObjectReferringDocument;

public class CrDocumentNodeAttributes extends ObjectReferringDocument {
    
    private String publicVersionUUID;
    private Boolean publicDocument;

    public CrDocumentNodeAttributes() {
        publicVersionUUID = null;
        publicDocument = false;
    }

    public Boolean getPublicDocument() {
        return publicDocument;
    }

    public void setPublicDocument(Boolean publicDocument) {
        this.publicDocument = publicDocument;
    }

    public String getPublicVersionUUID() {
        return publicVersionUUID;
    }

    public void setPublicVersionUUID(String publicVersionUUID) {
        this.publicVersionUUID = publicVersionUUID;
    }
}
