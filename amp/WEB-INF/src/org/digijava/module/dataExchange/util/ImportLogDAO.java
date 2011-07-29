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
					"lpe.deSourceSetting=:deSourceSettingId";
			Query query			= hbSession.createQuery(queryString);
			query.setLong("deSourceSettingId", sourceSettingId );
			List<DELogPerExecution> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public List<DELogPerExecution> getAmpLogPerExectutionObjsBySourceSetting(Long sourceSettingId, String sortBy,String sortDir) {
		try{
			String queryString 	= "select lpe from "	+ DELogPerExecution.class.getName() + " lpe where " +
					"lpe.deSourceSetting=:deSourceSettingId";
			//sort
			if(sortBy!=null && sortDir!=null){
				if (sortBy.equals("dbId") && sortDir.equals("asc")) {
					queryString += " order by lpe.id " ;
				} else if (sortBy.equals("dbId") && sortDir.equals("desc")) {
					queryString += " order by lpe.id desc " ;
				}else if(sortBy.equals("date") && sortDir.equals("asc")){
					queryString += " order by lpe.externalTimestamp ";
				}else if(sortBy.equals("date") && sortDir.equals("desc")){
					queryString += " order by lpe.externalTimestamp desc ";
				}else if(sortBy.equals("time") && sortDir.equals("asc")){
					queryString += " order by lpe.externalTimestamp ";
				}else if(sortBy.equals("time") && sortDir.equals("desc")){
					queryString += " order by lpe.externalTimestamp desc ";
				}
			}else{
				queryString += " order by lpe.id ";
			}
			Query query			= hbSession.createQuery(queryString);
			query.setLong("deSourceSettingId", sourceSettingId );
			List<DELogPerExecution> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public List<DELogPerItem> getAllAmpLogPerItem() {
		try{
			String queryString 	= "select lpi from "	+ DELogPerItem.class.getName() + " lpi";
			Query query			= hbSession.createQuery(queryString);
			List<DELogPerItem> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public List<DELogPerItem> getAmpLogPerItemObjsByExec(Long logPerExecId) {
		try{
			String queryString 	= "select lpi from "	+ DELogPerItem.class.getName() + " lpi where " +
					"lpi.deLogPerExecution=:logPerExecId";
			Query query			= hbSession.createQuery(queryString);
			query.setLong("logPerExecId", logPerExecId );
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
	
	public Object loadObject (Class cls, Long id) {
		try{
			return hbSession.load(cls, id);
		}
		finally {
			this.releaseSession();
		}
	}
	
	
}
