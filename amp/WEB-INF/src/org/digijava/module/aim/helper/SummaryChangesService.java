package org.digijava.module.aim.helper;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.ActivityUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * @author Aldo Picca
 */
public class SummaryChangesService {

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

    /**
     * Return a list of activities that were modified in the 24 hours prior to the date.
     *
     * @param fromDate filter by date
     * @return list of activities.
     */
    public static List<AmpActivityVersion> getActivitiesChanged(Date fromDate) {
        return ActivityUtil.getActivitiesChanged(fromDate);
    }

    public static String buildActivitiesChanged(Date fromDate) {
        StringBuffer results = new StringBuffer();

        LinkedHashMap<String, Object> activityList = SummaryChangesService.getSummaryChanges(SummaryChangesService
                .getActivitiesChanged(fromDate));

        for (String activity : activityList.keySet()) {
            Session session = PersistenceManager.getRequestDBSession();
            AmpActivityVersion activityVersion = (AmpActivityVersion) session.load(AmpActivityVersion.class,
                    Long.parseLong(activity));

            LinkedHashMap<String, Object> changesList = (LinkedHashMap) activityList.get(activity);
            SummaryChangeHtmlRenderer renderer = new SummaryChangeHtmlRenderer(activityVersion, changesList, null);
            LOGGER.info(renderer.render());

        }
        return results.toString();
    }

    /**
     * Return a list of approvers whit the activities that changed.
     *
     * @param activities activities list.
     * @return list of approvers and activities.
     */
    public static Map<String, Collection<AmpActivityVersion>> getValidators(List<AmpActivityVersion> activities) {

        Map<String, Collection<AmpActivityVersion>> results = new LinkedHashMap<>();

        for (AmpActivityVersion currentActivity : activities) {
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
     * Return a list of activities whit every funding change.
     *
     * @param activities activities list.
     * @return list of activities and changes.
     */
    public static LinkedHashMap<String, Object> getSummaryChanges(List<AmpActivityVersion> activities) {

        LinkedHashMap<String, Object> activitiesChanges = new LinkedHashMap<>();
        for (AmpActivityVersion currentActivity : activities) {
            activitiesChanges.putAll(processActivity(currentActivity));
        }
        return activitiesChanges;
    }

    /**
     * Return a list of changes for the activity.
     *
     * @param currentActivity The newest version of the activity.
     * @return list of changes for this activity.
     */
    public static LinkedHashMap<String, Object> processActivity(AmpActivityVersion currentActivity) {

        AmpActivityVersion previousActivity = ActivityUtil.getPreviousVersion(currentActivity);

        LinkedHashMap<String, Object> activitiesChanges = new LinkedHashMap<>();
        Map<String, Collection<SummaryChange>> differences = new LinkedHashMap<String, Collection<SummaryChange>>();
        Map<String, SummaryChange> editedFunding = new LinkedHashMap<String, SummaryChange>();

        String ids = currentActivity.getAmpActivityId() + ", " + previousActivity.getAmpActivityId();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String fundingQuery = " (SELECT amp_funding_id FROM amp_funding WHERE amp_activity_id IN (" + ids +
                        "))";

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
                        + "     WHERE ( a.reporting_date = b.reporting_date "
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
                        + "     WHERE a.reporting_date = b.reporting_date "
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
                        + "     WHERE ( a.reporting_date = b.reporting_date "
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
                        + "     WHERE a.reporting_date = b.reporting_date "
                        + "       AND a.amp_fund_mtef_projection_id <> b.amp_fund_mtef_projection_id "
                        + "       AND (a.projection_date <> b.projection_date "
                        + "            OR a.amount <> b.amount "
                        + "            OR a.amp_currency_id <> b.amp_currency_id "
                        + "            OR a.amp_projected_categoryvalue_id <> b.amp_projected_categoryvalue_id) ) "
                        + " ORDER BY transaction_date,reporting_date,amp_funding_id";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, queryString, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        AmpCurrency currency = CurrencyUtil.getAmpcurrency(rs.getLong(AMP_CURRENCY_ID));
                        AmpCategoryValue categoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(
                                rs.getLong(ADJUSTMENT_TYPE));

                        if (rs.getBoolean(ALL_DIFF_DETAIL)) {
                            SummaryChange summaryChange;
                            if (rs.getLong(AMP_ACTIVITY_ID) == currentActivity.getAmpActivityId()) {
                                summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, NEW, null, null,
                                        rs.getDouble(TRANSACTION_AMOUNT), currency,
                                        rs.getDate(TRANSACTION_DATE));
                            } else {
                                summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, DELETED, rs.getDouble(TRANSACTION_AMOUNT),
                                        currency, null, null, rs.getDate(TRANSACTION_DATE));
                            }
                            addChange(differences, summaryChange);
                        } else {
                            if (editedFunding.get(rs.getString(REPORTING_DATE)) == null) {
                                SummaryChange summaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, EDITED, rs.getDouble(TRANSACTION_AMOUNT), currency,
                                        rs.getDouble(TRANSACTION_AMOUNT), currency, rs.getDate(TRANSACTION_DATE));
                                editedFunding.put(rs.getString(REPORTING_DATE), summaryChange);
                            } else {
                                SummaryChange originalSummaryChange = editedFunding.get(rs.getString(REPORTING_DATE));

                                originalSummaryChange.setCurrentValue(rs.getDouble(TRANSACTION_AMOUNT));
                                originalSummaryChange.setCurrentCurrency(currency);

                                SummaryChange newSummaryChange = new SummaryChange(rs.getInt(TRANSACTION_TYPE),
                                        categoryValue, EDITED, originalSummaryChange.getPreviousValue(),
                                        originalSummaryChange.getPreviousCurrency(),
                                        rs.getDouble(TRANSACTION_AMOUNT), currency, rs.getDate(TRANSACTION_DATE));

                                boolean addOriginalSummary = false;
                                if (!originalSummaryChange.getTransactionDate().equals(newSummaryChange
                                        .getTransactionDate()) || !originalSummaryChange.getAdjustmentType().equals(
                                        newSummaryChange.getAdjustmentType())) {
                                    originalSummaryChange.setChangeType(DELETED);
                                    newSummaryChange.setChangeType(NEW);
                                    addOriginalSummary = true;
                                }
                                if (addOriginalSummary) {
                                    addChange(differences, originalSummaryChange);
                                }
                                addChange(differences, newSummaryChange);
                            }
                        }
                    }
                }
            }
        });

        activitiesChanges.put(currentActivity.getAmpActivityId().toString(), differences);

        return activitiesChanges;
    }

    private static void addChange(Map<String, Collection<SummaryChange>> objDiff, SummaryChange summaryChange) {
        String key = summaryChange.getQuarter().getQuarterNumber().toString();

        if (objDiff.get(key) == null) {
            objDiff.put(key, new ArrayList<SummaryChange>());
        }

        objDiff.get(key).add(summaryChange);
    }

}
