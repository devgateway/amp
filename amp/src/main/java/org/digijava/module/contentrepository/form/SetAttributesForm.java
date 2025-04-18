/**
 * 
 */
package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Alex Gartner
 *
 */
public class SetAttributesForm extends ActionForm {

    private String uuid;
    private String action;
    private String type;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SetAttributesForm() {
        uuid    = null;
        action  = null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
    
}
