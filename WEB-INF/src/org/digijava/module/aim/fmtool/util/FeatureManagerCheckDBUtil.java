package org.digijava.module.aim.fmtool.util;

import java.util.*;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.*;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.*;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.fmtool.dbentity.AmpFeatureSource;
import org.digijava.module.aim.fmtool.types.*;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.*;
import org.hibernate.criterion.*;

public class FeatureManagerCheckDBUtil {
	private static Logger logger = Logger
			.getLogger(FeatureManagerCheckDBUtil.class);

	//################ get FME

	public static Map<Long, FMVisibilityWrapper> getAllAmpModulesVisibility() throws DgException {
		return getAllFMVisibility("amp_modules_visibility", FMToolConstants.FEATURE_TYPE_MODULE);
	}

	public static Map<Long, FMVisibilityWrapper> getAllAmpFeaturesVisibility()  throws DgException{
		return getAllFMVisibility("amp_features_visibility", FMToolConstants.FEATURE_TYPE_FEATURE);
	}	

	public static Map<Long, FMVisibilityWrapper> getAllAmpFieldsVisibility() throws DgException {
		return getAllFMVisibility("amp_fields_visibility", FMToolConstants.FEATURE_TYPE_FIELD);
	}	
	
	private static  Map<Long, FMVisibilityWrapper> getAllFMVisibility(String tableName, String type) throws DgException {
		Map<Long, FMVisibilityWrapper> retValue = null;

		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			List<Object[]> sqlResult = session.createSQLQuery("SELECT id,name,hasLevel,parent,description FROM " + tableName 
					+ " ")
//					+ " where id > 9000 ")
				.addScalar("id", Hibernate.LONG)
				.addScalar("name", Hibernate.STRING)
				.addScalar("hasLevel", Hibernate.BOOLEAN)
				.addScalar("parent", Hibernate.LONG)
				.addScalar("description", Hibernate.STRING).list();

			retValue = new HashMap<Long, FMVisibilityWrapper>();
			for (Object[] obj : sqlResult) {
				FMVisibilityWrapper fvw = new FMVisibilityWrapper();
				fvw.setValues(obj,type);
				retValue.put(fvw.getId(), fvw);
			}			
			
		} catch (HibernateException e) {
			logger.debug("Exception from getAllFMVisibility", e);
			throw new DgException(e);
		}
		return retValue;

	}
	
	public static List<FMSourceWrapper> getAllFeatureSources() throws DgException{
		List<FMSourceWrapper> retValue = null;

		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			List<Object[]> sqlResult = session.createSQLQuery("SELECT amp_feature_source_id,column_type,name,feature_id FROM amp_feature_source")
				.addScalar("amp_feature_source_id", Hibernate.LONG)
				.addScalar("column_type", Hibernate.STRING)
				.addScalar("name", Hibernate.STRING)
				.addScalar("feature_id", Hibernate.LONG).list();

			retValue = new ArrayList<FMSourceWrapper>();
			for (Object[] obj : sqlResult) {
				FMSourceWrapper fsw = new FMSourceWrapper(obj);
				retValue.add(fsw);
			}			
			
		} catch (HibernateException e) {
			logger.debug("Exception from getAllFeatureSources", e);
			throw new DgException(e);
		}
		return retValue;		
	}
	
	
	//################ get duplication

	public static List<FMDuplicatesWrapper> getModulesDuplicates() throws DgException{
		return getFMDuplicates("amp_modules_visibility", FMToolConstants.FEATURE_TYPE_MODULE);
	}

	public static List<FMDuplicatesWrapper> getFeaturesDuplicates() throws DgException{
		return getFMDuplicates("amp_features_visibility", FMToolConstants.FEATURE_TYPE_FEATURE);
	}	

	public static List<FMDuplicatesWrapper> getFieldsDuplicates() throws DgException{
		return getFMDuplicates("amp_fields_visibility", FMToolConstants.FEATURE_TYPE_FIELD);
	}	
	
	private static  List<FMDuplicatesWrapper> getFMDuplicates(String tableName, String type) throws DgException {
		List<FMDuplicatesWrapper> retValue = null;

		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			List<Object[]> sqlResult = session.createSQLQuery("SELECT name, count(*) count FROM  "+ tableName + " group by name ")
				.addScalar("name", Hibernate.STRING)
				.addScalar("count", Hibernate.LONG).list();

			retValue = new ArrayList<FMDuplicatesWrapper>();
			for (Object[] obj : sqlResult) {
				Long count = (Long)obj[1];
				if (count > 1){
					String name = (String)obj[0];
					FMDuplicatesWrapper fvw = new FMDuplicatesWrapper(name, count,type);
					retValue.add(fvw);
				}
			}			
			
		} catch (HibernateException e) {
			logger.debug("Exception from getFMDuplicates", e);
			throw new DgException(e);
		}
		return retValue;

	}	
	
	
	//################ delete FME
	public static void deleteModulesByNames(String[] names, boolean deleteAll) throws DgException{
		deleteFMEByNames(names, AmpModulesVisibility.class, deleteAll);
	}
	public static void deleteFeaturesByNames(String[] names, boolean deleteAll) throws DgException{
		deleteFMEByNames(names, AmpFeaturesVisibility.class, deleteAll);
	}
	public static void deleteFieldsByNames(String[] names, boolean deleteAll) throws DgException{
		deleteFMEByNames(names, AmpFieldsVisibility.class, deleteAll);
	}

	private static void deleteFMEByNames(String[] names, Class clazz, boolean deleteAll) throws DgException{
        Transaction tx = null;
		Session session = null;
		
		if (names != null && names.length > 0)
		try {
			session = PersistenceManager.getRequestDBSession();
			
			Criteria criteria = session.createCriteria(clazz);
			Criterion crit = Restrictions.eq("name", names[0]);
			
			for (int i = 1; i < names.length; i++) {
				crit = Restrictions.or(crit, Restrictions.eq("name", names[i]));
			}
			List qurResult = criteria.add(crit).list();
			
            tx = session.beginTransaction();
			
			for (Object obj : qurResult) {
				AmpObjectVisibility item = (AmpObjectVisibility)obj;

				if (!deleteAll){
				// ######### if FME has one child we do not remove it. 
					if (item instanceof AmpModulesVisibility){
						if ((item.getItems() !=null && item.getItems().size() > 0 ) || 
						(((AmpModulesVisibility) item).getSubmodules() != null && ((AmpModulesVisibility) item).getSubmodules().size()>0)){
							continue;
						}
					} else if (item instanceof AmpFeaturesVisibility){
						if (item.getItems() !=null && item.getItems().size() > 0 ){
								continue;
						}
					} else if (item instanceof AmpFieldsVisibility){
						if (item.getItems() !=null && item.getItems().size() > 0 ){
							continue;
						}
					}
				}
				
				AmpObjectVisibility parent = ((AmpObjectVisibility)item).getParent();
				if (parent != null){
					if (item instanceof AmpModulesVisibility){
						((AmpModulesVisibility)parent).getSubmodules().remove(item);
					} else if (item instanceof AmpFeaturesVisibility){
						parent.getItems().remove(item);
					} else if (item instanceof AmpFieldsVisibility){
						parent.getItems().remove(item);
					}
				}
		
				session.delete(item);
			}

			tx.commit();
			
		} catch (HibernateException ex) {
			logger.debug("Exception from fixModulesDuplicates", ex);
			throw new DgException(ex);
		} finally{ 
            if (tx != null && !tx.wasCommitted()) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.error("rollback() failed", ex);
                }
            }				
		}
	}
	

	//############## check if parent present for features and fields
	public static List<FMProblemWrapper> getFeaturesCheckParent() throws DgException{
		return getFMCheckParent("amp_features_visibility", FMToolConstants.FEATURE_TYPE_FEATURE);
	}	

	public static List<FMProblemWrapper> getFieldsCheckParent() throws DgException{
		return getFMCheckParent("amp_fields_visibility", FMToolConstants.FEATURE_TYPE_FIELD);
	}	
	
	
	private static  List<FMProblemWrapper> getFMCheckParent(String tableName, String type) throws DgException {
		List<FMProblemWrapper> retValue = null;

		Session session = null;

		try {
			session = PersistenceManager.getRequestDBSession();

			List<Object[]> sqlResult = session.createSQLQuery("SELECT name, id FROM  "+ tableName + " WHERE parent is null ")
				.addScalar("name", Hibernate.STRING)
				.addScalar("id", Hibernate.LONG).list();

			retValue = new ArrayList<FMProblemWrapper>();
			for (Object[] obj : sqlResult) {
				FMProblemWrapper fvw = new FMProblemWrapper((Long)obj[1], (String)obj[0], type, "Parent not present");
				retValue.add(fvw);
			}			
			
		} catch (HibernateException e) {
			logger.debug("Exception from getFMDuplicates", e);
			throw new DgException(e);
		}
		return retValue;		
	}

	
	public static void clearModuleSource(Long id){
		if (id != null)
			clearSource(" where f.module_id = "+ id.toString());
	}

	public static void clearFeatureSource(Long id){
		if (id != null)
			clearSource(" where f.feature_id = "+ id.toString());
	}

	public static void clearFieldSource(Long id){
		if (id != null)
			clearSource(" where f.field_id = "+ id.toString());
	}

	public static void clearAllSource(){
		clearSource("");
	}
	
	private static void clearSource(String where){
		Session session = null;
        Transaction tx = null;
		
		String qryStr = null;
		Query qry = null;

		try {
			session = PersistenceManager.getRequestDBSession();
			qryStr = "select f from " + AmpFeatureSource.class.getName() + " f " + where;
			qry = session.createQuery(qryStr);
			List qResult = qry.list();
			
            tx = session.beginTransaction();
			for (Object obj : qResult) {
				session.delete(obj);
			}
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally{ 
	        if (tx != null && !tx.wasCommitted()) {
	            try {
	                tx.rollback();
	            } catch (HibernateException ex) {
	                logger.error("rollback() failed", ex);
	            }
	        }				
		}
	}
}
