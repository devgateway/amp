package org.dgfoundation.amp.nireports.schema;

import java.util.List;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * specifies an entity a report can be run on, e.g. a measure or a column
 * @author Dolghier Constantin
 *
 * @param <K> the type of cells produced/living in this entity
 */
public abstract class NiReportedEntity<K extends Cell> {
	
	protected final Behaviour<?> behaviour;
	public final String name;
	public final String description;
	public final String representingString;
	
	protected NiReportedEntity(String name, Behaviour<?> behaviour, String description) {
		this.name = name;
		this.behaviour = behaviour;
		this.description = description;
		this.representingString = String.format("%s %s", this.getClass().getName(), getName());
	}

	
	public abstract List<K> fetch(NiReportsEngine engine) throws Exception;
	public abstract List<ReportRenderWarning> performCheck();
	
	/**
	 * returns the behaviour of this column/measure
	 * @return
	 */
	public Behaviour<?> getBehaviour() {
		return behaviour;
	}
		
	public String getName() {
		return name;
	}
	
	@Override public int hashCode() {
		return representingString.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		NiReportedEntity<?> o = (NiReportedEntity<?>) oth;
		return representingString.equals(o.representingString);
	}

}
