package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class ImportedProjectCurrency implements Serializable {
    private Long id;
    private String importedProjectName;
    private String currencyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImportedProjectName() {
        return importedProjectName;
    }

    public void setImportedProjectName(String importedProjectName) {
        this.importedProjectName = importedProjectName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
