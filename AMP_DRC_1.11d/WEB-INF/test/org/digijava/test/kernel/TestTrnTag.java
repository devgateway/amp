package org.digijava.test.kernel;

import org.digijava.test.module.um.TestUserLogon;
import org.digijava.test.util.DigiTestBase;
import org.digijava.module.translation.taglib.TrnTag;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspFactory;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;
import javax.servlet.Servlet;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Enumeration;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache.CachedSite;
import org.digijava.kernel.util.SiteCache;

public class TestTrnTag
    extends DigiTestBase {

  public TestTrnTag(String name) {
    super(name, "org/digijava/test/kernel/conf/trnTest.properties");
  }


  public void testTrnTag() throws Exception {
    TrnTag trnTag = new TrnTag();


    HttpServletRequestSimulator request = getMockRequest();
    HttpServletResponseSimulator response = getMockResponse();

    Locale testLocale = new Locale();
    testLocale.setCode("en");
    request.setAttribute(Constants.NAVIGATION_LANGUAGE, testLocale);

    SiteDomain siteDomain = new SiteDomain();
    Site site = new Site("newdemo","newdemo");
    site.setId(new Long(0));

    SiteCache siteCache = SiteCache.getInstance();

    siteDomain.setSite(site);
    request.setAttribute(Constants.
            CURRENT_SITE, siteDomain);



    class PageContextSimulator
        extends PageContext {
      public void initialize(Servlet servlet, ServletRequest servletRequest,
                             ServletResponse servletResponse, String string,
                             boolean boolean4, int int5, boolean boolean6) throws
          IOException, IllegalStateException, IllegalArgumentException {
      }

      public void release() {
      }

      public void setAttribute(String string, Object object) {
      }

      public void setAttribute(String string, Object object, int int2) {
      }

      public Object getAttribute(String string) {
        return null;
      }

      public Object getAttribute(String string, int int1) {
        return null;
      }

      public Object findAttribute(String string) {
        return null;
      }

      public void removeAttribute(String string) {
      }

      public void removeAttribute(String string, int int1) {
      }

      public int getAttributesScope(String string) {
        return 0;
      }

      public Enumeration getAttributeNamesInScope(int int0) {
        return null;
      }

      public JspWriter getOut() {
        return null;
      }

      public HttpSession getSession() {
        return null;
      }

      public Object getPage() {
        return null;
      }

      public ServletRequest getRequest() {
        return getMockRequest();
      }

      public ServletResponse getResponse() {
        return getMockResponse();
      }

      public Exception getException() {
        return null;
      }

      public ServletConfig getServletConfig() {
        return null;
      }

      public ServletContext getServletContext() {
        return null;
      }

      public void forward(String string) throws ServletException, IOException {
      }

      public void include(String string) throws ServletException, IOException {
      }

      public void handlePageException(Exception exception) throws
          ServletException, IOException {
      }

      public void handlePageException(Throwable throwable) throws
          ServletException, IOException {
      }
    }

    PageContext context = new PageContextSimulator();


//    trnTag.setBodyContent();
    trnTag.setPageContext(context);
    trnTag.setBodyText("This is some test message resource");
    trnTag.setKey("test.digijava.testKey");
    trnTag.doAfterBody();
    String content = trnTag.getBodyContent().getString();

  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

}