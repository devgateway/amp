package org.digijava.test.module.um;

import java.util.List;

import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import org.digijava.module.um.action.UserRegisterBlank;
import org.digijava.module.um.form.UserRegisterForm;
import org.digijava.module.um.util.DbUtil;
import org.digijava.test.util.DigiTestBase;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import org.apache.struts.config.ForwardConfig;

public class TestUserRegistration extends DigiTestBase {
  private UserRegisterBlank userRegisterBlank = null;

  public TestUserRegistration(String name) {
    super(name, "org/digijava/test/module/um/conf/testUserRegister.properties");
  }

  public void testRegisterUser() throws Exception{
    HttpServletRequestSimulator request = getMockRequest();
    HttpServletResponseSimulator response = getMockResponse();

    SiteDomain sd = new SiteDomain();
    sd.setSiteDomain("www.digilogin.com");
    Site site = null;

    List sites = org.digijava.module.admin.util.DbUtil.getSites();
    if (sites != null && sites.size()>0) {
      site = (Site)sites.get(0);
    }

    Locale lang = new Locale();
    lang.setCode("en");

    request.setAttribute(Constants.NAVIGATION_LANGUAGE, lang);




/*
    site.setSiteId("loginSite");
    site.setUserLanguages(new HashSet());
    site.setTranslationLanguages(new HashSet());
    site.setModuleInstances(new HashSet());*/
    sd.setSite(site);
//    SiteCache.getInstance().addSite(site);
    request.setAttribute(Constants.CURRENT_SITE, sd);
    UserRegisterForm userRegisterForm = new UserRegisterForm();

    long userId =
        DbUtil.getuserId(getTestProperty("beanParameter.userRegisterForm.email"));


    Transaction tx = null;
    Session session = null;

    //Delete test user if it exists
    if (userId != 0) {
      User usr = null;
      try {
        session = PersistenceManager.getSession();
        tx = session.beginTransaction();
        String queryString = "select from " +
            User.class.getName() +
            " rs where rs.id = " + Long.toString(userId);
        Query q = session.createQuery(queryString);

        String querryString1 = "select from " +
            UserLangPreferences.class.getName() +
            " rs where rs.id.user.id = " + Long.toString(userId);
        Query q1 = session.createQuery(querryString1);


        UserLangPreferences userLangPreferences = null;
        if (q1.iterate().hasNext()) {
          userLangPreferences = (UserLangPreferences) q1.iterate().next();
          session.delete(userLangPreferences);
        }


//        session.delete(queryString);
        User user = null;
        if (q.iterate().hasNext()) {
          user = (User) q.iterate().next();
          session.delete(user);
        }
        tx.commit();


      }
      catch (Exception ex) {

      }
      finally {
        try {
          PersistenceManager.releaseSession(session);
        }
        catch (Exception ex2) {
          logger.warn("releaseSession() failed", ex2);
        }
      }
    }
//Register new test user
      populateFormParameters (userRegisterForm, "userRegisterForm");
      /*
      userRegisterForm.setEmail("utest@utest.org");
      userRegisterForm.setEmailConfirmation("utest@utest.org");
      userRegisterForm.setFirstNames("utest");
      userRegisterForm.setLastName("utestkumar");
      userRegisterForm.setPassword("utest");
      userRegisterForm.setPasswordConfirmation("utest");
      userRegisterForm.setSelectedCountryResidence("us");
      userRegisterForm.setSelectedLanguage("en");
        */
      userRegisterBlank = new UserRegisterBlank();
//      mapping.findForward("success")


      ActionMapping mapping = new ActionMapping();
      ForwardConfig fw = new ForwardConfig();
      fw.setName("success");
      fw.setPath(" ");

      mapping.addForwardConfig(fw);

      try {
        userRegisterBlank.execute(mapping, userRegisterForm, request, response);
      } catch (Exception ex) {

      }

      userId =
        DbUtil.getuserId(getTestProperty("beanParameter.userRegisterForm.email"));


    assertEquals(userId == 0, false);

  }


  protected void tearDown() throws Exception {
    userRegisterBlank = null;
    super.tearDown();
  }

}
