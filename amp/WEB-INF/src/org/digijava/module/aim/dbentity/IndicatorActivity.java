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
	
	private AmpActivity activity;

	public AmpActivity getActivity() {
		return activity;
	}

	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}

}
