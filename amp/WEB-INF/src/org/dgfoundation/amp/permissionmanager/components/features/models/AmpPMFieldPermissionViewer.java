/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * @author dan
 *
 */
public class AmpPMFieldPermissionViewer implements Comparable, Serializable{

    /**
     * 
     */
    public AmpPMFieldPermissionViewer() {
        // TODO Auto-generated constructor stub
    }

    public AmpPMFieldPermissionViewer(PermissionMap pm) {
        this.fullName   = FeaturesUtil.getModuleNameVisibility(pm.getObjectIdentifier());
        this.path       = this.getPath(fullName);
        this.name       = this.getShortName(fullName);
        this.strategy   = getStringBuilder(pm).toString();
        this.id         = pm.getObjectIdentifier();
        this.pm         = pm;
    }

    private StringBuilder getStringBuilder(PermissionMap pm) {
        StringBuilder sb = new StringBuilder("");
        this.assignedPerms = PMUtil.getContentInfoFieldPermission(pm.getPermission(), sb);
        return sb;
    }

    private Long    id;
    private String fullName;
    private String name;
    private String path;
    private String strategy;
    private PermissionMap pm;
    private Set<AmpPMPermContentBean> assignedPerms;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    
    public String getPath(String s) {
        if(StringUtils.isNotEmpty(s) && s.contains("/"))
            return s.substring(0,s.lastIndexOf("/"));
        return s;
    }
    
    public String getShortName(String s) {
        if(StringUtils.isNotEmpty(s) && s.contains("/"))
            return s.substring(s.lastIndexOf("/")+1, s.length());
        return s;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        AmpPMFieldPermissionViewer oo = (AmpPMFieldPermissionViewer)o;
        return this.getId().compareTo(oo.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionMap getPm() {
        return pm;
    }

    public void setPm(PermissionMap pm) {
        this.pm = pm;
    }

    public Set<AmpPMPermContentBean> getAssignedPerms() {
        return assignedPerms;
    }

    public void setAssignedPerms(Set<AmpPMPermContentBean> assignedPerms) {
        this.assignedPerms = assignedPerms;
    }


    
}
