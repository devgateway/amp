package org.digijava.module.dataExchange.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.dataExchange.dbentity.DELogPerExecution;
import org.digijava.module.dataExchange.dbentity.DELogPerItem;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class ImportLogDAO {
	private static Logger logger = Logger.getLogger(ImportLogDAO.class);
	protected Session hbSession	;
	
	public ImportLogDAO () throws HibernateException, SQLException {
		this.hbSession	= PersistenceManager.getSession();
	}
	
	protected void releaseSession() {
		try {
			PersistenceManager.releaseSession(hbSession);
		} catch (Exception ex2) {
			logger.error("releaseSession() failed :" + ex2);
		}
	}
	
	public List<DELogPerExecution> getAllAmpLogPerExecutionObjects() {
		try{
			String queryString 	= "select lpe from "	+ DELogPerExecution.class.getName() + " lpe";
			Query query			= hbSession.createQuery(queryString);
			List<DELogPerExecution> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	public List<DELogPerExecution> getAmpLogPerExectutionObjsBySourceSetting(DESourceSetting ss) {
		return this.getAmpLogPerExectutionObjsBySourceSetting(ss.getId() );
	}
	public List<DELogPerExecution> getAmpLogPerExectutionObjsBySourceSetting(Long sourceSettingId) {
		try{
			String queryString 	= "select lpe from "	+ DELogPerExecution.class.getName() + " lpe where " +
					"lpe.ampSourceSetting=:ampSourceSettingId";
			Query query			= hbSession.createQuery(queryString);
			query.setLong("ampSourceSettingId", sourceSettingId );
			List<DELogPerExecution> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public List<DELogPerItem> getAllAmpLogPerItemByExecution(Long ampLogPerExecutionId) {
		try{
			String queryString 	= "select lpi from "	+ DELogPerItem.class.getName() + " lpi where lpi.executionInstance=:executionInstanceId";
			Query query			= hbSession.createQuery(queryString);
			query.setLong("executionInstanceId", ampLogPerExecutionId );
			List<DELogPerItem> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public void saveObject(Object object) {
		try{
			this.hbSession.saveOrUpdate(object);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			this.releaseSession();
		}
	}
	
	
}
