package org.digijava.module.aim.dbentity;

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
	private AmpActivityVersion activity;
	
	/**
	 * Indicator risk.
	 * Actually risk is in each connection of indicator and activity.
	 */
	private AmpIndicatorRiskRatings risk;
        

	public AmpActivityVersion getActivity() {
		return activity;
	}

	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}

	public AmpIndicatorRiskRatings getRisk() {
		return risk;
	}

	public void setRisk(AmpIndicatorRiskRatings risk) {
		this.risk = risk;
	}
	
	@Override
	public boolean equals(Object obj) {
		IndicatorActivity ia = (IndicatorActivity) obj; 
		return getId().compareTo(ia.getId()) == 0;
	}

}
