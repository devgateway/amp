package org.digijava.kernel.lucene;

import org.digijava.module.aim.dbentity.AmpContentTranslation;

import java.util.List;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class ActivityLuceneDocument {

    private String ampActivityId;
    
    private String projectId;
    
    private String name;
    
    private String description;
    
    private String objective;
    
    private String purpose;
    
    private String results;
    
    private String contactName;
    
    private String crisNumber;
    
    private String budgetCodeProjectId;
    
    private String budgetCodes;
    
    private List<String> componentCodes;
    
    private List<AmpContentTranslation> translations;

    public String getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(String ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCrisNumber() {
        return crisNumber;
    }

    public void setCrisNumber(String crisNumber) {
        this.crisNumber = crisNumber;
    }

    public String getBudgetCodeProjectId() {
        return budgetCodeProjectId;
    }

    public void setBudgetCodeProjectId(String budgetCodeProjectId) {
        this.budgetCodeProjectId = budgetCodeProjectId;
    }

    public String getBudgetCodes() {
        return budgetCodes;
    }

    public void setBudgetCodes(String budgetCodes) {
        this.budgetCodes = budgetCodes;
    }

    public List<String> getComponentCodes() {
        return componentCodes;
    }

    public void setComponentCodes(List<String> componentCodes) {
        this.componentCodes = componentCodes;
    }

    public List<AmpContentTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<AmpContentTranslation> translations) {
        this.translations = translations;
    }

}
