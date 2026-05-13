/**
 * PermissionForm.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.gateperm.core.Permission;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * PermissionForm.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.gateperm.form
 * @since 04.09.2007
 */
public class PermissionForm extends ActionForm {
    private String name;
    private String description;
    private String type;
    private Long id;
    private Boolean intersection;
    
    public Boolean getIntersection() {
        return intersection;
    }

    public void setIntersection(Boolean intersection) {
        this.intersection = intersection;
    }

    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
        super.reset(mapping, request);
        intersection=false;
    }



    private Long[] permissions;
    private String gateTypeName;
    private List<MetaInfo> gateParameters;
    private String[] actions;
    
    private String mode;
    
    private List<Class> _availableGateTypes;
    private List<BeanWrapperImpl> _availableActions;
    private List<Permission> _availablePermissions;
    
    
    public List<Permission> get_availablePermissions() {
        return _availablePermissions;
    }

    public void List_availablePermissions(List<Permission> permissions) {
        _availablePermissions = permissions;
    }
    
    public void clear() {
        permissions=null;
        gateTypeName="";
        gateParameters= new ArrayList<MetaInfo>();
        actions=null;
        name="";
        description="";
        mode="";
        id=new Long(0);
        type="Gate";
        intersection=false;
    }

    public MetaInfo getGateParameter(int index) {
        return gateParameters.get(index);
    }


    
    
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = -4018921971886803673L;


    public List<BeanWrapperImpl> get_availableActions() {
        return _availableActions;
    }

    public void set_availableActions(List<BeanWrapperImpl> actions2) {
        _availableActions = actions2;
    }

    public List<Class> get_availableGateTypes() {
        return _availableGateTypes;
    }

    public void set_availableGateTypes(List<Class> gateTypes) {
        _availableGateTypes = gateTypes;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MetaInfo> getGateParameters() {
        return gateParameters;
    }

    public void setGateParameters(List<MetaInfo> gateParameters) {
        this.gateParameters = gateParameters;
    }

    public String getGateTypeName() {
        return gateTypeName;
    }

    public void setGateTypeName(String gateTypeName) {
        this.gateTypeName = gateTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long[] getPermissions() {
        return permissions;
    }

    public void setPermissions(Long[] permissions) {
        this.permissions = permissions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void set_availablePermissions(List<Permission> permissions) {
        _availablePermissions = permissions;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
