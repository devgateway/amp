package org.digijava.module.message.jobs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.AmpReportFilters;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.endpoints.security.Security;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.startup.BuildVersionVerifier;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.message.triggers.ActivityMeassureComparisonTrigger;
import org.hibernate.jdbc.Work;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class MeasureAMeasureBRatioCalculationJob extends ConnectionCleaningJob implements StatefulJob {

    protected static Logger logger = Logger.getLogger(MeasureAMeasureBRatioCalculationJob.class);
    private static Double DEFAULT_PERCENTAGE = 1D;
    private static BigDecimal HUNDRED = new BigDecimal(100);
    private static Integer DAYS_AFTER_QUARTER = 25;
    private static final Integer DEFAULT_SCALE = 6;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        AmpJobsUtil.populateRequest();
        Long ampTeamId = FeaturesUtil
                .getGlobalSettingValueLong(GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FROM_JOB);
        // default percentage is 1
        String measureA = MeasureConstants.ACTUAL_DISBURSEMENTS;
        String measureB = MeasureConstants.PLANNED_DISBURSEMENTS;

        if (AmpJobsUtil.setTeamForNonRequestReport(ampTeamId)) {
            Date lowerDateReport = null;
            Date upperDateReport = null;
            // we first need to check if we are DAYS_AFTER_QUARTER days after the last quarter
            // ended
            Quarter previousQuarter = checkIfShouldRunReport();
            if (previousQuarter != null) {
                lowerDateReport = previousQuarter.getQuarterStartDate();
                upperDateReport = previousQuarter.getQuarterEndDate();

                // if measures are configured we used those (if still active) if
                // not
                // default ones
                String configuredMeasureA = FeaturesUtil
                        .getGlobalSettingValue(GlobalSettingsConstants.MEASURE_A_FOR_FUNDING_GAP_NOTIFICATION);
                String configuredMeasureB = FeaturesUtil
                        .getGlobalSettingValue(GlobalSettingsConstants.MEASURE_B_FOR_FUNDING_GAP_NOTIFICATION);
                if (configuredMeasureA != null) {

                    // the measure is configured, we check if still visible
                    if (MeasuresVisibility.getVisibleMeasures().contains(configuredMeasureA)) {
                        measureA = configuredMeasureA;
                    } else {
                        // in this case the measure is configured but is not
                        // longer
                        // visible
                        measureA = null;
                    }
                }
                if (configuredMeasureB != null) {
                    // the measure is configured, we check if still visible
                    if (MeasuresVisibility.getVisibleMeasures().contains(configuredMeasureB)) {
                        measureB = configuredMeasureB;
                    } else {
                        // in this case the measure is configured but is not
                        // longer
                        // visible
                        measureB = null;
                    }
                }
                if (measureA != null && measureB != null) {

                    String name = "MeasureAMeasureBRatioCalculationJob";
                    ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
                    List<AmpActivityVersion> activitiesToNofity = new ArrayList<AmpActivityVersion>();

                    BigDecimal percentage = BigDecimal.valueOf(FeaturesUtil
                            .getGlobalSettingDouble(GlobalSettingsConstants.FUNDING_GAP_NOTIFICATION_THRESHOLD) != null
                            ? FeaturesUtil.getGlobalSettingDouble(
                            GlobalSettingsConstants.FUNDING_GAP_NOTIFICATION_THRESHOLD)
                            : DEFAULT_PERCENTAGE);

                    spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
                    spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
                    spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
                    spec.addMeasure(new ReportMeasure(measureA));
                    spec.addMeasure(new ReportMeasure(measureB));
                    AmpReportFilters filterRules = new AmpReportFilters();

                    try {
                        filterRules.addDateRangeFilterRule(lowerDateReport, upperDateReport);
                        logger.debug(this.getClass() + " start date " + lowerDateReport);
                        logger.debug(this.getClass() + " end date " + upperDateReport);
                    } catch (AmpApiException e1) {

                        logger.error(e1.getMessage(), e1);
                    }
                    spec.setFilters(filterRules);

                    try {
                        GeneratedReport report = EndpointUtils.runReport(spec, ReportAreaImpl.class, null);
                        List<ReportArea> ll = report.reportContents.getChildren();

                        for (ReportArea reportArea : ll) {
                            AmpActivityVersion activityToNotify = new AmpActivityVersion();
                            BigDecimal dblMeasureA = new BigDecimal(0);
                            BigDecimal dblMeasureB = new BigDecimal(0);
                            Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
                            Set<ReportOutputColumn> col = row.keySet();

                            for (ReportOutputColumn reportOutputColumn : col) {

                                if (reportOutputColumn.originalColumnName.equals(measureA)) {
                                    dblMeasureA = dblMeasureA.add((BigDecimal) row.get(reportOutputColumn).value);
                                } else {
                                    if (reportOutputColumn.originalColumnName.equals(measureB)) {
                                        dblMeasureB = dblMeasureB.add((BigDecimal) row.get(reportOutputColumn).value);
                                    } else {
                                        if (reportOutputColumn.originalColumnName.equals(ColumnConstants.ACTIVITY_ID)) {
                                            activityToNotify.setAmpActivityId(
                                                    new Long(row.get(reportOutputColumn).value.toString()));
                                        } else {
                                            if (reportOutputColumn.originalColumnName
                                                    .equals(ColumnConstants.PROJECT_TITLE)) {
                                                activityToNotify.setName((String) row.get(reportOutputColumn).value);
                                            } else {
                                                if (reportOutputColumn.originalColumnName
                                                        .equals(ColumnConstants.AMP_ID)) {
                                                    activityToNotify
                                                            .setAmpId((String) row.get(reportOutputColumn).value);
                                                }
                                            }

                                        }
                                    }

                                }
                            }
                            if (dblMeasureA.compareTo(BigDecimal.ZERO) != 0
                                    && dblMeasureB.compareTo(BigDecimal.ZERO) != 0
                                    && (HUNDRED.subtract(dblMeasureA.multiply(HUNDRED))
                                    .divide(dblMeasureB, DEFAULT_SCALE, RoundingMode.HALF_EVEN)
                                    .compareTo(percentage)) >= 0) {

                                activitiesToNofity.add(activityToNotify);
                            } else {
                                if (dblMeasureA.compareTo(BigDecimal.ZERO) == 0
                                        && dblMeasureB.compareTo(BigDecimal.ZERO) != 0) {
                                    activitiesToNofity.add(activityToNotify);
                                }
                            }
                        }
                        if (activitiesToNofity.size() > 0) {
                            for (AmpActivityVersion activityToNofify : activitiesToNofity) {
                                logger.debug("AMP ID:" + activityToNofify.getAmpId());
                                new ActivityMeassureComparisonTrigger(activityToNofify);
                            }
                        }
                        TLSUtils.getThreadLocalInstance().request = null;
                    } catch (Exception e) {
                        logger.error("Cannot execute JOB", e);
                    }
                    // after the job was run we store an event in global event
                    // log

                    final String eventName = MeasureAMeasureBRatioCalculationJob.class.getName();
                    final String quarter = previousQuarter.toString();
                    PersistenceManager.getSession().doWork(new Work() {
                        @Override
                        public void execute(Connection conn) throws SQLException {

                            SQLUtils.executeQuery(conn,
                                    String.format(
                                            "insert into amp_global_event_log "
                                                    + " select  nextval('amp_global_event_log_id_seq') ,now(),'%s','%s',amp_version,amp_version_encoded  "
                                                    + " from amp_global_event_log where id =(select max(id) from amp_global_event_log where event_name='AMP startup') ",
                                            eventName, quarter));
                        }
                    });

                } else {
                    // in this case the measures were configured but not visible
                    // any
                    // more
                    logger.error(this.getClass()
                            + " could not run because one (or both)of the following measures were configured and not visible anymmore  "
                            + GlobalSettingsConstants.MEASURE_A_FOR_FUNDING_GAP_NOTIFICATION + " or "
                            + GlobalSettingsConstants.MEASURE_B_FOR_FUNDING_GAP_NOTIFICATION);
                }
            } else {
                // should run report
                logger.error(this.getClass()
                        + " Should not run since its not DAYS_AFTER_QUARTER days after the last quarter ended or it has already run for this quarter");
            }
        } else {
            logger.error(this.getClass() + " could not run because the team is not correctly configured for setting "
                    + GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FROM_JOB);
        }
    }

    private Quarter checkIfShouldRunReport() {
        Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
        AmpFiscalCalendar fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);
        DateTime today = new DateTime();

        Quarter currentQuarter = new Quarter(fiscalCalendar, new Date());

        DateTime lastQuarterStartDayPlusDAYS_AFTER_QUARTER = new DateTime(
                currentQuarter.getPreviousQuarter().getQuarterEndDate());
        lastQuarterStartDayPlusDAYS_AFTER_QUARTER = lastQuarterStartDayPlusDAYS_AFTER_QUARTER
                .plusDays(DAYS_AFTER_QUARTER);

        // we check if we are DAYS_AFTER_QUARTER days after the quarter has
        // ended and that it has not run for that quarter
        final ValueWrapper<Boolean> shouldRunJob = new ValueWrapper<Boolean>(true);
        try {
            final String previousQuarterName = currentQuarter.getPreviousQuarter().toString();
            final String eventName = MeasureAMeasureBRatioCalculationJob.class.getName();
            PersistenceManager.getSession().doWork(new Work() {
                public void execute(Connection conn) throws SQLException {
                    String query = String.format(
                            "select * from amp_global_event_log where event_name ='%s' and message='%s'", eventName,
                            previousQuarterName);
                    RsInfo shouldRunJobRS = SQLUtils.rawRunQuery(conn, query, null);
                    while (shouldRunJobRS.rs.next()) {
                        shouldRunJob.value = false;
                    }
                    shouldRunJobRS.close();
                }

            });
        } catch (Exception e) {
            logger.error("could get last job run for this quarter", e);
        }

        if (shouldRunJob.value && today.isAfter((lastQuarterStartDayPlusDAYS_AFTER_QUARTER))) {

            return currentQuarter.getPreviousQuarter();
        } else {

            return null;
        }
    }
}
