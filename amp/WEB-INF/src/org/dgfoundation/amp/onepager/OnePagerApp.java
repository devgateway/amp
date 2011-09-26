/**
 * 
 */
package org.dgfoundation.amp.onepager;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.yui.YuiLib;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.dgfoundation.amp.onepager.components.features.AmpActivityFormFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.translation.AmpAjaxBehavior;
import org.dgfoundation.amp.onepager.translation.TranslationComponentResolver;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.util.JspResolver;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.dgfoundation.amp.permissionmanager.web.pages.PermissionManager;
import org.springframework.security.AuthenticationManager;
import org.wicketstuff.jquery.JQueryBehavior;
import org.wicketstuff.mergedresources.ResourceMount;
import org.wicketstuff.mergedresources.ResourceSpec;

/**
 * @author mihai
 *
 */
public class OnePagerApp extends AuthenticatedWebApplication {

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
		 
		 getResourceSettings().setStripJavascriptCommentsAndWhitespace(true);
		 //getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
		 if (true) {		
			 ResourceMount.mountWicketResources("script", this);

			 ResourceMount mount = new ResourceMount();
			 //.setResourceVersionProvider(new RevisionVersionProvider());
			 
			 
			 LinkedList<ResourceSpec> csslist = new LinkedList<ResourceSpec>();
			 //csslist.add(new ResourceSpec(YuiLib.class, "calendar/assets/skins/sam/calendar.css"));
			 //csslist.add(new ResourceSpec(new ResourceReference("TEMPLATE/ampTemplate/css_2/amp-wicket.css")));
			 
			 
			 LinkedList<ResourceSpec> jslist = new LinkedList<ResourceSpec>();
			 jslist.add(new ResourceSpec(JQueryBehavior.class, "jquery.js"));
			 jslist.add(new ResourceSpec(JQueryBehavior.class, "jquery.debug.js"));
			 //jslist.add(new ResourceSpec(AutoCompleteBehavior.class, "wicket-autocomplete.js"));
			 jslist.add(new ResourceSpec(AbstractDefaultAjaxBehavior.class, "wicket-ajax.js"));
			 jslist.add(new ResourceSpec(IHeaderContributor.class, "wicket-event.js"));
			 jslist.add(new ResourceSpec(AmpSubsectionFeaturePanel.class, "subsectionSlideToggle.js"));
			 jslist.add(new ResourceSpec(AmpStructuresFormSectionFeature.class, "gisPopup.js"));
//			 jslist.add(new ResourceSpec(YuiLib.class, "yahoo/yahoo-min.js"));			 
//			 jslist.add(new ResourceSpec(YuiLib.class, "yahoodomevent/yahoo-dom-event.js"));			 
//			 jslist.add(new ResourceSpec(YuiLib.class, "yuiloader.js")); //can't use the min version, because the normal one will be included too
//			 jslist.add(new ResourceSpec(YuiLib.class, "calendar/calendar-min.js"));
//			 jslist.add(new ResourceSpec(DatePicker.class, "wicket-date.js"));
			 jslist.add(new ResourceSpec(AbstractDefaultAjaxBehavior.class, "wicket-ajax-debug.js"));
			 jslist.add(new ResourceSpec(AmpAjaxBehavior.class, "translationsOnDocumentReady.js"));
			 jslist.add(new ResourceSpec(AmpActivityFormFeature.class, "previewLogframe.js"));
			 
			 mount.clone()
			 	.setPath("/style/all-23.css")
			 	.addResourceSpecs(csslist)
			 	.mount(this);
			 
			 mount.clone()
			 .setPath("/style/all-2.js")
			 .addResourceSpecs(jslist)
			 .mount(this);
		 }
 		 
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
		 
		 
		 getApplicationSettings().setPageExpiredErrorPage(AmpLoginRedirectPage.class);
		 getApplicationSettings().setAccessDeniedPage(AmpLoginRedirectPage.class);
		
		 //wicket session timeout
		 //WebRequest request = (WebRequest) WebRequestCycle.get().getRequest();
		 //request.getHttpServletRequest().getSession().setMaxInactiveInterval(2 * 60 * 60); //2hours
		 
		 getPageSettings().addComponentResolver(new TranslationComponentResolver());
		 getPageSettings().addComponentResolver(new JspResolver());
		 mountBookmarkablePage("onepager", OnePager.class);
		 mountBookmarkablePage("permmanager", PermissionManager.class);
		 
//		 ServletContext servletContext = getServletContext();
//		 Resource resource = new FileSystemResource(servletContext.getRealPath("spring-config.xml"));
//		 BeanFactory factory = new XmlBeanFactory(resource);
//		 sessionFactory=(SessionFactory) factory.getBean("sessionFactory");
		 
		 /**
		  * Added through recurring patch now
		  *	Check if One Pager FM root exists, if not try to add it
		  *
		 FMUtil.checkFmRoot(FMUtil.fmRootActivityForm);
		 FMUtil.checkFmRoot(FMUtil.fmRootPermissionManager);
		  *
		  */
		 
		 //set UTF-8 as the default encoding for all requests
		 getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		 getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); 
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
	 

	

}
