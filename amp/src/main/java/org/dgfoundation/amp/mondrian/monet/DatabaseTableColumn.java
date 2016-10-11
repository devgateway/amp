package org.dgfoundation.amp.mondrian.monet;

/**
 * describes a column in a database table
 * @author Dolghier Constantin
 *
 */
public class DatabaseTableColumn implements Comparable<DatabaseTableColumn>{
	public final String columnName;
	public final String columnDefinition;
	public final boolean indexed;
	
	public DatabaseTableColumn(String columnName, String columnDefinition, boolean indexed) {
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
		DatabaseTableColumn other = (DatabaseTableColumn) oth;
		return this.columnName.equals(other.columnName);
	}
	
	@Override public int compareTo(DatabaseTableColumn other) {
		return this.columnName.compareTo(other.columnName);
	}
}
