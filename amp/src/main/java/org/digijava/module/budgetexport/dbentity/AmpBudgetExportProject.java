package org.digijava.module.budgetexport.dbentity;

import java.util.Date;
import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:20 PM
 */
public class AmpBudgetExportProject {
    public static final int DATA_SOURCE_CSV = 0;
    public static final int DATA_SOURCE_SERVICE = 1;

    
    private Long id;
    private String name;
    private String description;
    private Date creationDate;
    private List<AmpBudgetExportMapRule> rules;
    private Long ampReportId;
    private String mappingImportServiceURL;
    private String serviceActionURL;
    private int dataSource;
    
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

    public String getMappingImportServiceURL() {
        return mappingImportServiceURL;
    }

    public void setMappingImportServiceURL(String mappingImportServiceURL) {
        this.mappingImportServiceURL = mappingImportServiceURL;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public String getServiceActionURL() {
        return serviceActionURL;
    }

    public void setServiceActionURL(String serviceActionURL) {
        this.serviceActionURL = serviceActionURL;
    }
}
