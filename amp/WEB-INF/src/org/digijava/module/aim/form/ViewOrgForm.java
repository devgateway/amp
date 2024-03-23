/*
 * Created on 12/04/2005
 * @author akashs
 *
 */
package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;

import java.util.Collection;

public class ViewOrgForm extends ActionForm {
    
    private Collection org = null;
    
    /**
     * @return Returns the org.
     */
    public Collection getOrg() {
        return org;
    }
    /**
     * @param org The org to set.
     */
    public void setOrg(Collection org) {
        this.org = org;
    }
}
