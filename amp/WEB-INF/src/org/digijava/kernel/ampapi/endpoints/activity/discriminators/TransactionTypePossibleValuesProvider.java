package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.digijava.module.aim.annotations.interchange.PossibleValuesEntity;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@PossibleValuesEntity(AmpCategoryValue.class)
public class TransactionTypePossibleValuesProvider extends AbstractTransactionTypePossibleValuesProvider {

    @Override
    public boolean isVisibleInFeatureManager(Integer transactionTypeId) {
        String fmName = AmpFundingItemFeaturePanel.FM_NAME_BY_TRANSACTION_TYPE.get(transactionTypeId);
        return FeaturesUtil.isVisibleModule("/Activity Form/Funding/Funding Group/Funding Item/" + fmName);
    }
}
