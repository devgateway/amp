/**
 * CompositePermission.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * CompositePermission.java
 * This is a permission object that consists of a group of other Permission objects
 * The actions allowed by this Permission object would be the union/intersection of all actions
 * added in this Composite. This is a convenience object that allows user to 
 * quickly group permissions and add the group directly to objects instead of
 * each permission separately
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 25.08.2007
 */
public class CompositePermission extends Permission {
    
    private static final long serialVersionUID = 7116433013946849925L;
    protected Boolean intersection;
    protected Set<Permission> permissions;

    public CompositePermission() {
        permissions=new TreeSet<Permission>();
        dedicated=false;
    }
    
    public CompositePermission(boolean dedicated) {
        super(dedicated);
        permissions=new TreeSet<Permission>();
    }
    
    /** @see org.digijava.module.gateperm.core.Permission#getAllowedActions()
     */
    @Override
    public Set<String> getAllowedActions(Map scope) {
        TreeSet<String> actions=new TreeSet<String>();
        boolean firstRun=true;
        Iterator<Permission> i=permissions.iterator();
        while (i.hasNext()) {
            Permission element = (Permission) i.next();
            Set<String> allowedActions = element.getAllowedActions(scope);
            if(intersection==null || !intersection.booleanValue()) {
            if(allowedActions!=null) 
                    actions.addAll(allowedActions);
            } else {
                if(firstRun) {
                        if(allowedActions!=null) actions.addAll(allowedActions);firstRun=false;
                    }
                    else if(allowedActions!=null) actions.retainAll(allowedActions); else actions.clear();
            }
            
        }
        return actions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Boolean getIntersection() {
        return intersection;
    }

    public void setIntersection(Boolean intersection) {
        this.intersection = intersection;
    }

}
