package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;

/**
 * @author Octavian Ciubotaru
 */
public class UserValueProvider implements InterchangeableValueProvider<User> {

    @Override
    public String getValue(User user) {
        return String.format("%s %s", user.getFirstNames(), user.getLastName());
    }
}
