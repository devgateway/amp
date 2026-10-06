package org.digijava.kernel.ampapi.endpoints.activity.discriminators;

import org.dgfoundation.amp.algo.Memoizer;
import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.util.OrganisationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOrgRoleDiscriminationConfigurer implements DiscriminationConfigurer {

    private Memoizer<Map<String, AmpRole>> rolesByCode = new Memoizer<>(this::loadOrgRoles);

    private Map<String, AmpRole> loadOrgRoles() {
        Map<String, AmpRole> roles = new HashMap<>();
        for (AmpRole role : OrganisationUtil.getOrgRoles()) {
            roles.put(role.getRoleCode(), role);
        }
        return roles;
    }

    @Override
    public void configure(Object obj, String fieldName, String discriminationValue) {
        AmpOrgRole role = (AmpOrgRole) obj;
        role.setRole(rolesByCode.get().get(discriminationValue));
    }

}
