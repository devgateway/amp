package org.digijava.module.contentrepository.dbentity;

import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "CR_DOCUMENT_NODE_ATTRIBUTES")
@Cacheable
public class CrDocumentNodeAttributes extends ObjectReferringDocument {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "public_version_uuid")
    private String publicVersionUUID;

    @Column(name = "public_document")
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
