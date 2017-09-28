/*
 * AMP FEATURE TEMPLATES
 */
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpModulesVisibility extends AmpObjectVisibility implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 292612393819900427L;

    /**
     * @author dan
     */
    private Set<AmpModulesVisibility> submodules;

    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getParent()
     */
    public AmpObjectVisibility getParent() {
        return super.parent;
    }

    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getVisible()
     */
    public String getVisible() {
        return getTemplate().getItems().contains(this) ? "true" : "false";
    }

    /**
     * @param id
     * @return
     * @deprecated does not provide the latest state when outside Admin session, use FeaturesUtil
     */
    public boolean isVisibleId(Long id) {
        for (Iterator it = this.getTemplates().iterator(); it.hasNext();) {
            AmpTemplatesVisibility x = (AmpTemplatesVisibility) it.next();
            if (x.getId().compareTo(id) == 0)
                return true;

        }
        return false;
    }

    /**
     * @param aObjVis
     * @return
     * @deprecated does not provide the latest state when outside Admin session, use FeaturesUtil
     */
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis) {
        for (Iterator it = aObjVis.getItems().iterator(); it.hasNext();) {
            AmpModulesVisibility x = (AmpModulesVisibility) it.next();
            if (x.getId().compareTo(id) == 0)
                return true;

        }
        return false;
    }

    /**
     * @see org.dgfoundation.amp.visibility.AmpObjectVisibility#getTemplate()
     */
    public AmpTemplatesVisibility getTemplate() {
        return (AmpTemplatesVisibility) parent;
    }

    /**
     * @return
     */
    public TreeSet getSortedAlphaSubModules() {
        TreeSet mySet = new TreeSet(FeaturesUtil.ALPHA_ORDER);
        mySet.addAll(submodules);
        return mySet;
    }

    /**
     * @return
     */
    public Set<AmpModulesVisibility> getSubmodules() {
        return submodules;
    }

    /**
     * @param submodules
     */
    public void setSubmodules(Set<AmpModulesVisibility> submodules) {
        this.submodules = submodules;
    }


    /**
     * @see org.digijava.module.gateperm.core.Permissible#getPermissibleCategory()
     */
    @Override
    public Class getPermissibleCategory() {
        return AmpModulesVisibility.class;
    }

}
