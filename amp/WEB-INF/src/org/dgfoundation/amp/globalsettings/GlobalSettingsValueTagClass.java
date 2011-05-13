package org.dgfoundation.amp.globalsettings;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.digijava.module.aim.util.FeaturesUtil;

public class GlobalSettingsValueTagClass extends BodyTagSupport {

    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } 
    
    @Override
    public int doStartTag() throws JspException {
        String settingValue = FeaturesUtil.getGlobalSettingValue(name);
        JspWriter out = pageContext.getOut();
        try {
            if (settingValue != null) {
                out.print(settingValue);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
