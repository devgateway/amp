package org.digijava.module.message.jobs;

import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.TeamUtil;
import org.hibernate.jdbc.Work;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class AmpJobsUtil {
    private AmpJobsUtil() {

    }

    public static void populateRequest() {
        if (TLSUtils.getRequest() == null) {
            TLSUtils.populateMockTlsUtils();
        }
    }


    public static Boolean setTeamForNonRequestReport(Long ampTeamId) throws JobExecutionException {
            // we need to fetch one team member of the configured team
            final String query = "select min(tm.amp_team_mem_id) from amp_team_member tm ,amp_team  t "
                    + " where tm.amp_member_role_id in(1,3) " + " and tm.amp_team_id=t.amp_team_id "
                    + " and t.amp_team_id= " + ampTeamId + " group by tm.amp_team_id";

            ValueWrapper<List<Long>> ampTeamMemberId = getTeamMembers(query);
            if (ampTeamMemberId.value.size() > 0) {
                TeamUtil.setupFiltersForLoggedInUser(TLSUtils.getRequest(),
                        TeamUtil.getAmpTeamMember(ampTeamMemberId.value.get(0)));
                return true;
            } else {
                return false;
            }
    }

    public static ValueWrapper<List<Long>> getTeamMembers(String query) {

        final ValueWrapper<List<Long>> ampTeamMemberId = new ValueWrapper<>(new ArrayList<>());
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                RsInfo ampTeamMemberIdQry = SQLUtils.rawRunQuery(conn, query, null);
                while (ampTeamMemberIdQry.rs.next()) {
                    ampTeamMemberId.value.add(ampTeamMemberIdQry.rs.getLong(1));
                }
                ampTeamMemberIdQry.close();
            }

        });
        return ampTeamMemberId;
    }
}
