/**
 * 
 */
package org.dgfoundation.amp;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/**
 * @author mihai
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneAMPStartup {
	private static Logger logger = Logger.getLogger(StandaloneAMPStartup.class);
	
	
	  public static AmpActivity loadActivity(Long id, Session session) throws DgException {
		AmpActivity result = null;

		try {
//session.flush();
			result = (AmpActivity) session.get(AmpActivity.class, id);
			session.evict(result);
			result = (AmpActivity) session.get(AmpActivity.class, id);
		} catch (ObjectNotFoundException e) {
			logger.debug("AmpActivity with id=" + id + " not found");
		} catch (Exception e) {
			throw new DgException("Canno load AmpActivity with id" + id, e);
		}
		return result;
	}

	/**
	 * Generate 200,000 activity copies when AMP Starts, using XML Import/Export.
	 * @param session the Hibernate Session
	 * @param ampActivityId the id of the source AmpActivity
	 * @throws DgException
	 * @throws JAXBException
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private static void generate200k(Long ampActivityId,Session session) throws DgException, JAXBException, HibernateException, SQLException {
/*  AmpHarvester was removed from AMP
		AmpActivity activity = loadActivity(ampActivityId,session);				
		ActivityType xmlActivity = ExportManager.getXmlActivity(activity, session);
		
		
		ObjectFactory objFactory = new ObjectFactory();
		Activities aList=objFactory.createActivities();
		aList.getActivity().add(xmlActivity);
		
		xmlActivity.setAmpId("200K ");
		xmlActivity.getTitle().setValue("Generated Activity ");
		byte[] toByte = XmlTransformerHelper.marshalToByte(aList);
		
		String title=xmlActivity.getTitle().getValue();
		ImportManager im=new ImportManager(toByte);
		for(long i=1;i<200000;i++){ 
		im.startImportHttp(new String("#"+i), activity.getTeam());
		if((i % 100)==0) logger.info("Completed #"+i);
		}
		PersistenceManager.releaseSession(session);
*/				
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			
			DigiConfigManager.initialize("repository");
			PersistenceManager.initialize(false);
			
			
			
			//BELOW THIS LINE, HIBERNATE IS AVAILABLE, ADD YOUR SCRIPT INVOCATION HERE			
			
			
			
			try {
				//EXAMPLE OF A WORKING HIBERNATE SESSION OBJECT:
				Session session = PersistenceManager.getSession();
				
		 
				//generate200k((long)9,session);
				
				PersistenceManager.releaseSession(session);
				
				
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} 
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}

	}

}
