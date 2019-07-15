package org.digijava.kernel.validators.activity;

import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.helper.Constants;

/**
 * @author Octavian Ciubotaru
 */
public class HardcodedRoles {

    private final AmpRole donorRole;

    public HardcodedRoles() {
        donorRole = new AmpRole();
        donorRole.setRoleCode(Constants.ROLE_CODE_DONOR);
    }

    public AmpRole getDonorRole() {
        return donorRole;
    }
}
