/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel implements IModel<AmpActivity> {
	private static Logger logger = Logger.getLogger(AmpActivityModel.class);
	
	protected transient AmpActivity a;
	protected Long id;
	protected transient Session session;
	protected transient Transaction transaction;
	public Session getSession() {
		return session;
	}

	private static final long serialVersionUID = 3915904820384659360L;

	
	public AmpActivityModel() {
		a = new AmpActivity();
		AmpAuthWebSession s =  (AmpAuthWebSession) org.apache.wicket.Session.get();
		a.setActivityCreator(s.getAmpCurrentMember());
		a.setCreatedBy(s.getAmpCurrentMember());
		a.setCategories(new HashSet());
		a.setInternalIds(new HashSet());
		//a.setRegionalFundings(new HashSet());
		a.setLocations(new HashSet());
		a.setActPrograms(new HashSet());
		a.setSectors(new HashSet());
		a.setFunding(new HashSet());
		a.setOrgrole(new HashSet());
		a.setComponents(new HashSet());
		a.setIssues(new HashSet());
		a.setRegionalObservations(new HashSet());
		a.setActivityContacts(new HashSet());
		a.setMember(new HashSet());
	}	
	
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
		
		if (transaction==null){
			logger.error("Transaction was null!");
			transaction = session.beginTransaction();
		}
		
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
