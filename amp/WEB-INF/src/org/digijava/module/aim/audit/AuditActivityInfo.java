package org.digijava.module.aim.audit;

import java.util.function.Supplier;

import org.digijava.module.aim.dbentity.AmpTeamMember;

public final class AuditActivityInfo {
    
    private static ThreadLocal<AuditActivityInfo> threadLocalInstance = new ThreadLocal<AuditActivityInfo>();
    
    private AuditActivityInfo() { }
    
    private AmpTeamMember modifiedBy;
    
    private static AuditActivityInfo getThreadLocalInstance() {
        AuditActivityInfo auditActivityInfo = threadLocalInstance.get();
        
        if (auditActivityInfo == null) {
            auditActivityInfo = new AuditActivityInfo();
            threadLocalInstance.set(auditActivityInfo);
        }
        
        return auditActivityInfo;
    }
    
    public static AmpTeamMember getModifiedTeamMember() {
        return getThreadLocalInstance().modifiedBy;
    }
    
    /**
     * Execute a method in team member context
     * @param member takes as input the team member
     */
    public static void doInTeamMemberContext(AmpTeamMember member, Runnable fn) {
        doInTeamMemberContext(member, () -> {
            fn.run();
            return Void.class;
        });
    }
    
    /**
     * Execute a method in team member context
     * @param member takes as input the team member
     * @param <R> return type
     * @return result of the supplier
     */
    public static <R> R doInTeamMemberContext(AmpTeamMember member, Supplier<R> fn) {
        try {
            getThreadLocalInstance().modifiedBy = member;
            return fn.get();
        } finally {
            getThreadLocalInstance().modifiedBy = null;
        }
    }
    
}
