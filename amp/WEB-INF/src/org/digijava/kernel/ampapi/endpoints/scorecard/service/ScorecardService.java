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
 * 
 * @author Emanuel Perez
 * 
 */
public class ScorecardService {

	private static final int DEFAULT_START_YEAR = 2010;
	private static final Logger LOGGER = Logger.getLogger(ScorecardService.class);
	private AmpScorecardSettings settings;
	private AmpFiscalCalendar fiscalCalendar;

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
						+ " l.authorname,l.loggeddate,a.amp_activity_id,l.modifydate "
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
						activityUpdate.setActivityId(rs.getLong("amp_activity_id"));
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

		Date currentDate = new Date(startDate.getTime());
		while (currentDate.compareTo(endDate) < 1) {
			for (int i = 1; i <= 4; i++) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				Quarter quarter = new Quarter(fiscalCalendar, i, cal.get(Calendar.YEAR));
				quarters.add(quarter);
				cal.add(Calendar.MONTH, 3);
				currentDate.setTime(cal.getTimeInMillis());
			}
		}
		return quarters;
	}

	public Map<Long, Map<String, ColoredCell>> getOrderedScorecardCells(List<ActivityUpdate> activityUpdates) {
		Map<Long, Map<String, ColoredCell>> data = initializeScorecardCellData();
		ICalendarWorker worker = fiscalCalendar.getworker();
		for (ActivityUpdate activityUpdate : activityUpdates) {
			Long donorId = activityUpdate.getDonorId();
			worker.setTime(activityUpdate.getModifyDate());
			Quarter quarter = new Quarter(fiscalCalendar, activityUpdate.getModifyDate());
			ColoredCell cell = data.get(donorId).get(quarter.toString());
			LOGGER.info("Quarter" + quarter);
			cell.setColor(ColoredCell.Colors.GREEN);
			// if the update is on the grace period mark previous one as yellow
			if (isUpdateOnGracePeriod(activityUpdate.getModifyDate())) {
				Quarter previousQuarter = quarter.getPreviousQuarter();
				ColoredCell previousQuarterCell = data.get(donorId).get(previousQuarter.toString());
				Integer amountOfUpdatedActivites = cell.getAmountOfUpdatedActivites();
				previousQuarterCell.setAmountOfUpdatedActivites(++amountOfUpdatedActivites);
				if (previousQuarterCell.getColor().equals(Colors.RED)) {
					previousQuarterCell.setColor(Colors.YELLOW);
				}
			}
			Integer amountOfUpdatedActivites = cell.getAmountOfUpdatedActivites();
			cell.setAmountOfUpdatedActivites(++amountOfUpdatedActivites);
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
				// TODO check for every quarter and donor. It is redundant.
				boolean isNoUpdateDonor = isNoUpdatesDonor(null, donorId);
				if (isNoUpdateDonor) {
					cell.setColor(ColoredCell.Colors.GRAY);
				}
				quarterCellMap.put(quarter.toString(), cell);
			}
			data.put(donorId, quarterCellMap);

		}
		return data;
	}

	/**
	 * Checks if a given donor is on the list of the donors that have been
	 * declared as not having updated for a given quarter
	 * 
	 * @param notUpdatedDonorIds
	 *            list of donors ids of the donors not having projects updates
	 *            on a given quarter
	 * @param donorId
	 *            the donor to check wether is on the list or not
	 * @return true if is marked as not having updates on a quarter, false
	 *         otherwise
	 */
	private boolean isNoUpdatesDonor(List<Long> notUpdatedDonorIds, Long donorId) {
		boolean isNoUpdate = false;
		if (notUpdatedDonorIds != null && !notUpdatedDonorIds.isEmpty()) {
			for (Long donorIdFromList : notUpdatedDonorIds) {
				if (donorIdFromList.equals(donorId)) {
					isNoUpdate = true;
					break;
				}
			}
		}
		return isNoUpdate;
	}
}
