package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.annotations.interchange.InterchangeableValueProvider;
import org.digijava.module.aim.dbentity.AmpTeamMember;

/**
 * @author Octavian Ciubotaru
 */
public class TeamMemberValueProvider implements InterchangeableValueProvider<AmpTeamMember> {

    @Override
    public String getValue(AmpTeamMember member) {
        User user = member.getUser();
        return String.format("%s %s", user.getFirstNames(), user.getLastName());
    }
}
