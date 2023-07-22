package org.digijava.module.budgetexport.dbentity;

import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapter;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:32 PM
 */
import javax.persistence.*;

@Entity
@Table(name = "amp_budget_export_map_rule")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpBudgetExportMapRule {
    public static final int CSV_COL_DELIMITER_COMA = 0;
    public static final int CSV_COL_DELIMITER_TAB = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_budget_export_map_rule_seq")
    @SequenceGenerator(name = "amp_budget_export_map_rule_seq", sequenceName = "amp_budget_export_map_rule_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "has_header")
    private boolean header;

    @Column(name = "allow_all")
    private boolean allowAllItem;

    @Column(name = "allow_none")
    private boolean allowNoneItem;

    @Column(name = "csv_col_delimiter")
    private int csvColDelimiter;

    @Column(name = "data_retriever_class")
    private String dataRetrieverClass;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project")
    private AmpBudgetExportProject project;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "amp_column")
    private AmpColumns ampColumn;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AmpBudgetExportMapItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AmpBudgetExportCSVItem> csvItems = new ArrayList<>();

    
    //Not persistent fields
    private int totalAmpEntityCount;

    
    
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

    public AmpBudgetExportProject getProject() {
        return project;
    }

    public void setProject(AmpBudgetExportProject project) {
        this.project = project;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public AmpColumns getAmpColumn() {
        return ampColumn;
    }

    public void setAmpColumn(AmpColumns ampColumn) {
        this.ampColumn = ampColumn;
    }

    public List<AmpBudgetExportMapItem> getItems() {
        return items;
    }

    public void setItems(List<AmpBudgetExportMapItem> items) {
        this.items = items;
    }

    public List<AmpBudgetExportCSVItem> getCsvItems() {
        return csvItems;
    }

    public void setCsvItems(List<AmpBudgetExportCSVItem> csvItems) {
        this.csvItems = csvItems;
    }

    public int getTotalAmpEntityCount() {
        return totalAmpEntityCount;
    }

    public void setTotalAmpEntityCount(int totalAmpEntityCount) {
        this.totalAmpEntityCount = totalAmpEntityCount;
    }

    public int getMappedAmpEntityCount() {
        return items != null ? items.size() : 0;
    }

    public int getCsvItemCount() {
        return csvItems != null ? csvItems.size() : 0;
    }

    public int getCsvColDelimiter() {
        return csvColDelimiter;
    }

    public void setCsvColDelimiter(int csvColDelimiter) {
        this.csvColDelimiter = csvColDelimiter;
    }

    public boolean isAllowNoneItem() {
        return allowNoneItem;
    }

    public void setAllowNoneItem(boolean allowNoneItem) {
        this.allowNoneItem = allowNoneItem;
    }

    public boolean isAllowAllItem() {
        return allowAllItem;
    }

    public void setAllowAllItem(boolean allowAllItem) {
        this.allowAllItem = allowAllItem;
    }

    public String getDataRetrieverClass() {
        return dataRetrieverClass;
    }

    public void setDataRetrieverClass(String dataRetrieverClass) {
        this.dataRetrieverClass = dataRetrieverClass;
    }
}
