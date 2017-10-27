package org.digijava.kernel.ampapi.endpoints.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.DisbursementsAfterActivityDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoDisbursementsAfterFundingDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedStatusAfterFundingDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleAttributeOption;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.util.StringUtils;

/**
 * 
 * @author Viorel Chihai
 *
 */
public final class PerformanceRuleManager {

    private static PerformanceRuleManager performanceRuleManager;

    private List<PerformanceRuleMatcherDefinition> definitions;

    private List<AmpPerformanceRule> cachedPerformanceRules;

    private List<PerformanceRuleMatcher> cachedPerformanceRuleMatchers;

    private static final Logger logger = Logger.getLogger(PerformanceRuleManager.class);

    private PerformanceRuleManager() {
        initPerformanceRuleDefinitions();
    }

    /**
     * 
     * @return PerfomanceRuleManager instance
     */
    public static PerformanceRuleManager getInstance() {
        if (performanceRuleManager == null) {
            performanceRuleManager = new PerformanceRuleManager();
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

        AmpCategoryValue dbLevel = requireCategoryValueExists(performanceRule.getLevel().getId());
        performanceRule.setLevel(dbLevel);

        Session session = PersistenceManager.getSession();
        session.merge(performanceRule);

        invalidateCachedPerformanceRules();
    }

    public void savePerformanceRule(AmpPerformanceRule performanceRule) {

        if (performanceRule.getLevel() == null) {
            throw new PerformanceRuleException(PerformanceRulesErrors.REQUIRED_ATTRIBUTE, "level");
        }

        AmpCategoryValue dbLevel = requireCategoryValueExists(performanceRule.getLevel().getId());
        performanceRule.setLevel(dbLevel);

        Session session = PersistenceManager.getSession();
        session.saveOrUpdate(performanceRule);

        invalidateCachedPerformanceRules();
    }

    public void deletePerformanceRule(Long id) {
        AmpPerformanceRule performanceRule = getPerformanceRuleById(id);

        Session session = PersistenceManager.getSession();
        session.delete(performanceRule);

        invalidateCachedPerformanceRules();
    }

    public List<AmpPerformanceRule> getPerformanceRules() {
        if (cachedPerformanceRules == null) {
            updateCachedPerformanceRules();
        }

        return cachedPerformanceRules;
    }

    private void updateCachedPerformanceRules() {
        Session session = PersistenceManager.getSession();

        cachedPerformanceRules = session.createCriteria(AmpPerformanceRule.class).addOrder(Order.asc("id")).list();
        invalidateCachedPerformanceRuleMatchers();
    }

    private void invalidateCachedPerformanceRules() {
        cachedPerformanceRules = null;
        invalidateCachedPerformanceRuleMatchers();
    }

    private void updateCachedPerformanceRuleMatchers() {
        List<AmpPerformanceRule> rules = getPerformanceRules().stream().filter(rule -> rule.getEnabled())
                .collect(Collectors.toList());

        cachedPerformanceRuleMatchers = new ArrayList<>();

        for (AmpPerformanceRule rule : rules) {
            try {
                cachedPerformanceRuleMatchers.add(getMatcherDefinition(rule.getTypeClassName()).createMatcher(rule));
            } catch (IllegalArgumentException e) {
                logger.error("Rule [" + rule.getName() + "] is not valid. Please check the attributes");
            } catch (PerformanceRuleException e) {
                logger.error("Type [" + rule.getTypeClassName() + "] for rule [" + rule.getName() + "] is invalid.");
            }
        }
    }

    private void invalidateCachedPerformanceRuleMatchers() {
        cachedPerformanceRuleMatchers = null;
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
        if (cachedPerformanceRuleMatchers == null) {
            updateCachedPerformanceRuleMatchers();
        }

        return cachedPerformanceRuleMatchers;
    }

    /**
     * This method should only when the the user wants to match one activity. In
     * order to match a list of activities, get the matcher list once and match
     * the items one by one
     * 
     * @param a
     *            activity
     * @return matchers
     */
    public List<PerformanceIssue> findPerformanceIssues(AmpActivityVersion a) {
        List<PerformanceRuleMatcher> matchers = getPerformanceRuleMatchers();

        return findPerformanceIssues(matchers, a);
    }

    public List<PerformanceIssue> findPerformanceIssues(List<PerformanceRuleMatcher> matchers, AmpActivityVersion a) {

        List<PerformanceIssue> performanceIssues = new ArrayList<>();

        for (PerformanceRuleMatcher matcher : matchers) {
            PerformanceIssue issue = matcher.findPerformanceIssue(a);
            if (matcher.findPerformanceIssue(a) != null) {
                performanceIssues.add(issue);
            }
        }

        return performanceIssues;
    }

    public AmpPerformanceRuleAttribute getAttributeFromRule(AmpPerformanceRule rule, String attributeName) {
        AmpPerformanceRuleAttribute attribute = rule.getAttributes().stream()
                .filter(attr -> attr.getName().equals(attributeName)).findAny().orElse(null);

        return attribute;
    }

    public String getAttributeValue(AmpPerformanceRule rule, String attributeName) {
        AmpPerformanceRuleAttribute attribute = getAttributeFromRule(rule, attributeName);

        return attribute.getValue();
    }

    public int getCalendarTimeUnit(String timeUnit) {
        switch (timeUnit) {
        case PerformanceRuleConstants.TIME_UNIT_DAY:
            return Calendar.DAY_OF_YEAR;
        case PerformanceRuleConstants.TIME_UNIT_MONTH:
            return Calendar.MONTH;
        case PerformanceRuleConstants.TIME_UNIT_YEAR:
        default:
            return Calendar.YEAR;
        }
    }

    public String buildPerformanceIssuesMessage(Map<AmpActivityVersion, List<PerformanceIssue>> actsWithIssues) {
        
        StringBuilder sb = new StringBuilder();
        
        Map<Long, Map<PerformanceRuleMatcher, List<AmpActivityVersion>>> actByDonorAndRule = new HashMap<>();
        Map<Long, AmpOrganisation> donorById = new HashMap<>();
        
        actsWithIssues.forEach((act, issues) -> {
            for (PerformanceIssue issue : issues) {
                List<AmpOrganisation> donors = issue.getDonors();
                PerformanceRuleMatcher matcher = issue.getMatcher();
                for (AmpOrganisation donor : donors) {
                    Long donorId = donor.getAmpOrgId();
                    if (!actByDonorAndRule.containsKey(donorId)) {
                        actByDonorAndRule.put(donorId, new HashMap<>());
                        donorById.put(donorId, donor);
                    }
                    
                    if (!actByDonorAndRule.get(donorId).containsKey(matcher)) {
                        actByDonorAndRule.get(donorId).put(matcher, new ArrayList<>());
                    }
                    
                    actByDonorAndRule.get(donorId).get(matcher).add(act);
                }
            }
        });
        String ampIdLabel = TranslatorWorker.translateText("AMP ID");
        String titleLabel = TranslatorWorker.translateText("Title");

        //TODO get the url correctly
        String url = SiteUtils.getBaseUrl();
        
        if (actByDonorAndRule.isEmpty()) {
            String noActivityWithRule = TranslatorWorker
                    .translateText("No activities with performance issues have been found");
            sb.append("<br/>" + noActivityWithRule + ".<br/>");
        }
        
        sb.append("<table><tr><td>");
        actByDonorAndRule.entrySet().forEach(donorEntry -> {
            String donorName = donorById.get(donorEntry.getKey()).getName();
            sb.append("<br/>");
            sb.append(String.format("<b>%s</b>", HtmlUtils.htmlEscape(donorName)));
            sb.append("<br/>");
            
            Map<PerformanceRuleMatcher, List<AmpActivityVersion>> activitiesByRule = donorEntry.getValue();
            sb.append("<table border=1 cellpadding=5 cellspacing=0 width=100%>");
            activitiesByRule.entrySet().forEach(e -> {
                sb.append("<tr>");
                
                PerformanceRuleMatcher matcher = e.getKey();
                String ruleMsg = TranslatorWorker.translateText(getPerformanceRuleMatcherMessage(matcher));
                String ruleLabel = TranslatorWorker.translateText(matcher.getRule().getLevel().getLabel());
                sb.append(String.format("<td colspan=2><b>%s (%s)</b></td></tr>", 
                        HtmlUtils.htmlEscape(ruleMsg), HtmlUtils.htmlEscape(ruleLabel)));
                
                sb.append(String.format("<tr><td width='150px'><b>%s</b></td><td><b>%s</b></td></tr>", 
                        ampIdLabel, titleLabel));
                e.getValue().forEach(a -> {
                    sb.append("<tr>");
                    sb.append(String.format("<td>%s</td>", a.getAmpId()));
                    sb.append("<td>");
                    sb.append(String.format("<a href=\"http://%s/aim/viewActivityPreview.do~activityId=%s\">%s</a>", 
                            url, a.getAmpActivityId(), HtmlUtils.htmlEscape(a.getName())));
                    sb.append("</td>");
                    sb.append("</tr>");
                });
            });
            sb.append("</table>");
        });
        sb.append("</td></tr></table>");
        
        
        return sb.toString();
    }
    
    public String getPerformanceRuleMatcherMessage(PerformanceRuleMatcher matcher) {
        String message = TranslatorWorker.translateText(matcher.getDefinition().getMessage());
        for (AmpPerformanceRuleAttribute attr : matcher.getRule().getAttributes()) {
            String attrValue = attr.getValue();

            if (!attr.getType().equals(PerformanceRuleAttributeType.AMOUNT)) {
                attrValue = getTranslatedLabel(attr);

                if (attr.getType().equals(PerformanceRuleAttributeType.TIME_UNIT)) {
                    attrValue += "(s)";
                }
            }

            message = StringUtils.replace(message, String.format("{%s}", attr.getName()), attrValue);
        }

        return message;
    }
    
    public String getTranslatedLabel(AmpPerformanceRuleAttribute attribute) {
        PerformanceRuleAttributeOption performanceRuleAttributeOption = PerformanceRuleMatcherPossibleValuesSupplier
                .getDefaultPerformanceRuleAttributePossibleValues(attribute.getType())
                .stream().filter(option -> option.getName().equals(attribute.getValue()))
                .findAny().orElseGet(null);
        
        if (performanceRuleAttributeOption == null) {
            return "";
        } 
        
        return performanceRuleAttributeOption.getTranslatedLabel();
    }

    public Set<AmpCategoryValue> getPerformanceIssuesFromActivity(AmpActivityVersion a) {
        Set<AmpCategoryValue> issues = new HashSet<>();
        
        if (a.getCategories() != null) {
            issues = a.getCategories().stream().filter(
                    acv -> acv.getAmpCategoryClass().getKeyName().equals(CategoryConstants.PERFORMANCE_ALERT_LEVEL_KEY))
                    .sorted()
                    .collect(Collectors.toSet());
        }
        
        return issues;
    }

    public void updatePerformanceIssuesInActivity(AmpActivityVersion a, Set<AmpCategoryValue> from, 
            Set<AmpCategoryValue> to) {
        
        if (from != null && !from.isEmpty()) {
            a.getCategories().removeAll(from);
        }

        if (to != null && !to.isEmpty()) {
            a.getCategories().addAll(to);
        }
    }

    public boolean canActivityContainPerformanceIssues(AmpActivityVersion a) {
        return !a.isCreatedAsDraft() && !a.getDraft() && !a.getDeleted() && a.getTeam() != null
                && AmpARFilter.validatedActivityStatus.contains(a.getApprovalStatus());
    }

    public Set<AmpCategoryValue> getPerformanceLevelsFromIssues(List<PerformanceIssue> issues) {
        Set<PerformanceRuleMatcher> matchers = issues.stream()
                .map(PerformanceIssue::getMatcher)
                .collect(Collectors.toSet());
        
        return matchers.stream()
                .map(prm -> prm.getRule().getLevel())
                .sorted()
                .collect(Collectors.toSet());
    }

    /**
     * 
     * @param col1 - the first collection, must not be null
     * @param col2 - the second collection, must not be null
     * 
     * @return true if the collections contain the same performance levels
     */
    public boolean isEqualPerformanceLevelCollection(Set<AmpCategoryValue> col1, Set<AmpCategoryValue> col2) {
        if (col1.size() != col2.size()) {
            return false;
        }
        
        Set<Long> colId1 = col1.stream().map(AmpCategoryValue::getId).collect(Collectors.toSet());
        Set<Long> colId2 = col2.stream().map(AmpCategoryValue::getId).collect(Collectors.toSet());
        
        return colId1.equals(colId2);
    }
    
}
