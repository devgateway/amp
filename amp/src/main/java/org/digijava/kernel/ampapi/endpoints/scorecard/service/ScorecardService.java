package org.digijava.kernel.ampapi.endpoints.scorecard.service;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.digijava.kernel.ampapi.endpoints.common.model.Org;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ActivityUpdate;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.ColoredCell.Colors;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.DonorScoreCardStats;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardOrganisation;
import org.digijava.module.aim.dbentity.AmpScorecardSettings;
import org.digijava.module.aim.dbentity.AmpScorecardSettingsCategoryValue;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.fiscalcalendar.GregorianBasedWorker;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Calendar.YEAR;

/**
 * Service class for Scorecard generation
 *
 * @author Emanuel Perez
 */
public class ScorecardService {

    private static final int DEFAULT_START_YEAR = 2010;
    private static final Logger logger = Logger.getLogger(ScorecardService.class);
    private AmpScorecardSettings settings;
    private AmpFiscalCalendar fiscalCalendar;
    private Double DEFAULT_THRESHOLD = 70d;

    private static final int MONTHS_IN_QUARTER = 3;
    private static final int QUARTERS_IN_YEAR = 4;

    public ScorecardService() {
        getAmpScorecardSettings();
        Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        this.fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);
    }

    private void getAmpScorecardSettings() {
        Collection<AmpScorecardSettings> settingsList = DbUtil.getAll(AmpScorecardSettings.class);
        if (settingsList != null && !settingsList.isEmpty()) {
            settings = settingsList.iterator().next();
        } else {
            // set default values
            settings = new AmpScorecardSettings();
            settings.setPercentageThreshold(DEFAULT_THRESHOLD);
            settings.setValidationPeriod(false);
            settings.setClosedStatuses(new HashSet<AmpScorecardSettingsCategoryValue>());
        }
    }

    @SuppressWarnings("unchecked")
    public List<AmpScorecardOrganisation> getAllNoUpdateDonors(final int startYear, final int endYear) {
        Date startDate = CalendarUtil.getStartDate(fiscalCalendar.getAmpFiscalCalId(), startYear);
        Date endDate = CalendarUtil.getEndDate(fiscalCalendar.getAmpFiscalCalId(), endYear);
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "from " + AmpScorecardOrganisation.class.getName()
                + " nuo where nuo.modifyDate >= :startDate and nuo.modifyDate <= :endDate and nuo.toExclude = false";
        Query query = session.createQuery(queryString);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.list();
    }

    /**
     * Returns the list of all ActivityUpdates that occurred on the system except the ones from private WS
     *
     * @return List<ActivityUpdate> , list with all ActivityUpdates
     */
    public List<ActivityUpdate> getDonorActivityUpdates(final int startYear, final int endYear) {
        final List<ActivityUpdate> activityUpdateList = new ArrayList<ActivityUpdate>();
        Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        Date startDate = CalendarUtil.getStartDate(gsCalendarId, startYear);
        Date endDate = CalendarUtil.getEndDate(gsCalendarId, endYear);
        String pattern = "yyyy-MM-dd";
        final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
        final String formattedEndDate = new SimpleDateFormat(pattern).format(endDate);
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = "select id,organisation ,teamname, " + " authorname,loggeddate,amp_id,modifydate "
                        + " from ( " + " select l.id,r.organisation ,l.teamname, "
                        + " l.authorname,l.loggeddate,a.amp_id,l.modifydate , row_number() over(partition by "
                        + "amp_activity_group_id order by l.id desc) as row "
                        + " FROM amp_audit_logger l, amp_activity_version a, amp_org_role r  "
                        + " WHERE objecttype = 'org.digijava.module.aim.dbentity.AmpActivityVersion' "
                        + " AND a.amp_activity_id = l.objectid:: integer  " + " AND r.activity=a.amp_activity_id  "
                        + " AND not exists(select 1 from amp_organisation ao where ao.deleted = true and ao"
                        + ".amp_org_id=r.organisation) "
                        + " AND    (EXISTS (  SELECT af.amp_donor_org_id "
                        + " FROM   amp_funding af, amp_activity_version v, amp_team t WHERE  r.organisation = af"
                        + ".amp_donor_org_id "
                        + " AND v.amp_activity_id = af.amp_activity_id AND v.amp_activity_id=a.amp_activity_id AND v"
                        + ".deleted is false AND (a.draft = false OR a.draft is null) "
                        + " AND v.amp_team_id = t.amp_team_id AND t.isolated = false  "
                        + " AND    (( af.source_role_id IS NULL)  "
                        + " OR     af.source_role_id =( SELECT amp_role_id         FROM   amp_role "
                        + " WHERE  role_code='DN'))  "
                        + " AND af.amp_donor_org_id NOT IN (SELECT amp_donor_id FROM amp_scorecard_organisation WHERE"
                        + " to_exclude = true))) "
                        + " AND modifydate>='" + formattedStartDate + "' AND modifydate <= '" + formattedEndDate + "'  "
                        + " AND (a.draft = false OR a.draft is null) AND a.deleted is false  " + " ) t "
                        + " where t.row=1 ";

                query += "ORDER BY organisation , modifydate ";

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
                    rsi.close();
                }

            }
        });

        return activityUpdateList;
    }

    /**
     * Returns the list of Scorecard Donors
     *
     * @param toExlcude
     * @return List<ScorecardNoUpdateDonor> , list with donors
     * if toExclude param is true, the method will return all the donors except:
     * - excluded donors
     * - donors without activities in non-private WS
     * if toExclude param is false, the method will return the no update donors
     */
    public List<ScorecardNoUpdateDonor> getScorecardDonors(final boolean toExlcude) {

        final List<ScorecardNoUpdateDonor> donorsList = new ArrayList<ScorecardNoUpdateDonor>();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn, "amp_organisation",
                        "amp_org_id", "name");

                String query = "SELECT (o.amp_org_id) amp_org_id, o.name, o.acronym FROM  amp_organisation o WHERE o"
                        + ".amp_org_id IN ("
                        + "SELECT distinct o.amp_org_id FROM  amp_organisation o, amp_org_role aor, amp_role r "
                        + "WHERE (o.amp_org_id = aor.organisation AND aor.role = r.amp_role_id AND r.role_code = 'DN') "
                        + "AND activity NOT IN (select amp_activity_id from amp_activity_version where amp_team_id IN"
                        + " (SELECT amp_team_id from amp_team where isolated = true))"
                        + "UNION "
                        + "SELECT distinct o.amp_org_id FROM  amp_organisation o, amp_funding af, "
                        + "amp_activity_version v, amp_role r   "
                        + "WHERE  o.amp_org_id = af.amp_donor_org_id  AND v.amp_activity_id = af.amp_activity_id  AND"
                        + " (v.deleted is false) "
                        + "AND ((af.source_role_id IS NULL) OR af.source_role_id = r.amp_role_id and r.role_code = "
                        + "'DN') "
                        + "AND v.amp_team_id NOT IN (SELECT amp_team_id from amp_team where isolated = true)"
                        + ") AND (o.deleted IS NULL OR o.deleted = false ) "
                        + "AND o.amp_org_id ";

                if (toExlcude) {
                    query += "NOT ";
                }
                query += "IN (SELECT amp_donor_id FROM amp_scorecard_organisation";

                if (!toExlcude) {
                    query += " WHERE to_exclude = " + toExlcude;
                }

                query += ")";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        ScorecardNoUpdateDonor donor = new ScorecardNoUpdateDonor();
                        donor.setAmpDonorId(rs.getLong("amp_org_id"));
                        if (ContentTranslationUtil.multilingualIsEnabled()) {
                            donor.setName(organisationsNames.get(rs.getLong("amp_org_id")));
                        } else {
                            donor.setName(rs.getString("name"));
                        }
                        donorsList.add(donor);
                    }
                    rsi.close();

                }
            }
        });

        return donorsList;
    }

    /**
     * Gets the default start year for the donor scorecard. It tries to get it
     * from Global Settings, if it is not defined it uses a default value of
     * 2010.
     *
     * @return the default start year for generating the donor scorecard
     */
    public int getDefaultStartYear() {
        int defaultStartYear =
                FeaturesUtil.getGlobalSettingValueInteger(Constants.GlobalSettings.START_YEAR_DEFAULT_VALUE);
        int startYear = defaultStartYear > 0 ? defaultStartYear : DEFAULT_START_YEAR;

        return startYear;
    }

    /**
     * Gets the default end year for the donor scorecard. It tries to get it
     * from Global Settings, if it is not defined it uses the current year.
     *
     * @return the default end year for generating the donor scorecard
     */
    public int getDefaultEndYear() {
        int defaultEndYear = FeaturesUtil.getGlobalSettingValueInteger(Constants.GlobalSettings.END_YEAR_DEFAULT_VALUE);
        int endYear = defaultEndYear > 0 ? defaultEndYear : Calendar.getInstance().get(Calendar.YEAR);

        return endYear;
    }

    /**
     * Returns a list of all the donors
     */
    public List<AmpOrganisation> getFilteredDonors() {
        List<AmpOrganisation> donors = new ArrayList<AmpOrganisation>();
        List<Org> orgs = QueryUtil.getDonors(true);
        for (Org org : orgs) {
            AmpOrganisation donor = new AmpOrganisation();
            donor.setName(org.getName());
            donor.setAmpOrgId(org.getId());
            donor.setAcronym(org.getAcronym());
            donors.add(donor);
        }
        return donors;
    }

    /**
     * Returns a list of all quarters that will span the donor scorecard. The
     * start and end of the period is defined through Global Settings:
     * START_YEAR_DEFAULT_VALUE and END_YEAR_DEFAULT_VALUE
     *
     * @return List<Quarter>, the list of the quarters that will represent the
     * headers of the donor scorecard file.
     */
    public List<Quarter> getQuarters(final int startYear, final int endYear, String filteredQuartersAsString) {
        final List<Quarter> quarters = new ArrayList<Quarter>();
        List<String> filteredQuarters = Arrays.asList(filteredQuartersAsString.split(","));

        Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        long startTime = CalendarUtil.getStartDate(gsCalendarId, startYear).getTime();
        long endTime = CalendarUtil.getEndDate(gsCalendarId, endYear).getTime();

        int year = startYear;

        try {
            ICalendarWorker worker = fiscalCalendar.getworker();
            while (startTime < endTime) {
                long currentTime = startTime;
                for (int i = 1; i <= 4; i++) {
                    worker.setTime(new Date(currentTime));
                    Quarter quarter = new Quarter(fiscalCalendar, i, worker.getYear());
                    if (filteredQuarters.contains(quarter.getLabel())) {
                        quarters.add(quarter);
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(startTime);
                    cal.add(Calendar.MONTH, 3 * i);
                    currentTime = cal.getTimeInMillis();
                }
                year++;
                startTime = CalendarUtil.getStartDate(gsCalendarId, year).getTime();
            }
        } catch (Exception e) {
            logger.error("Couldn't generate quarters ", e);
        }

        return quarters;
    }

    public List<Quarter> getSettingsQuarters() {
        return getQuarters(getDefaultStartYear(), getDefaultEndYear(), settings.getQuarters());
    }

    private List<Quarter> getAllQuarters(final int startYear, final int endYear) {
        return getQuarters(startYear, endYear, "Q1,Q2,Q3,Q4");
    }

    /**
     * Returns the last past quarter
     *
     * @return Quarter, the last past quarter
     */
    public Quarter getPastQuarter() {
        List<Quarter> quarters = getAllQuarters(getDefaultStartYear(), getDefaultEndYear());
        Quarter quarter = null;
        Date currentDate = new Date();

        int i = 0;
        if (quarters.size() > 0) {
            while (i < quarters.size() && quarters.get(i).getQuarterEndDate().before(currentDate)) {
                i++;
            }
            quarter = i > 0 ? quarters.get(i) : quarters.get(0);
        }

        return quarter;
    }

    /**
     * Generates the Map<Long, Map<String, ColoredCell>> with the data of the
     * ColoredCells that will be used to create the donor scorecard
     *
     * @param activityUpdates the List of all ActivityUpdates that took place during the
     *                        period for which the scorecard will be generated.
     * @return a Map<Long, Map<String, ColoredCell>> with all ColoredCells
     * filled with the appropiate colors. For each quarter and donor
     * there is a ColoredCell.
     */
    public Map<Long, Map<String, ColoredCell>> getOrderedScorecardCells(final int startYear, final int endYear,
                                                                        List<ActivityUpdate> activityUpdates) {
        Map<Long, Map<String, ColoredCell>> data = initializeScorecardCellData(startYear, endYear);
        data = countActivityUpdates(activityUpdates, data);
        data = processCells(startYear, endYear, data);
        return data;
    }

    /**
     * Sets the fill color for every ColoredCell on Map<Long, Map<String,
     * ColoredCell>> based on the rules: - Green cell: If a donor has updated
     * more projects that the number defined by doing: Number of Updated
     * Projects by donor on a quarter > = Threshold (determined from
     * AmoScorecardSettings) X Total live projects of a donor on a given quarter
     * -Yellow cell: if a donor updates projects during the grace period, the
     * update occurred is counted double. It is counted on the quarter to which
     * the grace period belongs; and it is also counted on the current quarter.
     * If the updates performed on the grace period make the number of updates
     * to reach or surpass the threshold for that quarter, then the previous
     * quarter is marked with yellow.
     *
     * @param startYear
     * @param endYear
     * @param data      Map<Long, Map<String, ColoredCell>>
     * @return the Map<Long, Map<String, ColoredCell>> with the ColoredCell
     * filled with colors.
     */
    private Map<Long, Map<String, ColoredCell>> processCells(final int startYear, final int endYear,
                                                             final Map<Long, Map<String, ColoredCell>> data) {
        Set<AmpScorecardSettingsCategoryValue> statuses = settings.getClosedStatuses();
        String closedStatuses = "";
        for (AmpScorecardSettingsCategoryValue status : statuses) {
            closedStatuses += "" + status.getAmpCategoryValueStatus().getId() + ",";
        }
        if (!closedStatuses.equals("")) {
            closedStatuses = closedStatuses.substring(0, closedStatuses.length() - 1);
        }
        final String status = closedStatuses;
        List<Quarter> quarters = getAllQuarters(startYear, endYear);

        for (final Quarter quarter : quarters) {
            PersistenceManager.getSession().doWork(new Work() {
                public void execute(Connection conn) throws SQLException {
                    Double threshold = settings.getPercentageThreshold();
                    if (threshold == null) {
                        threshold = DEFAULT_THRESHOLD;
                    }
                    String quarterEndDate = new SimpleDateFormat("yyyy-MM-dd")
                            .format(getGregorianDate(quarter.getQuarterEndDate()));
                    //Get total (not completed nor deleted) activities  by donor at the end of a given quarter

                    String query = "SELECT COUNT(DISTINCT (a.amp_id)) AS total_activities, r.organisation as donor_id"
                            + " FROM amp_activity_version a, amp_org_role r, amp_organisation o, "
                            + " amp_activities_categoryvalues c, amp_category_value v"
                            + " WHERE r.activity = a.amp_activity_id "
                            + " AND a.amp_team_id NOT IN (SELECT amp_team_id FROM amp_team where isolated = true)"
                            + " AND o.amp_org_id = r.organisation " + "AND (o.deleted IS NULL OR o.deleted = false)"
                            + " AND (EXISTS (SELECT af.amp_donor_org_id" + " FROM amp_funding af,amp_activity_version v"
                            + " WHERE r.organisation = af.amp_donor_org_id"
                            + " AND a.amp_activity_id = v.amp_activity_id"
                            + " AND v.amp_activity_id = af.amp_activity_id"
                            + " AND (v.draft = false OR v.draft is null)"
                            + " AND v.deleted is false"
                            + " AND ((af.source_role_id IS NULL)"
                            + " OR af.source_role_id =(SELECT amp_role_id FROM  amp_role"
                            + " WHERE role_code='DN')))) " + " AND date_created <= '" + quarterEndDate + "' "
                            + " AND a.deleted is false AND (a.draft = false OR a.draft is null) "
                            + " AND a.amp_activity_id = c.amp_activity_id"
                            + " AND c.amp_categoryvalue_id = v.id"
                            + " AND v.amp_category_class_id = (SELECT id "
                            + " FROM amp_category_class WHERE keyname='activity_status')";
                    if (!status.equals("")) {
                        query += " AND c.amp_categoryvalue_id NOT IN (" + status + " ) ";
                    }
                    query += " AND a.amp_activity_id IN ("
                            + getLatestActivityIdsByDateQuery(quarterEndDate) + ") "
                            + " AND r.organisation NOT IN ("
                            + " SELECT amp_donor_id FROM amp_scorecard_organisation WHERE to_exclude = true)"
                            + " GROUP by r.organisation;";

                    try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                        ResultSet rs = rsi.rs;
                        while (rs.next()) {
                            Integer totalActivities = rs.getInt("total_activities");
                            Long donorId = rs.getLong("donor_id");
                            ColoredCell cell = data.get(donorId).get(quarter.toString());
                            cell.setTotalActivities(totalActivities);

                            //Imagine "Organisation 1" is first added as donor for an activity on Quarter 2, 2015.
                            //Then Quarter 1, 2015 for that organisation, total activities will be 0. Because that
                            // organisation was not
                            //yet a donor.
                            if (isDonorFirstQuarter(data, quarter, donorId)) {
                                Quarter previousQuarter = quarter.getPreviousQuarter();
                                ColoredCell previousCell = data.get(donorId).get(previousQuarter.toString());

                                //if the previous cell has 0 total activities, but some activities were updated on
                                // his grace period
                                // then it is marked as YELLOW
                                if (previousCell.getUpdatedActivitiesOnGracePeriod().size() > 0) {
                                    previousCell.setColor(Colors.YELLOW);
                                }
                            }

                            Integer totalUpdatedActivities = cell.getUpdatedActivites().size();
                            Integer totalUpdatedActivitiesOnGracePeriod = cell.getUpdatedActivitiesOnGracePeriod()
                                    .size();
                            //we don't process process cells for no update donors

                            if (cell.getColor().equals(Colors.GRAY)) {
                                continue;
                            }

                            double thresholdAct = totalActivities * (threshold / BigInteger.TEN.pow(2).doubleValue());
                            if (totalUpdatedActivities >= thresholdAct) {
                                cell.setColor(Colors.GREEN);
                            } else if ((totalUpdatedActivities + totalUpdatedActivitiesOnGracePeriod) >= thresholdAct) {
                                cell.setColor(Colors.YELLOW);
                            }
                        }
                        rsi.close();

                    }

                }


            });
        }
        return data;
    }

    /**
     * Verifies if it is the first quarter where the the donor has related activities.
     * It represents the first quarter in which the organisation acts as a donor.
     *
     * @param data    the Map <String,ColoredCell> with ColoredCells by donors
     * @param quarter the quarter to verify if it is the first time the donor has activities
     * @param donorId donor organisation id
     * @return true  it is the first quarter where the the donor has related activities, false otherwise
     */
    private boolean isDonorFirstQuarter(final Map<Long, Map<String, ColoredCell>> data,
                                        final Quarter quarter, Long donorId) {
        boolean isDonorFirstQuarter = false;
        Quarter previousQuarter = quarter.getPreviousQuarter();
        ColoredCell previousCell = data.get(donorId).get(previousQuarter.toString());
        if (previousCell != null && previousCell.getTotalActivities() == 0) {
            isDonorFirstQuarter = true;
        }
        return isDonorFirstQuarter;

    }

    /**
     * Gets the latest activity ids for all existing activity that were updated before the end of a quarter.
     * <p>
     * We can have an activity that is 'on going' on Quarter 3 and 'completed' on quarter 4. In order to
     * validate quarter 3 correctly we need to know which is the latest version of the activity that was updated
     * before the quarter end and we should also omit updates and versions that happened after the end of the quarter.
     *
     * @param endPeriodDate, the end date of a quarter
     * @return String query with the latest versions of activities
     */
    public String getLatestActivityIdsByDateQuery(final String endPeriodDate) {
        String query = "SELECT max(a.amp_activity_id) AS last_activity_id"
                + " FROM amp_activity_version a"
                + " WHERE date_updated <= '" + endPeriodDate + "' "
                + " GROUP BY amp_id";
        return query;
    }

    /**
     * Count the number of activities that were updated on a given quarter for a
     * donor.
     *
     * @param activityUpdates the list of activity of all activity updates
     * @param data            the structure holding the ColoredCells for each donor and
     *                        quarter.
     * @return the Map<Long, Map<String, ColoredCell>> with ColoredCells
     * containing the amount of activity updates.
     */
    private Map<Long, Map<String, ColoredCell>> countActivityUpdates(List<ActivityUpdate> activityUpdates,
                                                                     Map<Long, Map<String, ColoredCell>> data) {
        for (ActivityUpdate activityUpdate : activityUpdates) {
            Long donorId = activityUpdate.getDonorId();
            Quarter quarter = new Quarter(fiscalCalendar, activityUpdate.getModifyDate());
            ColoredCell cell = data.get(donorId).get(quarter.toString());
            if (isUpdateOnGracePeriod(activityUpdate.getModifyDate())) {
                Quarter previousQuarter = quarter.getPreviousQuarter();
                ColoredCell previousQuarterCell = data.get(donorId).get(previousQuarter.toString());
                if (!isFirstQuarterOfPeriod(previousQuarterCell)) {
                    previousQuarterCell.getUpdatedActivitiesOnGracePeriod().add(activityUpdate.getActivityId());
                }
            }
            cell.getUpdatedActivites().add(activityUpdate.getActivityId());
        }
        return data;
    }

    private boolean isFirstQuarterOfPeriod(ColoredCell previousQuarterCell) {
        return (previousQuarterCell == null);
    }

    /**
     * Checks if a given date is on the Grace period of a quarter. If
     * AmpScorecardSettings property's validationPeriod is set to false, then
     * Grace period is disabled and the result is always false. Otherwise it
     * checks if the current date is between the current quarter start date and
     * the (current Quarter start date + number of weeks of the validation
     * period) if that is the case, the result is true
     *
     * @param updateDate , the date to check whether it is in the grave period or not.
     * @return true if it is in the grace period (validation period), false
     * otherwise
     */
    private boolean isUpdateOnGracePeriod(Date updateDate) {
        boolean isOnGracePeriod = false;
        if (settings != null && settings.getValidationPeriod() != null && settings.getValidationPeriod()) {
            Integer weekNumber = settings.getValidationTime();
            Quarter quarter = new Quarter(fiscalCalendar, updateDate);
            Date quarterStartDate = getGregorianDate(quarter.getQuarterStartDate());
            Calendar calendarGracePeriod = Calendar.getInstance();
            calendarGracePeriod.setTime(quarterStartDate);
            calendarGracePeriod.add(Calendar.WEEK_OF_YEAR, weekNumber);
            if (quarterStartDate.compareTo(updateDate) <= 0
                    && updateDate.compareTo(calendarGracePeriod.getTime()) <= 0) {
                isOnGracePeriod = true;
            }
        }
        return isOnGracePeriod;
    }

    /**
     * Populates the Map<Long, Map<String, ColoredCell>> with all the donors and
     * quarters that will be included on the donor scorecard. It also creates
     * the ColoredCells for every donor/quarter pair and fill it with
     * Colors.Gray if it is in the NoUpdateDonor list for the given quarter
     *
     * @param startYear
     * @param endYear
     * @return the initialized Map<Long, Map<String, ColoredCell>> with the
     * populated quarters and donors
     */
    private Map<Long, Map<String, ColoredCell>> initializeScorecardCellData(final int startYear, final int endYear) {
        Map<Long, Map<String, ColoredCell>> data = new HashMap<Long, Map<String, ColoredCell>>();
        List<Quarter> quarters = getAllQuarters(startYear, endYear);
        List<Org> donors = QueryUtil.getDonors(true);
        for (Org donor : donors) {
            Long donorId = donor.getId();
            Map<String, ColoredCell> quarterCellMap = new HashMap<String, ColoredCell>();
            for (Quarter quarter : quarters) {
                ColoredCell cell = new ColoredCell();
                cell.setDonorId(donorId);
                cell.setQuarter(quarter);
                quarterCellMap.put(quarter.toString(), cell);
            }
            data.put(donorId, quarterCellMap);

        }
        data = markNoUpdateDonorCells(startYear, endYear, data);
        return data;
    }

    /**
     * Sets the ColoredCells' fill color to gray for the donors and quarters
     * that have explicitly declared that they don't have projects updates for
     * the given quarter
     *
     * @param startYear
     * @param endYear
     * @param data      Map<Long, Map<String, ColoredCell>>, containing the
     *                  ColoredCells for every donor and quarter
     * @return Map<Long, Map < String, ColoredCell>> the ColoredCells for each
     * donor and quarter filled with gray when it has been declared that
     * the donor/quarter don't have a project update
     */
    private Map<Long, Map<String, ColoredCell>> markNoUpdateDonorCells(final int startYear, final int endYear,
                                                                       Map<Long, Map<String, ColoredCell>> data) {
        Collection<AmpScorecardOrganisation> noUpdateDonors = this.getAllNoUpdateDonors(startYear, endYear);

        for (AmpScorecardOrganisation noUpdateDonor : noUpdateDonors) {
            Quarter quarter = new Quarter(fiscalCalendar, noUpdateDonor.getModifyDate());
            Map<String, ColoredCell> donorQuarterCellMap = data.get(noUpdateDonor.getAmpDonorId());
            if (donorQuarterCellMap.containsKey(quarter.toString())) {
                ColoredCell noUpdateCell = donorQuarterCellMap.get(quarter.toString());
                noUpdateCell.setColor(Colors.GRAY);
            }
        }
        return data;
    }

    /**
     * Gets the count of active organisations for the past quarter
     *
     * @return int the count of active organisations for the past quarter
     */
    public int getPastQuarterOrganizationsCount() {

        final IntWrapper orgCount = new IntWrapper();

        Quarter pastQuarter = getPastQuarter();
        Date startDate = pastQuarter == null ? new Date() : getGregorianDate(pastQuarter.getQuarterStartDate());
        Date endDate = pastQuarter == null ? new Date() : getGregorianDate(pastQuarter.getQuarterEndDate());
        String pattern = "yyyy-MM-dd";
        final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
        final String formattedEndDate = new SimpleDateFormat(pattern).format(endDate);

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = "SELECT Count(DISTINCT( o.amp_org_id )) "
                        + " FROM   amp_organisation o  "
                        + " WHERE  ( ( EXISTS (SELECT af.amp_donor_org_id "
                        + " FROM   amp_funding af,  amp_activity v  "
                        + " WHERE  o.amp_org_id = af.amp_donor_org_id "
                        + " AND v.amp_activity_id = af.amp_activity_id "
                        + " AND v.date_updated <= '" + formattedEndDate + "' "
                        + " AND v.date_updated >= '" + formattedStartDate + "'  "
                        + " AND ( ( af.source_role_id IS NULL )  "
                        + " OR EXISTS (SELECT 1  FROM   amp_role r "
                        + " WHERE  role_code = 'DN' "
                        + " AND r.amp_role_id = af.source_role_id "
                        + " ) )) )  "
                        + " OR ( EXISTS (SELECT org.organisation  "
                        + " FROM   amp_org_role org,  " + " amp_activity av "
                        + " WHERE  org.activity = av.amp_activity_id "
                        + " and EXISTS  (SELECT 1   "
                        + " FROM   amp_role r WHERE  role_code = 'DN' "
                        + " AND r.amp_role_id=org.role  " + " ) "
                        + " AND av.date_updated <= '" + formattedEndDate + "' "
                        + " AND av.date_updated >= '" + formattedStartDate + "'  "
                        + " AND org.organisation = o.amp_org_id) ) )  "
                        + " AND ( o.deleted IS NULL  "
                        + " OR o.deleted = false ) ";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        orgCount.inc(rs.getInt("count"));
                    }
                    rsi.close();
                } catch (SQLException e) {
                    throw new ApiRuntimeException(
                            ApiError.toError("Exception while getting org types amount: " + e.getMessage()));
                }
            }
        });

        return orgCount.intValue();
    }

    /**
     * Gets the count of users logged into the System in the past quarter
     *
     * @return int the count of logged users in the past quarter
     */
    public int getPastQuarterUsersCount() {
        String queryString = "select count(distinct o.authorName) from amp_audit_logger o where o.action = 'login'";

        return getPastQuarterObjectsCount(queryString, "loggedDate");
    }

    /**
     * Gets the count of projects with action in the past quarter
     *
     * @return int the count of projects with action in the past quarter
     */
    public int getPastQuarterProjectsCount() {
        String queryString = "select count(distinct o.objectName) from amp_audit_logger o "
                + " where o.objecttype = 'org.digijava.module.aim.dbentity.AmpActivityVersion'";

        return getPastQuarterObjectsCount(queryString, "modifyDate");
    }

    private int getPastQuarterObjectsCount(String inputQuery, final String paramDate) {
        Quarter pastQuarter = getPastQuarter();

        Date startDate = pastQuarter == null ? new Date() : pastQuarter.getQuarterStartDate();
        Date endDate = pastQuarter == null ? new Date() : pastQuarter.getQuarterEndDate();

        String pattern = "yyyy-MM-dd";
        final String formattedStartDate = new SimpleDateFormat(pattern).format(startDate);
        final String formattedEndDate = new SimpleDateFormat(pattern).format(endDate);

        final String queryString = inputQuery + " AND o." + paramDate + " >= '" + formattedStartDate + "' "
                + "AND o." + paramDate + " <= '" + formattedEndDate + "'";

        final IntWrapper count = new IntWrapper();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, queryString, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        count.inc(rs.getInt("count"));
                    }
                    rsi.close();

                } catch (SQLException e) {
                    throw new ApiRuntimeException(
                            ApiError.toError("Exception while getting past quarter objects: " + e.getMessage()));
                }
            }
        });


        return count.intValue();
    }

    private Date getGregorianDate(Date sourceDate) {
        if (!(fiscalCalendar.getworker() instanceof GregorianBasedWorker)) {
            return FiscalCalendarUtil.toGregorianDate(sourceDate, fiscalCalendar);
        }

        return sourceDate;
    }

    public DonorScoreCardStats getDonorScorecardStats(final Integer scorecardYear, final Integer quarter) {
        Quarter scorecardQuarter = getQuarterForDonorStats(scorecardYear, quarter);
        int year = scorecardQuarter.getYear();
        int numOfDonors = getFilteredDonors().size();
        Map<Long, Map<String, ColoredCell>> orderedScorecardCells = getOrderedScorecardCells(year, year,
                getDonorActivityUpdates(year, year));

        List<AmpScorecardOrganisation> allNoUpdateDonors = this.getAllNoUpdateDonors(year, year);
        List<Long> allNoUpdateDonorsIds = allNoUpdateDonors.stream()
                .map(AmpScorecardOrganisation::getAmpDonorId)
                .collect(Collectors.toList());

        IntWrapper lateWrapper = new IntWrapper();
        IntWrapper onTimeWrapper = new IntWrapper();
        IntWrapper validationPeriodWrapper = new IntWrapper();

        orderedScorecardCells.entrySet().stream()
                .filter((a -> !allNoUpdateDonorsIds.contains(a.getKey())))
                .forEach(a -> {
                    Colors color = a.getValue().get(scorecardQuarter.toString()).getColor();
                    if (color.equals(Colors.RED)) {
                        lateWrapper.inc();
                    } else if (color.equals(Colors.GREEN)) {
                        onTimeWrapper.inc();
                    } else if (color.equals(Colors.YELLOW)) {
                        validationPeriodWrapper.inc();
                    }
                });

        int onTime = getPercentage(new BigDecimal(onTimeWrapper.intValue()), new BigDecimal(numOfDonors)).intValue();
        int late = getPercentage(new BigDecimal(lateWrapper.intValue()), new BigDecimal(numOfDonors)).intValue();
        int no = getPercentage(new BigDecimal(allNoUpdateDonors.size()), new BigDecimal(numOfDonors)).intValue();
        int validationPeriod = getPercentage(new BigDecimal(validationPeriodWrapper.intValue()),
                new BigDecimal(numOfDonors)).intValue();
        return new DonorScoreCardStats(onTime, late, no, validationPeriod);
    }

    private Quarter getQuarterForDonorStats(final Integer year, final Integer quarter) {
        if (year == null && quarter == null) {
            return getPastQuarter();
        }

        int currentYear = Calendar.getInstance().get(YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        if (year < getDefaultStartYear() || year > currentYear) {
            throw new ApiRuntimeException(ApiError.toError("Invalid year"));
        }

        if (quarter == null) {
            if (year < currentYear) {
                return getQuarters(year, year, "Q4").get(0);
            } else {
                return getPastQuarter();
            }
        } else {
            if (quarter > QUARTERS_IN_YEAR) {
                throw new ApiRuntimeException(ApiError.toError("Invalid quarter"));
            } else if (year == currentYear && quarter - 1 > currentMonth / MONTHS_IN_QUARTER) {
                throw new ApiRuntimeException(ApiError.toError("Invalid quarter and year"));
            }
        }

        return getQuarters(year, year, "Q" + quarter).get(0);
    }

    protected BigDecimal getPercentage(BigDecimal a, BigDecimal b) {
        return a.divide(b, NiFormula.DIVISION_MC).scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
    }

}
