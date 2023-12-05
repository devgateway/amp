package org.dgfoundation.amp.nireports.testcases;

public enum TestMetaCategory {
    
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
    RECIPIENT_ORG("recipient_org"),
    RECIPIENT_ROLE("recipient_role"),
    SOURCE_ROLE("source_role");
    
    public final String category;
    private TestMetaCategory(String category) {
        this.category = category;
    }
}
