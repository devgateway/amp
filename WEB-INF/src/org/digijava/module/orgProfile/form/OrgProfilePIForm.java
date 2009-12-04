package org.digijava.module.orgProfile.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.orgProfile.helper.ParisIndicatorHelper;

public class OrgProfilePIForm extends ActionForm{
    private List<ParisIndicatorHelper> indicators;
    private String name;
    private Long fiscalYear;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
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
