package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.algo.AmpCollections;

/**
 * class describing a report cell
 * @author Dolghier Constantin
 *
 */
public abstract class ReportCell implements Comparable<ReportCell> {
	public final Comparable<?> value;
	public final String displayedValue;

	//to facilitate the sorting, we will store the parent area
	transient public ReportArea area;
	
	public ReportCell(Comparable<?> value, String displayedValue) {
		this.value = value;
		this.displayedValue = displayedValue;
	}
	
	@Override public int compareTo(ReportCell oth) {
		return AmpCollections.nullCompare(value, oth.value);
	}
	
	@Override public boolean equals(Object oth) {
		if (!(oth instanceof ReportCell))
			return false;
		ReportCell other = (ReportCell) oth;
		return this.compareTo(other) == 0;
	}
	
	@Override public int hashCode() {
		return this.value == null ? 0 : this.value.hashCode();
	}
	
	@Override public String toString() {
		return String.format("[%s]", this.displayedValue);
	}
}
