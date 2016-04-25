package org.dgfoundation.amp.newreports;

import java.util.Set;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * a cached, etl_log-invalidated, WorkspaceFilter which takes into account both the team and the team filters (if any)
 * @author Dolghier Constantin
 *
 */
public class CompleteWorkspaceFilter implements IdsGeneratorSource {
	
	public final TeamMember tm;
	public final AmpARFilter workspaceFilter;
	final ExpiringCacher<Boolean, Set<Long>> cacher = 
			new ExpiringCacher<Boolean, Set<Long>>("workspaceFilter", b -> computeIds(), new DatabaseChangedDetector(), 3 * 60 * 1000);
	
	public CompleteWorkspaceFilter(TeamMember tm, AmpARFilter workspaceFilter) {
		this.tm = tm;
		this.workspaceFilter = workspaceFilter;
	}
	
	@Override public Set<Long> getIds() {
		return cacher.buildOrGetValue(true);
	}
	
	/** called when the cacher determines that the cache has been invalidated */
	public Set<Long> computeIds() {
		Set<Long> allowedActivities = ActivityUtil.fetchLongs(WorkspaceFilter.generateWorkspaceFilterQuery(tm));
		if (workspaceFilter != null) {
			Set<Long> wfIds = ActivityUtil.fetchLongs(workspaceFilter.getGeneratedFilterQuery());
			allowedActivities.addAll(wfIds);
		}
		return allowedActivities;
	}
}
