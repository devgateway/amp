package org.digijava.module.budgetexport.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;
import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:20 PM
 */
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "amp_budget_export_project")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpBudgetExportProject {
    public static final int DATA_SOURCE_CSV = 0;
    public static final int DATA_SOURCE_SERVICE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_budget_export_project_seq")
    @SequenceGenerator(name = "amp_budget_export_project_seq", sequenceName = "amp_budget_export_project_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "report_id")
    private Long ampReportId;

    @Column(name = "mapping_service_url")
    private String mappingImportServiceURL;

    @Column(name = "service_action_url")
    private String serviceActionURL;

    @Column(name = "data_source")
    private int dataSource;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AmpBudgetExportMapRule> rules = new ArrayList<>();


    
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
