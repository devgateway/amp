package org.dgfoundation.amp.newreports;

/**
 * class describing a column to be used in a report
 * @author Dolghier Constantin
 *
 */
public abstract class ReportColumn extends NamedTypedEntity {
	
	/**
	 * 
	 * @param columnName - the name of the column
	 * @param columnType - the type of entity to which the column pertains, like Pledge / Activity 
	 */
	public ReportColumn(String columnName, ReportEntityType columnType) {
		super(columnName, columnType);
	}
	
	/**
	 * equivalent to calling {@link #ReportColumn(String, ENTITY_TYPE_ALL) )}
	 * @param columnName - the name of the column
	 */
	public ReportColumn(String columnName) {
		this(columnName, ReportEntityType.ENTITY_TYPE_ALL);
	}
	
	public String getColumnName() {
		return this.entityName;
	}
}
