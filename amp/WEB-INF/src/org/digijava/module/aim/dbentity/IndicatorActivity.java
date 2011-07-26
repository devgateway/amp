package org.digijava.module.aim.dbentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.digijava.module.aim.util.Output;

/**
 * Project Indicator.
 * This is connection between indicator and activity. Most fields are in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml 
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorActivity extends IndicatorConnection implements Versionable, Cloneable{

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

	@Override
	public boolean equalsForVersioning(Object obj) {
		IndicatorActivity aux = (IndicatorActivity) obj;
		return aux.getIndicator().getIndicatorId() == getIndicator().getIndicatorId();
	}

	@Override
	public Object getValue() {
		return this.getIndicator().getName();
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { " Name:&nbsp;" }, new Object[] { this.getIndicator() != null ? this.getIndicator().getName()
						: "Empty Name" }));
		return null;
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		IndicatorActivity aux = (IndicatorActivity) clone();
		aux.activity = newActivity;
		aux.setId(null);
		
		if (aux.values != null && aux.values.size() > 0){
			HashSet<AmpIndicatorValue> set = new HashSet<AmpIndicatorValue>();
			Iterator<AmpIndicatorValue> i = aux.values.iterator();
			while (i.hasNext()) {
				AmpIndicatorValue ampIndicatorValue = (AmpIndicatorValue) i.next().clone();
				ampIndicatorValue.setIndValId(null);
				ampIndicatorValue.setIndicatorConnection(aux);
				set.add(ampIndicatorValue);
			}
			aux.values = set;
		}
		else
			aux.values = null;
		return aux;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
