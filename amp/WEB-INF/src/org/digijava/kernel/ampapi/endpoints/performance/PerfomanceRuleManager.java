package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class PerfomanceRuleManager {

    private static PerfomanceRuleManager performanceRuleManager;
    
    private List<PerformanceRuleMatcherDefinition> definitions;
    
    private static final Logger logger = Logger.getLogger(PerfomanceRuleManager.class);
    
    private PerfomanceRuleManager() {
        initPerformanceRuleDefinitions();
    }

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
    
    private void initPerformanceRuleDefinitions() {
        definitions = new ArrayList<>();
        definitions.add(new NoUpdatedStatusAfterFundingDateMatcherDefinition());
        definitions.add(new NoDisbursementsAfterFundingDateMatcherDefinition());
        definitions.add(new DisbursementsAfterActivityDateMatcherDefinition());
        definitions.add(new NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition());
    }
    
    public List<PerformanceRuleMatcherDefinition> getPerformanceRuleDefinitions() {
        return definitions;
    }

    public AmpPerformanceRule getPerformanceRuleById(Long id) {
        AmpPerformanceRule performanceRule = (AmpPerformanceRule) PersistenceManager.getSession()
                .get(AmpPerformanceRule.class, id);

        if (performanceRule == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.RULE_INVALID, String.valueOf(id));
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

    public List<AmpPerformanceRule> getPerformanceRules() {

        Session session = PersistenceManager.getSession();

        return session.createCriteria(AmpPerformanceRule.class).addOrder(Order.asc("id")).list();
    }

    public ResultPage<AmpPerformanceRule> getPerformanceRules(int page, int size) {

        int recordsPerPage = size > 0 ? size : PerformanceRuleConstants.DEFAULT_RECORDS_PER_PAGE;
        int start = page > 0 ? (page - 1) * recordsPerPage : 0;

        Session session = PersistenceManager.getSession();

        List<AmpPerformanceRule> pagePerformanceRules = session.createCriteria(AmpPerformanceRule.class)
                .addOrder(Order.asc("id")).setFirstResult(start).setMaxResults(recordsPerPage).list();

        int totalRecords = (int) session.createCriteria(AmpPerformanceRule.class).setProjection(Projections.rowCount())
                .uniqueResult();

        ResultPage<AmpPerformanceRule> resultPage = new ResultPage<>();
        resultPage.setItems(pagePerformanceRules);
        resultPage.setTotalRecords(totalRecords);

        return resultPage;
    }

    public PerformanceRuleMatcherDefinition getMatcherDefinition(String type) {
        PerformanceRuleMatcherDefinition matcherDefiniton = getPerformanceRuleDefinitions().stream()
                .filter(m -> m.getName().equals(type)).findAny()
                .orElseThrow(() -> new PerformanceRuleException(PerformanceRulesErrors.RULE_TYPE_INVALID, type));

        return matcherDefiniton;
    }

    public List<PerformanceRuleMatcher> getPerformanceRuleMatchers() {
        List<AmpPerformanceRule> rules = getPerformanceRules().stream()
                .filter(rule -> rule.getEnabled())
                .collect(Collectors.toList());
        
        List<PerformanceRuleMatcher> matchers = new ArrayList<>();
        for (AmpPerformanceRule rule : rules) {
            try {
                matchers.add(getMatcherDefinition(rule.getTypeClassName()).createMatcher(rule));
            } catch (IllegalArgumentException e) {
                logger.error("Rule [" + rule.getName() + "] is not valid. Please check the attributes");
            } catch (PerformanceRuleException e) {
                logger.error("Type [" + rule.getTypeClassName() + "] for rule [" + rule.getName() + "] is invalid.");
            }
        }
        
        return matchers;
    }
    
    /**
     * This method should only when the the user wants to match one activity.
     * In order to match a list of activities, get the matcher list once and match the items one by one
     * @param a activity
     * @return performance alert level (null if activity does not have performance issues)
     */
    public AmpCategoryValue matchActivity(AmpActivityVersion a) {
        List<PerformanceRuleMatcher> matchers = getPerformanceRuleMatchers();

        return matchActivity(matchers, a);
    }

    public AmpCategoryValue matchActivity(List<PerformanceRuleMatcher> matchers, AmpActivityVersion a) {

        AmpCategoryValue level = null;
        
        for (PerformanceRuleMatcher matcher : matchers) {
            AmpCategoryValue matchedLevel = matcher.match(a) ? matcher.getRule().getLevel() : null;
            level = getHigherLevel(level, matchedLevel);
        }
        
        return level;
    }
    

    public AmpPerformanceRuleAttribute getAttributeFromRule(AmpPerformanceRule rule, String attributeName) {
        AmpPerformanceRuleAttribute attribute = rule.getAttributes().stream()                
                .filter(attr -> attr.getName().equals(attributeName))
                .findAny().orElse(null);
        
        return attribute;
    }
    
    public String getAttributeValue(AmpPerformanceRule rule, String attributeName) {
        AmpPerformanceRuleAttribute attribute = getAttributeFromRule(rule, attributeName);
        
        return attribute.getValue();
    }
    
    public AmpCategoryValue getHigherLevel(AmpCategoryValue level1, AmpCategoryValue level2) {
        if (level1 == null) {
            return level2;
        } else if (level2 == null) {
            return level1;
        } else if (level1.getIndex() > level2.getIndex()) {
            return level2;
        }
        
        return level1;
    }
    
    public int getCalendarTimeUnit(String timeUnit) {
        switch(timeUnit) {
            case PerformanceRuleConstants.TIME_UNIT_DAY :
                return Calendar.DAY_OF_YEAR;
            case PerformanceRuleConstants.TIME_UNIT_MONTH :
                return Calendar.MONTH;
            case PerformanceRuleConstants.TIME_UNIT_YEAR :
            default :
                return Calendar.YEAR;
        }
    }
    
}
