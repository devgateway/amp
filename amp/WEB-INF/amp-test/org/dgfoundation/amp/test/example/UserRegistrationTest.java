package org.dgfoundation.amp.test.example;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.um.action.UserRegisterBlank;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class UserRegistrationTest extends BasicActionTestCaseAdapter {

	private static Logger logger = Logger.getLogger(UserRegistrationTest.class);

	private UserRegisterBlank userRegisterBlank;
	private UserRegisterForm userRegisterForm;

	public UserRegistrationTest(String name) {
		super(name);

	}

	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		userRegisterBlank = new UserRegisterBlank();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(UserRegisterBlank.class.getName(), userRegisterBlank);
		userRegisterForm = (UserRegisterForm) createActionForm(UserRegisterForm.class);
		setValidate(false);
		fillForm();
	}

	public void testRegisterUser() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpServletResponse response = getActionMockObjectFactory().getMockResponse();

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);

		long userId = DbUtil.getuserId("utest@utest.org");

		Transaction tx = null;
		Session session = null;

		// Delete test user if it exists
		if (userId != 0) {
			User usr = null;
			try {
				session = PersistenceManager.getSession();
				tx = session.beginTransaction();
				String queryString = "select from " + User.class.getName() + " rs where rs.id = " + Long.toString(userId);
				Query q = session.createQuery(queryString);

				String querryString1 = "select from " + UserLangPreferences.class.getName() + " rs where rs.id.user.id = " + Long.toString(userId);
				Query q1 = session.createQuery(querryString1);

				UserLangPreferences userLangPreferences = null;
				if (q1.iterate().hasNext()) {
					userLangPreferences = (UserLangPreferences) q1.iterate().next();
					session.delete(userLangPreferences);
				}

				// session.delete(queryString);
				User user = null;
				if (q.iterate().hasNext()) {
					user = (User) q.iterate().next();
					session.delete(user);
				}
				tx.commit();

			} catch (Exception ex) {

			} finally {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception ex2) {
					logger.warn("releaseSession() failed", ex2);
				}
			}
		}

		// create the form needed to execute de action

		setValidate(false);
		setReset(false);
		actionPerform(UserRegisterBlank.class, userRegisterForm);
		userId = DbUtil.getuserId("utest@utest.org");
		assertEquals(userId == 0, false);
		verifyForward("success");
	}

	public void testRegisterDuplicateEmail() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		MockHttpServletResponse response = getActionMockObjectFactory().getMockResponse();

		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		setValidate(false);
		setReset(false);
		actionPerform(UserRegisterBlank.class, userRegisterForm);
		verifyForward("failure");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void fillForm() {
		userRegisterForm.setEmail("utest@utest.org");
		userRegisterForm.setEmailConfirmation("utest@utest.org");
		userRegisterForm.setFirstNames("utest");
		userRegisterForm.setLastName("utest");
		userRegisterForm.setPassword("utest");
		userRegisterForm.setPasswordConfirmation("utest");
		userRegisterForm.setSelectedCountryResidence("us");
		userRegisterForm.setSelectedLanguage("en");
	}
}
