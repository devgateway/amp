package org.dgfundation.amp.support.hibernate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		HibernateUtil.getSession();
	}

	public void contextDestroyed(ServletContextEvent event) {
		HibernateUtil.getSession().close(); // 
	}
}