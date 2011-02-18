/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.dgfoundation.amp.onepager.OnePagerApp;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;

/**
 * 
 * @author aartimon@dginternational.org
 * @since Feb 17, 2011
 */
public class PersistentObjectModel<T> extends LoadableDetachableModel<T>{

	private static Logger logger = Logger.getLogger(PersistentObjectModel.class);
	private static final long serialVersionUID = 1L;
	private Class _clazz;
	private Serializable _id;
	
	private PersistenceManager _myDao;
	
	private void setPrivateFields(T object){
		if (object instanceof HibernateProxy)
			_clazz = ((HibernateProxy)object).getHibernateLazyInitializer().getImplementation().getClass();
		else
			_clazz = object.getClass();
		
		//_id = ((IPersistentObject)object).getId();
		
		Session session;
		try {
			session = PersistenceManager.getRequestDBSession();
			String idProperty = session.getSessionFactory()
			.getClassMetadata(_clazz)
			.getIdentifierPropertyName();
			
			Method method = _clazz.getMethod("get" + Strings.capitalize(idProperty));
			Object result = method.invoke(object);
			_id = (Serializable)result;
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public PersistentObjectModel(final T object) {
		super(object);
		
		setPrivateFields(object);
		
		//InjectorHolder.getInjector().inject(this);
	}
	
	public PersistentObjectModel() {
		_id = null;
		_clazz = null;
	}
	public PersistentObjectModel(final Class clazz, final Serializable id) {
		_clazz = clazz;
		_id = id;
		
		//InjectorHolder.getInjector().inject(this);
	}
	
	@Override
	protected T load() {
		if (_id == null)
			return null;
		
		try {
			return (T) _myDao.getRequestDBSession().load(_clazz, _id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public T getObject() {
		return super.getObject();
	}
	
	public void setObject(T object) {
		setPrivateFields(object);
		super.setObject(object);
	}

	@Override
	public void detach() {
		super.detach();
	}
}
