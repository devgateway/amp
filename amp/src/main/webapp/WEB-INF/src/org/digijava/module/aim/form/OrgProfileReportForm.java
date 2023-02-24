package org.digijava.module.aim.form;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.donorReport.DonorReportHelper;
import org.digijava.module.aim.helper.donorReport.OrganizationReportRecord;

public class OrgProfileReportForm  extends ActionForm {
    private static final long serialVersionUID = 1L;
    private String[] columns;
    private String[] selectedColumns;
    private String action;
    private List<OrganizationReportRecord> records;
    private List<AmpOrganisation> organizations;
    private Collection<AmpOrgType> orgTypes;
    private Collection<AmpOrgGroup> groups;
    private Long selectedOrgId;
    private Long selectedOrgGropId;
    private Long selectedTypeId;
    private List<AmpOrganisation> selectedOrganizations;
    private HashMap<String, DonorReportHelper> map;
    private String selectedColumnsList;
    //ISSUE AMP-16507
    private Long donorType;
    private Long orgGroup;
    private String actionFlag;
    
    
    
    public Long getOrgGroup() {
        return orgGroup;
    }
    public void setOrgGroup(Long orgGroup) {
        this.orgGroup = orgGroup;
    }
    public String getActionFlag() {
        return actionFlag;
    }
    public void setActionFlag(String actionFlag) {
        this.actionFlag = actionFlag;
    }
    public Long getDonorType() {
        return donorType;
    }
    public void setDonorType(Long donorType) {
        this.donorType = donorType;
    }
    public String getSelectedColumnsList() {
        return selectedColumnsList;
    }
    public void setSelectedColumnsList(String selectedColumnsList) {
        this.selectedColumnsList = selectedColumnsList;
    }
    public HashMap<String, DonorReportHelper> getMap() {
        return map;
    }
    public void setMap(HashMap<String, DonorReportHelper> map) {
        this.map = map;
    }
    public List<AmpOrganisation> getSelectedOrganizations() {
        return selectedOrganizations;
    }
    public void setSelectedOrganizations(List<AmpOrganisation> selectedOrganizations) {
        this.selectedOrganizations = selectedOrganizations;
    }
    public List<AmpOrganisation> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }
    public Collection<AmpOrgType> getOrgTypes() {
        return orgTypes;
    }
    public void setOrgTypes(Collection<AmpOrgType> orgTypes) {
        this.orgTypes = orgTypes;
    }
    public Collection<AmpOrgGroup> getGroups() {
        return groups;
    }
    public void setGroups(Collection<AmpOrgGroup> groups) {
        this.groups = groups;
    }
    public Long getSelectedOrgId() {
        return selectedOrgId;
    }
    public void setSelectedOrgId(Long selectedOrgId) {
        this.selectedOrgId = selectedOrgId;
    }
    public Long getSelectedOrgGropId() {
        return selectedOrgGropId;
    }
    public void setSelectedOrgGropId(Long selectedOrgGropId) {
        this.selectedOrgGropId = selectedOrgGropId;
    }
    public Long getSelectedTypeId() {
        return selectedTypeId;
    }
    public void setSelectedTypeId(Long selectedTypeId) {
        this.selectedTypeId = selectedTypeId;
    }

    public List<OrganizationReportRecord> getRecords() {
        return records;
    }
    public void setRecords(List<OrganizationReportRecord> records) {
        this.records = records;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String[] getColumns() {
        return columns;
    }
    public void setColumns(String[] columns) {
        this.columns = columns;
    }
    public String[] getSelectedColumns() {
        return selectedColumns;
    }
    public void setSelectedColumns(String[] selectedColumns) {
        this.selectedColumns = selectedColumns;
    }
    
    
}
