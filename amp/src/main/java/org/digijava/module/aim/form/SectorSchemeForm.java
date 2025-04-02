
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
/*
 *@author Govind G Dalwani 
 */
public class SectorSchemeForm extends ActionForm {

    Collection formSectorSchemes;
    Collection formFirstLevelSectors;
    Long secSchemeId;
    String secSchemeName;
    String secSchemeCode;
    boolean deleteSchemeFlag = true;

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

    
}
