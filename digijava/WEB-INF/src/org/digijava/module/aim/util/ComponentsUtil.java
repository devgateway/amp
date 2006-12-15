package org.digijava.module.aim.util;
/*
 * @author Govind G Dalwani
 */
import java.util.*;

import org.apache.log4j.*;
import org.digijava.kernel.persistence.*;
import org.digijava.module.aim.dbentity.*;
import net.sf.hibernate.*;

public class ComponentsUtil{

	private static Logger logger = Logger.getLogger(ComponentsUtil.class);

	public static Collection getAmpComponents()
	{
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select distinct co from "+AmpComponent.class.getName()+" co order by co.title";
			qry = session.createQuery(queryString);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Components  from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}
	public static Collection getComponentForEditing(Long id)
	{
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponent.class.getName()+" co where co.ampComponentId=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

	/*
	 * update component details
	 */
	public static void updateComponents(AmpComponent ampComp) {
		DbUtil.update(ampComp);
	}

	/*
	 * add a new Component
	 */
	public static void addNewComponent(AmpComponent ampComp){
			DbUtil.add(ampComp);

	}

	/*
	 * delete a Component
	 */
	public static void deleteComponent(Long compId)
	{

		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpComponent ampComp = (AmpComponent) session.load(
					AmpComponent.class,compId);
			logger.info("here123");
			tx = session.beginTransaction();
			logger.info("here");
			session.delete(ampComp);
			tx.commit();
		}
		catch (Exception e)
		{
			logger.error("Exception from deleteComponent() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception trbf)
				{
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null)
			{
				try
				{
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf)
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}
	
	/*
	 * To get the Component Fundings from the ampComponentFundings Table
	 * parameter passed is the component id
	 */
	public static Collection getComponentFunding(Long id)
	{
		logger.info(" getting the components funding details................");
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpComponentFunding.class.getName()+" co where co.amp_component_id=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}
	
	/*
	 * To get the physical progress for a component from the physical progress table...
	 * parameter passed is the amp component id
	 */
	public static Collection getComponentPhysicalProgress(Long id)
	{
		logger.info(" getting the components funding details................");
		Collection col = null;
		String queryString=null;
		Session session = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select co from "+AmpPhysicalPerformance.class.getName()+" co where co.amp_component_id=:id";
			qry = session.createQuery(queryString);
			qry.setParameter("id",id,Hibernate.LONG);

			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.error("Unable to get Component for editing from database " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch (Exception ex2)
			{
				logger.error("releaseSession() failed ");
			}
		}
		return col;
	}

}
