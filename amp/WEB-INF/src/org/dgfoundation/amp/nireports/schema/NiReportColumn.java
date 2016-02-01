package org.dgfoundation.amp.nireports.schema;

import java.util.Optional;

import org.dgfoundation.amp.nireports.Cell;

/**
 * a class which holds complete info for NiReports so that those know how to: 
 * <ol>
 * 	<li>name a column</li>
 * 	<li>fetch a column, using filters</li> 
 * 	<li>split a column by hierarchies</li> 
 * 	<li>aggregate a column</li>s
 * </ol> 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn<K extends Cell> extends NiReportedEntity<K> {
	
	public final Optional<NiDimension.LevelColumn> levelColumn;
		
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, Behaviour<?> behaviour, String description) {
		super(name, behaviour, description);
		this.levelColumn = Optional.ofNullable(levelColumn);
	}
		
	@Override public String toString() {
		return String.format("coldef: <%s>", name);
	}
}
