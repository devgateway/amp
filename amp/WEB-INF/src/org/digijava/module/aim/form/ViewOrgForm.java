/*
 * Created on 12/04/2005
 * @author akashs
 *
 */
package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

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
