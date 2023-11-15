package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;
import java.util.HashMap;
/*
 *@ Author Govind G Dalwani
 */
public class AddSectorForm extends ActionForm {

    private Collection subSectors = null;
    private Long parentId = null;
    private String levelType = null;
    
    //required 
    Collection formSectorSchemes;
    //poi
    
    Collection formFirstLevelSectors;
    Long secSchemeId;
    String secSchemeName;
    String secSchemeCode;
    boolean jspFlag = false;
    boolean deleteSchemeFlag = true;
    private String idGot;
        private String event;
        private Long ampSectorId;


    private Long sectorId = null;
    private Long parentSectorId = null;
    private String parentSectorName = null;
    private String sectorCode = null;
    private String sectorName = null;
    private String type = null;
    private Long ampOrganisationId = null;
    private String ampOrganisation = null;
    private String description = null;
    private HashMap organisationList = null;
    private String flag = null;
    private String sectorCodeOfficial = null;



    public String getSectorCodeOfficial() {
        return sectorCodeOfficial;
    }

    public void setSectorCodeOfficial(String sectorCodeOfficial) {
        this.sectorCodeOfficial = sectorCodeOfficial;
    }

    public boolean getJspFlag() {
        return jspFlag;
    }

    public void setJspFlag(boolean flag) {
        this.jspFlag = flag;
    }

    public String getIdGot() {
        return idGot;
    }

    public void setIdGot(String idGot) {
        this.idGot = idGot;
    }

        /*
     * get the Scheme COde
     */
        public String getSecSchemeCode() {
            return secSchemeCode;
        }

    /*
     * sets the Scheme Code
     */
        public void setSecSchemeCode(String secSchemeCode) {
            this.secSchemeCode = secSchemeCode;
        }

    /*
     * gets the Scheme Name
     */
        public String getSecSchemeName() {
            return secSchemeName;
        }

    /*
     * sets the Scheme Name
     */
        public void setSecSchemeName(String secSchemeName) {
            this.secSchemeName = secSchemeName;
        }

    /*
     * gets the first level sectors
     */
        public Collection getFormFirstLevelSectors() {
            return formFirstLevelSectors;
        }

    /*
     * sets the first level sectors
     */
        public void setFormFirstLevelSectors(Collection formFirstLevelSectors) {
            this.formFirstLevelSectors = formFirstLevelSectors;
        }

    /*
     * gets the Schemes
     */
        public Collection getFormSectorSchemes() {
            return formSectorSchemes;
        }

    /*
     * sets the schemes
     */
        public void setFormSectorSchemes(Collection formSectorSchemes) {
            this.formSectorSchemes = formSectorSchemes;
        }

    /*
     * gets the scheme Id
     */
        public Long getSecSchemeId() {
            return secSchemeId;
        }

    /*
     * sets the scheme Id
     */
        public void setSecSchemeId(Long secSchemeId) {
            this.secSchemeId = secSchemeId;
        }

        public boolean isDeleteSchemeFlag() {
            return deleteSchemeFlag;
        }

        public void setDeleteSchemeFlag(boolean deleteSchemeFlag) {
            this.deleteSchemeFlag = deleteSchemeFlag;
        }

public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

public Collection getSubSectors() {
        return subSectors;
    }

    public void setSubSectors(Collection subSectors) {
        this.subSectors = subSectors;
    }

    ///previously done
    public Long getSectorId() {
        return (this.sectorId);
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorCode() {
        return (this.sectorCode);
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;

    }

    public String getSectorName() {
        return (this.sectorName);
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmpOrganisationId() {
        return (this.ampOrganisationId);
    }

    public void setAmpOrganisationId(Long ampOrganisationId) {
        this.ampOrganisationId = ampOrganisationId;
    }

    public String getDescription() {
        return (this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentSectorId() {
        return (this.parentSectorId);
    }

    public void setParentSectorId(Long parentSectorId) {
        this.parentSectorId = parentSectorId;
    }

    public HashMap getOrganisationList() {
        return (this.organisationList);
    }

    public void setOrganisationList(HashMap organisationList) {
        this.organisationList = organisationList;
    }

    public String getAmpOrganisation() {
        return (this.ampOrganisation);
    }

    public void setAmpOrganisation(String ampOrganisation) {
        this.ampOrganisation = ampOrganisation;
    }

    public String getFlag() {
        return (this.flag);
    }

    public void setFlag(String flag)  {
        this.flag = flag;
    }

    public String getParentSectorName() {
              return (this.parentSectorName);
    }

  public String getEvent() {
    return event;
  }

  public Long getAmpSectorId() {
    return ampSectorId;
  }

  public void setParentSectorName(String parentSectorName) {
              this.parentSectorName = parentSectorName;
    }

  public void setEvent(String event) {
    this.event = event;
  }

  public void setAmpSectorId(Long ampSectorId) {
    this.ampSectorId = ampSectorId;
  }
}
