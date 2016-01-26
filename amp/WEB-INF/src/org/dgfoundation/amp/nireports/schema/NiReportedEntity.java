package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.Optional;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * specifies an entity a report can be run on, e.g. a measure or a column
 * @author Dolghier Constantin
 *
 * @param <K> the type of cells produced/living in this entity
 */
public abstract class NiReportedEntity<K extends Cell> {
	
	protected final Behaviour behaviour;
	public final String name;

	protected NiReportedEntity(String name, Behaviour behaviour) {
		this.name = name;
		this.behaviour = behaviour;
	}

	public abstract List<K> fetch(NiReportsEngine engine) throws Exception;
	public abstract List<ReportRenderWarning> performCheck();
	
	/**
	 * returns the behaviour of this column/measure
	 * @return
	 */
	public Behaviour getBehaviour() {
		return behaviour;
	}
		
	public String getName() {
		return name;
	}
	
	public String getRepresentingString() {
		return String.format("%s %s", this.getClass().getName(), getName());
	}
	
	@Override public int hashCode() {
		return getRepresentingString().hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		NiReportedEntity<?> o = (NiReportedEntity<?>) oth;
		return getRepresentingString().equals(o.getRepresentingString());
	}

}
