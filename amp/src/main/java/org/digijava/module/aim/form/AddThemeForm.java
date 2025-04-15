package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @deprecated
 *
 */
public class AddThemeForm extends ActionForm {

    private Long themeId = null;
    private String themeCode = null;
    private String themeName = null;
    private String type = null;
    private String description = null;
    private String flag = null;

    public Long getThemeId() {
        return (this.themeId);
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getThemeCode() {
        return (this.themeCode);
    }

    public void setThemeCode(String themeCode) {
        this.themeCode = themeCode;
    }

    public String getThemeName() {
        return (this.themeName);
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return (this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlag() {
        return (this.flag);
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
