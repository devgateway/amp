/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.IndicatorUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mpostelnicu@dgateway.org since Sep 24, 2010
 */
public class AmpActivityModel implements IModel<AmpActivityVersion> {
	private static Logger logger = Logger.getLogger(AmpActivityModel.class);
	
	protected transient AmpActivityVersion a;
	protected Long id;
	protected transient Session session;
	protected transient Transaction transaction;
	public Session getSession() {
		return session;
	}

	private static final long serialVersionUID = 3915904820384659360L;

	
	public AmpActivityModel() {
		a = new AmpActivityVersion();
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

	protected AmpActivityVersion load() {
		AmpActivityVersion ret = null;
		if(session==null || !session.isOpen()) initSession();
		ret = (AmpActivityVersion) session.load(AmpActivityVersion.class, id);
		transaction=session.beginTransaction();
		return ret;
	}

	@Override   
	public void setObject(AmpActivityVersion arg0) {
 
		try {
			if(a==null) a=(AmpActivityVersion) arg0;
			if(session==null) initSession();
			
			if (transaction==null){
				logger.error("Transaction was null!");
				transaction = session.beginTransaction();
			}
			

			if (a.getAmpActivityId() != null){
				//cleanup old indicators
				Set<IndicatorActivity> old = IndicatorUtil.getAllIndicatorsForActivity(a.getAmpActivityId());
				
				if (old != null){
					Iterator<IndicatorActivity> it = old.iterator();
					while (it.hasNext()) {
						IndicatorActivity oldInd = (IndicatorActivity) it
						.next();
						
						boolean found=false;
						Iterator<IndicatorActivity> it2 = a.getIndicators().iterator();
						while (it2.hasNext()) {
							IndicatorActivity newind = (IndicatorActivity) it2
									.next();
							if (newind.getId().compareTo(oldInd.getId()) == 0){
								found=true;
								break;
							}
						}
						if (!found){
							Object tmp = session.load(IndicatorActivity.class, oldInd.getId());
							session.delete(tmp);
						}
					}
				}
				
			}
			
			Set<IndicatorActivity> inds = a.getIndicators();
			if (inds != null){
				Iterator<IndicatorActivity> it = inds.iterator();
				while (it.hasNext()) {
					IndicatorActivity ind = (IndicatorActivity) it
							.next();
					ind.setActivity(a);
					session.saveOrUpdate(ind);
				}
			}
			session.saveOrUpdate(a);
			transaction.commit();
			logger.error("New activity id=" + a.getAmpActivityId());
			transaction=session.beginTransaction();
		} catch (Exception exception) {
			logger.error(exception); // Log the exception
			if (exception instanceof SQLException) {
			   while(exception != null) {
			         // Get cause if present
			         Throwable t = exception.getCause();
			         while(t != null) {
			               logger.info("Cause:" + t);
			               t = t.getCause();
			         }
			         // procees to the next exception
			         exception = ((SQLException) exception).getNextException();
			         logger.error(exception);
			   }
			}
		}
		
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

}
