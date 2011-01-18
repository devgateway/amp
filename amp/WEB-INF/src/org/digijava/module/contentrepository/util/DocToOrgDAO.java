package org.digijava.module.contentrepository.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.contentrepository.action.DocToOrgAction;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.hibernate.Query;
import org.hibernate.Session;

public class DocToOrgDAO {
	private static Logger logger	= Logger.getLogger(DocToOrgDAO.class);
	public static void saveObject(CrDocumentsToOrganisations obj) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			hbSession.saveOrUpdate(obj);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<AmpOrganisation> getOrgsObjByUuid(String uuid) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select dto.ampOrganisation from "	+ CrDocumentsToOrganisations.class.getName() + " dto where " +
					"dto.uuid=:uuid";
			Query query			= hbSession.createQuery(queryString);
			query.setString("uuid", uuid );
			List<AmpOrganisation> resultList	=  query.list();
			return resultList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<CrDocumentsToOrganisations> getDocToOrgObjsByUuid(String uuid) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select dto from "	+ CrDocumentsToOrganisations.class.getName() + " dto where " +
					"dto.uuid=:uuid";
			Query query			= hbSession.createQuery(queryString);
			query.setString("uuid", uuid );
			List<CrDocumentsToOrganisations> resultList	=  query.list();
			return resultList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static CrDocumentsToOrganisations getDocToOrgObjByUuid(Long id) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			return (CrDocumentsToOrganisations) hbSession.load(CrDocumentsToOrganisations.class, id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<CrDocumentsToOrganisations> getAll() {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select dto from "	+ CrDocumentsToOrganisations.class.getName() + " dto ";
			Query query			= hbSession.createQuery(queryString);
			List<CrDocumentsToOrganisations> resultList	=  query.list();
			return resultList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void deleteDocToOrgObjs(String uuid, Long ampOrganisationId) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "delete from "	+ CrDocumentsToOrganisations.class.getName() + " dto where " +
					"dto.uuid=:uuid and dto.ampOrganisation=:ampOrganisationId";
			Query query			= hbSession.createQuery(queryString);
			query.setString("uuid", uuid );
			query.setLong("ampOrganisationId", ampOrganisationId);
			int result	= query.executeUpdate();
			
			if (result == 0) {
				logger.error("No object was deleted. That's an error.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
