package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.hibernate.Session;
import org.hibernate.Query;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class GPIUtils {
	private static Logger logger = Logger.getLogger(GPIUtils.class);
	
	public static AmpGPINiAidOnBudget getAidOnBudgetById(Long id){
		return (AmpGPINiAidOnBudget) PersistenceManager.getSession().get(AmpGPINiAidOnBudget.class, id);
	}
	
	public static List<AmpGPINiAidOnBudget> getAidOnBudgetList(){		
		Session dbSession = PersistenceManager.getSession();
        String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName() + " gpi";
        return dbSession.createQuery(queryString).list(); 
                  
	}
	
	public static AmpOrganisation getOrganisation(Long id){		
		return (AmpOrganisation) PersistenceManager.getSession().get(AmpOrganisation.class, id);		
	}
	
	public static void saveAidOnBudget(AmpGPINiAidOnBudget aidOnBudget) {		
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.saveOrUpdate(aidOnBudget);			
		} catch (Exception e) {
			logger.error("Exception from saveAidOnBudget: " + e.getMessage());
		}		
	}
	
	public static void delete(Long id) {	
		AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.delete(aidOnBudget);
		} catch (Exception e) {
			logger.error("Exception from saveAidOnBudget: " + e.getMessage());
		}		
	}
	
	public static boolean similarRecordExists(Date date, Long donorId){
		Session dbSession = PersistenceManager.getSession();	    		
        String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName() + " gpi where gpi.donorId.ampOrgId=:orgId and year(gpi.date)=:indicatorYear";
        Query query = dbSession.createQuery(queryString);
        query.setParameter("orgId", donorId);        
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
	    int year = calendar.get(Calendar.YEAR);	    
        query.setParameter("indicatorYear",year);        
        return query.list().size() > 0;                   
	}
}
