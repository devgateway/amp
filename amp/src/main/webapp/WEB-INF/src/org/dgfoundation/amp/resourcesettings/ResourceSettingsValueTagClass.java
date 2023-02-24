package org.dgfoundation.amp.resourcesettings;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.digijava.module.aim.util.ResourceManagerSettingsUtil;

public class ResourceSettingsValueTagClass extends BodyTagSupport {

    private String name = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            String settingValue = ResourceManagerSettingsUtil.getResourceSettingValue(name);
            JspWriter out = pageContext.getOut();

            if (settingValue != null) {
                out.print(settingValue);
            }

            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspException("Failed to write resource setting value", e);
        }
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
