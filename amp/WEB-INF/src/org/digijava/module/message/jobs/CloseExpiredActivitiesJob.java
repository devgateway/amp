package org.digijava.module.message.jobs;

import java.util.Date;
import java.util.List;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.GlobalSettings;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AmpDateUtils;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.triggers.ActivityProposedStartDateTrigger;
import org.digijava.module.message.util.AmpMessageUtil;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.math.BigInteger;
import java.text.SimpleDateFormat;

public class CloseExpiredActivitiesJob implements StatefulJob {
	
	public java.util.Map<Long, Long> getStatuses(Session session, List<Long> eligibleIds)
	{
		java.util.Map<Long, Long> res = new java.util.TreeMap<Long, Long>();
		String query = "SELECT amp_activity_id, amp_status_id FROM v_status WHERE amp_activity_id IN (" + Util.toCSString(eligibleIds) + ")";
		List<Object[]> bla = session.createSQLQuery(query).list();
		for(Object[] ass:bla)
		{
			BigInteger amp_bi = (BigInteger) ass[0], status_bi = (BigInteger) ass[1];			
			Long amp_id = amp_bi.longValue();
			Long status_id = status_bi.longValue();
			res.put(amp_id, status_id);
		}
		return res;
	}
	
//	public String getNewStatusReason(Session session, AmpActivityVersion ver)
//	{
//		if (ver.getStatusReason() == null)
//		{
//			
//		}
//		DbUtil.getEditorList(ver.getStatusReason(), site)
//	}
	
    public void execute(JobExecutionContext context) throws JobExecutionException{

    	try
    	{
    		Session session = PersistenceManager.getSession();
    		
    		String val = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATICALLY_CLOSE_ACTIVITIES);
    		boolean autoClosingEnabled = "true".equalsIgnoreCase(val);
    		if (!autoClosingEnabled)
    			return; // feature disabled => nothing to do;

    		System.out.println("EU SUNT DESTEPT!!!!");
		
//    		if (System.currentTimeMillis() > 1)  // BOZO: THIS IS DONE TO DISCONNECT THE FOLLOWING CRASHING CODE IN RUNNING INSTANCES
//    			return;
		
    		Long closedCategoryValue = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);
    		String filterQuery = "SELECT amp_activity_last_version_id FROM amp_activity_group aag WHERE aag.autoclosedonexpiration = false AND " + 
				" aag.amp_activity_last_version_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft IS NULL or draft=false) and approval_status IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") AND proposed_completion_date < now())" +
				" AND aag.amp_activity_last_version_id IN (select amp_activity_id FROM v_status WHERE amp_status_id != " + closedCategoryValue + ")" +
				"";
		
    		List<Long> eligibleIds = DbHelper.getInActivities(filterQuery);
    		java.util.Map<Long, Long> statusIdsByActivityIds = getStatuses(session, eligibleIds);
		
    		String queryString = "select aav from " + AmpActivityVersion.class.getName() +
		          " aav " + "where aav.ampActivityId IN (" + Util.toCSString(eligibleIds) + ")";
            		
    		List<AmpActivityVersion> closeableActivities = session.createQuery(queryString).list();
    		for(AmpActivityVersion ver:closeableActivities)
    		{
        		String newStatus = ver.getApprovalStatus().equals(Constants.STARTED_APPROVED_STATUS) ? Constants.STARTED_STATUS : Constants.EDITED_STATUS;
        		
    			System.out.format("\tautoclosing activity %d, changing status ID from %d to %d and approvalStatus from <%s> to <%s>...", 
    					ver.getAmpActivityId(), statusIdsByActivityIds.get(ver.getAmpActivityId()), closedCategoryValue, 
    					ver.getApprovalStatus(), newStatus);
    			
        		AmpTeamMember ampClosingMember = AmpBackgroundActivitiesCloser.createActivityCloserTeamMemberIfNeeded(ver.getTeam());
        		
    			//AmpActivityVersion newVer = org.dgfoundation.amp.onepager.util.ActivityUtil.saveActivityNewVersion(ver, ampClosingMember, false, session);
        		AmpActivityVersion newVer = (AmpActivityVersion) session.load(AmpActivityVersion.class, ver.getAmpActivityId());
    			newVer.setApprovalStatus(newStatus);
    			newVer.getAmpActivityGroup().setAutoClosedOnExpiration(true);
    			newVer.setModifiedBy(ampClosingMember);
    			//newVer.setStatusReason(getNewStatusReason(newVer));
    			session.update(newVer.getAmpActivityGroup());
    			session.update(newVer);
    			
    			Query updateQuery1 = session.createSQLQuery("UPDATE amp_activities_categoryvalues SET amp_categoryvalue_id=:closedCategoryValue WHERE amp_activity_id=:amp_activity_id AND amp_categoryvalue_id=:oldCategoryValue") 
    					.addSynchronizedQuerySpace("amp_activities_categoryvalues")
    					.addSynchronizedQuerySpace("amp_activity_version")
    					.setLong("amp_activity_id", newVer.getAmpActivityId())
    					.setLong("closedCategoryValue", closedCategoryValue)
    					.setLong("oldCategoryValue", statusIdsByActivityIds.get(ver.getAmpActivityId()));
    			updateQuery1.executeUpdate();
    					
    			System.out.format("... done, new amp_activity_id=%d\n", newVer.getAmpActivityId());
    		}
    	}
    	catch(Exception e)
    	{    		
    		e.printStackTrace();
    		// not rethrowing, because we don't want the thread to die
    	}
    }
}
