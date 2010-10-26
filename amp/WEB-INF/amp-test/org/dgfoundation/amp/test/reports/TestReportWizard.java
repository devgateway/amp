package org.dgfoundation.amp.test.reports;

import java.util.Collection;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.DeleteAllReports;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.action.reportwizard.ReportWizardAction;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.reportwizard.ReportWizardForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.TeamUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class TestReportWizard extends BasicActionTestCaseAdapter {

	private ReportWizardAction action;
	private ReportWizardForm form;

	private DeleteAllReports deleteAction;

	MockHttpSession session;
	MockHttpServletRequest request;

	public TestReportWizard(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		Configuration.initConfig();
		action = new ReportWizardAction();
		// register the action class
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(ViewNewAdvancedReport.class.getName(), action);
		context.setAttribute(DeleteAllReports.class.getName(), deleteAction);

		// create the form
		form = (ReportWizardForm) createActionForm(ReportWizardForm.class);

		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();

		setValidate(true);
		prepare();
	}

	public void prepare() throws Exception {
		TestUtil.setSiteDomain(request);
		TestUtil.setLocaleEn(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);

	}

	public void beginSaveTwoColumnsReport() throws Exception {
		form.setReportTitle("AUTO_TEST_REPORT");
		form.setReportType("donor");

		Collection<Long> selectedColumns = new Vector<Long>();
		Collection<Long> selectedMeasures = new Vector<Long>();

		Collection<AmpColumns> columns = AdvancedReportUtil.getColumnList();

		for (AmpColumns ampColumns : columns) {
			if (ampColumns.getColumnName().equalsIgnoreCase("Project Title")) {
				selectedColumns.add(ampColumns.getColumnId());
			} else if (ampColumns.getColumnName().equalsIgnoreCase("Primary Sector")) {
				selectedColumns.add(ampColumns.getColumnId());
			}
		}

		Collection<AmpMeasures> measures = AdvancedReportUtil.getMeasureList();
		for (AmpMeasures ampMeasure : measures) {
			selectedMeasures.add(ampMeasure.getMeasureId());
		}

		form.setSelectedColumns(selectedColumns.toArray(new Long[2]));
		form.setSelectedMeasures(selectedMeasures.toArray(new Long[selectedMeasures.size()]));
		form.setUseFilters(false);
		form.setReportDescription("This is a unit test generated report");
		addRequestParameter("reportTitle", form.getReportTitle());
	}

	public void testSaveTwoColumnsReport() throws Exception {
		try {
			beginSaveTwoColumnsReport();
			actionPerform(ReportWizardAction.class, form);
			verifyNoActionMessages();
			verifyNoActionMessages();

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	public void prepareSaveAllColumnsReport() throws Exception {
		form.setReportTitle("AUTO_TEST_REPORT_ALL_COLUMNS");
		form.setReportType("donor");

		Collection<Long> selectedColumns = new Vector<Long>();
		Collection<Long> selectedMeasures = new Vector<Long>();

		Collection<AmpColumns> columns = AdvancedReportUtil.getColumnList();

		for (AmpColumns ampColumns : columns) {
			selectedColumns.add(ampColumns.getColumnId());

		}

		Collection<AmpMeasures> measures = AdvancedReportUtil.getMeasureList();
		for (AmpMeasures ampMeasure : measures) {
			selectedMeasures.add(ampMeasure.getMeasureId());
		}

		form.setSelectedColumns(selectedColumns.toArray(new Long[2]));
		form.setSelectedMeasures(selectedMeasures.toArray(new Long[selectedMeasures.size()]));
		form.setUseFilters(false);
		form.setReportDescription("This is a unit test generated report");
		addRequestParameter("reportTitle", form.getReportTitle());
	}

	public void testSaveAllColumnsReport() throws Exception {
		try {
			prepareSaveAllColumnsReport();
			actionPerform(ReportWizardAction.class, form);
			verifyNoActionMessages();
			verifyNoActionMessages();

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	public void testDeleteReport() throws Exception {
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		// just in case ATL is no the leader
		teamMember.setTeamHead(true);
		session.setAttribute("teamLeadFlag", "true");
		Collection<AmpReports> reports = TeamUtil.getAllTeamReports(teamMember.getTeamId());
		Long id = null;
		for (AmpReports ampReports : reports) {
			if (ampReports.getName().equalsIgnoreCase("AUTO_TEST_REPORT")) {
				id = ampReports.getAmpReportId();
				break;
			}
		}
		if (id == null) {
			fail("Report Doesn't exists");
		}
		addRequestParameter("isTab", "2");
		addRequestParameter("rid", String.valueOf(id));
		actionPerform(DeleteAllReports.class);
		// verifyNoActionMessages();
		verifyForward("forwardReports");
	}

	public void testDeleteAllColumnReport() throws Exception {
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		teamMember.setTeamHead(true);
		session.setAttribute("teamLeadFlag", "true");
		Collection<AmpReports> reports = TeamUtil.getAllTeamReports(teamMember.getTeamId());
		Long id = null;
		for (AmpReports ampReports : reports) {
			if (ampReports.getName().equalsIgnoreCase("AUTO_TEST_REPORT_ALL_COLUMNS")) {
				id = ampReports.getAmpReportId();
				break;
			}
		}
		if (id == null) {
			fail("Report Doesn't exists");
		}

		addRequestParameter("isTab", "2");
		addRequestParameter("rid", String.valueOf(id));
		actionPerform(DeleteAllReports.class);
		// verifyNoActionMessages();
		verifyForward("forwardReports");
	}

	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}
