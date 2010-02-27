package org.dgfoundation.amp.test.search;

import java.util.Collection;
import java.util.Date;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.search.action.Search;
import org.digijava.module.search.form.SearchForm;
import org.digijava.module.search.util.SearchUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class SearchTest extends TestCase{
	
	
	public SearchTest (String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	


	public void testReportsSearch() throws Exception {
		TeamMember tm = new TeamMember();
		Collection<LoggerIdentifiable> resultList = null;
		 Long memId = new Long(112);
		
		 Long teamId =  new Long(33); 
		 Date fromDate = new Date(2008, 01, 01);
		 Date toDate = new Date(2009, 01, 01);
		
		
		tm.setMemberId(memId);
		tm.setTeamId(teamId);
		
		String keyword = "test";

		
		resultList = SearchUtil.getReports(tm, keyword, fromDate, toDate);
		assertNotNull(resultList);
		for (LoggerIdentifiable identifiable : resultList) {
			AmpReports report = (AmpReports)identifiable;
			boolean containsKeyword = report.getName().contains(keyword);
			assertTrue(containsKeyword);
			assertEquals(tm.getMemberId(), report.getOwnerId().getAmpTeamMemId());
		}
		
	}
	public void testTabSearch(){
		TeamMember tm = new TeamMember();
		Collection<LoggerIdentifiable> resultList = null;
		 Long memId = new Long(112);
		 Long teamId = new Long(33); 
		 Date fromDate = new Date(2008, 01, 01);
		 Date toDate = new Date(2009, 01, 01);
		
		tm.setMemberId(memId);
		tm.setTeamId(teamId);
		
		String keyword = "test";

		
		resultList = SearchUtil.getTabs(tm, keyword, fromDate, toDate);
		assertNotNull(resultList);
		for (LoggerIdentifiable identifiable : resultList) {
			AmpReports tabs = (AmpReports)identifiable;
			boolean containsKeyword = tabs.getName().contains(keyword);
			assertTrue(containsKeyword);
			assertEquals(tm.getMemberId(), tabs.getOwnerId().getAmpTeamMemId());
		}
	}


}
