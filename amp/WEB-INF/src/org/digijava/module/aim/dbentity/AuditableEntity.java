package org.digijava.module.aim.dbentity;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.audit.AuditActivityInfo;
import org.digijava.module.aim.util.TeamMemberUtil;

public interface AuditableEntity {
    
    AuditableEntity getParent();
    
    default void touch() {
        if (getParent() != null) {
            getParent().touch();
        }
    }
    
    default AmpTeamMember getModifier() {
        AmpTeamMember modifier = AuditActivityInfo.getModifiedTeamMember();
        if (modifier == null) {
            modifier = TeamMemberUtil.getCurrentAmpTeamMember(TLSUtils.getRequest());
        }
    
        if (modifier == null) {
            throw new RuntimeException("Modified team member cannot be null");
        }
        
        return modifier;
    }
}
