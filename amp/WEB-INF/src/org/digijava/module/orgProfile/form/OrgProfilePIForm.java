package org.digijava.module.orgProfile.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;

public class OrgProfilePIForm extends ActionForm{
    private List<ParisIndicatorHelper> indicators;
    private AmpOrganisation organization;

    public AmpOrganisation getOrganization() {
        return organization;
    }

    public void setOrganization(AmpOrganisation organization) {
        this.organization = organization;
    }
    private Long fiscalYear;

    public Long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

  

    public List<ParisIndicatorHelper> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<ParisIndicatorHelper> indicators) {
        this.indicators = indicators;
    }

}
