package org.dgfoundation.amp.nireports.schema;

public class PercentageTokenBehaviour implements Behaviour {

	public final static PercentageTokenBehaviour instance = new PercentageTokenBehaviour();
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private PercentageTokenBehaviour(){}
}
