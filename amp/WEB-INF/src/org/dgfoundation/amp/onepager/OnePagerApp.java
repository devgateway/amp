/**
 *
 */
package org.dgfoundation.amp.onepager;

import net.ftlines.wicketsource.WicketSource;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.DecoratingHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.servlet.ResponseIOException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.dgfoundation.amp.onepager.translation.TranslationComponentResolver;
import org.dgfoundation.amp.onepager.util.FMComponentResolver;
import org.dgfoundation.amp.onepager.util.JspResolver;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.dgfoundation.amp.permissionmanager.web.pages.PermissionManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketException;

/**
 * @author mihai
 */
public class OnePagerApp extends AuthenticatedWebApplication {
    
    public static boolean IS_DEVELOPMENT_MODE = false;

    private static Logger logger = Logger.getLogger(OnePagerApp.class);

    // To be injected by Spring
    private AuthenticationManager authenticationManager;

    /* (non-Javadoc)
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        // TODO Auto-generated method stub
        return OnePager.class;
    }

    public OnePagerApp() {
    }

    @Override
    public void init() {
        super.init();

        RuntimeConfigurationType configurationType = Application.get().getConfigurationType();

        if (RuntimeConfigurationType.DEVELOPMENT.equals(configurationType)) {
            //enable Wicket-Source if we are in development mode
            WicketSource.configure(this);
            IS_DEVELOPMENT_MODE = true;
        }

        //getResourceSettings().setStripJavaScriptCommentsAndWhitespace(true);
        //getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
        //TODO:
        //TODO:1.5
        //TODO:
         /*
          * 
         if (true) {        
             ResourceMount.mountWicketResources("script", this);

             ResourceMount mount = new ResourceMount();
             //.setResourceVersionProvider(new RevisionVersionProvider());
             
             
             LinkedList<ResourceSpec> csslist = new LinkedList<ResourceSpec>();
             //csslist.add(new ResourceSpec(YuiLib.class, "calendar/assets/skins/sam/calendar.css"));
             //csslist.add(new ResourceSpec(new ResourceReference("TEMPLATE/ampTemplate/css_2/amp-wicket.css")));
             
             
             LinkedList<ResourceSpec> jslist = new LinkedList<ResourceSpec>();
             jslist.add(new ResourceSpec(JQueryBehavior.class, JQueryBehavior.JQUERY_FILE_NAME));
             //jslist.add(new ResourceSpec(AutoCompleteBehavior.class, "wicket-autocomplete.js"));
             jslist.add(new ResourceSpec(AbstractDefaultAjaxBehavior.class, "wicket-ajax.js"));
             jslist.add(new ResourceSpec(IHeaderContributor.class, "wicket-event.js"));
             jslist.add(new ResourceSpec(AmpSubsectionFeaturePanel.class, "subsectionSlideToggle.js"));
             jslist.add(new ResourceSpec(AmpStructuresFormSectionFeature.class, "gisPopup.js"));
//           jslist.add(new ResourceSpec(YuiLib.class, "yahoo/yahoo-min.js"));           
//           jslist.add(new ResourceSpec(YuiLib.class, "yahoodomevent/yahoo-dom-event.js"));             
//           jslist.add(new ResourceSpec(YuiLib.class, "yuiloader.js")); //can't use the min version, because the normal one will be included too
//           jslist.add(new ResourceSpec(YuiLib.class, "calendar/calendar-min.js"));
//           jslist.add(new ResourceSpec(DatePicker.class, "wicket-date.js"));
             jslist.add(new ResourceSpec(AbstractDefaultAjaxBehavior.class, "wicket-ajax-debug.js"));
             jslist.add(new ResourceSpec(AmpAjaxBehavior.class, "translationsOnDocumentReady.js"));
             jslist.add(new ResourceSpec(AmpActivityFormFeature.class, "previewLogframe.js"));
             jslist.add(new ResourceSpec(AmpActivityFormFeature.class, "saveNavigationPanel.js"));
             
             mount.clone()
                .setPath("/style/all-23.css")
                .addResourceSpecs(csslist)
                .mount(this);
             
             mount.clone()
             .setPath("/style/all-2.js")
             .addResourceSpecs(jslist)
             .mount(this);
         }
          */

        /**
         *
         * Should be replaceable by annotations...
         *
         // Security settings.
         // List every(!) page and component here for which access is forbidden unless
         // the current user has the correct role.
         */
        getSecuritySettings().setAuthorizationStrategy(new MetaDataRoleAuthorizationStrategy(this));
        MetaDataRoleAuthorizationStrategy.authorize(OnePager.class, "ROLE_AUTHENTICATED");
        //MetaDataRoleAuthorizationStrategy.authorizeAll(OnePager.class);
        MetaDataRoleAuthorizationStrategy.authorize(PermissionManager.class, "ROLE_AUTHENTICATED");


        //getApplicationSettings().setPageExpiredErrorPage(AmpLoginRedirectPage.class);
        //getApplicationSettings().setAccessDeniedPage(AmpLoginRedirectPage.class);


        //wicket session timeout
        //WebRequest request = (WebRequest) WebRequestCycle.get().getRequest();
        //request.getHttpServletRequest().getSession().setMaxInactiveInterval(2 * 60 * 60); //2hours

        getPageSettings().addComponentResolver(new TranslationComponentResolver());
        getPageSettings().addComponentResolver(new FMComponentResolver());
        getPageSettings().addComponentResolver(new JspResolver());
        mountPage(OnePagerConst.ONEPAGER_URL_PREFIX, OnePager.class);
        mountPage("permmanager", PermissionManager.class);

//       ServletContext servletContext = getServletContext();
//       Resource resource = new FileSystemResource(servletContext.getRealPath("spring-config.xml"));
//       BeanFactory factory = new XmlBeanFactory(resource);
//       sessionFactory=(SessionFactory) factory.getBean("sessionFactory");

        /**
         * Added through recurring patch now
         *  Check if One Pager FM root exists, if not try to add it
         *
         FMUtil.checkFmRoot(FMUtil.fmRootActivityForm);
         FMUtil.checkFmRoot(FMUtil.fmRootPermissionManager);
         *
         */

        //set UTF-8 as the default encoding for all requests
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        setHeaderResponseDecorator(new IHeaderResponseDecorator() {
            @Override
            public IHeaderResponse decorate(IHeaderResponse response) {
                return new DecoratingHeaderResponse(response) {
                    @Override
                    public void render(HeaderItem item) {
                        if (item instanceof StringHeaderItem) {
                            StringHeaderItem stringHeaderItem = (StringHeaderItem) item;

                            // make specific header item coming from <wicket:head> a priority one
                            if (stringHeaderItem.getString().toString().contains("X-UA-Compatible")) {
                                super.render(new PriorityHeaderItem(stringHeaderItem));
                            } else {
                                super.render(item);
                            }
                        } else {
                            super.render(item);
                        }
                    }
                };
            }
        });
    }


    @Override
    public final Session newSession(Request request, Response response) {
        return new AmpAuthWebSession(request);
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return AmpLoginRedirectPage.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return AmpAuthWebSession.class;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    // To be injected by Spring
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected WebResponse newWebResponse(WebRequest webRequest, HttpServletResponse httpServletResponse) {
        return new ServletWebResponse((ServletWebRequest) webRequest, httpServletResponse) {
            @Override
            public void flush() {
                try {
                    getContainerResponse().flushBuffer();
                } catch (SocketException e) {
                    logger.warn("Socket exception encountered, ignoring", e);
                } catch (IOException e) {
                    // Socket Exception can be wrapped by a container specific exception.
                    // So we check the cause of the container exception
                    Throwable rootCause = null != e.getCause() ? e.getCause() : e;
                    if (rootCause instanceof SocketException) {
                        logger.warn("Socket exception encountered, ignoring.", rootCause);
                        return;
                    } else {
                        logger.warn("Root Cause not instance of SocketException");
                    }
                    throw new ResponseIOException(e);
                }
            }
        };
    }


}
