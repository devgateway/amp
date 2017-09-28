package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

public class DataFreezeManagerForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = -5620829792660554156L;
    private boolean enabled;
    private Integer gracePeriod;
    private String action;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
