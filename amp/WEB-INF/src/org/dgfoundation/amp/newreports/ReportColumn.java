package org.dgfoundation.amp.newreports;

/**
 * class describing a column to be used in a report
 * @author Dolghier Constantin
 *
 */
public class ReportColumn extends NamedTypedEntity {
	
	private boolean measureMovedAsColumn;
	
	/**
	 * equivalent to calling {@link #ReportColumn(String, ENTITY_TYPE_ALL) )}
	 * @param columnName - the name of the column
	 */
	public ReportColumn(String columnName) {
		super(columnName);
		this.measureMovedAsColumn = false;
	}
	
	public ReportColumn(String columnName, boolean measureMovedAsColumn) {
		super(columnName);
		this.measureMovedAsColumn = measureMovedAsColumn;
	}

	public String getColumnName() {
		return this.entityName;
	}

	public boolean isMeasureMovedAsColumn() {
		return measureMovedAsColumn;
	}
}