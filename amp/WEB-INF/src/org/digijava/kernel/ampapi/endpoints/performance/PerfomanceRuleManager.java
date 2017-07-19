package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

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

        Session session = PersistenceManager.getSession();

        return session.createCriteria(AmpPerformanceRule.class)
                .addOrder(Order.asc("id"))
                .list();
    }

    public AmpPerformanceRule getPerformanceRuleById(Long id) {
        AmpPerformanceRule performanceRule = (AmpPerformanceRule) PersistenceManager.getSession()
                .get(AmpPerformanceRule.class, id);

        if (performanceRule == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.PERFORMANCE_RULE_INVALID, String.valueOf(id));
        }

        return performanceRule;
    }

    public AmpCategoryValue requireCategoryValueExists(Long id) {
        AmpCategoryValue acv = (AmpCategoryValue) PersistenceManager.getSession().get(AmpCategoryValue.class, id);

        if (acv == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.CATEGORY_VALUE_INVALID, String.valueOf(id));
        }

        return acv;
    }

    public void updatePerformanceRule(AmpPerformanceRule performanceRule) {
        getPerformanceRuleById(performanceRule.getId());

        if (performanceRule.getLevel() == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.REQUIRED_ATTRIBUTE, "level");
        }

        requireCategoryValueExists(performanceRule.getLevel().getId());

        Session session = PersistenceManager.getSession();
        session.merge(performanceRule);
    }

    public void savePerformanceRule(AmpPerformanceRule performanceRule) {

        if (performanceRule.getLevel() == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.REQUIRED_ATTRIBUTE, "level");
        }

        requireCategoryValueExists(performanceRule.getLevel().getId());

        Session session = PersistenceManager.getSession();
        session.saveOrUpdate(performanceRule);
    }

    public void deletePerformanceRule(Long id) {
        AmpPerformanceRule performanceRule = getPerformanceRuleById(id);

        Session session = PersistenceManager.getSession();
        session.delete(performanceRule);
    }

    public ResultPage<AmpPerformanceRule> getAdminPage(int page, int size) {
        List<AmpPerformanceRule> allPerformanceRules = getAllPerformanceRules();

        int recordsPerPage = size > 0 ? size : PerformanceRuleConstants.DEFAULT_RECORDS_PER_PAGE;
        int start = page > 0 ? (page - 1) * recordsPerPage : 0;
        
        Session session = PersistenceManager.getSession();

        List<AmpPerformanceRule> pagePerformanceRules = session.createCriteria(AmpPerformanceRule.class)
                .addOrder(Order.asc("id"))
                .setFirstResult(start)
                .setMaxResults(recordsPerPage)
                .list();;

        ResultPage<AmpPerformanceRule> adminPage = new ResultPage<>();
        adminPage.setPerformanceRules(pagePerformanceRules);
        adminPage.setTotalRecords(allPerformanceRules.size());

        return adminPage;
    }
}
