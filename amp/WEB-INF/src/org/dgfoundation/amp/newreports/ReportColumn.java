package org.dgfoundation.amp.newreports;

/**
 * class describing a column to be used in a report
 * @author Dolghier Constantin
 *
 */
public class ReportColumn extends NamedTypedEntity {

	// FIXME this property can be removed once the hack from NiReportsFormatter is solved
	private boolean hideSubtotals;

	/**
	 * equivalent to calling {@link #ReportColumn(String, ENTITY_TYPE_ALL) )}
	 * @param columnName - the name of the column
	 */
	public ReportColumn(String columnName) {
		this(columnName, false);
	}

	public ReportColumn(String columnName, boolean hideSubtotals) {
		super(columnName);
		this.hideSubtotals = hideSubtotals;
	}

	public String getColumnName() {
		return this.entityName;
	}

	public boolean isHideSubtotals() {
		return hideSubtotals;
	}
}