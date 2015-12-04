package org.dgfoundation.amp.nireports.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn<K extends Cell> {
	
	public final String name;
	public final Optional<NiDimension.LevelColumn> levelColumn;
	
	/**
	 * never null
	 */
	public final Map<String, String> filtering;
	
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, Map<String, String> filtering) {
		this.name = name;
		this.levelColumn = Optional.ofNullable(levelColumn);
		this.filtering = filtering == null ? Collections.emptyMap() : Collections.unmodifiableMap(new TreeMap<>(filtering));
	}
	
	public abstract List<K> fetchColumn(NiReportsEngine engine);
	
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
