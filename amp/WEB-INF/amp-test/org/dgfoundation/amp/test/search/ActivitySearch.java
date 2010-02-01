package org.dgfoundation.amp.test.search;


import java.util.Collection;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.dgfoundation.amp.test.util.mock.AMPMockServletContext;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.action.Search;
import org.digijava.module.search.form.SearchForm;
import org.digijava.module.search.util.SearchUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class ActivitySearch extends BasicActionTestCaseAdapter{

	protected Search searchAction;
	protected SearchForm searchForm;
	protected MockHttpSession session;
	protected MockHttpServletRequest request;
	

	
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		searchAction = new Search();
		
		ServletContext context 	= getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(SearchForm.class.getName(), searchForm);
		searchForm = (SearchForm) createActionForm(SearchForm.class);
		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();
		setValidate(false);
		TestUtil.setLocaleEn(request);
		
		try {
			TestUtil.setSiteDomain(request);
		} catch (DgException e) {
			
			e.printStackTrace();
		}
		
		try {
			TestUtil.setCurrentMemberFirstATLTeam(session);
		} catch (DgException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	} 
	
	public void testActivitySearch(){
		 searchForm.setActSearchKey(1);
		 searchForm.setKeyword("test");
		 searchForm.setQueryType(0);
		actionPerform(Search.class, searchForm);
		verifyNoActionErrors();
		verifyNoActionMessages();
		verifyForward("forward");
		
	}
	
	public void testActivitySearchByDate(){
		 searchForm.setActSearchKey(1);
		 searchForm.setKeyword("test");
		 searchForm.setQueryType(0);
		 searchForm.setSearchByDate(false);
		 searchForm.setToDate("01/01/2010");
		 searchForm.setFromDate("01/01/2009");
			actionPerform(Search.class, searchForm);
			verifyNoActionErrors();
			verifyNoActionMessages();
			verifyForward("forward");
	}

}
