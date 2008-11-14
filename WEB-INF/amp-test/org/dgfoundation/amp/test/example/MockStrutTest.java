package org.dgfoundation.amp.test.example;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.helper.Configuration;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.aim.action.SelectTeam;
import org.digijava.module.aim.form.LoginForm;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class MockStrutTest extends BasicActionTestCaseAdapter {
	
	private SelectTeam selectaAction;
	private LoginForm form;
	MockHttpSession session;
	MockHttpServletRequest request;
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		selectaAction = new SelectTeam();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(SelectTeam.class.getName(), selectaAction);
		form = (LoginForm) createActionForm(LoginForm.class);

		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();

		setValidate(false);
		setRelatedObjects();

	}

	protected void setRelatedObjects() {

		Locale testLocale = new Locale();
		testLocale.setCode("en");
		request.setAttribute(Constants.NAVIGATION_LANGUAGE, testLocale);

		SiteDomain siteDomain = new SiteDomain();
		Site site = new Site("amp", "amp");
		site.setId(new Long(3));

		siteDomain.setSite(site);
		request.setAttribute(Constants.CURRENT_SITE, siteDomain);

		addRequestParameter("id", "79");
		addRequestParameter("user", "atl@amp.org");

	}

	public void testSuccessfulSelectTeam() {
		form.setUserId("atl@amp.org");
		form.setPassword("atl");
		actionPerform(SelectTeam.class, form);

		verifyNoActionErrors();
		verifyNoActionMessages();
		verifyForward("forward");
		assertNotNull(session.getAttribute("teamLeadFlag"));
	}
}
