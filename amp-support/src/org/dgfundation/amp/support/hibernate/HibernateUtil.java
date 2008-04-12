package org.dgfundation.amp.support.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {

	private static Logger log = Logger.getLogger(HibernateUtil.class.getName());

	private static final SessionFactory sessionFactory;

	static {
		try {

			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure();
			sessionFactory = cfg.buildSessionFactory();
		} catch (Throwable ex) {
			log.error(ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}
}
