package org.dgfoundation.amp.nireports.schema;

import java.util.Set;

import org.dgfoundation.amp.nireports.Cell;

/**
 * a class which holds complete info for NiReports so that those know how to process them (please see {@link NiReportColumn}). <br />
 * A NiReportMeasure is very much a regular NiReportColumn, but with some engine-supported niceties: <ul>
 * <li>measures are split by temporal columns</li>
 * <li>measures support dependency checking</li>
 * <li>others???</li>
 * </ul>
 * TODO: consider making NiReportMeasure a subclass of NiReportColumn
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportMeasure<K extends Cell> extends NiReportedEntity<K> {
			
	protected NiReportMeasure(String name, Behaviour behaviour, String description) {
		super(name, behaviour, description);
	}
	
	/**
	 * returns the list of measures which should be computed before this measure's {@link #fetch(NiReportContext)} function will be called
	 * @return
	 */
	public abstract Set<String> getPrecursorMeasures();	
		
	@Override public String toString() {
		return String.format("measdef: <%s>", name);
	}
}
