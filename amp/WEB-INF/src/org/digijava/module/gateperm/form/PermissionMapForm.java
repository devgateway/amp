/**
 * PermissionForm.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * PermissionForm.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.form
 * @since 04.09.2007
 */
public class PermissionMapForm extends ActionForm {

    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 6352329100263590016L;

    private String      permissibleCategory;

    private String      objectIdentifier;
    
    private Map<String,Class>       _permissibleCategoriesMap;
   
    private Long          permissionId;

    private List<Class>       _availablePermissibleCategories;

    private List<PermissionMap> permissionMaps;

    private List<Permission>  _availablePermissions;

    private String      mode;

    public String getMode() {
    return mode;
    }

    public void setMode(String mode) {
    this.mode = mode;
    }

    public void clear() {
    permissionId = null;
    permissibleCategory = null;
    objectIdentifier = null;
    _permissibleCategoriesMap=new HashMap<String, Class>();
    permissionMaps=new ArrayList<PermissionMap>();
    }

    public List<Class> get_availablePermissibleCategories() {
    return _availablePermissibleCategories;
    }

    public void set_availablePermissibleCategories(List<Class> availablePermissibleCategories) {
    _availablePermissibleCategories = availablePermissibleCategories;
    }

    public List<PermissionMap> getPermissionMaps() {
        return permissionMaps;
    }

    public List<Permission> get_availablePermissions() {
    return _availablePermissions;
    }

    public void set_availablePermissions(List<Permission> permissions) {
    _availablePermissions = permissions;
    }

    public String getPermissibleCategory() {
    return permissibleCategory;
    }

    public void setPermissibleCategory(String permissibleCategory) {
    this.permissibleCategory = permissibleCategory;
    }

    public Long getPermissionId() {
    return permissionId;
    }

    public void setPermissionId(Long permissionId) {
    this.permissionId = permissionId;
    }

    public String getObjectIdentifier() {
    return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
    this.objectIdentifier = objectIdentifier;
    }

    public PermissionMap getPermissionMap(int index) {  
    return permissionMaps.get(index);
}

    public void setPermissionMaps(List<PermissionMap> permissionMaps) {
        this.permissionMaps = permissionMaps;
    }

    public Map<String, Class> get_permissibleCategoriesMap() {
        return _permissibleCategoriesMap;
    }

    public void set_permissibleCategoriesMap(Map<String, Class> categoriesMap) {
        _permissibleCategoriesMap = categoriesMap;
    }

    
}
