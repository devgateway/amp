package org.dgfoundation.amp.mondrian.currencies;

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