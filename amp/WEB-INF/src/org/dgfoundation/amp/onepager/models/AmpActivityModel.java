/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.startup.AmpSessionListener;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel extends LoadableDetachableModel<AmpActivityVersion> implements IModel<AmpActivityVersion>  {
	private static Logger logger = Logger.getLogger(AmpActivityModel.class);
	
	
	protected transient AmpActivityVersion a;
	protected Long id;

	private static final long serialVersionUID = 3915904820384659360L;

	
	
	public AmpActivityModel() {
		beginConversation();
		a = new AmpActivityVersion();
	}	
	
	public AmpActivityModel(Long id) {
		beginConversation();
		
		this.id = id;
		this.a = null;
	}
	
	/**
	 * Start a multi-request dialogue known as a conversation.
	 * This creates a hibernate session that is kept in the HttpSession (thus one per each user session)
	 * @see AmpSessionListener - the hibernate session is guaranteed to be closed on session death
	 */
	public void beginConversation() {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		s.setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, null);
		s.setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, null);
		s.setMetaData(OnePagerConst.EDITOR_ITEMS, null);
		s.setMetaData(OnePagerConst.COMMENTS_ITEMS, null);
		s.setMetaData(OnePagerConst.COMMENTS_DELETED_ITEMS, null);

		Session ses = getHibernateSession();
		ses.clear();
	}
	
	
	/**
	 * We keep hibernate session in the http session. This will ensure there is one session per wicket conversation (pager).
	 * When its null, a new session is created and a transaction started
	 * Always set {@link FlushMode#MANUAL} so that changes are committed only by {@link ActivityUtil#saveActivity(AmpActivityModel, boolean)}
	 * No {@link Transaction} nor {@link Session} references are kept in the model!
	 * @return
	 */
	public static Session getHibernateSession() {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		Session hibernateSession = (Session) s.getHttpSession().getAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY);
		if(hibernateSession==null)  {
			try {
				hibernateSession = PersistenceManager.openNewSession();
				hibernateSession.setFlushMode(FlushMode.MANUAL);
				hibernateSession.beginTransaction();
				s.getHttpSession().setAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY,hibernateSession);
			} catch (HibernateException e) {
				throw new RuntimeException(e);
			}
		}
		return hibernateSession;
	}

	protected AmpActivityVersion load() {
		beginConversation();
		AmpActivityVersion ret = ActivityUtil.load(this, id);

		return ret;
	}

	@Override   
	public void setObject(AmpActivityVersion arg0) {
		a = arg0;
	}

	@Override
	public void detach() {
	}

	@Override
	public AmpActivityVersion getObject() {
		if (a == null)
			a = load();
		return a;
	}


	/**
	 * @see #beginConversation()
	 * Ends a conversation (multi step/request dialogue). Ensures to retrieve the Hibernate session and to commit the current transaction + flush it
	 * Then in a finally block we close the session.
	 */
	public static void endConversation() {		
			AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
			Session hibernateSession = (Session) s.getHttpSession().getAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY);
			s.getHttpSession().setAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY,null);
			if(hibernateSession==null) throw new RuntimeException("Attempted to close an unexisting Hibernate session!");
			if(!hibernateSession.isOpen()) throw new RuntimeException("Attempted to close an already closed session!");
			try {
				hibernateSession.getTransaction().commit();
				hibernateSession.flush();
				hibernateSession.setFlushMode(FlushMode.AUTO);
			} catch (Throwable t) {
			try {
				if (hibernateSession.getTransaction().isActive()) {
					logger.info("Trying to rollback database transaction after exception");
					hibernateSession.getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				logger.error("Could not rollback transaction after exception!",
						rbEx);
			}
		} finally { 
			hibernateSession.close(); 
		}
	}
	
}
