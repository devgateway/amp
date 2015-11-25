package org.dgfoundation.amp.nireports.schema;

import java.util.List;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiFilters;

/**
 * 
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportColumn<K extends Cell> {
	
	public final String name;
	public final NiDimension.LevelColumn levelColumn;
	
	/**
	 * might be null if this column is never filtered on
	 */
	public final ImmutablePair<String, String> fundingViewFilter;
	
	protected NiReportColumn(String name, NiDimension.LevelColumn levelColumn, ImmutablePair<String, String> fundingViewFilter) {
		this.name = name;
		this.levelColumn = levelColumn;
		this.fundingViewFilter = fundingViewFilter;
	}
	
	public abstract List<K> fetchColumn(NiFilters filters);
	
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
