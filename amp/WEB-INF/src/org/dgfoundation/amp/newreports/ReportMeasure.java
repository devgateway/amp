package org.dgfoundation.amp.newreports;

/**
 * class describing a measure to be used in a report
 * @author Dolghier Constantin
 *
 */
public abstract class ReportMeasure extends NamedTypedEntity {
	
	/**
	 * 
	 * @param columnName - the name of the column
	 * @param columnType - the type of entity to which the column pertains, like Pledge / Activity 
	 */
	public ReportMeasure(String measureName, ReportEntityType columnType) {
		super(measureName, columnType);
	}
	
	/**
	 * equivalent to calling {@link #ReportColumn(String, ENTITY_TYPE_ALL) )}
	 * @param columnName - the name of the column
	 */
	public ReportMeasure(String measureName) {
		this(measureName, ReportEntityType.ENTITY_TYPE_ALL);
	}
	
	public String getMeasureName() {
		return this.entityName;
	}
}
