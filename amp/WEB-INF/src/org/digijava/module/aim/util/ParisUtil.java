package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.ParisIndicatorReportHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;


/**
 * Utility class for persisting all Paris indicators related entities
 * @author govind
 */

public class ParisUtil {
	private static Logger logger = Logger.getLogger(ParisUtil.class);

	/*
	 * This function will return all the donors for the Paris Indicator Reports
	 */
	
	public static Collection getParisIndicatorReport1()
	{
		String queryString = null,queryQuestString = null;
		Session session = null;
		Collection col = new ArrayList();
		Query qry = null,quesqry=null;		
		try
		{
			session = PersistenceManager.getSession();				
			queryString ="select distinct pi.acronym from "+ AmpOrganisation.class.getName() +" pi, " +AmpFunding.class.getName() + " f where pi.ampOrgId = f.ampDonorOrgId and (pi.deleted is null or pi.deleted = false) ";
				qry = session.createQuery(queryString);
				Iterator itr1 = qry.list().iterator();
				while(itr1.hasNext())
				{
					ParisIndicatorReportHelper pi=new ParisIndicatorReportHelper();
					pi.setPiHelperDonorName((String)itr1.next());
					col.add(pi);
				}
		}		
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	 * This will return the Paris Indicators
	 */
	public static Collection getParisIndicators()
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;
		
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+AmpAhsurveyIndicator.class.getName()+" pi ";	
			qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	/**
	 * 
	 * @return
	 */
	public static Collection getParisIndicatorToBeEdited(Integer pid)
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;
		
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+AmpAhsurveyIndicator.class.getName()+" pi where pi.ampIndicatorId=:pid";	
			qry = session.createQuery(queryString);
			qry.setParameter("pid",pid,IntegerType.INSTANCE);
			col = qry.list();
		}
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	 * This is to get the Questions under each Paris Indicator
	 */
	public static Collection getParisQuestions(Integer pid)
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;
	
		try
		{
			session = PersistenceManager.getSession();
			queryString ="select pi from "+ AmpAhsurveyQuestion.class.getName()+" pi where pi.ampIndicatorId=:pid";
			qry = session.createQuery(queryString);
			qry.setParameter("pid",pid,IntegerType.INSTANCE);
			col = qry.list();
			
		}
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	 * This is to delete a Paris Indicator
	 */
	public static void deleteParisIndicator(Long pid)
	{
		Session session = null;
		Transaction tx = null;
		try 
		{
			session = PersistenceManager.getSession();
			AmpAhsurveyIndicator parisIndicator = 
				(AmpAhsurveyIndicator) session.load(AmpAhsurveyIndicator.class,pid);
//beginTransaction();
			session.delete(parisIndicator);
			//tx.commit();
		} 
		catch (Exception e) 
		{
			logger.error("Exception from deleteQIndicator() :" + e.getMessage());
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
	 * this function is to get the question for editing
	 */
	
	public static Collection getParisQuestionToBeEdited(Long qid)
	{
		String queryString = null;
		Session session = null;
		Collection col = null;
		Query qry = null;
		
	
		try
		{
			session = PersistenceManager.getSession();
			queryString = "select pi from "+ AmpAhsurveyQuestion.class.getName()+" pi where pi.ampQuestionId=:qid";
			qry = session.createQuery(queryString);
			qry.setParameter("qid",qid,LongType.INSTANCE);
			col = qry.list();
			
		}
		
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	 * this is to add a new Paris Indicator Question
	 */
	public static Collection addParisIndicatorQuestion()
	{
		Session session = null;
		Collection col = null;
		try
		{
			session = PersistenceManager.getSession();			
		}
		
		catch(Exception ex) 		
		{
			logger.error("Unable to get report names  from database " + ex.getMessage());
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
	 * This is to delete a Paris Indicator Question
	 */
	public static void deleteQuestion(Long qid)
	{
		Session session = null;
		Transaction tx = null;
		try 
		{
			session = PersistenceManager.getSession();
			AmpAhsurveyQuestion surveyQuestion = (AmpAhsurveyQuestion) session.load(
												AmpAhsurveyQuestion.class,qid);
//beginTransaction();
			session.delete(surveyQuestion);
			//tx.commit();
		} 
		catch (Exception e) 
		{
			logger.error("Exception from deleteQuestion() :" + e.getMessage());
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

	public static void deleteAhResponse(Long rId)
	{
		Session session = null;
		Transaction tx = null;
		try 
		{
			session = PersistenceManager.getSession();
			AmpAhsurveyResponse ahr = (AmpAhsurveyResponse) session.load(
					AmpAhsurveyResponse.class,rId);
//beginTransaction();
		 	session.delete(ahr);
			//tx.commit();
		} 
		catch (Exception e) 
		{
			logger.error("Exception from deleteahresponse() :" + e.getMessage());
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

	public static void deleteAhSurvey(Long sId)
	{
		Session session = null;
		Transaction tx = null;
		try 
		{
			session = PersistenceManager.getSession();
			AmpAhsurvey ahs = (AmpAhsurvey) session.load(
					AmpAhsurvey.class,sId);
			AmpOrganisation org=(AmpOrganisation) session.load(AmpOrganisation.class,ahs.getAmpDonorOrgId().getAmpOrgId());
			
//beginTransaction();
			
			ahs.getAmpDonorOrgId().getSurvey().remove(ahs);
			AmpActivityVersion act=ahs.getAmpActivityId();
			act.getSurvey().remove(ahs);
			ahs.setAmpActivityId(null);
			ahs.setPointOfDeliveryDonor(null);
			org.getSurvey().remove(ahs);
			
			//session.save(org);
		 	session.delete(ahs);
			//tx.commit();

		} 
		catch (Exception e) 
		{
			logger.error("Exception from deleteahsurvey() :" + e.getMessage());
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

	
	
	public static AmpAhsurveyIndicator findIndicatorId(String name,String code)
	{
		Session session = null;
		Query qry = null;
		Collection col = null;
		AmpAhsurveyIndicator piInd = null;
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ind from "
								+ AmpAhsurveyIndicator.class.getName() + " ind " 
								+ "where (name like '" + name + "' && indicator_code like '" + code + "')";
			qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				piInd = (AmpAhsurveyIndicator) itr.next();
			}
		}
		catch(Exception e)
		{
			logger.debug("UNABLE to find PI Indicators with the name,code.", e);
		}
		finally
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} 
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return piInd;
	}
}