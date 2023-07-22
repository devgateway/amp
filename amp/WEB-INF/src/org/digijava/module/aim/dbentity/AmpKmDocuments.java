package org.digijava.module.aim.dbentity ;

import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "AMP_KM_DOCUMENTS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpKmDocuments
{
    private static Logger logger = Logger.getLogger(AmpKmDocuments.class) ;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_KM_DOCUMENTS_seq")
    @SequenceGenerator(name = "AMP_KM_DOCUMENTS_seq", sequenceName = "AMP_KM_DOCUMENTS_seq", allocationSize = 1)
    @Column(name = "amp_km_id")
    private Long ampKmId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "version")
    private String version;

    @ManyToOne
    @JoinColumn(name = "document_type", referencedColumnName = "id")
    private AmpCategoryValue documentType;
    private Set activities;

    /**
     * @return
     */
    public Set getActivities() {
        return activities;
    }

    /**
     * @return
     */
    public Long getAmpKmId() {
        return ampKmId;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param set
     */
    public void setActivities(Set set) {
        activities = set;
    }

    /**
     * @param long1
     */
    public void setAmpKmId(Long long1) {
        ampKmId = long1;
    }
    
    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setLanguage(String string) {
        language = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    /**
     * @param string
     */
    public void setVersion(String string) {
        version = string;
    }

    public AmpCategoryValue getDocumentType() {
        return documentType;
    }

    public void setDocumentType(AmpCategoryValue documentType) {
        this.documentType = documentType;
    }

}
