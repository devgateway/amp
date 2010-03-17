package org.dgfoundation.amp.test.versioning;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.action.CompareActivityVersions;
import org.digijava.module.aim.action.SaveActivity;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.um.util.AmpUserUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class TestCompareVersion extends BasicActionTestCaseAdapter {

	private CompareActivityVersionsForm form;
	private CompareActivityVersions action;
	private MockHttpSession session;
	private MockHttpServletRequest request;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();

		action = new CompareActivityVersions();
		form = new CompareActivityVersionsForm();
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		TestUtil.initializeContext(context);
		context.setAttribute(CompareActivityVersions.class.getName(), action);
		AmpTreeVisibility ampTreeVisibility = new AmpTreeVisibility();
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getTemplateById(FeaturesUtil
				.getGlobalSettingValueLong("Visibility Template"));
		ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
		context.setAttribute("ampTreeVisibility", ampTreeVisibility);
		TestUtil.setSiteDomain(request);
		TestUtil.setLocaleEn(request);
		TestUtil.setCurrentMemberFirstATLTeam(session);
		session.setAttribute("org.digijava.kernel.user", AmpUserUtil.getAllUsers(false).iterator().next());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Compare 2 different activities and check for exceptions. (There is a
	 * bigger chance of errors comparing 2 different activities.)
	 */
	public void testCompare() {
		AmpActivityVersion activityOne = ActivityUtil.getAllActivityVersionsList().get(0);
		AmpActivityVersion activityTwo = ActivityUtil.getAllActivityVersionsList().get(1);
		form.setActivityOneId(activityOne.getAmpActivityId());
		form.setActivityTwoId(activityTwo.getAmpActivityId());

		actionPerform(CompareActivityVersions.class, form);
		verifyNoActionErrors();
		verifyNoActionMessages();
	}
}
