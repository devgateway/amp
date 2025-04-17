package org.dgfoundation.amp.newreports;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.TeamMember;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Octavian Ciubotaru
 */
public final class ReportEnvBuilder {

    private static final GlobalActivityIdsGenerator GLOBAL_ACTIVITY_IDS_GENERATOR = new GlobalActivityIdsGenerator();

    private static final ConcurrentMap<TeamMember, IdsGeneratorSource> TEAM_MEMBER_CACHE = new ConcurrentHashMap<>();

    private static IdsGeneratorSource publicFilter;

    private ReportEnvBuilder() {
    }

    /**
     * Return global report environment. Includes all activities except the ones not assigned to a workspace.
     * Implemented to allow computing
     */
    public static IReportEnvironment global() {
        return from(GLOBAL_ACTIVITY_IDS_GENERATOR);
    }

    /**
     * Creates a report environment based on current session. If a team member is authenticated, then return report
     * environment which will display activities visible to this team member.
     * Otherwise return public report environment.
     */
    public static IReportEnvironment forSession() {
        HttpServletRequest request = TLSUtils.getRequest();
        TeamMember tm = request != null && request.getSession() != null
                ? (TeamMember) request.getSession().getAttribute("currentMember") : null;
        return forTeamMember(tm);
    }

    /**
     * Returns report environment which will display activities visible to this team member.
     */
    public static IReportEnvironment forTeamMember(TeamMember tm) {
        if (tm == null) {
            if (publicFilter == null) {
                publicFilter = new CompleteWorkspaceFilter(null);
            }
            return from(publicFilter);
        } else {
            return from(TEAM_MEMBER_CACHE.computeIfAbsent(tm, z -> new CompleteWorkspaceFilter(tm)));
        }
    }

    /**
     * Returns report environment that takes ids from {@link IdsGeneratorSource source}.
     */
    private static IReportEnvironment from(IdsGeneratorSource source) {
        return source::getIds;
    }

    public static IReportEnvironment dummy() {
        return () -> {
            throw new RuntimeException("not implemented");
        };
    }
}
