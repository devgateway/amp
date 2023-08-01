package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAhsurveyIndicator;
import org.digijava.module.aim.dbentity.AmpAhsurveyQuestion;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

import java.util.Collection;
import java.util.Iterator;


/**
 * Utility class for persisting all Paris indicators related entities
 * @author govind
 */

public class ParisUtil {
    private static Logger logger = Logger.getLogger(ParisUtil.class);

    
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
        return col;
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
        return col;
    }
    
    /*
     * This is to delete a Paris Indicator Question
     */
    public static void deleteQuestion(Long qid)
    {
        Session session = null;
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
        return piInd;
    }
}
