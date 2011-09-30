package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 9/30/11
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdditionalGraphicsForm extends ActionForm {
    private String actionType;

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
