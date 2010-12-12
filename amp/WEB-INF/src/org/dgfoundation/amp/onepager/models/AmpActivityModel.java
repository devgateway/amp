/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel implements IModel<AmpActivity> {
	protected transient AmpActivity a;
	protected Long id;
	protected transient Session session;
	protected transient Transaction transaction;
	public Session getSession() {
		return session;
	}

	private static final long serialVersionUID = 3915904820384659360L;

	public AmpActivityModel(Long id) {
		this.id = id;
		this.a = null;
	}
	
	protected void initSession() {
		try {
			session = PersistenceManager.getSession();
			
		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected AmpActivity load() {
		AmpActivity ret = null;
		if(session==null || !session.isOpen()) initSession();
		ret = (AmpActivity) session.load(AmpActivity.class, id);
		transaction=session.beginTransaction();
		return ret;
	}

	@Override   
	public void setObject(AmpActivity arg0) {
		if(a==null) a=arg0;
		if(session==null) initSession();
		session.saveOrUpdate(a);
		transaction.commit();
		transaction=session.beginTransaction();
	}

	@Override
	public void detach() {

	}

	@Override
	public AmpActivity getObject() {
		if (a == null)
			a = load();
		return a;
	}

}
