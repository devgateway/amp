package org.digijava.kernel.ampapi.endpoints.indicator.manager;

public class ResponsibleOrgDTO {
    private Long orgId;
    private String orgName;

    public ResponsibleOrgDTO(Long orgId, String orgName) {
        this.orgId = orgId;
        this.orgName = orgName;
    }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
}

