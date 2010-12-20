/**
 * 
 */
package org.dgfoundation.amp.onepager;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.dgfoundation.amp.onepager.translation.AmpComponentResolver;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author mihai
 *
 */
public class OnePagerApp extends WebApplication {

	private static Logger logger = Logger.getLogger(OnePagerApp.class);
	public static SessionFactory sessionFactory;
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
		 getPageSettings().addComponentResolver(new AmpComponentResolver());
		 mountBookmarkablePage("onepager", OnePager.class);
		 
//		 ServletContext servletContext = getServletContext();
//		 Resource resource = new FileSystemResource(servletContext.getRealPath("spring-config.xml"));
//		 BeanFactory factory = new XmlBeanFactory(resource);
//		 sessionFactory=(SessionFactory) factory.getBean("sessionFactory");
		 
	 }

	 @Override
		public final Session newSession(Request request, Response response) {
			return new AmpWebSession(request);
		}
	  
}
