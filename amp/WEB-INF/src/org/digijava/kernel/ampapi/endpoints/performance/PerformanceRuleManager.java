package org.digijava.kernel.ampapi.endpoints.performance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.dto.ResultPage;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.PerformanceRuleMatcher;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.DisbursementsAfterActivityDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoDisbursementsAfterFundingDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedDisbursementsAfterTimePeriodMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.NoUpdatedStatusAfterFundingDateMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleAttributeOption;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherDefinition;
import org.digijava.kernel.ampapi.endpoints.performance.matcher.definition.PerformanceRuleMatcherPossibleValuesSupplier;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPerformanceRule;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute;
import org.digijava.module.aim.dbentity.AmpPerformanceRuleAttribute.PerformanceRuleAttributeType;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.ImmutableMap;

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
    
    private ExecutorService execService;
    
    public static final Map<String, Long> PERF_ALERT_TYPE_TO_ID = ImmutableMap.of(
            PerformanceRuleConstants.MATCHER_DISB_ACTIVITY_DATE, 1L,
            PerformanceRuleConstants.MATCHER_NO_DISB_FUNDING_DATE, 2L,
            PerformanceRuleConstants.MATCHER_NO_UPD_DISB_TIME_PERIOD, 3L,
            PerformanceRuleConstants.MATCHER_NO_UPDATED_STATUS, 4L
        );
    
    public static final Map<String, String> PERF_ALERT_TYPE_TO_DESCRIPTION = ImmutableMap.of(
            PerformanceRuleConstants.MATCHER_DISB_ACTIVITY_DATE, 
                PerformanceRuleConstants.MATCHER_DESCR_DISB_ACTIVITY_DATE,
            PerformanceRuleConstants.MATCHER_NO_DISB_FUNDING_DATE, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_DISB_FUNDING_DATE,
            PerformanceRuleConstants.MATCHER_NO_UPD_DISB_TIME_PERIOD, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_UPD_DISB_TIME_PERIOD,
            PerformanceRuleConstants.MATCHER_NO_UPDATED_STATUS, 
                PerformanceRuleConstants.MATCHER_DESCR_NO_UPDATED_STATUS
        );

    private static final Logger logger = Logger.getLogger(PerformanceRuleManager.class);
    

    private PerformanceRuleManager() {
        initPerformanceRuleDefinitions();
        execService = Executors.newCachedThreadPool();
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
        
        execService.submit(() -> matchActivitiesByPerformanceRule(performanceRule));
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
        
        execService.submit(() -> matchActivitiesByPerformanceRule(performanceRule));
        updatePerformanceAlertETL(session);
        invalidateCachedPerformanceRules();
    }

    public void deletePerformanceRule(Long id) {
        AmpPerformanceRule performanceRule = getPerformanceRuleById(id);

        Session session = PersistenceManager.getSession();
        session.delete(performanceRule);
        
        deleteRelatedActivitiesFromPerformanceRuleTable(session, id);
        updatePerformanceAlertETL(session);
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

        return new ResultPage<>(pagePerformanceRules, totalRecords);
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
     * @param a activity
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
            if (issue != null) {
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

        Map<Long, AmpOrganisation> organisationById = new HashMap<>();
        Map<Long, Map<Long, Map<PerformanceRuleMatcher, Set<AmpActivityVersion>>>> actByDonorAndRuleByRole =
                new HashMap<>();

        actsWithIssues.forEach((AmpActivityVersion act, List<PerformanceIssue> issues) -> {
            Map<Long, Map<PerformanceRuleMatcher, Set<AmpActivityVersion>>> actByDonorAndRule = new HashMap<>();

            for (PerformanceIssue issue : issues) {
                List<AmpOrganisation> donors = issue.getDonors();
                PerformanceRuleMatcher matcher = issue.getMatcher();

                for (AmpOrganisation donor : donors) {
                    Long donorId = donor.getAmpOrgId();
                    if (!actByDonorAndRule.containsKey(donorId)) {
                        actByDonorAndRule.put(donorId, new HashMap<>());
                        organisationById.put(donorId, donor);
                    }

                    if (!actByDonorAndRule.get(donorId).containsKey(matcher)) {
                        actByDonorAndRule.get(donorId).put(matcher, new LinkedHashSet<>());
                    }

                    actByDonorAndRule.get(donorId).get(matcher).add(act);
                }

                //once we have grouped the donor for this activity we have to group at a higher level
                // and group by the role


                act.getOrgrole().stream().filter(o -> o.getRole().getRoleCode().
                        equals(PerformanceRuleConstants.GROUPING_ROLE_CODE)).forEach(role -> {
                    Map<Long, Map<PerformanceRuleMatcher, Set<AmpActivityVersion>>>
                            actByDonorAndRuleExisting = actByDonorAndRuleByRole.
                            get(role.getOrganisation().getAmpOrgId());
                    if (actByDonorAndRuleExisting == null) {
                        //if we don't have the donors by the role yet we just add this
                        actByDonorAndRuleByRole.put(role.getOrganisation().getAmpOrgId(), actByDonorAndRule);
                        organisationById.put(role.getOrganisation().getAmpOrgId(), role.getOrganisation());
                    } else {
                        //if the role already has the donor, we need to merge them
                        actByDonorAndRule.entrySet().forEach(actByDonorAndRuleToMerge -> {
                            if (actByDonorAndRuleExisting.get(actByDonorAndRuleToMerge.getKey()) != null) {
                                Map<PerformanceRuleMatcher, Set<AmpActivityVersion>> performanceRuleForDonorToMerge =
                                        actByDonorAndRuleExisting.get(actByDonorAndRuleToMerge.getKey());

                                actByDonorAndRuleToMerge
                                        .getValue().entrySet().forEach(mapaTemp2It -> {
                                    performanceRuleForDonorToMerge.merge(mapaTemp2It.getKey(), mapaTemp2It.getValue(),
                                            (v1, v2) -> {
                                        v1.addAll(v2);
                                        return v1;
                                    });
                                });
                                actByDonorAndRuleExisting.put(actByDonorAndRuleToMerge.getKey(),
                                        performanceRuleForDonorToMerge);
                            } else {
                                actByDonorAndRuleExisting.put(actByDonorAndRuleToMerge.getKey(),
                                        actByDonorAndRuleToMerge.getValue());
                            }
                        });
                    }
                });
            }
        });
        String ampIdLabel = TranslatorWorker.translateText("AMP ID");
        String titleLabel = TranslatorWorker.translateText("Title");
        String donorAgencyLabel = TranslatorWorker.translateText("Donor Agency");
        String groupingAgencyLabel = TranslatorWorker.translateText(PerformanceRuleConstants.GROUPING_ROLE_LABEL);


        //TODO get the url correctly
        String url = SiteUtils.getBaseUrl();

        if (actByDonorAndRuleByRole.isEmpty()) {
            String noActivityWithRule = TranslatorWorker
                    .translateText("No activities with performance issues have been found");
            sb.append("<br/>" + noActivityWithRule + ".<br/>");
        }

        // we sort the map by org asc
        DbUtil.HelperAmpOrganisationNameComparator helperAmpOrganisationNameComparator = new
                DbUtil.HelperAmpOrganisationNameComparator(new Locale(TLSUtils.getLangCode()));

        Map<Long, Map<Long, Map<PerformanceRuleMatcher, Set<AmpActivityVersion>>>>  actByDonorAndRuleByRoleSorted
                = actByDonorAndRuleByRole.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(new Comparator<Long>() {
                    @Override
                    public int compare(Long o1, Long o2) {
                        return helperAmpOrganisationNameComparator.compare(organisationById.get(o1), organisationById
                                .get(o2));
                    }
                }))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        actByDonorAndRuleByRoleSorted.entrySet().forEach(groupingRoleEntry -> {
            sb.append(String.format("<b>%s</b>: %s ", groupingAgencyLabel, organisationById.get(groupingRoleEntry
                    .getKey()).getName()));
            sb.append("<table witdh=\"100%\" border=\"1\"><tr><td>");
            buildTableByDonor(sb, groupingRoleEntry.getValue(), organisationById, ampIdLabel, titleLabel,
                    donorAgencyLabel, url);
            sb.append("</td></tr></table>");
            sb.append("<br/><br/>");
        });

        return sb.toString();
    }

    /**
     * Builds the donor table. this table will go inside the table that is build for the role we are grouping by
     * @param sb
     * @param actByDonorAndRule
     * @param organisationById
     * @param ampIdLabel
     * @param titleLabel
     * @param url
     */
    private void buildTableByDonor(StringBuilder sb, Map<Long, Map<PerformanceRuleMatcher, Set<AmpActivityVersion>>>
            actByDonorAndRule, Map<Long, AmpOrganisation> organisationById, String ampIdLabel, String titleLabel,
                                   String donorAgencyLabel, String url) {
        sb.append("<table><tr><td>");
        actByDonorAndRule.entrySet().forEach(donorEntry -> {
            String donorName = organisationById.get(donorEntry.getKey()).getName();
            sb.append("<br/>");
            sb.append(String.format("<b>%s: </b>%s", donorAgencyLabel, HtmlUtils.htmlEscape(donorName)));
            sb.append("<br/>");

            Map<PerformanceRuleMatcher, Set<AmpActivityVersion>> activitiesByRule = donorEntry.getValue();
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

    public boolean canActivityContainPerformanceIssues(AmpActivityVersion a) {
        return !a.isCreatedAsDraft() && !Boolean.TRUE.equals(a.getDraft()) && !a.getDeleted() && a.getTeam() != null
                && AmpARFilter.VALIDATED_ACTIVITY_STATUS.contains(a.getApprovalStatus());
    }

    public Set<AmpPerformanceRule> getPerformanceRulesFromIssues(List<PerformanceIssue> issues) {
        return issues.stream()
                .map(issue -> issue.getMatcher().getRule())
                .collect(Collectors.toSet());
    }

    /**
     * 
     * @param col1 - the first collection, must not be null
     * @param col2 - the second collection, must not be null
     * 
     * @return true if the collections contain the same performance levels
     */
    public boolean isEqualPerformanceRuleCollection(Set<AmpPerformanceRule> col1, Set<AmpPerformanceRule> col2) {
        if (col1.size() != col2.size()) {
            return false;
        }
        
        Set<Long> colId1 = col1.stream().map(AmpPerformanceRule::getId).collect(Collectors.toSet());
        Set<Long> colId2 = col2.stream().map(AmpPerformanceRule::getId).collect(Collectors.toSet());
        
        return colId1.equals(colId2);
    }
    
    public static String getAlertDescriptionFromMatcher(String matcherName) {
        return TranslatorWorker.translateText(PERF_ALERT_TYPE_TO_DESCRIPTION.get(matcherName));
    }

    public Set<AmpPerformanceRule> getActivityPerformanceRules(Long activityId) {
        String query = "SELECT performance_rule_id FROM amp_activity_performance_rule WHERE amp_activity_id = %s";
        Set<Long> ruleIds = new HashSet<>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                ruleIds.addAll(SQLUtils.fetchLongs(conn, String.format(query, activityId)));
            }
        });
        
        return cachedPerformanceRules.stream().filter(p -> ruleIds.contains(p.getId())).collect(Collectors.toSet());
    }

    public void updateActivityPerformanceRules(Long activityId, Set<AmpPerformanceRule> matchedRules) {
        String deleteQuery = "DELETE FROM amp_activity_performance_rule WHERE amp_activity_id = %s";
        String insertQuery = "INSERT INTO amp_activity_performance_rule (amp_activity_id, performance_rule_id) "
                + "VALUES (%s, %s)";

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, String.format(deleteQuery, activityId));
                if (!matchedRules.isEmpty()) {
                    for (AmpPerformanceRule rule : matchedRules) {
                        SQLUtils.executeQuery(conn, String.format(insertQuery, activityId, rule.getId()));
                    }
                }
            }
        });
    }
    
    /**
     * Insert a new item in amp_activity_performance_rule table.
     * 
     * @param session
     * @param activityId
     * @param ruleId
     */
    public void insertActivityPerformanceRule(Session session, Long activityId, Long ruleId) {
        String insertQuery = "INSERT INTO amp_activity_performance_rule (amp_activity_id, performance_rule_id) "
                + "VALUES (%s, %s)";

        session.doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, String.format(insertQuery, activityId, ruleId));
            }
        });
    }
    
    /**
     * Delete all data from from amp_activity_performance_rule table
     * 
     * @param session
     */
    public void deleteAllActivityPerformanceRules(Session session) {
        String deleteQuery = "DELETE FROM amp_activity_performance_rule";
        session.doWork(new Work() {
            public void execute(Connection conn) throws SQLException {  
                SQLUtils.executeQuery(conn, deleteQuery);
            }
        });
    }
    
    /**
     * Delete the rows from amp_activity_performance_rule table with a specific activity id.
     * 
     * @param session
     * @param activityId
     */
    public void deleteActivityPerformanceRule(Session session, Long activityId) {
        String deleteQuery = "DELETE FROM amp_activity_performance_rule WHERE amp_activity_id = %s";
        session.doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, String.format(deleteQuery, activityId));
            }
        });
    }
    
    /**
     * Delete the rows from amp_activity_performance_rule table with a specific ruleId.
     * 
     * @param session
     * @param ruleId
     */
    private void deleteRelatedActivitiesFromPerformanceRuleTable(Session session, Long ruleId) {
        String deleteQuery = "DELETE FROM amp_activity_performance_rule WHERE performance_rule_id = %s";
        session.doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, String.format(deleteQuery, ruleId));
            }
        });
    }
    
    /**
     * Check activities for performance for a specific rule. Updates the tabel amp_activity_performance_rule
     * 
     * @param rule
     */
    private void matchActivitiesByPerformanceRule(AmpPerformanceRule rule) {
        Session session = PersistenceManager.openNewSession();
        Transaction tx = session.beginTransaction();
        try {
            if (rule.getEnabled()) {
                List<Long> actIds = org.digijava.module.aim.util.ActivityUtil.getValidatedActivityIds();
                PerformanceRuleMatcher matcher = getMatcherDefinition(rule.getTypeClassName()).createMatcher(rule);
                deleteRelatedActivitiesFromPerformanceRuleTable(session, rule.getId());
                
                for (Long actId : actIds) {
                    try {
                        AmpActivityVersion a = ActivityUtil.loadActivity(actId);
                        if (matcher.findPerformanceIssue(a) != null) {
                            insertActivityPerformanceRule(session, actId, rule.getId());
                        }
                    } catch (DgException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            } else {
                deleteRelatedActivitiesFromPerformanceRuleTable(session, rule.getId());
            }
            updatePerformanceAlertETL(session);
        } catch (Throwable e) {
            tx.rollback();
            throw e;
        } finally {
            PersistenceManager.closeSession(session);
        }
    }
    
    /**
     * Updates the ETL table by adding a row in it. 
     * In order to refresh the report fetching of performance columns, we need to add an item in this table
     * 
     * @param session
     */
    public void updatePerformanceAlertETL(Session session) {
        String query = "INSERT INTO amp_etl_changelog(entity_name, entity_id) VALUES ('alert', 1);";
        session.doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, query);
            }
        });
        
    }
}
