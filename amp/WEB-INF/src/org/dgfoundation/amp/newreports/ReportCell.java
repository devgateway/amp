package org.dgfoundation.amp.newreports;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.dgfoundation.amp.algo.AmpCollections;

/**
 * class describing a report cell
 * 
 * @author Dolghier Constantin
 */
@JsonSerialize(include=Inclusion.NON_NULL)
public abstract class ReportCell implements Comparable<ReportCell> {
	
	@JsonIgnore
	public final Comparable<?> value;
	
	public final String displayedValue;
	
	/**
	 * TODO: delete this field once we are done with killing off Mondrian
	 * to facilitate the sorting, we will store the parent area
	 */
	@Deprecated
	transient public ReportArea area;
	
	public ReportCell(Comparable<?> value, String displayedValue) {
		this.value = value;
		this.displayedValue = displayedValue;
	}
	
	@Override public int compareTo(ReportCell oth) {
		if (value == null && (oth == null || oth.value == null))
			return 0;
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
