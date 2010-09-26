package org.dgfoundation.amp.test.usermanager;


import java.util.Collection;
import java.util.Random;

import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.um.action.RegisterUser;
import org.digijava.module.um.form.AddUserForm;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import org.digijava.module.um.util.DbUtil;

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
		verifyNoActionMessages();

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

	@SuppressWarnings("unchecked")
	private void fillForm() {
		boolean ok = false;
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
		
		// these nested "for" statements ensure the fields will be filled with 
		// a valid selectedOrgType, selectedOrgGroup and, selectedOrganizationId,
		addUserForm.setOrgTypeColl(DbUtil.getAllOrgTypes());
		for(Iterator it = addUserForm.getOrgTypeColl().iterator(); it.hasNext();){
			AmpOrgType orgType = (AmpOrgType)it.next();
			addUserForm.setSelectedOrgType(orgType.getAmpOrgTypeId());
			addUserForm.setOrgGroupColl(DbUtil.getOrgGroupByType(addUserForm.getSelectedOrgType()));
			for(Iterator it2 = addUserForm.getOrgGroupColl().iterator(); it2.hasNext();){
				AmpOrgGroup orgGroup = (AmpOrgGroup)it2.next();
				addUserForm.setSelectedOrgGroup(orgGroup.getAmpOrgGrpId());
			    addUserForm.setOrgColl(DbUtil.getOrgByGroup(addUserForm.getSelectedOrgGroup()));
				if(addUserForm.getOrgColl().iterator().hasNext()){
					AmpOrganisation org = (AmpOrganisation)addUserForm.getOrgColl().iterator().next();
					addUserForm.setSelectedOrganizationId(org.getAmpOrgId());
			    	ok = true;
			    	break;
				}
			}
			if(ok==true)
				break;
		}
	}
}
