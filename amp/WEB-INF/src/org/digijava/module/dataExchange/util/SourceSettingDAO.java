package org.digijava.module.dataExchange.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SourceSettingDAO {
	private static Logger logger = Logger.getLogger(SourceSettingDAO.class);
	protected Session hbSession	;
	
	public SourceSettingDAO () throws HibernateException, SQLException {
		this.hbSession	= PersistenceManager.getSession();
	}
	
	protected void releaseSession() {
		try {
			PersistenceManager.releaseSession(hbSession);
		} catch (Exception ex2) {
			logger.error("releaseSession() failed :" + ex2);
		}
	}
	
	public List<DESourceSetting> getAllAmpSourceSettingsObjects() {
		try{
			String queryString 	= "select ss from "	+ DESourceSetting.class.getName() + " ss";
			Query query			= hbSession.createQuery(queryString);
			List<DESourceSetting> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public int getAllAmpSourceSettingsObjectsCount() {
		try{
			String queryString 	= "select count(*) from "	+ DESourceSetting.class.getName() + " ss";
			Query query			= hbSession.createQuery(queryString);
			int resultList	=  (Integer)query.uniqueResult();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public List<DESourceSetting> getPagedAmpSourceSettingsObjects(int fromIndex, String sortBy,String sortDir) {
		try{
			String queryString 	= "select ss from "	+ DESourceSetting.class.getName() + " ss";
			//sort
			if(sortBy!=null && sortDir!=null){
				if (sortBy.equals("Name") && sortDir.equals("asc")) {
					queryString += " order by ss.name " ;
				} else if (sortBy.equals("Name") && sortDir.equals("desc")) {
					queryString += " order by ss.name desc " ;
				}else if(sortBy.equals("Source") && sortDir.equals("asc")){
					queryString += " order by ss.source ";
				}else if(sortBy.equals("Source") && sortDir.equals("desc")){
					queryString += " order by ss.source desc ";
				}else if(sortBy.equals("Workspace") && sortDir.equals("asc")){
					queryString += " order by ss.importWorkspace.name ";
				}else if(sortBy.equals("Workspace") && sortDir.equals("desc")){
					queryString += " order by ssimportWorkspace.name desc ";
				}
			}
			Query query			= hbSession.createQuery(queryString);
			query.setFirstResult(fromIndex);
			query.setMaxResults(10);
//			if(resultNum!=-1){
//				query.setMaxResults(resultNum);
//			}
			List<DESourceSetting> resultList	=  query.list();
			return resultList;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public DESourceSetting getSourceSettingById(Long id) {
		try{
			DESourceSetting ret	= (DESourceSetting)hbSession.load(DESourceSetting.class, id);
			return ret;
		}
		finally {
			this.releaseSession();
		}
	}
	
	public void deleteObject(Long id) {
		try{
			Transaction rx 	= hbSession.beginTransaction();
			Object obj		= this.hbSession.load(DESourceSetting.class, id);
			this.hbSession.delete(obj);
			rx.commit();
			hbSession.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			this.releaseSession();
		}
	}
	
	public void saveObject(Object object) {
		try{
			Transaction rx = hbSession.beginTransaction();
			this.hbSession.saveOrUpdate(object);
			rx.commit();
			hbSession.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			this.releaseSession();
		}
	}
	
}
