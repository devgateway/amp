package org.dgfoundation.amp.test.parisindicator;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.ParisIndicatorReport;
import org.digijava.module.aim.action.SelectTeam;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.LoginForm;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class ParisIndicatorActionTest extends BasicActionTestCaseAdapter {

	private MockHttpSession session;
	private MockHttpServletRequest request;
	private ParisIndicatorReportForm form;
	private TeamMember teamMember;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		form = (ParisIndicatorReportForm) createActionForm(ParisIndicatorReportForm.class);
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		setValidate(false);
		setRelatedObjects();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void setRelatedObjects() throws Exception {
		//Setup some user data.
		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);
		AmpApplicationSettings ampAppSettings = DbUtil.getMemberAppSettings(((AmpTeamMember) session.getAttribute("JUnitAmpTeamMember")).getAmpTeamMemId());
		ApplicationSettings appSettings = new ApplicationSettings();
		appSettings.setAppSettingsId(ampAppSettings.getAmpAppSettingsId());
		appSettings.setDefRecsPerPage(ampAppSettings.getDefaultRecordsPerPage().intValue());
		appSettings.setCurrencyId(ampAppSettings.getCurrency().getAmpCurrencyId());
		appSettings.setFisCalId(ampAppSettings.getFiscalCalendar().getAmpFiscalCalId());
		appSettings.setValidation(ampAppSettings.getValidation());
		((TeamMember) session.getAttribute("currentMember")).setAppSettings(appSettings);
		
	}
	
	/**
	 * Test if a not authenticated user can watch the PI.
	 */
	public void testUserNotLoggedRejection() {
		request.getSession().setAttribute("currentMember", null);
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyNoActionMessages();
		verifyForward("index");
	}
	
	/**
	 * Test if the first report type is selected for some reports.
	 */
	public void testFirstReportSelection() {
		//Report 3.
		form.setIndicatorCode("3");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "1");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
		
		//Report 4.
		form.setIndicatorCode("4");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "2");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
		
		//Report 5a.
		form.setIndicatorCode("5a");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "3");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
		
		//Report 5b.
		form.setIndicatorCode("5b");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "4");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
		
		//Report 9.
		form.setIndicatorCode("9");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "7");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
		
		//Report 10a.
		form.setIndicatorCode("10a");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "8");
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report1");
	}
	
	/**
	 * Test if the second report type is selected for some reports.
	 */
	public void testSecondReportSelection() {
		// Report 6.
		form.setIndicatorCode("6");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "5");

		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report2");

		// Report 7.
		form.setIndicatorCode("7");
		form.setReset(true);
		form.setFilterFlag(false);
		addRequestParameter("indcId", "6");

		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("report2");
	}

	/**
	 * Test if the user is redirected to the menu when some parameters are missing.
	 */
	public void testMissingParameterRejection() {
		form.setReset(true);
		form.setFilterFlag(false);
		
		actionPerform(ParisIndicatorReport.class, form);
		verifyNoActionMessages();
		verifyForward("menu");
	}
}