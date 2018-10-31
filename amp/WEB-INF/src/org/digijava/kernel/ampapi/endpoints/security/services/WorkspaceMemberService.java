package org.digijava.kernel.ampapi.endpoints.security.services;

import java.sql.Connection;
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
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;

/**
 * Workspace Member Service
 * 
 * @author Nadejda Mandrescu
 */
@Component
public class WorkspaceMemberService {
    private static Logger logger = Logger.getLogger(WorkspaceMemberService.class);
   
    public List<WorkspaceMember> getWorkspaceMembers(List<Long> ids) {
        List<WorkspaceMember> wsMermbers = new ArrayList<>();
        String restrictions;
        if (ids.isEmpty()) {
            restrictions = "";
        } else {
            restrictions = " WHERE atm.amp_team_mem_id in (" + Util.toCSStringForIN(ids) + ")";
        }
        String sqlQuery = "SELECT atm.amp_team_mem_id, atm.user_, atm.amp_team_id, atmr.role, atm.deleted "
                + "FROM amp_team_member atm "
                + "LEFT JOIN amp_team_member_roles atmr ON atmr.amp_team_mem_role_id = atm.amp_member_role_id"
                + restrictions;
        Consumer<ResultSet> resultSetConsumer = new Consumer<ResultSet>() {
            @Override
            public void accept(ResultSet rs) {
                WorkspaceMember wsMember = new WorkspaceMember();
                try {
                    wsMember.setId(rs.getLong("amp_team_mem_id"));
                    wsMember.setUserId(rs.getLong("user_"));
                    wsMember.setWorkspaceId(rs.getLong("amp_team_id"));
                    wsMember.setRoleId(WorkspaceMemberRoleConstants.ROLE_ID_MAP.get(rs.getString("role")));
                    wsMember.setDeleted(rs.getBoolean("deleted"));
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
                wsMermbers.add(wsMember);
            }
        };
        PersistenceManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                SQLUtils.forEachRow(connection, sqlQuery, resultSetConsumer);
            }
        });
        return wsMermbers;
    }

}
