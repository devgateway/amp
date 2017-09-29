package org.digijava.module.aim.helper;

import org.apache.commons.lang.time.DateUtils;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.FilterParam;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.ActivityUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.jdbc.Work;

/**
 * @author Aldo Picca
 */
public final class SummaryChangesService {

    private static final Logger LOGGER = Logger.getLogger(SummaryChangesService.class);
    public static final String NEW = "New";
    public static final String EDITED = "Edited";
    public static final String DELETED = "Deleted";
    public static final String ALL_DIFF_DETAIL = "allDiffDetail";
    public static final String AMP_CURRENCY_ID = "amp_currency_id";
    public static final String ADJUSTMENT_TYPE = "adjustment_type";
    public static final String AMP_ACTIVITY_ID = "amp_activity_id";
    public static final String TRANSACTION_TYPE = "transaction_type";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String REPORTING_DATE = "reporting_date";
    public static final String CURRENT_VERSION = "current_version";

    private static VivificatingMap<Long, AmpCurrency> currencyCache = new VivificatingMap<Long, AmpCurrency>(new
            HashMap<>(), CurrencyUtil::getAmpcurrency);
    private static VivificatingMap<Long, AmpCategoryValue> categoryValueCache = new VivificatingMap<Long,
            AmpCategoryValue>(new HashMap<>(), CategoryManagerUtil::getAmpCategoryValueFromDb);

    private SummaryChangesService() {
    }

    /**
     * Return a list of approvers whit the activities that changed.
     *
     * @param activities activities list.
     * @return list of approvers and activities.
     */
    public static Map<String, Collection<AmpActivityVersion>> getValidators(LinkedHashMap<Long, Long> activities) {

        Map<String, Collection<AmpActivityVersion>> results = new LinkedHashMap<>();

        for (Long activityId : new HashSet<Long>(activities.values())) {
            AmpActivityVersion currentActivity = ActivityUtil.loadAmpActivity(activityId);

            List<AmpTeamMember> teamHeadAndAndApprovers = TeamMemberUtil.getTeamHeadAndApprovers(currentActivity
                    .getTeam().getAmpTeamId());

            for (AmpTeamMember approver : teamHeadAndAndApprovers) {
                String key = approver.getUser().getEmail();
                if (results.get(key) == null) {
                    results.put(key, new ArrayList<AmpActivityVersion>());
                }
                results.get(key).add(currentActivity);
            }
        }
        return results;
    }

    /**
     * Return a list of activities that were modified in the 24 hours prior to the date.
     * @param fromDate filter by date
     * @return list of activities.
     */
    public static LinkedHashMap<Long, Long> getActivities(Date fromDate) {
        List<AmpActivityVersion> activities = new ArrayList<AmpActivityVersion>();
        StringBuffer ids = new StringBuffer();
        LinkedHashMap<Long, Long> activitiesIds = new LinkedHashMap<>();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String queryString = String.format(
                        "SELECT ampAct.amp_activity_id current_version, lv.amp_activity_id amp_activity_id "
                                + "FROM amp_activity_version ampAct "
                                + "INNER JOIN v_activity_latest_and_validated_grouped lv ON lv.amp_activity_group_id "
                                + "= ampAct.amp_activity_group_id "
                                + "WHERE ampAct.amp_activity_id IN "
                                + "    (SELECT act.amp_activity_id "
                                + "     FROM amp_activity act "
                                + "     WHERE draft = FALSE "
                                + "       AND NOT amp_team_id IS NULL "
                                + "       AND date_updated >= ? "
                                + "       AND approval_status IN ( %s ) "
                                + "       AND EXISTS "
                                + "         (SELECT actappr.amp_activity_id "
                                + "          FROM amp_activity_version actappr "
                                + "          WHERE approval_status IN ( '%s', '%s' ) "
                                + "            AND act.amp_activity_group_id = actappr.amp_activity_group_id ) ) "
                                + "  AND ampAct.amp_team_id IN "
                                + "    (SELECT amp_team_id "
                                + "     FROM amp_team "
                                + "     WHERE isolated = FALSE "
                                + "       OR isolated IS NULL ) ",
                        Constants.ACTIVITY_NEEDS_APPROVAL_STATUS,
                        Constants.APPROVED_STATUS, Constants.STARTED_APPROVED_STATUS);

                ArrayList<FilterParam> params = new ArrayList<FilterParam>();
                params.add(new FilterParam(DateUtils.addDays(fromDate, -1), Types.TIMESTAMP));

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, queryString, params)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        activitiesIds.put(rs.getLong(AMP_ACTIVITY_ID), rs.getLong(CURRENT_VERSION));
                    }
                }
            }
        });

        return activitiesIds;

    }

    /**
     * Return a list of changes for the activity.
     *
     * @param activitiesIds list of activities ids of current version and previuos version.
     * @return list of changes for this activity.
     */
    public static LinkedHashMap<Long, Collection<SummaryChange>> processActivity(LinkedHashMap<Long, Long>
                                                                                         activitiesIds) {

        LinkedHashMap<Long, Collection<SummaryChange>> activitiesChanges = new LinkedHashMap<>();
        Map<String, SummaryChange> editedFunding = new LinkedHashMap<String, SummaryChange>();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String fundingQuery = " (SELECT amp_funding_id FROM amp_funding WHERE amp_activity_id IN ("
                        + Util.toCSString(activitiesIds.keySet().stream().collect(Collectors.toList())) + "))";

                String fundingDetailQuery = "a.reporting_date, a.amp_funding_id, fd.amp_activity_id, transaction_date, "
                        + " transaction_amount, amp_currency_id, adjustment_type, transaction_type "
                        + " FROM amp_funding_detail a "
                        + " INNER JOIN amp_funding fd ON a.amp_funding_id = fd.amp_funding_id "
                        + " WHERE a.amp_funding_id IN " + fundingQuery;

                String fundingMtefQuery = "a.reporting_date, a.amp_funding_id, fd.amp_activity_id, projection_date, "
                        + " amount, amp_currency_id, amp_projected_categoryvalue_id, " + Constants.MTEFPROJECTION
                        + " FROM amp_funding_mtef_projection a "
                        + " INNER JOIN amp_funding fd ON a.amp_funding_id = fd.amp_funding_id "
                        + " WHERE a.amp_funding_id IN " + fundingQuery;

                String queryString = "SELECT true AS allDiffDetail, " + fundingDetailQuery
                        + "  AND NOT EXISTS "
                        + "    (SELECT b.amp_fund_detail_id "
                        + "     FROM amp_funding_detail b "
                        + "     WHERE b.amp_funding_id IN " + fundingQuery + " AND "
                        + "       ( a.reporting_date = b.reporting_date "
                        + "       AND a.amp_fund_detail_id <> b.amp_fund_detail_id ) or "
                        + "( a.reporting_date is null AND b.reporting_date is null "
                        + "       AND a.amp_fund_detail_id <> b.amp_fund_detail_id  "
                        + "       AND (a.adjustment_type = b.adjustment_type  "
                        + "       AND a.transaction_date = b.transaction_date  "
                        + "       AND a.transaction_amount = b.transaction_amount  "
                        + "       AND a.amp_currency_id = b.amp_currency_id) ) "
                        + "  ) "
                        + "UNION "
                        + " SELECT false AS allDiffDetail, " + fundingDetailQuery
                        + "  AND EXISTS "
                        + "    (SELECT b.amp_fund_detail_id "
                        + "     FROM amp_funding_detail b "
                        + "     WHERE b.amp_funding_id IN " + fundingQuery + " AND "
                        + "       a.reporting_date = b.reporting_date "
                        + "       AND a.amp_fund_detail_id <> b.amp_fund_detail_id "
                        + "       AND (a.adjustment_type <> b.adjustment_type "
                        + "            OR a.transaction_date <> b.transaction_date "
                        + "            OR a.transaction_amount <> b.transaction_amount "
                        + "            OR a.amp_currency_id <> b.amp_currency_id)) "
                        + "UNION "
                        + " SELECT true AS allDiffDetail, " + fundingMtefQuery
                        + "  AND NOT EXISTS "
                        + "    (SELECT b.amp_funding_id "
                        + "     FROM amp_funding_mtef_projection b "
                        + "     WHERE b.amp_funding_id IN " + fundingQuery + " AND "
                        + "       ( a.reporting_date = b.reporting_date "
                        + "       AND a.amp_fund_mtef_projection_id <> b.amp_fund_mtef_projection_id ) or "
                        + "         ( a.reporting_date is null AND b.reporting_date is null "
                        + "       AND a.amp_fund_mtef_projection_id <> b.amp_fund_mtef_projection_id "
                        + "       AND a.projection_date = b.projection_date "
                        + "       AND a.amount = b.amount "
                        + "       AND a.amp_currency_id = b.amp_currency_id "
                        + "       AND a.amp_projected_categoryvalue_id = b.amp_projected_categoryvalue_id "
                        + ") ) "
                        + "UNION "
                        + " SELECT false AS allDiffDetail, " + fundingMtefQuery
                        + "  AND EXISTS "
                        + "    (SELECT b.amp_funding_id "
                        + "     FROM amp_funding_mtef_projection b "
                        + "     WHERE b.amp_funding_id IN " + fundingQuery + " AND "
                        + "       a.reporting_date = b.reporting_date "
                        + "       AND a.amp_fund_mtef_projection_id <> b.amp_fund_mtef_projection_id "
                        + "       AND (a.projection_date <> b.projection_date "
                        + "            OR a.amount <> b.amount "
                        + "            OR a.amp_currency_id <> b.amp_currency_id "
                        + "            OR a.amp_projected_categoryvalue_id <> b.amp_projected_categoryvalue_id) ) "
                        + " ORDER BY amp_activity_id,reporting_date,amp_funding_id";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, queryString, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        AmpCurrency currency = currencyCache.getOrCreate(rs.getLong(AMP_CURRENCY_ID));
                        AmpCategoryValue categoryValue = categoryValueCache.getOrCreate(rs.getLong(ADJUSTMENT_TYPE));

                        if (rs.getBoolean(ALL_DIFF_DETAIL)) {
                            SummaryChange summaryChange;
                            if (activitiesIds.get(rs.getLong(AMP_ACTIVITY_ID)) == rs.getLong(AMP_ACTIVITY_ID)) {
                                summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE), categoryValue, NEW);
                                summaryChange.setCurrentValue(rs.getDouble(TRANSACTION_AMOUNT));
                                summaryChange.setCurrentCurrency(currency);
                                summaryChange.setTransactionDate(rs.getDate(TRANSACTION_DATE));
                                addChange(rs.getLong(AMP_ACTIVITY_ID), summaryChange, activitiesChanges);
                            } else {
                                summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE), categoryValue, DELETED);
                                summaryChange.setPreviousValue(rs.getDouble(TRANSACTION_AMOUNT));
                                summaryChange.setPreviousCurrency(currency);
                                summaryChange.setTransactionDate(rs.getDate(TRANSACTION_DATE));
                                addChange(activitiesIds.get(rs.getLong(AMP_ACTIVITY_ID)), summaryChange,
                                        activitiesChanges);
                            }

                        } else {
                            if (editedFunding.get(rs.getString(REPORTING_DATE)) == null) {
                                SummaryChange summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, EDITED);
                                summaryChange.setPreviousValue(rs.getDouble(TRANSACTION_AMOUNT));
                                summaryChange.setCurrentValue(rs.getDouble(TRANSACTION_AMOUNT));
                                summaryChange.setPreviousCurrency(currency);
                                summaryChange.setCurrentCurrency(currency);
                                summaryChange.setTransactionDate(rs.getDate(TRANSACTION_DATE));
                                editedFunding.put(rs.getString(REPORTING_DATE), summaryChange);
                            } else {
                                SummaryChange originalSummaryChange = editedFunding.get(rs.getString(REPORTING_DATE));
                                originalSummaryChange.setCurrentValue(rs.getDouble(TRANSACTION_AMOUNT));
                                originalSummaryChange.setCurrentCurrency(currency);

                                SummaryChange newSummaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, EDITED);
                                newSummaryChange.setPreviousValue(originalSummaryChange.getPreviousValue());
                                newSummaryChange.setPreviousCurrency(originalSummaryChange.getPreviousCurrency());
                                newSummaryChange.setCurrentValue(rs.getDouble(TRANSACTION_AMOUNT));
                                newSummaryChange.setCurrentCurrency(currency);
                                newSummaryChange.setTransactionDate(rs.getDate(TRANSACTION_DATE));

                                boolean addOriginalSummary = false;
                                if (!originalSummaryChange.getTransactionDate().equals(newSummaryChange
                                        .getTransactionDate()) || !originalSummaryChange.getAdjustmentType().equals(
                                        newSummaryChange.getAdjustmentType())) {
                                    originalSummaryChange.setChangeType(DELETED);
                                    newSummaryChange.setChangeType(NEW);
                                    addOriginalSummary = true;
                                }
                                if (addOriginalSummary) {
                                    addChange(rs.getLong(AMP_ACTIVITY_ID), originalSummaryChange, activitiesChanges);
                                }
                                addChange(rs.getLong(AMP_ACTIVITY_ID), newSummaryChange, activitiesChanges);
                            }
                        }

                    }
                }
            }
        });

        return activitiesChanges;
    }

    private static void addChange(Long id, SummaryChange newSummaryChange, LinkedHashMap<Long,
            Collection<SummaryChange>> activitiesChanges) throws SQLException {
        if (activitiesChanges.get(id) == null) {
            activitiesChanges.put(id, new LinkedHashSet<SummaryChange>());
        }
        activitiesChanges.get(id).add(newSummaryChange);
    }

}
