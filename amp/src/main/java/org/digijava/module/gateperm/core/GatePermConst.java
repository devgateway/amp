/**
 * GateConstants.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * GateConstants.java 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 05.09.2007
 */
public final class GatePermConst {
    
    public static Class[] availableGatesSingleton=null;
    public static Hashtable<String,Class> availableGatesBySimpleNames = null;

    /**
     * list here all the available permissibles in the system. Since subclass search
     * through reflection is not supported by Java, we need a list with them
     * All Permissibles must extend the Permissible class
     */
    public static final Class[] availablePermissibles = new Class[] {AmpActivityVersion.class,
        AmpModulesVisibility.class,
        AmpFeaturesVisibility.class, 
        AmpFieldsVisibility.class };

    public static Hashtable<String,Class> availablePermissiblesBySimpleNames = null;
    
    /**
     * Add here all the new actions that you may need to implement. Do not
     * forget to add them into getImplementedActions for your Permissible
     * GateConstants.Actions TODO description here
     * 
     * @author mihai
     * @package org.digijava.module.gateperm.core
     * @since 05.09.2007
     */
    public static final class Actions {
        public static final String EDIT = "EDIT";

        public static final String NEW = "NEW";

        public static final String VIEW = "VIEW";

        public static final String PUBLISH = "PUBLISH";
    }
    
    public static final class ScopeKeys {
        public static final MetaInfo PERMISSIBLE=new MetaInfo("permissible","object to which the gate is associated");
        public static final MetaInfo ACTIVITY=new MetaInfo("activity","the currently edited activity");
        public static final MetaInfo CURRENT_MEMBER= new MetaInfo("currentMember","TeamMember object for the currently logged in user");
        public static final MetaInfo CURRENT_USER= new MetaInfo("currentUser","User object for the currently logged in user");
        public static final MetaInfo CURRENT_ORG= new MetaInfo("currentOrg","Current displayed organisation");
        public static final MetaInfo CURRENT_ORG_ROLE= new MetaInfo("currentOrgRole","Current displayed organisation role as string");
        public static final MetaInfo CURRENT_DONOR_ORGANISATION = new MetaInfo("currentDonorOrganisation",
                "Current donor organisation as LONG");
        public static final MetaInfo FUNDING_DETAIL= new MetaInfo("currentFundingDetail","The org.digijava.module.aim.helper.FundingDetail currently " +
                "in iteration in the funding popup");
        public static final MetaInfo CURRENT_REGION = new MetaInfo("currentRegion", "Current displayed region");
    }
    
    public static final Map<String,MetaInfo> scopeKeysMap 
    = Collections.unmodifiableMap(new HashMap<String,MetaInfo>() {{
        put(ScopeKeys.PERMISSIBLE.getCategory(), ScopeKeys.PERMISSIBLE);
        put(ScopeKeys.ACTIVITY.getCategory(), ScopeKeys.ACTIVITY);
        put(ScopeKeys.CURRENT_MEMBER.getCategory(), ScopeKeys.CURRENT_MEMBER);
        put(ScopeKeys.CURRENT_ORG.getCategory(), ScopeKeys.CURRENT_ORG);
        put(ScopeKeys.CURRENT_ORG_ROLE.getCategory(), ScopeKeys.CURRENT_ORG_ROLE);
        put(ScopeKeys.FUNDING_DETAIL.getCategory(), ScopeKeys.FUNDING_DETAIL);
    }});


    
    public static final String SCOPE="GATEPERM_SCOPE";
    
    public static final String ACTION_MODE="ACTION_MODE";
    
    public static final String UPDATED_PERMISSIONS="updatedPermissions";
    public static final String ADDED_PERMISSIONS="addedPermissions";
}
