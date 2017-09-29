package org.dgfoundation.amp.activity.builder;

import java.util.Date;
import java.util.HashSet;

import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class FundingBuilder {

    private AmpFunding funding;

    public FundingBuilder() {
        funding = new AmpFunding();
    }

    public FundingBuilder withClassificationDate(Date fundingClassificationDate) {
        funding.setFundingClassificationDate(fundingClassificationDate);

        return this;
    }

    public FundingBuilder addTransaction(AmpFundingDetail fundingDetail) {
        if (funding.getFundingDetails() == null) {
            funding.setFundingDetails(new HashSet<>());
        }
        
        funding.getFundingDetails().add(fundingDetail);

        return this;
    }

    public AmpFunding getFunding() {
        return funding;
    }
}
