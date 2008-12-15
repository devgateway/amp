package org.dgfoundation.amp.test.usermanager;


import java.util.Collection;
import java.util.Random;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.action.RegisterUser;
import org.digijava.module.um.form.AddUserForm;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class AdminAddUserTest extends BasicActionTestCaseAdapter {

	private static Logger logger = Logger.getLogger(AdminAddUserTest.class);

	private RegisterUser userRegister;
	private AddUserForm addUserForm;
	public AdminAddUserTest(String name) {
		super(name);

	}
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		userRegister = new RegisterUser();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(RegisterUser.class.getName(), userRegister);
		addUserForm = (AddUserForm) createActionForm(AddUserForm.class);
		setValidate(false);
		fillForm();
	}
	public void testAddWorkSpace() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		addUserForm.setAddWorkspace(true);
		setValidate(true);
		setReset(false);
		actionPerform(RegisterUser.class, addUserForm);
		verifyForward("forward");
		verifyNoActionErrors();

	}
	public void testRolesAvailable() throws Exception {	
		addRoles();
		assertFalse("There are not Roles!!", addUserForm.getAmpRoles().isEmpty());
	}
	public void testWorkspacesAvailable() throws Exception {	
		addWorkspaces();
		assertFalse("There are not Workspaces!!", addUserForm.getWorkspaces().isEmpty());
	}
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	private void addWorkspaces(){
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
			
		Collection<AmpTeam> ampWorkspaces = (Collection<AmpTeam>) request.getSession().getAttribute("ampWorkspaces");
		if (ampWorkspaces == null) {
			ampWorkspaces = TeamUtil.getAllTeams();
		}
		addUserForm.setWorkspaces(ampWorkspaces);
	}
	private void addRoles(){
		addUserForm.setAmpRoles(TeamMemberUtil.getAllTeamMemberRoles());
	}

	private void fillForm() {
		Random id = new Random();
		String userEmail = "test" + id.nextInt()+"@utest.org";
		addUserForm.setEmail(userEmail);
		addUserForm.setEmailConfirmation(userEmail);
		addUserForm.setFirstNames("utest");
		addUserForm.setLastName("utest");
		addUserForm.setPassword("utest");
		addUserForm.setPasswordConfirmation("utest");
		addUserForm.setPasswordConfirmation("utest");
		addUserForm.setSelectedCountryResidence("us");
		addUserForm.setSelectedLanguage("en");
		addUserForm.setSendEmail(false);
		addUserForm.setSelectedOrgType(4L);
		addUserForm.setSelectedOrgGroup(32L);
		addUserForm.setSelectedOrganizationId(21L);
	}
}
