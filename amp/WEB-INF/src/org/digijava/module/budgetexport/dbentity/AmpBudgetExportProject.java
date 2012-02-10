package org.digijava.module.budgetexport.dbentity;

import java.util.Date;
import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:20 PM
 */
public class AmpBudgetExportProject {
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private List<AmpBudgetExportMapRule> rules;
    private Long ampReportId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<AmpBudgetExportMapRule> getRules() {
        return rules;
    }

    public void setRules(List<AmpBudgetExportMapRule> rules) {
        this.rules = rules;
    }

    public Long getAmpReportId() {
        return ampReportId;
    }

    public void setAmpReportId(Long ampReportId) {
        this.ampReportId = ampReportId;
    }
}
