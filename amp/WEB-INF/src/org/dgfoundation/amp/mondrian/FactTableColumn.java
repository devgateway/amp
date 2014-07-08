package org.dgfoundation.amp.mondrian;

/**
 * describes a column in the fact table
 * @author Dolghier Constantin
 *
 */
public class FactTableColumn implements Comparable<FactTableColumn>{
	public final String columnName;
	public final String columnDefinition;
	public final boolean indexed;
	
	public FactTableColumn(String columnName, String columnDefinition, boolean indexed) {
		this.columnName = columnName;
		this.columnDefinition = columnDefinition;
		this.indexed = indexed;
	}
	
	@Override public String toString() {
		return String.format("column (name, definition, indexed) = (%s, %s, %s)", columnName, columnDefinition, indexed);
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
