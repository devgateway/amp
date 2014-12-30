package org.digijava.module.message.jobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.startup.AMPStartupListener;
import org.digijava.module.aim.startup.AmpBackgroundActivitiesCloser;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.esrigis.helpers.DbHelper;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;


public class CloseExpiredActivitiesJob implements StatefulJob {
	
	private static Logger logger = Logger.getLogger(CloseExpiredActivitiesJob.class);
	

	/**
	 * clones activity, sets modifying member, modification date, etc
	 * @param session
	 * @param oldActivity
	 * @return
	 * @throws CloneNotSupportedException
	 */
	protected AmpActivityVersion cloneActivity(Session session,
                                               AmpActivityVersion oldActivity,
                                               String newStatus,
                                               Long closedProjectStatusCategoryValue) throws CloneNotSupportedException
	{		
        ContentTranslationUtil.cloneTranslations(oldActivity);
        Long ampActivityGroupId = oldActivity.getAmpActivityGroup().getAmpActivityGroupId();
		AmpActivityVersion auxActivity = ActivityVersionUtil.cloneActivity(oldActivity, oldActivity.getModifiedBy());
		auxActivity.setAmpActivityId(null);

		session.evict(oldActivity);
		
		// Code related to versioning.
		AmpActivityGroup auxActivityGroup = (AmpActivityGroup) session.load(AmpActivityGroup.class, ampActivityGroupId);
		AmpActivityVersion prevVersion		= auxActivityGroup.getAmpActivityLastVersion();
		auxActivityGroup.setAmpActivityLastVersion(auxActivity);
		auxActivityGroup.setAutoClosedOnExpiration(true);
		session.save(auxActivityGroup);
		auxActivity.setAmpActivityGroup(auxActivityGroup);
		auxActivity.setModifiedDate(oldActivity.getModifiedDate());
		auxActivity.setModifiedBy(oldActivity.getModifiedBy());
		       
		// don't ask me why is this done
        AmpActivityContact actCont;
        Set<AmpActivityContact> contacts = new HashSet<AmpActivityContact>();
        Set<AmpActivityContact> activityContacts = auxActivity.getActivityContacts();
        if (activityContacts != null) {
            for (AmpActivityContact activityContact : activityContacts) {
                actCont = activityContact;
                actCont.setId(null);
                actCont.setActivity(auxActivity);
                session.save(actCont);
                contacts.add(actCont);
            }
            auxActivity.setActivityContacts(contacts);
        }
        auxActivity.setApprovalStatus(newStatus);
        auxActivity.getCategories().remove(CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME, auxActivity.getCategories()));
        auxActivity.getCategories().add(CategoryManagerUtil.getAmpCategoryValueFromDb(closedProjectStatusCategoryValue));
        session.save(auxActivity);
        
        java.util.Locale javaLocale = new java.util.Locale("en");
        LuceneUtil.addUpdateActivity(AMPStartupListener.SERVLET_CONTEXT_ROOT_REAL_PATH, true, 
        		SiteUtils.getDefaultSite(), javaLocale, 
        		auxActivity, prevVersion);
        return auxActivity;
	}
		
	/**
	 * commits & closes a session and then removes it from the SessionStackTraceMap
	 * @param session
	 */
	public final static void cleanupSession(Session session)
	{
		try{session.getTransaction().commit();}catch(Exception e){
			//logger.info("error committing transaction");
			e.printStackTrace();
		}
		try{session.close();}catch(Exception e){};
		try{PersistenceManager.removeClosedSessionsFromMap();}catch(Exception e){};
		try{
			synchronized(PersistenceManager.sessionStackTraceMap)
			{
				PersistenceManager.sessionStackTraceMap.remove(session);
			}
		}
		catch(Exception e){};
	}

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	try {
    		String val = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AUTOMATICALLY_CLOSE_ACTIVITIES);
    		boolean autoClosingEnabled = "true".equalsIgnoreCase(val);
    		if (!autoClosingEnabled)
    			return; // feature disabled => nothing to do;

    		logger.info("Running the activity autocloser job...");
    		
    		//no longer available: TLSUtils.forceLocaleUpdate(org.digijava.module.um.util.DbUtil.getLanguageByCode("en"));
    		TLSUtils.getThreadLocalInstance().site = SiteUtils.getDefaultSite();
    		
			Session session = PersistenceManager.getRequestDBSession();
			
			AmpBackgroundActivitiesCloser.createActivityCloserUserIfNeeded();
			cleanupSession(session); // commit user in case it was created
			session = PersistenceManager.getRequestDBSession();
		
    		Long closedCategoryValue = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.CLOSED_ACTIVITY_VALUE);

    		String filterQuery = "SELECT amp_activity_last_version_id FROM amp_activity_group aag WHERE aag.autoclosedonexpiration = false AND " +
				" aag.amp_activity_last_version_id IN (SELECT amp_activity_id FROM amp_activity WHERE (draft IS NULL or draft=false) " +
                    "AND (amp_team_id IS NOT NULL) " +
                    "AND (deleted IS NULL OR deleted = false) " +
                    "AND approval_status IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") " +
                    "AND actual_completion_date < now())" +
				" AND aag.amp_activity_last_version_id IN (select amp_activity_id FROM v_status WHERE amp_status_id != " + closedCategoryValue + ")" +
				"";
		
    		List<Long> eligibleIds = DbHelper.getInActivities(filterQuery);
    		//java.util.Map<Long, Long> statusIdsByActivityIds = getStatuses(session, eligibleIds);		            	
    		
    		List<AmpActivityVersion> closeableActivities;
    		if (eligibleIds.isEmpty()) {
                closeableActivities = new ArrayList<AmpActivityVersion>();
            } else {
        		String queryString = "select aav from " + AmpActivityVersion.class.getName() +
      		          " aav " + "where aav.ampActivityId IN (" + Util.toCSString(eligibleIds) + ")";
    			closeableActivities = session.createQuery(queryString).list();
    		}

            logger.info("Total " + closeableActivities.size() + " expired activities found");
    		
    		String newStatus = Constants.APPROVED_STATUS;
    		for (AmpActivityVersion ver : closeableActivities) {
    			if ("On".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECTS_VALIDATION))) {
    				 newStatus = ver.getApprovalStatus().equals(Constants.STARTED_APPROVED_STATUS) ? Constants.STARTED_STATUS : Constants.EDITED_STATUS;
    			}
        		
        		AmpCategoryValue oldActivityStatus = CategoryManagerUtil.getAmpCategoryValueFromList(CategoryConstants.ACTIVITY_STATUS_NAME, ver.getCategories());
        		
    			logger.info(String.format("\tautoclosing activity %d, changing status ID from %s to %d and approvalStatus from <%s> to <%s>...", 
    					ver.getAmpActivityId(), oldActivityStatus == null ?  "<null>" : Long.toString(oldActivityStatus.getId()), closedCategoryValue, 
    					ver.getApprovalStatus(), newStatus));

                // Copy the member from the previous version https://jira.dgfoundation.org/browse/AMP-18329
        		// AmpTeamMember ampClosingMember = AmpBackgroundActivitiesCloser.createActivityCloserTeamMemberIfNeeded(ver.getTeam());

                AmpActivityVersion newVer = cloneActivity(session, ver, newStatus, closedCategoryValue);

    			logger.info(String.format("... done, new amp_activity_id=%d\n", newVer.getAmpActivityId()));
    		}
    		cleanupSession(session); // close transaction and reopen it, for the changes to become visible to the rest of AMP and the next code run in this thread to have an available session
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		// not rethrowing, because we don't want the thread to die
    	}
    }
}