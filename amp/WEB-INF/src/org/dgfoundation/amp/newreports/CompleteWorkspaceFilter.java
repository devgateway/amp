package org.dgfoundation.amp.newreports;

import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * an WorkspaceFilter which takes into account both the team and the team filters (if any)
 * @author Dolghier Constantin
 *
 */
public class CompleteWorkspaceFilter implements IdsGeneratorSource {
	
	public final TeamMember tm;
	public final AmpARFilter workspaceFilter;
	
	public CompleteWorkspaceFilter(TeamMember tm, AmpARFilter workspaceFilter) {
		this.tm = tm;
		this.workspaceFilter = workspaceFilter;
	}
	
	@Override public Set<Long> getIds() {
		Set<Long> allowedActivities = ActivityUtil.fetchLongs(WorkspaceFilter.generateWorkspaceFilterQuery(tm));
		if (workspaceFilter != null) {
			Set<Long> wfIds = ActivityUtil.fetchLongs(workspaceFilter.getGeneratedFilterQuery());
			allowedActivities.addAll(wfIds);
		}
		return allowedActivities;
	}
}
