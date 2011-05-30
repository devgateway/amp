/**
 * 
 */
package org.dgfoundation.amp.onepager;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.dgfoundation.amp.onepager.translation.TranslationComponentResolver;
import org.dgfoundation.amp.onepager.util.JspResolver;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.dgfoundation.amp.permissionmanager.web.pages.PermissionManager;
import org.hibernate.SessionFactory;
import org.springframework.security.AuthenticationManager;

/**
 * @author mihai
 *
 */
public class OnePagerApp extends AuthenticatedWebApplication {

	private static Logger logger = Logger.getLogger(OnePagerApp.class);
	public static SessionFactory sessionFactory;
	
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
		 
		 getPageSettings().addComponentResolver(new TranslationComponentResolver());
		 getPageSettings().addComponentResolver(new JspResolver());
		 mountBookmarkablePage("onepager", OnePager.class);
		 mountBookmarkablePage("permmanager", PermissionManager.class);
		 
//		 ServletContext servletContext = getServletContext();
//		 Resource resource = new FileSystemResource(servletContext.getRealPath("spring-config.xml"));
//		 BeanFactory factory = new XmlBeanFactory(resource);
//		 sessionFactory=(SessionFactory) factory.getBean("sessionFactory");
		 
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
