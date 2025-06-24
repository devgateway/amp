/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.Map;
import java.util.Queue;

/**
 * Returns true if the current activity is of the given paremterized budget type
 * {@link Gate#getParameters()}
 * 
 * @author mpostelnicu@dgateway.org
 * @since 10 apr 2013
 */
public class ActivityBudgetTypeGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[] {  };

    private static final MetaInfo[] PARAM_INFO=new MetaInfo[] { new MetaInfo("on budget","true for on budget, false for off budget")};
    
    private static final String DESCRIPTION = "Returns true if the current activity is of the given paremtrized budget type";

    /**
     * @param scope
     * @param parameters
     */
    public ActivityBudgetTypeGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public ActivityBudgetTypeGate() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#logic()
     */
    @Override
    public boolean logic() throws Exception {
        
        AmpActivityVersion activity = (AmpActivityVersion) scope.get(GatePermConst.ScopeKeys.ACTIVITY);
        
        //this is needed because field permissions are also checked in the report manager
        if(activity==null) return true;
     
    
        AmpCategoryValue ampCategoryValue =
                CategoryManagerUtil.getAmpCategoryValueFromListByKey(CategoryConstants.ACTIVITY_BUDGET_KEY,activity.getCategories());

        String string = parameters.poll();
        
        if("true".equalsIgnoreCase(string) || "1".equals(string))
            return CategoryConstants.ACTIVITY_BUDGET_ON.equalsCategoryValue(ampCategoryValue);
        else
            return CategoryConstants.ACTIVITY_BUDGET_OFF.equalsCategoryValue(ampCategoryValue);     
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#parameterInfo()
     */
    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#mandatoryScopeKeys()
     */
    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.digijava.module.gateperm.core.Gate#description()
     */
    @Override
    public String description() {
        return DESCRIPTION;
    }

}
