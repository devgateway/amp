package org.digijava.module.gateperm.gates;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorkspaceGroupGate extends Gate {

    public static final MetaInfo[] SCOPE_KEYS = new MetaInfo[]{GatePermConst.ScopeKeys.CURRENT_MEMBER};

    public static final MetaInfo[] PARAM_INFO =
            new MetaInfo[]{new MetaInfo("workspaceGroupId", "The ID of the workspace group to "
                    + "check the user membership")};

    private static final String DESCRIPTION = "If workspaceGroupId the gate checks if the ws the user is "
            + "connected to is of the wsgroup received as parameter, if not it checks against thw workspaceId";


    public WorkspaceGroupGate(Map scope, Queue<String> parameters) {
        super(scope, parameters);
    }

    public WorkspaceGroupGate() {

    }

    @Override
    public boolean logic() throws Exception {
        TeamMember tm = (TeamMember) scope.get(GatePermConst.ScopeKeys.CURRENT_MEMBER);

        List<Long> workspaceGroupIds = Stream.of(parameters.poll().trim().split(",")).
                map(Long::valueOf).collect(Collectors.toList());

        if (tm == null || tm.getTeamId() == null) {
            return false;
        }
        AmpTeam userTeam = TeamUtil.getAmpTeam(tm.getTeamId());
        if (userTeam == null) {
            return false;
        }
        return workspaceGroupIds.stream().filter(workspaceGroupId
                -> workspaceGroupId.equals(userTeam.getWorkspaceGroup().getId())).count() > 0;
    }

    @Override
    public MetaInfo[] parameterInfo() {
        return PARAM_INFO;
    }

    @Override
    public MetaInfo[] mandatoryScopeKeys() {
        return SCOPE_KEYS;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }
}
