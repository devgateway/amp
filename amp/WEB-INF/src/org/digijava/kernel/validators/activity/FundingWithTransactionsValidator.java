package org.digijava.kernel.validators.activity;

import java.util.Collection;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * Some fields become required when there is at least one transaction.
 *
 * @author Octavian Ciubotaru
 */
public class FundingWithTransactionsValidator extends ConditionalRequiredValidator {

    public static final String TRANSACTION_PRESENT_KEY = "transaction_present";

    public FundingWithTransactionsValidator() {
        super(TRANSACTION_PRESENT_KEY);
    }

    @Override
    public boolean isActive(APIField type, Object value) {
        for (APIField transactionTypeField : type.getFieldsForFieldNameInternal("fundingDetails")) {
            Collection<AmpFundingDetail> fundingDetails = transactionTypeField.getFieldAccessor().get(value);

            if (!fundingDetails.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
