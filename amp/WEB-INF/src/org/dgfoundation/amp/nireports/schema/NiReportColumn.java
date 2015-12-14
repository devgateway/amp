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
public abstract class NiReportColumn<K extends Cell> {
	
	public final String name;
	public final Optional<NiDimension.LevelColumn> levelColumn;
	
	/**
	 * never null, but might be empty
	 */
	public final Map<String, String> filtering;
	
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, Map<String, String> filtering) {
		this.name = name;
		this.levelColumn = Optional.ofNullable(levelColumn);
		this.filtering = filtering == null ? Collections.emptyMap() : Collections.unmodifiableMap(new TreeMap<>(filtering));
	}
	
	/**
	 * fetch a column's initial contents, filtered but before any structure or hierarchies have been applied
	 * @param engine
	 * @return
	 * @throws Exception
	 */
	public abstract List<K> fetchColumn(NiReportsEngine engine) throws Exception;
	
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
