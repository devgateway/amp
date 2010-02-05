package org.dgfoundation.amp.test.workspacemanager;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.SelectorAction;
import org.digijava.module.aim.action.WorkspaceManager;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.WorkspaceForm;
import org.digijava.module.aim.util.TeamUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * @author Marcelo
 */
public class WorkspaceActionTest extends BasicActionTestCaseAdapter {
	private static Logger logger = Logger.getLogger(WorkspaceActionTest.class);

	private WorkspaceManager action;
	private WorkspaceForm form;

	public WorkspaceActionTest(String name) {
		super(name);

	}

	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		action = new WorkspaceManager();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(WorkspaceManager.class.getName(), action);
		form = (WorkspaceForm) createActionForm(WorkspaceForm.class);
		setValidate(false);
	}
	
	public void testWorskpaceCollection() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpServletResponse response = getActionMockObjectFactory().getMockResponse();
		request.setAttribute(SelectorAction.ATTRIBUTE_START, "/aim/workspaceManager");
		request.getSession().setAttribute("ampAdmin", "ampAdmin");

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		setValidate(false);
		setReset(false);

		actionPerform(WorkspaceManager.class, form);
		Collection workspaces = form.getWorkspaces();
		Collection<AmpTeam> ampWorkspaces = TeamUtil.getAllTeams();
   	
		assertTrue(workspaces.size()==ampWorkspaces.size());

		AmpTeam teama = (AmpTeam)workspaces.iterator().next();
		AmpTeam teamb = (AmpTeam)ampWorkspaces.iterator().next();

		assertTrue(teama.getName().equals(teamb.getName()) && teama.getAmpTeamId().longValue()==teamb.getAmpTeamId().longValue());
		
		verifyForward("forward");
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
