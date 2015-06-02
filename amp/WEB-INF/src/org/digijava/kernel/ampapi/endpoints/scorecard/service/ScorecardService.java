package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ActivityUpdate;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell.Colors;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.dbentity.NoUpdateOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.hibernate.jdbc.Work;

import clover.org.apache.commons.lang.StringUtils;
import clover.org.apache.log4j.Logger;

/**
 * Service class for Scorecard generation
 * 
 * @author Emanuel Perez
 * 
 */
public class ScorecardService {

	private static final int DEFAULT_START_YEAR = 2010;
	private static final Logger LOGGER = Logger.getLogger(ScorecardService.class);
	private AmpScorecardSettings settings;
	private AmpFiscalCalendar fiscalCalendar;
	private Double DEFAULT_THRESHOLD = 70d;

	public ScorecardService() {
		Collection<AmpScorecardSettings> settingsList = DbUtil.getAll(AmpScorecardSettings.class);
		if (settingsList != null && !settingsList.isEmpty()) {
			settings = settingsList.iterator().next();
		}
		Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
		this.fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);

	}

	public List<ActivityUpdate> getDonorActivityUpdates() {

		return getDonorActivityUpdates(null);
	}

	public List<ActivityUpdate> getDonorActivityUpdates(final List<String> allowedStatuses) {
		final List<ActivityUpdate> activityUpdateList = new ArrayList<ActivityUpdate>();
		int startYear = getDefaultStartYear();
		int endYear = getDefaultEndYear();
		String gsCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		Date startDate = CalendarUtil.getStartDate(Long.valueOf(gsCalendarId), startYear);
		Date endDate = CalendarUtil.getEndDate(Long.valueOf(gsCalendarId), endYear);
		String pattern = "yyyy-MM-dd";
		final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
		final String formattedEndDate = new SimpleDateFormat(pattern).format(endDate);
		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				String query = "SELECT distinct (l.id),r.organisation ,l.teamname, "
						+ " l.authorname,l.loggeddate,a.amp_id,l.modifydate "
						+ "FROM amp_audit_logger l, amp_activity_version a,amp_org_role r "
						+ "WHERE objecttype = 'org.digijava.module.aim.dbentity.AmpActivityVersion' "
						+ "AND a.amp_activity_id = l.objectid:: integer " + "AND r.activity=a.amp_activity_id "
						+ "AND    (EXISTS " + "        ( " + "        SELECT af.amp_donor_org_id "
						+ "       FROM   amp_funding af " + "      WHERE  r.organisation = af.amp_donor_org_id "
						+ "     AND    (( af.source_role_id IS NULL) "
						+ "     OR     af.source_role_id =( SELECT amp_role_id         FROM   amp_role "
						+ "  WHERE  role_code='DN')))) " + " AND modifydate>='" + formattedStartDate
						+ "' AND modifydate <= '" + formattedEndDate + "' ";

				if (allowedStatuses != null && !allowedStatuses.isEmpty()) {
					String status = StringUtils.join(allowedStatuses, ",");
					query += " AND approval_status IN (" + status + ") ";
				}
				query += "ORDER BY r.organisation,l.modifydate ";

				try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
					ResultSet rs = rsi.rs;
					while (rs.next()) {
						ActivityUpdate activityUpdate = new ActivityUpdate();
						activityUpdate.setActivityId(rs.getString("amp_id"));
						activityUpdate.setAuditLoggerId(rs.getLong("id"));
						activityUpdate.setDonorId(rs.getLong("organisation"));
						activityUpdate.setWorkspaceName(rs.getString("teamname"));
						activityUpdate.setUserName(rs.getString("authorname"));
						activityUpdate.setLoggedDate(rs.getDate("loggeddate"));
						activityUpdate.setModifyDate(rs.getDate("modifydate"));
						activityUpdateList.add(activityUpdate);
					}
				}

			}
		});

		return activityUpdateList;
	}

	private int getDefaultStartYear() {
		String defaultStartYear = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
		int startYear = DEFAULT_START_YEAR;
		if (defaultStartYear != null && !"".equalsIgnoreCase(defaultStartYear)
				&& Integer.parseInt(defaultStartYear) > 0) {
			startYear = Integer.parseInt(defaultStartYear);
		}
		return startYear;
	}

	private int getDefaultEndYear() {
		String defaultEndYear = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
		int endYear = Calendar.getInstance().get(Calendar.YEAR);
		if (defaultEndYear != null && !"".equalsIgnoreCase(defaultEndYear) && Integer.parseInt(defaultEndYear) > 0) {
			endYear = Integer.parseInt(defaultEndYear);
		}
		return endYear;
	}

	/**
	 * Returns a list of all the donors
	 * 
	 * @return List <String>
	 */
	public List<AmpOrganisation> getDonors() {
		List<AmpOrganisation> donors = new ArrayList<AmpOrganisation>();
		List<JsonBean> donorJson = QueryUtil.getDonors();
		for (JsonBean bean : donorJson) {
			AmpOrganisation donor = new AmpOrganisation();
			donor.setName(bean.getString("name"));
			donor.setAmpOrgId(Long.valueOf(bean.getString("id")));
			donor.setAcronym(bean.getString("acronym"));
			donors.add(donor);
		}
		return donors;
	}

	public List<Quarter> getQuarters() {
		final List<Quarter> quarters = new ArrayList<Quarter>();
		int startYear = getDefaultStartYear();
		int endYear = getDefaultEndYear();
		String gsCalendarId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_CALENDAR);
		Date startDate = CalendarUtil.getStartDate(Long.valueOf(gsCalendarId), startYear);
		Date endDate = CalendarUtil.getEndDate(Long.valueOf(gsCalendarId), endYear);
		try {
			ICalendarWorker worker = fiscalCalendar.getworker();
			Date currentDate = new Date (startDate.getTime());
			int index = 1;
			while (currentDate.compareTo(endDate) < 1) {
				for (int i = 1; i <= 4; i++) {
					worker.setTime(currentDate);
					Quarter quarter = new Quarter(fiscalCalendar, i, worker.getYear());
					quarters.add(quarter);
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.MONTH, 3 * index);
					index ++;
					currentDate.setTime(cal.getTimeInMillis());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Couldn't generate quarters ", e);
		}

		return quarters;
	}

	public Map<Long, Map<String, ColoredCell>> getOrderedScorecardCells(List<ActivityUpdate> activityUpdates) {
		Map<Long, Map<String, ColoredCell>> data = initializeScorecardCellData();
		data = countActivityUpdates(activityUpdates, data);
		data = processCells(data);
		return data;
	}

	private Map<Long, Map<String, ColoredCell>> processCells(final Map<Long, Map<String, ColoredCell>> data) {
		Set<AmpScorecardSettingsCategoryValue> statuses = settings.getClosedStatuses();
		String closedStatuses = "";
		for (AmpScorecardSettingsCategoryValue status : statuses) {
			closedStatuses += "'" + status.getAmpCategoryValueStatus().getValue() + "',";
		}
		if (!closedStatuses.equals("")) {
			closedStatuses = closedStatuses.substring(0, closedStatuses.length() - 1);
		}
		final String status = closedStatuses;
		List<Quarter> quarters = getQuarters();

		for (final Quarter quarter : quarters) {
			PersistenceManager.getSession().doWork(new Work() {
				public void execute(Connection conn) throws SQLException {
					Double threshold = settings.getPercentageThreshold();
					if (threshold == null) {
						threshold = DEFAULT_THRESHOLD;
					}
					String quarterStartDate = new SimpleDateFormat("yyyy-MM-dd").format(quarter.getQuarterStartDate());

					String query = "select count (distinct (a.amp_id)) as total_activities,r.organisation as donor_id "
							+ "from amp_activity_version a, amp_org_role r " + " WHERE  r.activity=a.amp_activity_id ";

					if (!status.equals("")) {
						query += "approval_status not in (" + status + " ) ";
					}
					query += "AND    (EXISTS  (SELECT af.amp_donor_org_id " + " FROM   amp_funding af "
							+ " WHERE  r.organisation = af.amp_donor_org_id "
							+ " AND    (( af.source_role_id IS NULL) "
							+ " OR     af.source_role_id =( SELECT amp_role_id         FROM   amp_role "
							+ " WHERE  role_code='DN')))) " + " and date_created <= '" + quarterStartDate + "' "
							+ " group by r.organisation; ";

					try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
						ResultSet rs = rsi.rs;
						while (rs.next()) {
							Integer totalActivities = rs.getInt("total_activities");
							Long donorId = rs.getLong("donor_id");
							ColoredCell cell = data.get(donorId).get(quarter.toString());
							Integer totalUpdatedActivities = cell.getUpdatedActivites().size();
							Integer totalUpdatedActivitiesOnGracePeriod = cell.getUpdatedActivitiesOnGracePeriod()
									.size();
							if (totalUpdatedActivities >= (totalActivities * (threshold / 100))) {
								cell.setColor(Colors.GREEN);
							} else if ((totalUpdatedActivities + totalUpdatedActivitiesOnGracePeriod) >= (totalActivities * (threshold / 100))) {
								cell.setColor(Colors.YELLOW);
							}
						}
					}

				}
			});
		}
		return data;
	}

	/**
	 * Count the number of activities that were updated on a given quarter for a
	 * donor.
	 * 
	 * @param activityUpdates
	 *            the list of activity of all activity updates
	 * @param data
	 *            the structure holding the ColoredCells for each donor and
	 *            quarter.
	 * @return the Map<Long, Map<String, ColoredCell>> with ColoredCells
	 *         containing the amount of activity updates.
	 */
	private Map<Long, Map<String, ColoredCell>> countActivityUpdates(List<ActivityUpdate> activityUpdates,
			Map<Long, Map<String, ColoredCell>> data) {
		ICalendarWorker worker = fiscalCalendar.getworker();
		for (ActivityUpdate activityUpdate : activityUpdates) {
			Long donorId = activityUpdate.getDonorId();
			worker.setTime(activityUpdate.getModifyDate());
			Quarter quarter = new Quarter(fiscalCalendar, activityUpdate.getModifyDate());
			ColoredCell cell = data.get(donorId).get(quarter.toString());
			LOGGER.info("Quarter" + quarter);
			if (isUpdateOnGracePeriod(activityUpdate.getModifyDate())) {
				Quarter previousQuarter = quarter.getPreviousQuarter();
				ColoredCell previousQuarterCell = data.get(donorId).get(previousQuarter.toString());
				previousQuarterCell.getUpdatedActivitiesOnGracePeriod().add(activityUpdate.getActivityId());
			}
			cell.getUpdatedActivites().add(activityUpdate.getActivityId());
		}
		return data;
	}

	private boolean isUpdateOnGracePeriod(Date updateDate) {
		boolean isOnGracePeriod = false;
		if (settings != null && settings.getValidationPeriod()) {
			Integer weekNumber = settings.getValidationTime();
			try {
				Quarter quarter = new Quarter(fiscalCalendar, updateDate);
				Date quarterStartDate = quarter.getQuarterStartDate();
				Calendar calendarGracePeriod = Calendar.getInstance();
				calendarGracePeriod.setTime(quarterStartDate);
				calendarGracePeriod.add(Calendar.WEEK_OF_YEAR, weekNumber);
				if (quarterStartDate.compareTo(updateDate) <= 0
						&& updateDate.compareTo(calendarGracePeriod.getTime()) <= 0) {
					isOnGracePeriod = true;
				}

			} catch (Exception e) {
				LOGGER.warn("Couldn't get quarter for date " + updateDate);
			}
		}
		return isOnGracePeriod;
	}

	private Map<Long, Map<String, ColoredCell>> initializeScorecardCellData() {
		Map<Long, Map<String, ColoredCell>> data = new HashMap<Long, Map<String, ColoredCell>>();
		List<Quarter> quarters = getQuarters();
		List<JsonBean> donors = QueryUtil.getDonors();
		for (JsonBean donor : donors) {
			Long donorId = (Long) donor.get("id");
			Map<String, ColoredCell> quarterCellMap = new HashMap<String, ColoredCell>();
			for (Quarter quarter : quarters) {

				ColoredCell cell = new ColoredCell();
				cell.setDonorId(donorId);
				cell.setQuarter(quarter);
				quarterCellMap.put(quarter.toString(), cell);
			}
			data.put(donorId, quarterCellMap);

		}
		data = markNoUpdateDonorCells(data);
		return data;
	}

	/**
	 * Sets the ColoredCells' fill color to gray for the donors and quarters
	 * that have explicitly declared that they don't have projects updates for
	 * the given quarter
	 * 
	 * @param data
	 *            Map<Long, Map<String, ColoredCell>>, containing the
	 *            ColoredCells for every donor and quarter
	 * @return Map<Long, Map<String, ColoredCell>> the ColoredCells for each
	 *         donor and quarter filled with gray when it has been declared that
	 *         the donor/quarter don't have a project update
	 */
	private Map<Long, Map<String, ColoredCell>> markNoUpdateDonorCells(Map<Long, Map<String, ColoredCell>> data) {
		Collection<NoUpdateOrganisation> noUpdateDonors = DbUtil.getAll(NoUpdateOrganisation.class);
		
		for (NoUpdateOrganisation noUpdateDonor : noUpdateDonors) {
			Quarter quarter = new Quarter(fiscalCalendar, noUpdateDonor.getModifyDate());
			ColoredCell noUpdateCell = data.get(noUpdateDonor.getAmpDonorId()).get(quarter.toString());
			noUpdateCell.setColor(Colors.GRAY);
		}
		return data;
	}

}
