/**
 * 
 */
package org.dgfoundation.amp.onepager.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRole;

/**
 * @author mihai
 *read the list of roles from Related Organizations page, and create a unique Set with the roles chosen
 */
public class AmpRelatedRolesModel extends AbstractReadOnlyModel<List<AmpRole>> {
    
    private IModel<AmpActivityVersion> am;
    private String[] roleFilter;

    public AmpRelatedRolesModel(IModel<AmpActivityVersion> am, String[] roleFilter) {
        this.am=am;
        this.roleFilter=roleFilter;
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
     */
    @Override
    public List<AmpRole> getObject() {
        Set<AmpRole> set = new TreeSet<AmpRole>();
        Set<AmpOrgRole> orgroles = am.getObject().getOrgrole();
        if (orgroles == null || orgroles.size() == 0)
            return new ArrayList<AmpRole>();

        for (AmpOrgRole orgrole : orgroles) {
            if (roleFilter != null)
                for (int i = 0; i < roleFilter.length; i++) {
                    if (roleFilter[i].equals(orgrole.getRole().getRoleCode()))
                        set.add(orgrole.getRole());
                }
            else
                set.add(orgrole.getRole());
        }
        return new ArrayList<AmpRole>(set);
    }

    public String[] getRoleFilter() {
        return roleFilter;
    }

    public void setRoleFilter(String[] roleFilter) {
        this.roleFilter = roleFilter;
    }
    
}
