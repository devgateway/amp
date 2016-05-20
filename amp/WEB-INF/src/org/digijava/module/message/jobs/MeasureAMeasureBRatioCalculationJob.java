package org.digijava.module.message.jobs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.scorecard.model.Quarter;
import org.digijava.kernel.ampapi.exception.AmpApiException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.TeamUtil;
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
	private DateTime previousFireTime;

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (TLSUtils.getRequest() == null) {
			TLSUtils.populateMockTlsUtils();
		}
		if (context.getTrigger().getPreviousFireTime() != null) {
			previousFireTime = new DateTime(context.getTrigger().getPreviousFireTime());
		}
		Long ampTeamId = FeaturesUtil
				.getGlobalSettingValueLong(GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FUNDING_GAP_NOTIFICATION);
		final ValueWrapper<Long> ampTeamMemberId = new ValueWrapper<Long>(null);
		// default percentage is 1
		String measureA = MeasureConstants.ACTUAL_DISBURSEMENTS;
		String measureB = MeasureConstants.PLANNED_DISBURSEMENTS;
		// we set the team to run the report

		// we need to fetch one team member of the configured team
		final String query = "select min(tm.amp_team_mem_id) from amp_team_member tm ,amp_team  t "
				+ " where tm.amp_member_role_id in(1,3) " + " and tm.amp_team_id=t.amp_team_id "
				+ " and t.amp_team_id= " + ampTeamId + " group by tm.amp_team_id";

		PersistenceManager.getSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				RsInfo ampTeamMemberIdQry = SQLUtils.rawRunQuery(conn, query, null);
				while (ampTeamMemberIdQry.rs.next()) {
					ampTeamMemberId.value = ampTeamMemberIdQry.rs.getLong(1);
				}
				ampTeamMemberIdQry.close();
			}

		});

		if (ampTeamMemberId.value != null) {
			TeamUtil.setupFiltersForLoggedInUser(TLSUtils.getRequest(), TeamUtil.getAmpTeamMember(ampTeamMemberId.value));
			// we first set the current member since its needed by features util

			Date lowerDateReport = null;
			Date upperDateReport = null;
			// we first need to check if we are 25 days after the last quarter
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
					MondrianReportFilters filterRules = new MondrianReportFilters();

					try {
						filterRules.addDateRangeFilterRule(lowerDateReport, upperDateReport);
						logger.debug(this.getClass() + " start date " + lowerDateReport);
						logger.debug(this.getClass() + " end date " + upperDateReport);
					} catch (AmpApiException e1) {
						
						logger.error(e1);
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
									&& (100 - dblMeasureA.multiply(HUNDRED).divide(dblMeasureB, 6, RoundingMode.HALF_EVEN)
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
				logger.error(this.getClass() + " Shouldnt run since its not 25 days after the last quarter ended ");
			}
		} else {
			logger.error(this.getClass() + " could not run because the team is not correctly configured for setting "
					+ GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FUNDING_GAP_NOTIFICATION);
		}
	}

	private Quarter checkIfShouldRunReport() {
		Long gsCalendarId = FeaturesUtil.getGlobalSettingValueLong(GlobalSettingsConstants.DEFAULT_CALENDAR);
		AmpFiscalCalendar fiscalCalendar = FiscalCalendarUtil.getAmpFiscalCalendar(gsCalendarId);

		Quarter currentQuarter = new Quarter(fiscalCalendar, new Date());

		DateTime lastQuarterStartDayPlus25 = new DateTime(currentQuarter.getPreviousQuarter().getQuarterEndDate());
		lastQuarterStartDayPlus25 = lastQuarterStartDayPlus25.plusDays(25);

		// we at least have
		if (previousFireTime == null || previousFireTime.isBefore(lastQuarterStartDayPlus25)) {
			// if the last fire date is before 25 days after the quarter has
			// ended
			return currentQuarter.getPreviousQuarter();
		} else {

			return null;
		}
	}
}
