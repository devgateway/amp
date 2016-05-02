package org.dgfoundation.amp.nireports.amp;

/**
 * category keys which can be found in the AmpReportsSchema metaInfo
 * @author simple
 *
 */
public enum MetaCategory {
	
	TRANSACTION_TYPE("transaction_type"),
	ADJUSTMENT_TYPE("adjustment_type"),
	AGREEMENT_ID("agreement_id"),
	CAPITAL_SPEND_PERCENT("capital_spend_percent"),
	DONOR_ORG("donor_org"),
	FINANCING_INSTRUMENT("financing_instrument"),
	TERMS_OF_ASSISTANCE("terms_of_assistance"),
	PLEDGE_ID("pledge_id"),
	FUNDING_STATUS("funding_status"),
	MODE_OF_PAYMENT("mode_of_payment"),
	EXPENDITURE_CLASS("expenditure_class"),
	SOURCE_ROLE("source_role"),
	SOURCE_ORG("source_org"),
	RECIPIENT_ROLE("recipient_role"),
	RECIPIENT_ORG("recipient_org"),
	DIRECTED_TRANSACTION_FLOW("directed_transaction_flow");
	
	public final String category;
	private MetaCategory(String category) {
		this.category = category;
	}
}