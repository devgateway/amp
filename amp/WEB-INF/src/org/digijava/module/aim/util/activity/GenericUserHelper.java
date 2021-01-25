package org.digijava.module.aim.util.activity;


import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil;

import static org.digijava.module.aim.startup.AmpBackgroundActivitiesUtil.createActivityUserIfNeeded;

public class GenericUserHelper {
    public static final String AMP_MODIFIER_USER_EMAIL = "amp_modifier@amp.org";
    public static final String AMP_MODIFIER_FIRST_NAME = "AMP";
    public static final String AMP_MODIFIER_LAST_NAME = "Activities Modifier";

    private static User user = new User(AMP_MODIFIER_USER_EMAIL, AMP_MODIFIER_FIRST_NAME, AMP_MODIFIER_LAST_NAME);

    public static AmpTeamMember getAmpTeamMemberModifier(AmpTeam team) throws Exception {
        return AmpBackgroundActivitiesUtil.createActivityTeamMemberIfNeeded(team, getAmpUserModifier());
    }

    public static User getAmpUserModifier() throws Exception {
        return createActivityUserIfNeeded(user);
    }

}
