package org.digijava.module.dataExchange.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.dataExchange.dbentity.DESourceSetting;
import org.digijava.module.dataExchange.utils.DEConstants;
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
		
	public List<DESourceSetting> getAllAmpSourceSettingsObjects() {

			String queryString 	= "select ss from "	+ DESourceSetting.class.getName() + " ss";
			Query query			= hbSession.createQuery(queryString);
			List<DESourceSetting> resultList	=  query.list();
			return resultList;
	}
	
	public int getAllAmpSourceSettingsObjectsCount() {
			String queryString 	= "select count(*) from "	+ DESourceSetting.class.getName() + " ss";
			Query query			= hbSession.createQuery(queryString);
			int resultList	=  (Integer)query.uniqueResult();
			return resultList;
	}
	
	public List<DESourceSetting> getPagedAmpSourceSettingsObjects(int fromIndex, String sortBy) {
			String queryString 	= "select ss from "	+ DESourceSetting.class.getName() + " ss";
			//sort
			if(sortBy!=null){
				if (sortBy.equals("name")) {
					queryString += " order by ss.name " ;
				} else if (sortBy.equals("name_desc")) {
					queryString += " order by ss.name desc " ;
				}else if(sortBy.equals("source")){
					queryString += " order by ss.source ";
				}else if(sortBy.equals("source_desc")){
					queryString += " order by ss.source desc ";
				}else if(sortBy.equals("workspace")){
					queryString += " order by ss.importWorkspace.name ";
				}else if(sortBy.equals("workspace_desc")){
					queryString += " order by ss.importWorkspace.name desc ";
				}
			}else{
				queryString += " order by ss.name " ;
			}
			Query query			= hbSession.createQuery(queryString);
			query.setFirstResult(fromIndex);
			query.setMaxResults(DEConstants.RECORDS_AMOUNT_PER_PAGE);
//			if(resultNum!=-1){
//				query.setMaxResults(resultNum);
//			}
			List<DESourceSetting> resultList	=  query.list();
			return resultList;
	}
	
	public DESourceSetting getSourceSettingById(Long id) {
			DESourceSetting ret	= (DESourceSetting)hbSession.load(DESourceSetting.class, id);
			return ret;
	}
	
	public static DESourceSetting getSourceSettingByName(String name){
		  Session session = null;
	      Query qry = null;
	      DESourceSetting result=null;
		try{
			session = PersistenceManager.getSession();
			String queryString 	= "select ss from "	+ DESourceSetting.class.getName() + " ss where ss.name=:name";
			qry = session.createQuery(queryString);
			qry.setString("name", name);
			result = (DESourceSetting)qry.uniqueResult();
		}catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}
	
	public void deleteObject(Long id) {
		try{
//beginTransaction();
			Object obj		= this.hbSession.load(DESourceSetting.class, id);
			this.hbSession.delete(obj);
			//rx.commit();
//session.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveObject(Object object) {
		try{
//beginTransaction();
			this.hbSession.saveOrUpdate(object);
			//rx.commit();
//session.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
