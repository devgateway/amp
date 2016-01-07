package org.dgfoundation.amp.nireports.schema;

public class TextualTokenBehaviour implements Behaviour {
	
	public final static TextualTokenBehaviour instance = new TextualTokenBehaviour(); 
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.NONE;
	}
	
	private TextualTokenBehaviour(){}
}
