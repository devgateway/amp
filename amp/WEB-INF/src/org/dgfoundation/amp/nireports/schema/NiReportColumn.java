package org.dgfoundation.amp.nireports.schema;

import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.nireports.CellColumn;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn {
	
	public final String name;
	public final NiDimension.LevelColumn levelColumn;
	public final ImmutablePair<String, String> fundingViewFilter;
	
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, ImmutablePair<String, String> fundingViewFilter) {
		this.name = name;
		this.levelColumn = levelColumn;
		this.fundingViewFilter = fundingViewFilter;
	}
	
	public abstract List<CellColumn> fetchColumn(NiFilters filters);
	
	@Override public int hashCode() {
		return name.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		NiReportColumn o = (NiReportColumn) oth;
		return this.name.equals(o.name);
	}
	
	@Override public String toString() {
		return String.format("coldef: <%s>", name);
	}
}
