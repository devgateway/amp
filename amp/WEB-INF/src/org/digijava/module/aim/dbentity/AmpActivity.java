/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity ;

import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.HierarchyListable;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
@Entity
@Table(name = "amp_activity")
@Polymorphism(type = PolymorphismType.EXPLICIT)
@Cacheable
public class AmpActivity extends AmpActivityVersion implements Cloneable, HierarchyListable {

    /**
     * 
     * NOTE:
     *    You shouldn't have the need to add new fields here.
     *    This class should be identical with AmpActivityVersion
     * 
     */
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }


    //methods for HierarchyListable interface
    public String getLabel() {
        return getName();
    }
    public String getUniqueId() {
        return String.valueOf(getAmpActivityId().longValue());
    }
    public String getAdditionalSearchString() {
        //Used by budget export module
        Set<AmpOrgRole> relOrgs = getOrgrole();
        StringBuilder relOrgsStr = new StringBuilder();
        if (relOrgs == null || relOrgs.isEmpty()) {
            relOrgsStr.append("None");
        } else {
            boolean hasBenefitiary = false;
            for (AmpOrgRole org : relOrgs) {
                if (org.getRole().getRoleCode().equalsIgnoreCase("BA")) {
                    if (relOrgsStr.length() > 0) {
                        relOrgsStr.append(", ");
                    }
                    relOrgsStr.append(org.getOrganisation().getName());
                    hasBenefitiary = true;
                }
            }

            if (!hasBenefitiary) {
                relOrgsStr.append("None");
            }
        }

        return relOrgsStr.toString();
    }
    public boolean getTranslateable() {
        return false;
    }
    public void setTranslateable(boolean translateable){}
    public Collection<? extends HierarchyListable> getChildren() {
        return null;
    }
    public int getCountDescendants() {
        return 0;
    }



}

