package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportCSVItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.util.AmpEntityMappedItem;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/4/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BEMapActionsForm extends ActionForm {
    private Long id;
    private Long ruleId;
    private Long projectId;
    private AmpBudgetExportMapRule rule;
    private List<AmpBudgetExportMapItem> mapItems;
    private List<HierarchyListable> ampEntities;
    private List<AmpBudgetExportCSVItem> csvItems;
    private List<AmpEntityMappedItem> ampEntityMappedItems;


    private List<HierarchyListable> additionalItems; //for All/None options


    private FormFile upload;
    private boolean noReload;
    int delimiter;
    private boolean additionalLabelCol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public List<AmpBudgetExportMapItem> getMapItems() {
        return mapItems;
    }

    public void setMapItems(List<AmpBudgetExportMapItem> mapItems) {
        this.mapItems = mapItems;
    }

    public FormFile getUpload() {
        return upload;
    }

    public void setUpload(FormFile upload) {
        this.upload = upload;
    }

    public AmpBudgetExportMapRule getRule() {
        return rule;
    }

    public void setRule(AmpBudgetExportMapRule rule) {
        this.rule = rule;
    }

    public boolean isNoReload() {
        return noReload;
    }

    public void setNoReload(boolean noReload) {
        this.noReload = noReload;
    }

    public List<HierarchyListable> getAmpEntities() {
        return ampEntities;
    }

    public void setAmpEntities(List<HierarchyListable> ampEntities) {
        this.ampEntities = ampEntities;
    }

    public List<AmpBudgetExportCSVItem> getCsvItems() {
        return csvItems;
    }

    public void setCsvItems(List<AmpBudgetExportCSVItem> csvItems) {
        this.csvItems = csvItems;
    }

    public List<AmpEntityMappedItem> getAmpEntityMappedItems() {
        return ampEntityMappedItems;
    }

    public void setAmpEntityMappedItems(List<AmpEntityMappedItem> ampEntityMappedItems) {
        this.ampEntityMappedItems = ampEntityMappedItems;
    }

    public List<HierarchyListable> getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(List<HierarchyListable> additionalItems) {
        this.additionalItems = additionalItems;
    }

    public int getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(int delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isAdditionalLabelCol() {
        return additionalLabelCol;
    }

    public void setAdditionalLabelCol(boolean hasAdditionalLabelCol) {
        this.additionalLabelCol = hasAdditionalLabelCol;
    }
}
