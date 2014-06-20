/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity ;

import java.util.Collection;
import java.util.Set;

import org.digijava.module.aim.util.HierarchyListable;

/**
 * @author nmandrescu
 * 
 */
public class AmpActivityExpanded extends AmpActivity implements Cloneable, HierarchyListable {

	private String expandedDescription;
	
	public String getExpandedDescription() {
		return expandedDescription;
	}


	public void setExpandedDescription(String expandedDescription) {
		this.expandedDescription = expandedDescription;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return (AmpActivityExpanded) super.clone();
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

