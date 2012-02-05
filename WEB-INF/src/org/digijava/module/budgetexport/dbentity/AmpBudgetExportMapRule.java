package org.digijava.module.budgetexport.dbentity;

import org.digijava.module.aim.dbentity.AmpColumns;

import java.util.List;

/**
 * User: flyer
 * Date: 2/2/12
 * Time: 5:32 PM
 */
public class AmpBudgetExportMapRule {
    private Long id;
    private String name;
    private AmpBudgetExportProject project;
    private boolean header;
    private AmpColumns ampColumn;
    private List<AmpBudgetExportMapItem> items;

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
}
