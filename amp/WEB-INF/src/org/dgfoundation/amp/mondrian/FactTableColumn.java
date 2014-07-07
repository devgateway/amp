package org.dgfoundation.amp.mondrian;

/**
 * describes a column in the fact table
 * @author Dolghier Constantin
 *
 */
public class FactTableColumn implements Comparable<FactTableColumn>{
	public final String columnName;
	public final String columnDefinition;
	public final boolean mandatory;
	
	public FactTableColumn(String columnName, String columnDefinition, boolean mandatory) {
		this.columnName = columnName;
		this.columnDefinition = columnDefinition;
		this.mandatory = mandatory;
	}
	
	@Override public String toString() {
		return String.format("column (name, definition, mandatory) = (%s, %s, %s)", columnName, columnDefinition, mandatory);
	}
	
	@Override public int hashCode() {
		return columnName.hashCode();
	}
	
	@Override public boolean equals(Object oth) {
		FactTableColumn other = (FactTableColumn) oth;
		return this.columnName.equals(other.columnName);
	}
	
	@Override public int compareTo(FactTableColumn other) {
		return this.columnName.compareTo(other.columnName);
	}
}
