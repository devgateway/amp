/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel extends LoadableDetachableModel<AmpActivityVersion> implements IModel<AmpActivityVersion>  {
	private static Logger logger = Logger.getLogger(AmpActivityModel.class);
	
	
	protected transient AmpActivityVersion a;
	protected Long id;
	protected transient Session session;
	protected transient Transaction transaction;

	private static final long serialVersionUID = 3915904820384659360L;

	
	
	public AmpActivityModel() {
		resetSession();
		a = new AmpActivityVersion();
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		a.setActivityCreator(s.getAmpCurrentMember());
		a.setCreatedBy(s.getAmpCurrentMember());
	}	
	
	public AmpActivityModel(Long id) {
		resetSession();
		
		this.id = id;
		this.a = null;
	}
	
	public void resetSession() {
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		s.setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, null);
		s.setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, null);
		s.setMetaData(OnePagerConst.EDITOR_ITEMS, null);
	}
	
	protected AmpActivityVersion load() {
		resetSession();

		AmpActivityVersion ret = ActivityUtil.load(this, id);

		return ret;
	}

	@Override   
	public void setObject(AmpActivityVersion arg0) {
		//if(a==null) a=(AmpActivityVersion) arg0;
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

	protected void initDBSession() {
		try {
			session = PersistenceManager.getSession();
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Session getSession() {
		if(session==null) 
			initDBSession();
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Transaction getTransaction() {
		if (transaction==null){
			logger.error("Transaction was null!");
			transaction = session.beginTransaction();
		}
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
