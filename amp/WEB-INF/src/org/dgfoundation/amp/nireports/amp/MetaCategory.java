package org.dgfoundation.amp.nireports.amp;

/**
 * category keys which can be found in the AmpReportsSchema metaInfo
 * @author Dolghier Constantin
 *
 */
public enum MetaCategory {
    
    TRANSACTION_TYPE("transaction_type"),
    ADJUSTMENT_TYPE("adjustment_type"),
    CAPITAL_SPEND_PERCENT("capital_spend_percent"),
    EXPENDITURE_CLASS("expenditure_class"),
    TYPE_OF_ASSISTANCE("type_of_assistance"),
    MODE_OF_PAYMENT("mode_of_payment"),
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
