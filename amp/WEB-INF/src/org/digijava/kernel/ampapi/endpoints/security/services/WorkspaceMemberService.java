/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.security.dto.WorkspaceMember;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * Workspace Member Service
 * 
 * @author Nadejda Mandrescu
 */
public class WorkspaceMemberService {
    private static Logger logger = Logger.getLogger(WorkspaceMemberService.class);
   
    public List<WorkspaceMember> getWorkspaceMembers(List<Long> userIds) {
        List<WorkspaceMember> wsMermbers = new ArrayList<>();
        String sqlQuery = "SELECT atm.amp_team_mem_id, atm.user_, atm.amp_team_id, atmr.role "
                + "FROM amp_team_member atm "
                + "LEFT JOIN amp_team_member_roles atmr ON atmr.amp_team_mem_role_id = atm.amp_member_role_id "
                + "WHERE user_ in (" + Util.toCSStringForIN(userIds) + ")";
        Consumer<ResultSet> resultSetConsumer = (rs) -> {
            WorkspaceMember wsMember = new WorkspaceMember();
            try {
                wsMember.setId(rs.getLong("amp_team_mem_id"));
                wsMember.setUserId(rs.getLong("user_"));
                wsMember.setWorkspaceId(rs.getLong("amp_team_id"));
                wsMember.setRoleId(WorkspaceMemberRoleConstants.ROLE_ID_MAP.get(rs.getString("role")));
            } catch (SQLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
            wsMermbers.add(wsMember);
        };
        PersistenceManager.getSession().doWork(connection -> {
            SQLUtils.forEachRow(connection, sqlQuery,  resultSetConsumer);
        });
        return wsMermbers;
    }

}
