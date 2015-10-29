package org.dgfoundation.amp.nireports;

import java.util.List;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn {
	
	public final String name;
	
	protected NiReportColumn(String name) {
		this.name = name;
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
