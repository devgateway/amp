

package org.digijava.module.orgProfile.form;

import org.apache.struts.action.ActionForm;


public class OrganizationSummaryForm extends ActionForm{
    private String orgBackground;
    private String orgDescription;
    private Long orgId;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrgBackground() {
        return orgBackground;
    }

    public void setOrgBackground(String orgBackground) {
        this.orgBackground = orgBackground;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }


}
