package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import java.util.Arrays;

import org.dgfoundation.amp.ar.ArConstants;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentTransactionTypePossibleValuesProvider extends AbstractTransactionTypePossibleValuesProvider {

    public ComponentTransactionTypePossibleValuesProvider() {
        super(Arrays.asList(ArConstants.COMMITMENT, ArConstants.DISBURSEMENT, ArConstants.EXPENDITURE));
    }
}
