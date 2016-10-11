package org.dgfoundation.amp.newreports;

public enum ReportRenderWarningType {
		
	/**
	 * example: sum(percentage) for primary sector for activity X > 100
	 */
	WARNING_TYPE_PERCENTAGE_SUM_EXCEEDS_100("sum>100"),
	
	/**
	 * example: sum(percentage) for primary sector for activity X < 100
	 */
	WARNING_TYPE_PERCENTAGE_SUM_LESS_100("sum<100"),
	
	/**
	 * example: some percentage for primary sector for activity X = NULL, the Mondrian-based reports do not support it
	 */	
	WARNING_TYPE_ENTRY_WITH_NULL("perc_is_null"),
	
	/**
	 * example: some percentage for primary sector for activity X = NULL, while others have numbers. The Mondrian-based reports do not support it
	 */		
	WARNING_TYPE_ENTRY_MIXES_NULL_AND_NOT_NULL("mixing_nulls_nonnulls"),
	
	/**
	 * example: primary sector Z for activity X has >=2 entries in the source table/view
	 */
	WARNING_TYPE_MULTIPLE_ENTRIES_FOR_SAME_CART_ID("mult_entries");
	
	private final String userFriendlyName;
	private ReportRenderWarningType(String ufn) {
		this.userFriendlyName = ufn;
	}
	
	@Override
	public String toString() {
		return userFriendlyName;
	}
}
