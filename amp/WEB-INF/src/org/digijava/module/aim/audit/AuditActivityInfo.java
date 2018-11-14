package org.digijava.module.aim.audit;

import org.digijava.module.aim.dbentity.AmpTeamMember;

public class AuditActivityInfo {
    
    private static ThreadLocal<AuditActivityInfo> threadLocalInstance = new ThreadLocal<AuditActivityInfo>();
    
    private AmpTeamMember modifiedBy;
    
    public static AuditActivityInfo getThreadLocalInstance() {
        AuditActivityInfo auditActivityInfo = threadLocalInstance.get();
        
        if (auditActivityInfo == null) {
            auditActivityInfo = new AuditActivityInfo();
            threadLocalInstance.set(auditActivityInfo);
        }
        
        return auditActivityInfo;
    }
    
    public AmpTeamMember getModifiedBy() {
        return modifiedBy;
    }
    
    public void setModifiedBy(AmpTeamMember modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public void clean() {
        this.modifiedBy = null;
    }
    
}
