package org.dgfoundation.amp.activity.builder;

import java.util.Date;

import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class TransactionBuilder {

    private AmpFundingDetail transaction;

    public TransactionBuilder() {
        transaction = new AmpFundingDetail();
    }

    public TransactionBuilder withTransactionType(int transactionType) {
        transaction.setTransactionType(transactionType);

        return this;
    }

    public TransactionBuilder withTransactionDate(Date transactionDate) {
        transaction.setTransactionDate(transactionDate);

        return this;
    }

    public TransactionBuilder withPledge(FundingPledges pledge) {
        transaction.setPledgeid(pledge);

        return this;
    }

    public AmpFundingDetail getTransaction() {
        return transaction;
    }
}
