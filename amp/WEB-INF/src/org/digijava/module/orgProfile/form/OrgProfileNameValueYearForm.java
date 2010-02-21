
package org.digijava.module.orgProfile.form;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.orgProfile.helper.NameValueYearHelper;

public class OrgProfileNameValueYearForm extends ActionForm {
    
    private List<NameValueYearHelper> values;
    private int type;

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
