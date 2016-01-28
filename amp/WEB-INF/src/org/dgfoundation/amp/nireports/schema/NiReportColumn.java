package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiReportsEngine;

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
		
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, Behaviour behaviour, String description) {
		super(name, behaviour, description);
		this.levelColumn = Optional.ofNullable(levelColumn);
	}
	
	@Override public int hashCode() {
		return name.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		if (!(oth instanceof NiReportColumn))
			return false;
		NiReportColumn<K> o = (NiReportColumn<K>) oth;
		return this.name.equals(o.name);
	}
	
	@Override public String toString() {
		return String.format("coldef: <%s>", name);
	}
}
