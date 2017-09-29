package org.digijava.module.message.jobs;

import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.jdbc.Work;

public final class AmpJobsUtil {
    private AmpJobsUtil() {

    }

    public static void populateRequest() {
        if (TLSUtils.getRequest() == null) {
            TLSUtils.populateMockTlsUtils();
        }
    }

    public static Boolean setTeamForNonRequestReport(Long ampTeamId) {

        final ValueWrapper<Long> ampTeamMemberId = new ValueWrapper<Long>(null);
        // we need to fetch one team member of the configured team
        final String query = "select min(tm.amp_team_mem_id) from amp_team_member tm ,amp_team  t "
                + " where tm.amp_member_role_id in(1,3) " + " and tm.amp_team_id=t.amp_team_id "
                + " and t.amp_team_id= " + ampTeamId + " group by tm.amp_team_id";

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                RsInfo ampTeamMemberIdQry = SQLUtils.rawRunQuery(conn, query, null);
                while (ampTeamMemberIdQry.rs.next()) {
                    ampTeamMemberId.value = ampTeamMemberIdQry.rs.getLong(1);
                }
                ampTeamMemberIdQry.close();
            }

        });
        if (ampTeamMemberId.value != null) {
            TeamUtil.setupFiltersForLoggedInUser(TLSUtils.getRequest(),
                    TeamUtil.getAmpTeamMember(ampTeamMemberId.value));
            return true;
        } else {
            return false;
        }
    }
}
