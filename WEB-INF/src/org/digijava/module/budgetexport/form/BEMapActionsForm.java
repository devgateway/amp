package org.digijava.module.budgetexport.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;

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
    private FormFile upload;

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
}
