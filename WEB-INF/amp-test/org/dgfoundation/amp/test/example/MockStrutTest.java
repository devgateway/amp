package org.dgfoundation.amp.test.example;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
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

	protected void setRelatedObjects() throws Exception {

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);

		addRequestParameter("id", "79");
		addRequestParameter("user", "atl@amp.org");

	}

	public void testSuccessfulSelectTeam() {
		form.setUserId("atl@amp.org");
		form.setPassword("atl");
		actionPerform(SelectTeam.class, form);

		verifyNoActionMessages();
		verifyNoActionMessages();
		verifyForward("forward");
		assertNotNull(session.getAttribute("teamLeadFlag"));
	}
}
