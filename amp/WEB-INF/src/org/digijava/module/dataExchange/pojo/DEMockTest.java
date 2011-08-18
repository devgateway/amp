/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import javax.servlet.ServletContext;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

/**
 * @author dan
 *
 */
public class DEMockTest extends BasicActionTestCaseAdapter {
	MockHttpSession session;
	MockHttpServletRequest request;
	
	public void setUp(Long userId, String email) throws Exception {
		//super.setUp();
		//Configuration.initConfig();
		//ServletContext context = getActionMockObjectFactory().getMockServletContext();

		session = getActionMockObjectFactory().getMockSession();
		request = getActionMockObjectFactory().getMockRequest();

		//setValidate(false);
		setRelatedObjects(userId, email);
	}
	
	
	protected void setRelatedObjects(Long userId, String email) throws Exception {

		Locale lang = new Locale();
		lang.setCode("en");
		request.setAttribute(org.digijava.kernel.Constants.NAVIGATION_LANGUAGE, lang);
		Site s = DataExchangeUtils.getSite(3L);
		SiteDomain sd = new SiteDomain();
		sd.setSite(s);
		request.setAttribute(org.digijava.kernel.Constants.CURRENT_SITE, sd);

		addRequestParameter("id", userId.toString());
		addRequestParameter("user", email);

	}

	public MockHttpSession getSession() {
		return session;
	}
	public void setSession(MockHttpSession session) {
		this.session = session;
	}
	public MockHttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(MockHttpServletRequest request) {
		this.request = request;
	}
}
