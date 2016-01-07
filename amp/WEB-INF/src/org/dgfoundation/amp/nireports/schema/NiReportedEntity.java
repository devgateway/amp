package org.dgfoundation.amp.nireports.schema;

import org.dgfoundation.amp.nireports.Cell;

/**
 * specifies an entity a report can be run on, e.g. a measure or a column
 * @author Dolghier Constantin
 *
 * @param <K> the type of cells produced/living in this entity
 */
public abstract class NiReportedEntity<K extends Cell> {
	
	protected final Behaviour behaviour;
	
	protected NiReportedEntity(Behaviour behaviour) {
		this.behaviour = behaviour;
	}
	
	/**
	 * returns the behaviour of this column/measure
	 * @return
	 */
	public Behaviour getBehaviour() {
		return behaviour;
	}
}
