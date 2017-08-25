package org.digijava.kernel.ampapi.endpoints.contact;

import org.dgfoundation.amp.nireports.ImmutablePair;
import org.digijava.kernel.ampapi.endpoints.activity.ContextMatcher;
import org.digijava.kernel.ampapi.endpoints.activity.FEContext;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;

/**
 * @author Octavian Ciubotaru
 */
public class PhoneDiscriminatorContextMatcher implements ContextMatcher {

    @Override
    public boolean inContext(FEContext context) {
        ImmutablePair<Class<?>, Object> discriminationInfo = context.getDiscriminationInfoStack().peek();
        return discriminationInfo != null
                && AmpContactProperty.class.equals(discriminationInfo.k)
                && Constants.CONTACT_PROPERTY_NAME_PHONE.equals(discriminationInfo.v);
    }
}
