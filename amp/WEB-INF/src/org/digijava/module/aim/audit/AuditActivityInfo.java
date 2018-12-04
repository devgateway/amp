package org.digijava.module.aim.audit;

import java.util.function.Consumer;
import java.util.function.Function;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.hibernate.Session;

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
     * @param session the current hibernate session
     *
     */
    public static void doInTeamMemberContext(AmpTeamMember member, Session session, Consumer<Session> fn) {
        doInTeamMemberContext(member, session, s -> {
            fn.accept(s);
            return Void.class;
        });
    }
    
    /**
     * Execute a method in team member context
     * @param member takes as input the team member
     * @param session the current hibernate session
     * @param <R> return type
     * @return result of the supplier
     */
    public static <R> R doInTeamMemberContext(AmpTeamMember member, Session session, Function<Session, R> fn) {
        try {
            getThreadLocalInstance().modifiedBy = member;
            R result = fn.apply(session);
            session.flush();
            return result;
        } finally {
            getThreadLocalInstance().modifiedBy = null;
        }
    }
    
}
