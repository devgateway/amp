package org.digijava.module.aim.dbentity;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Project Indicator.
 * This is connection between indicator and activity. Most fields are in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml 
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorActivity extends IndicatorConnection{

	private static final long serialVersionUID = 2L;
	
	/**
	 * Activity
	 */
	private AmpActivity activity;
	
	/**
	 * Indicator risk.
	 * Actually risk is in each connection of indicator and activity.
	 */
	private  AmpCategoryValue riskValue;
        

	public AmpActivity getActivity() {
		return activity;
	}

	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}

	public  AmpCategoryValue getRiskValue() {
		return riskValue;
	}

	public void setRiskValue(AmpCategoryValue risk) {
		this.riskValue = risk;
	}

}
