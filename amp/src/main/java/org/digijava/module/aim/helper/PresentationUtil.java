package org.digijava.module.aim.helper;


public class PresentationUtil {
    
    public static TabColors setTabColors(int transactionType) {
        
        TabColors tc = new TabColors();
        
        tc.setCommitmentTabColor(Constants.INACTIVE_SUB_TAB_CLASS);
        tc.setDisbursementTabColor(Constants.INACTIVE_SUB_TAB_CLASS);
        tc.setExpenditureTabColor(Constants.INACTIVE_SUB_TAB_CLASS);
         
        if ( transactionType == Constants.COMMITMENT )
            tc.setCommitmentTabColor(Constants.ACTIVE_SUB_TAB_CLASS);
        else if ( transactionType == Constants.DISBURSEMENT )
            tc.setDisbursementTabColor(Constants.ACTIVE_SUB_TAB_CLASS);
        else if ( transactionType == Constants.EXPENDITURE )
            tc.setExpenditureTabColor(Constants.ACTIVE_SUB_TAB_CLASS);
            
        return tc ;
    }       
}   
