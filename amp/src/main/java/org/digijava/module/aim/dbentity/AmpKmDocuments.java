package org.digijava.module.aim.dbentity ;

import org.apache.log4j.Logger;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import java.util.Set;

public class AmpKmDocuments
{
    private static Logger logger = Logger.getLogger(AmpKmDocuments.class) ;
    
    private Long ampKmId ;
    private String name ;
    private String type; 
    private String description ;
    private String language ;
    private String version ;
    private Set activities ;
    private AmpCategoryValue documentType;
    

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
