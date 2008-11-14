package org.dgfoundation.amp.test;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.helper.Configuration;
import org.dgfoundation.amp.test.helper.TestUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class ReportsTest extends BasicActionTestCaseAdapter {

	private ViewNewAdvancedReport action;
	private AdvancedReportForm form;

	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		action = new ViewNewAdvancedReport();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(ViewNewAdvancedReport.class.getName(), action);
		form = (AdvancedReportForm) createActionForm(AdvancedReportForm.class);
		Configuration.initConfig();
		setValidate(false);
		setRelatedObject();
	}

	void setRelatedObject() throws DgException {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpSession session = getActionMockObjectFactory().getMockSession();
		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		
		TeamMember tm =TeamMemberUtil.getTMTeamHead(new Long(1));
		session.setAttribute("currentMember",tm);		
		
	}

	public void testGenerateReport() {
		actionPerform(ViewNewAdvancedReport.class);
		verifyActionErrorNotPresent("AA");
		
	}
}
