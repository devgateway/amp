package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import java.util.Collection;
  
public class FlagUploaderForm extends ActionForm {
    
    private Long countryId;
    private Collection countries;
    private FormFile flagFile;
    private Collection cntryFlags;
    
    /**
     * @return Returns the countries.
     */
    public Collection getCountries() {
        return countries;
    }
    /**
     * @param countries The countries to set.
     */
    public void setCountries(Collection countries) {
        this.countries = countries;
    }
    /**
     * @return Returns the countryId.
     */
    public Long getCountryId() {
        return countryId;
    }
    /**
     * @param countryId The countryId to set.
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    /**
     * @return Returns the flagFile.
     */
    public FormFile getFlagFile() {
        return flagFile;
    }
    /**
     * @param flagFile The flagFile to set.
     */
    public void setFlagFile(FormFile flagFile) {
        this.flagFile = flagFile;
    }
    /**
     * @return Returns the cntryFlags.
     */
    public Collection getCntryFlags() {
        return cntryFlags;
    }
    /**
     * @param cntryFlags The cntryFlags to set.
     */
    public void setCntryFlags(Collection cntryFlags) {
        this.cntryFlags = cntryFlags;
    }

}
