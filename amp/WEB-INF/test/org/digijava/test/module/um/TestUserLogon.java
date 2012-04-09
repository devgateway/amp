/*
 *   DigiTestBase.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: June 2, 2004
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.test.module.um;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.um.action.Login;
import org.digijava.module.um.form.LogonForm;
import servletunit.struts.MockStrutsTestCase;
import servletunit.HttpServletRequestSimulator;

import net.sf.swarmcache.ObjectCache;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.security.HttpLoginManager;
import servletunit.HttpServletResponseSimulator;
import javax.servlet.http.Cookie;
import org.digijava.kernel.security.HttpLoginManager.LoginInfo;
import org.digijava.test.util.DigiTestBase;

public class TestUserLogon extends DigiTestBase {
  private TestUserLogon testUserLogon = null;

  public TestUserLogon(String name) {
    super(name, "org/digijava/test/module/um/conf/test.properties");
  }


  /**
   * Test login with correct username and password.
   * @throws java.lang.Exception
   */
  public void testCorrectLogin() throws Exception{
    Login loginAction = new Login();

    HttpServletRequestSimulator request =  this.getMockRequest();
    HttpServletResponseSimulator response =  this.getMockResponse();

    //Set request header User-Agent from test.properties
    //testCorrectLogin.userAgent property
    request.setHeader("User-Agent", getTestProperty("testCorrectLogin.userAgent"));
    SiteDomain sd = new SiteDomain();

    sd.setSiteDomain("www.digilogin.com");
    request.setAttribute(Constants.CURRENT_SITE, sd);

    ActionMapping mapping = new ActionMapping();
    LogonForm form = new LogonForm();

    //Fille LogonForm object from test.properties testCorrectLogin set
    populateFormParameters (form, "testCorrectLogin");

    String realPath = request.getPathInfo();
    loginAction.execute(mapping, form, request, response);
    Cookie cookie = response.findCookie("digi_session_id");
    String dgSessionId = cookie.getValue();

    ObjectCache sessionCache = DigiCacheManager.getInstance().
        getCache("org.digijava.kernel.login_region");
    LoginInfo loginInfo = (LoginInfo) sessionCache.get(dgSessionId);
    assertEquals("Successfull login",loginInfo.getLoginResult(), HttpLoginManager.LOGIN_RESULT_OK);
  }

  /**
   * Test login with incorrect username and password.
   * @throws java.lang.Exception
   */
  public void testIncorrectLogin() throws Exception{
    Login loginAction = new Login();

    HttpServletRequestSimulator request =  this.getMockRequest();
    HttpServletResponseSimulator response =  this.getMockResponse();

    request.setHeader("User-Agent", getTestProperty("testIncorrectLogin.userAgent"));
    SiteDomain sd = new SiteDomain();

    sd.setSiteDomain("www.digilogin.com");
    request.setAttribute(Constants.CURRENT_SITE, sd);

    ActionMapping mapping = new ActionMapping();
    LogonForm form = new LogonForm();

    form.setUsername(getTestProperty("testIncorrectLogin.userName"));
    form.setPassword(getTestProperty("testIncorrectLogin.userPassword"));

    String realPath = request.getPathInfo();
    loginAction.execute(mapping, form, request, response);
    Cookie cookie = response.findCookie("digi_session_id");
    String dgSessionId = cookie.getValue();

    ObjectCache sessionCache = DigiCacheManager.getInstance().
        getCache("org.digijava.kernel.login_region");
    LoginInfo loginInfo = (LoginInfo) sessionCache.get(dgSessionId);
    assertEquals("Invalid login",loginInfo.getLoginResult(), HttpLoginManager.LOGIN_RESULT_INVALID);
  }

  protected void tearDown() throws Exception {
    testUserLogon = null;
    super.tearDown();
  }

}
