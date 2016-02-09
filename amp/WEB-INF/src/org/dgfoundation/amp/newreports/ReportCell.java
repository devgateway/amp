package org.dgfoundation.amp.newreports;

import java.util.Map;
import java.util.Set;

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
	public final Comparable<?> value;
	public final String displayedValue;
	private Map<Long, String> entitiesIdsValues;
	private Set<Long> entitiesIds;

	//to facilitate the sorting, we will store the parent area
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

	/**
	 * @return the entitiesIdsValues
	 */
	public Map<Long, String> getEntitiesIdsValues() {
		return entitiesIdsValues;
	}

	/**
	 * @param entitiesIdsValues the entitiesIdsValues to set
	 */
	public void setEntitiesIdsValues(Map<Long, String> entitiesIdsValues) {
		this.entitiesIdsValues = entitiesIdsValues;
	}

	/**
	 * @return the entitiesIds
	 */
	public Set<Long> getEntitiesIds() {
		return entitiesIds;
	}

	/**
	 * @param entitiesIds the entitiesIds to set
	 */
	public void setEntitiesIds(Set<Long> entitiesIds) {
		this.entitiesIds = entitiesIds;
	}
}
