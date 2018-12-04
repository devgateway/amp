package org.digijava.kernel.ampapi.endpoints.publicportal;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FIXME change public portal to work with PublicTopData
 */
public class PublicTopDonorFundingData extends PublicTopData {

    PublicTopDonorFundingData(PublicTopData copy) {
        super(copy.getHeaders(), copy.getTotals(), copy.getTopData(), copy.getCount(), copy.getNumberFormat(),
                copy.getCurrency());
    }

    @JsonProperty("donorFunding")
    @Override
    public List<Map<String, String>> getTopData() {
        return super.getTopData();
    }
}
