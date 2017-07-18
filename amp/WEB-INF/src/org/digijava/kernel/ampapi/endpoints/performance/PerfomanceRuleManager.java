package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerfomanceRuleManager {

    private static PerfomanceRuleManager performanceRuleManager;
    
    private static Logger logger = Logger.getLogger(PerfomanceRuleManager.class);

    /**
     * 
     * @return PerfomanceRuleManager instance
     */
    public static PerfomanceRuleManager getInstance() {
        if (performanceRuleManager == null) {
            performanceRuleManager = new PerfomanceRuleManager();
        }

        return performanceRuleManager;
    }

    public List<AmpPerformanceRule> getAllPerformanceRules() {

        Session dbSession = PersistenceManager.getSession();
        String queryString = "SELECT rule FROM " + AmpPerformanceRule.class.getName() + " rule";
        Query query = dbSession.createQuery(queryString);

        return query.list();
    }

    public AmpPerformanceRule getPerformanceRuleById(Long id) {
    	AmpPerformanceRule performanceRule = 
    			(AmpPerformanceRule) PersistenceManager.getSession().get(AmpPerformanceRule.class, id);
    	
    	if (performanceRule == null) {
    		throw new PerformanceRuleException("Performance rule not found with id " + id);
    	}
    	
        return performanceRule;
    }
    
    public AmpCategoryValue requireCategoryValueExists(Long id) {
    	AmpCategoryValue acv = (AmpCategoryValue) PersistenceManager.getSession().get(AmpCategoryValue.class, id);
    	
    	if (acv == null) {
    		throw new PerformanceRuleException("Level category not found with id " + id);
    	}
    	
        return acv;
    }
    
    public void updatePerformanceRule(AmpPerformanceRule performanceRule) {
    	getPerformanceRuleById(performanceRule.getId());
    	
    	if (performanceRule.getLevel() == null) {
    		throw new PerformanceRuleException("Level category cannot be null");
    	}
    	
    	requireCategoryValueExists(performanceRule.getLevel().getId());
        
    	try {
            Session session = PersistenceManager.getSession();
            session.merge(performanceRule);
        } catch (Exception e) {
            logger.error("Exception from savePerformanceRule: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void savePerformanceRule(AmpPerformanceRule performanceRule) {
    	
    	if (performanceRule.getLevel() == null) {
    		throw new PerformanceRuleException("Level category cannot be null");
    	}
    	
    	requireCategoryValueExists(performanceRule.getLevel().getId());
    	
        try {
            Session session = PersistenceManager.getSession();
            session.saveOrUpdate(performanceRule);
        } catch (Exception e) {
            logger.error("Exception from savePerformanceRule: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletePerformanceRule(Long id) {
        AmpPerformanceRule performanceRule = getPerformanceRuleById(id);

        try {
            Session session = PersistenceManager.getSession();
            session.delete(performanceRule);
        } catch (HibernateException e) {
            logger.error("Exception from deletePerformanceRule: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

	public PerformanceRulesAdminPage<AmpPerformanceRule> getAdminPage(long page, long size) {
		List<AmpPerformanceRule> allPerformanceRules = getAllPerformanceRules();

		long recordsPerPage = size > 0 ? size : PerformanceRuleConstants.DEFAULT_RECORDS_PER_PAGE;
		long start = page > 0 ? (page - 1) * 0 : PerformanceRuleConstants.DEFAULT_START_PAGE;

		List<AmpPerformanceRule> pagePerformanceRules = null;

		if (allPerformanceRules.size() > 0) {
			pagePerformanceRules = allPerformanceRules.stream()
					.skip(start)
					.limit(recordsPerPage)
			        .collect(Collectors.toList());
		}

		PerformanceRulesAdminPage<AmpPerformanceRule> adminPage = new PerformanceRulesAdminPage<>();
		adminPage.setPerformanceRules(pagePerformanceRules);
		adminPage.setTotalRecords(allPerformanceRules.size());

		return adminPage;
	}
}
