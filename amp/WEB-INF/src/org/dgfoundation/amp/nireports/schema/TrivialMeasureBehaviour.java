package org.dgfoundation.amp.nireports.schema;

/**
 * the behaviour of a trivial measure
 * @author Dolghier Constantin
 *
 */
public class TrivialMeasureBehaviour implements Behaviour {
	public static TrivialMeasureBehaviour getInstance() {return instance;}
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.MONTH;
	}
	
	
	public final static TrivialMeasureBehaviour instance = new TrivialMeasureBehaviour();
	private TrivialMeasureBehaviour() {}
}
