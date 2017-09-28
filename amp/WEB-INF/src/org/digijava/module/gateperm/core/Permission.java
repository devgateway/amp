/**
 * Permission.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.PropertyListable;

/**
 * Permission.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 25.08.2007
 */
public abstract class Permission extends PropertyListable implements Serializable, Comparable, ClusterIdentifiable {

    private static final long serialVersionUID = -1450990323894485519L;

    protected String      name;

    protected String      description;
    
    protected boolean dedicated;
    
    protected Set<PermissionMap> permissibleObjects;
    
    protected Set<CompositePermission> compositeLinkedPermissions;


    protected Long      id;
    

    public Permission() {   
    }
    
    public String getClusterIdentifier() {
        return name;
    }
    
    public Object getIdentifier() {
        return id;
    }
    
    
    
    public Permission(Boolean dedicated) {
    this.dedicated=dedicated;
    }

    public int compareTo(Object o) {
    Permission p = (Permission) o;
    return this.getName().compareTo(p.getName());
    }

    public abstract Set<String> getAllowedActions(Map scope);

    @Override
    @PropertyListableIgnore
    public String getBeanName() {
    return name;
    }

    public String getDescription() {
    return description;
    }

    public void setDescription(String description) {
    this.description = description;
    }

    @PropertyListableIgnore
    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }

    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;

    }

    public Set<CompositePermission> getCompositeLinkedPermissions() {
        return compositeLinkedPermissions;
    }

    public void setCompositeLinkedPermissions(Set<CompositePermission> reversedLinkedPermissions) {
        this.compositeLinkedPermissions = reversedLinkedPermissions;
    }
    
    public String toString() {
    return name+"{"+this.getClass().getSimpleName()+"}";
    }

    public Set<PermissionMap> getPermissibleObjects() {
        return permissibleObjects;
    }

    public void setPermissibleObjects(Set<PermissionMap> permissibles) {
        this.permissibleObjects = permissibles;
    }

    public boolean isDedicated() {
        return dedicated;
    }

    public void setDedicated(boolean dedicated) {
        this.dedicated = dedicated;
    }

    
}
