package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.onepager.components.fields.AmpComponentField;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentTransactionTypePossibleValuesProvider extends AbstractTransactionTypePossibleValuesProvider {

    public ComponentTransactionTypePossibleValuesProvider() {
        super(Arrays.asList(ArConstants.COMMITMENT, ArConstants.DISBURSEMENT, ArConstants.EXPENDITURE));
    }

    @Override
    public boolean isVisibleInFeatureManager(Integer transactionTypeId) {
        String fmName = AmpComponentField.FM_NAME_BY_TRANSACTION_TYPE.get(transactionTypeId);
        return FeaturesUtil.isVisibleModule("/Activity Form/Components/Component/" + fmName);
    }
}
