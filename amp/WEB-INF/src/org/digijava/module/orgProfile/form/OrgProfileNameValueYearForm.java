
package org.digijava.module.orgProfile.form;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.orgProfile.helper.NameValueYearHelper;

public class OrgProfileNameValueYearForm extends ActionForm {
    
    private List<NameValueYearHelper> values;
    private int type;
    private Long sectorClassConfigId;

    public Long getSectorClassConfigId() {
        return sectorClassConfigId;
    }

    public void setSectorClassConfigId(Long sectorClassConfigId) {
        this.sectorClassConfigId = sectorClassConfigId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public List<NameValueYearHelper> getValues() {
        return values;
    }

    public void setValues(List<NameValueYearHelper> values) {
        this.values = values;
    }

}
