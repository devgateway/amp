package org.digijava.module.message.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.triggers.ActivityMeassureComparisonTrigger;
import org.hibernate.jdbc.Work;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class MeasureAMeasureBRatioCalculationJob extends ConnectionCleaningJob implements StatefulJob {

	protected static Logger logger = Logger.getLogger(MeasureAMeasureBRatioCalculationJob.class);
	private static Double DEFAULT_PERCENTAGE = 1D;

	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (TLSUtils.getRequest() == null) {
			TLSUtils.populateMockTlsUtils();
		}

		Long ampTeamId = FeaturesUtil
				.getGlobalSettingValueLong(GlobalSettingsConstants.TEAM_TO_RUN_REPORT_FORACTIVITY_NOTIFICATION);
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
			// we first set the current member since its needed by features util
			TLSUtils.getRequest().getSession().setAttribute(Constants.CURRENT_MEMBER,
					new TeamMember(TeamUtil.getAmpTeamMember(ampTeamMemberId.value)));
			// if measures are configured we used those (if still active) if not
			// default ones
			String configuredMeasureA = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.MEASURE_A_FOR_THRESHOLD);
			String configuredMeasureB = FeaturesUtil
					.getGlobalSettingValue(GlobalSettingsConstants.MEASURE_B_FOR_THRESHOLD);
			if (configuredMeasureA != null) {

				// the measure is configured, we check if still visible
				if (MeasuresVisibility.getVisibleMeasures().contains(configuredMeasureA)) {
					measureA = configuredMeasureA;
				} else {
					// in this case the measure is configured but is not longer
					// visible
					measureA = null;
				}
			}
			if (configuredMeasureB != null) {
				// the measure is configured, we check if still visible
				if (MeasuresVisibility.getVisibleMeasures().contains(configuredMeasureB)) {
					measureB = configuredMeasureB;
				} else {
					// in this case the measure is configured but is not longer
					// visible
					measureB = null;
				}
			}
			if (measureA != null && measureB != null) {

				String name = "MeasureAMeasureBRatioCalculationJob";
				ReportSpecificationImpl spec = new ReportSpecificationImpl(name, ArConstants.DONOR_TYPE);
				GeneratedReport report = null;
				List<AmpActivityVersion> activitiesToNofity = new ArrayList<AmpActivityVersion>();
				
				Double percentage = FeaturesUtil
						.getGlobalSettingDouble(GlobalSettingsConstants.ACTIVITY_NOTIFICATION_THRESHOLD) != null
								? FeaturesUtil.getGlobalSettingDouble(
										GlobalSettingsConstants.ACTIVITY_NOTIFICATION_THRESHOLD)
								: DEFAULT_PERCENTAGE;

				spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_ID));
				spec.addColumn(new ReportColumn(ColumnConstants.PROJECT_TITLE));
				spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
				spec.addMeasure(new ReportMeasure(measureA));
				spec.addMeasure(new ReportMeasure(measureB));
				ReportExecutor generator = new MondrianReportGenerator(ReportAreaImpl.class,
						ReportEnvironment.buildFor(TLSUtils.getRequest()), false);

				try {
					report = generator.executeReport(spec);
					List<ReportArea> ll = report.reportContents.getChildren();
					for (ReportArea reportArea : ll) {
						AmpActivityVersion activityToNotify = new AmpActivityVersion();
						Double dblMeasureA = 0D;
						Double dblMeasureB = 0D;
						Map<ReportOutputColumn, ReportCell> row = reportArea.getContents();
						Set<ReportOutputColumn> col = row.keySet();
						for (ReportOutputColumn reportOutputColumn : col) {
							if (reportOutputColumn.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
								dblMeasureA = (Double) row.get(reportOutputColumn).value;
							} else {
								if (reportOutputColumn.originalColumnName
										.equals(MeasureConstants.PLANNED_DISBURSEMENTS)) {
									dblMeasureB = (Double) row.get(reportOutputColumn).value;
								} else {
									if (reportOutputColumn.originalColumnName.equals(ColumnConstants.ACTIVITY_ID)) {
										activityToNotify.setAmpActivityId(
												new Long(row.get(reportOutputColumn).value.toString()));
									} else {
										if (reportOutputColumn.originalColumnName
												.equals(ColumnConstants.PROJECT_TITLE)) {
											activityToNotify.setName((String) row.get(reportOutputColumn).value);
										} else {
											if (reportOutputColumn.originalColumnName.equals(ColumnConstants.AMP_ID)) {
												activityToNotify.setAmpId((String) row.get(reportOutputColumn).value);
											}
										}

									}
								}

							}
							if (!dblMeasureA.equals(0D) && !dblMeasureB.equals(0D)
									&& ((dblMeasureA / dblMeasureB) * 100) > percentage) {
								activitiesToNofity.add(activityToNotify);
							} else {
								if ((dblMeasureA.equals(0D) ^ dblMeasureB.equals(0D)) && dblMeasureA.equals(0D)) {
									activitiesToNofity.add(activityToNotify);
								}
							}

						}
					}
					if (activitiesToNofity.size() > 0) {
						for (AmpActivityVersion activityToNofify : activitiesToNofity) {
							new ActivityMeassureComparisonTrigger(activityToNofify);
						}
					}
					TLSUtils.getThreadLocalInstance().request = null;
				} catch (AMPException e) {
					logger.error("Cannot execute JOB", e);
				}
			} else {
				// in this case the measures were configured but not visible any
				// more
				logger.error(this.getClass()
						+ " could not run because one (or both)of the following measures were configured and not visible anymmore  "
						+ GlobalSettingsConstants.MEASURE_A_FOR_THRESHOLD + " or "
						+ GlobalSettingsConstants.MEASURE_B_FOR_THRESHOLD);
			}
		} else {
			logger.error(this.getClass() + " could not run because the team is not correctly configured for setting "
					+ GlobalSettingsConstants.TEAM_TO_RUN_REPORT_FORACTIVITY_NOTIFICATION);
		}
	}
}
