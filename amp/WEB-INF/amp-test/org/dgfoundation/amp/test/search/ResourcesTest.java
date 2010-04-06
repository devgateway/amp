package org.dgfoundation.amp.test.search;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.search.action.Search;
import org.digijava.module.search.form.SearchForm;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class ResourcesTest extends BasicActionTestCaseAdapter{
	
	private Search action;
	private SearchForm searchForm;
	MockHttpSession session;
	MockHttpServletRequest request;
	
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(Search.class.getName(), action);	
		searchForm = (SearchForm) createActionForm(SearchForm.class);
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		action = new Search();
		setValidate(false);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testResources(){
		Long perPage = new Long(-1);
		TeamMember tm = new TeamMember();
		 Long memId = new Long(112);
		 Long teamId =  new Long(33);
		 Long roleId = new Long(2);
		 tm.setMemberId(memId);
		 tm.setTeamId(teamId);
		 tm.setTeamAccessType("Team");
		 tm.setActivities(null);
		 tm.setAddActivity(true);
		 tm.setAppSettings(null);
		 tm.setComputation(true);
		 tm.setDelete(false);
		 tm.setEmail("test@test.com");
		 tm.setRead(true);
		 tm.setRoleId(roleId);
		 tm.setRoleName("Workspace Member");
		 tm.setTeamAccessType("Team");
		 tm.setTeamHead(false);
		 tm.setTeamType(null);
		 tm.setTranslator(false);
		 tm.setWrite(true);
			searchForm.setActSearchKey(1);
			searchForm.setDateSelection(1);
			searchForm.setKeyword("www");
			searchForm.setSearchByDate(false);
			searchForm.setQueryType(3);
			searchForm.setFromDate(null);
			searchForm.setToDate(null);
			searchForm.setResultsPerPage(perPage);
			request.setAttribute("form", searchForm);
		 request.getSession().setAttribute("currentMember", tm);
		 actionPerform(Search.class, searchForm);
	}

}
