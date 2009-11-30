package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.TeamMember;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NpdUtil {
	private static Logger logger = Logger.getLogger(NpdUtil.class);

	public static void updateSettings(NpdSettings settings) throws AimException {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(settings);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					logger.error("...Rollback of NPD failed");
					throw new AimException("Can't rollback", ex);
				}
			}
			throw new AimException("Can't update NPD settings", e);
		}
	}

	public static NpdSettings getCurrentSettings(Long teamId)
			throws AimException {
		Session session = null;
		NpdSettings npdSettings = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class, teamId);
			if (ampTeam.getNpdSettings() == null) {
				npdSettings = new NpdSettings();
				npdSettings.setWidth(new Integer(ChartUtil.CHART_WIDTH));
				npdSettings.setHeight(new Integer(ChartUtil.CHART_HEIGHT));
				npdSettings.setTeam(ampTeam);
			} else {
				npdSettings = ampTeam.getNpdSettings();
			}
		} catch (ObjectNotFoundException ex) {
			logger.error("Unable to load team");
			throw new AimException("Team can't be found", ex);
		} catch (Exception e) {
			logger.error("Unable to load NpdSettings");
			throw new AimException("Cannot load NPD Settings", e);
		}
		return npdSettings;
	}
	
	/**
	 * Converts year represented as String to date. It can create last day or
	 * first day of the year depending on the second parameter. todo: this
	 * should be changed to last and first second of the year.
	 *
	 * @param year
	 * @param lastSecondOfYear
	 * @return
	 * @throws AimException
	 */
	public static Date yearToDate(String year, boolean lastSecondOfYear) throws AimException {
		if (year != null) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, Integer.valueOf(year).intValue());
				if (lastSecondOfYear) {
					cal.set(Calendar.MONTH, Calendar.DECEMBER);
					cal.set(Calendar.DAY_OF_MONTH, 31);
				} else {
					cal.set(Calendar.MONTH, Calendar.JANUARY);
					cal.set(Calendar.DAY_OF_MONTH, 1);
				}
				return cal.getTime();
			} catch (Exception e) {
				logger.error(e);
				throw new AimException("Cannot convert year: " + year
						+ " to int. Invalid request param", e);
			}
		}
		return null;
	}
	
	/**
	 * Retrieves Activities filtered according params.
	 * @param ampThemeId filter activities assigned to program(Theme) specified with this id.
	 * @param statusCode filter activities, get only with this status.
	 * @param donorOrgId
	 * @param fromDate
	 * @param toDate
	 * @param locationId
	 * @param teamMember
	 * @param recurse
	 * @return
	 * @throws AimException
	 */
	public static Collection<ActivityItem> getActivities(Long ampThemeId,String statusCode,String donorOrgId,
			Date fromDate,Date toDate,Long locationId,TeamMember teamMember,Integer pageStart,Integer rowCount) throws AimException, DgException{
		
		Collection<ActivityItem> result=null;
		//search actvities in db, with pagination.
        result = ActivityUtil.searchActivitieProgPercents(ampThemeId,statusCode,donorOrgId,fromDate,toDate,locationId,teamMember,pageStart,rowCount);
        
		//Set<AmpActivity> activities = new TreeSet<AmpActivity>(new ActivityUtil.ActivityIdComparator());
		List<ActivityItem> sortedActivities = null;
		if (result!=null){
			sortedActivities= new ArrayList<ActivityItem>(result);
			Collections.sort(sortedActivities);
		}

		return sortedActivities;
	}
	
	public static Integer getActivitiesCount(Long ampThemeId,String statusCode,String donorOrgId,Date fromDate,Date toDate,
			Long locationId,TeamMember teamMember,boolean recurse) throws AimException, DgException{

		int result=0;
		result = ActivityUtil.searchActivitiesCount(ampThemeId,statusCode,donorOrgId,fromDate,toDate,locationId,teamMember);

		if (recurse){
			Collection<AmpTheme> children = ProgramUtil.getSubThemes(ampThemeId);
			if (children!= null && children.size() > 0){
				for (AmpTheme prog : children) {
					Integer childsActivities=getActivitiesCount(prog.getAmpThemeId(), statusCode, donorOrgId, fromDate, toDate, locationId, teamMember, recurse);
					if (childsActivities!=null){
						result+=childsActivities;
					}
				}
			}
		}

		return result;
	}

}
