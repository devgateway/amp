package org.digijava.module.aim.helper.donorReport;

import java.util.List;

public class OrganizationReportRecord {
    private String organizationName;
    private Long orgId;
    private List<OrgProfileReportHelper> helpers;
    private boolean ngo;
    public boolean isNgo() {
        return ngo;
    }
    public void setNgo(boolean ngo) {
        this.ngo = ngo;
    }
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    public Long getOrgId() {
        return orgId;
    }
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
    public List<OrgProfileReportHelper> getHelpers() {
        return helpers;
    }
    public void setHelpers(List<OrgProfileReportHelper> helpers) {
        this.helpers = helpers;
    }
}
