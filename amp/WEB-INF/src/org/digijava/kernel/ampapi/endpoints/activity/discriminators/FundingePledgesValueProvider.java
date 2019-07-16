package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import com.google.common.collect.ImmutableMap;

/**
 * @author Viorel Chihai
 */
public class FundingePledgesValueProvider extends PossibleValuesProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<FundingPledges> fundingPlegdes = PledgesEntityHelper.getPledges();
        List<PossibleValue> values = new ArrayList<>();
        for (FundingPledges fundingPledge : fundingPlegdes) {
            Map<String, String> extraInfo = new HashMap<>();
            if (fundingPledge.getOrganizationGroup() != null) {
                extraInfo.put("organization_group", fundingPledge.getOrganizationGroup().getName());
            }
            values.add(new PossibleValue(fundingPledge.getId(), fundingPledge.getEffectiveName(), 
                    ImmutableMap.of(), extraInfo));
        }
        return values;
    }
    
    @Override
    public Object toJsonOutput(Object object) {
        return object;
    }

    @Override
    public Long getIdOf(Object value) {
        if (value != null) {
            return ((FundingPledges) value).getId();
        }
        
        return null;
    }

    public Object toAmpFormat(Object obj) {
        return PersistenceManager.getSession().get(FundingPledges.class, ((Number) obj).longValue());
    }
    
}
