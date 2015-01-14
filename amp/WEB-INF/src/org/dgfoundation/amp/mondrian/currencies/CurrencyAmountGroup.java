package org.dgfoundation.amp.mondrian.currencies;

/**
 * encodes a group of related columns which signify a single type of an amount entered in an arbitrary currency at an arbitrary date
 * for explanation see https://wiki.dgfoundation.org/display/AMPDOC/AMP+2.10+ETL+process
 * @author Dolghier Constantin
 *
 */
public class CurrencyAmountGroup {
	public final String containingTable;
	public final String destinationTable;
	public final String prefix;
	public final String entityIdColumn;
	public final String containingEntityIdColumn;
	
	public CurrencyAmountGroup(String containingTable, String destinationTable, String containingEntityIdColumn, String entityIdColumn, String prefix) {
		if (containingTable.isEmpty() || destinationTable.isEmpty() || entityIdColumn.isEmpty() || prefix == null)
			throw new NullPointerException("none of the columns in CAG should be null");
		this.containingTable = containingTable;
		this.destinationTable = destinationTable;
		this.containingEntityIdColumn = containingEntityIdColumn;
		this.entityIdColumn = entityIdColumn;
		this.prefix = prefix;
	}
	
	public String getColumnName(long currId) {
		return prefix + "transaction_exch_" + currId;
	}
}