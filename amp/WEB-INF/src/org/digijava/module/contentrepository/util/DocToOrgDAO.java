package org.digijava.module.contentrepository.util;

import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.contentrepository.action.DocToOrgAction;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * utility class for Document-To-Organisations m2m handling
 */
public class DocToOrgDAO {
	private static Logger logger	= Logger.getLogger(DocToOrgDAO.class);
	public static void saveObject(CrDocumentsToOrganisations obj) {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			hbSession.saveOrUpdate(obj);
//session.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns a comma-separated list of the sorted names of the organisations of a Document
	 * @param uuid - the document uuid
	 * @return "a, b, c"
	 */
	public static String getOrganisationsAsString(String uuid){
		List<AmpOrganisation> orgs = getOrgsObjByUuid(uuid);
		if (orgs == null)
			return null;
		
		if (orgs.isEmpty())
			return null;
		
		SortedSet<String> names = new java.util.TreeSet<String>();
		for(AmpOrganisation org:orgs)
		{
			if (org.getName() != null)
				names.add(org.getName().trim());
			else
				logger.error("name of organisation " + org.getName() + " is null!");
		}
		
		StringBuilder buf = new StringBuilder();
		for(String name:names)
		{
			if (buf.length() > 0)
				buf.append(", ");
			buf.append(name);
		}
		return buf.toString();
	}
	
	/**
	 * returns the set of all the AmpOrgIds associated with a Document
	 * @param uuid - the document UUID
	 * @return
	 */
	public static java.util.Set<Long> getDocToOrgIdsByUUID(String uuid)
	{
		java.util.Set<Long> result = new java.util.HashSet<Long>();
		List<AmpOrganisation> orgs = getOrgsObjByUuid(uuid);
		if (orgs == null)
			return result; // empty
		for(AmpOrganisation org:orgs)
			result.add(org.getAmpOrgId());
		return result;
	}

	public static List<AmpOrganisation> getOrgsObjByUuid(String uuid) {
		Session hbSession;
		try{
            String uuidForPublicUUID = DocumentManagerUtil.getUUIDByPublicVersionUUID(uuid);


			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select dto.ampOrganisation from "	+ CrDocumentsToOrganisations.class.getName() + " dto where " +
					"dto.uuid=:uuid";
            if (uuidForPublicUUID != null) {
                queryString += " or dto.uuid=:uuidForPublic";
            }
			Query query			= hbSession.createQuery(queryString);
			query.setString("uuid", uuid );
            if (uuidForPublicUUID != null) {
                query.setString("uuidForPublic", uuidForPublicUUID );
            }
			List<AmpOrganisation> resultList	=  query.list();
			return resultList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * computes the list of all the used organisations in the current DB instance
	 * @return
	 */
	public static List<AmpOrganisation> getAllUsedOrganisations() {
		Session hbSession;
		try{
			hbSession	= PersistenceManager.getRequestDBSession();
			String queryString 	= "select dto.ampOrganisation from "	+ CrDocumentsToOrganisations.class.getName() + " dto";
			Query query			= hbSession.createQuery(queryString);
			List<AmpOrganisation> resultList =  query.list();
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
