/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import org.digijava.kernel.ampapi.endpoints.indicator.manager.IndicatorManagerService;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEIndicatorSearchModel extends
        AbstractAmpAutoCompleteModel<AmpIndicator> {

    public AmpMEIndicatorSearchModel(String input, String language,
                                     Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 8211300754918658832L;
    private Session session;
    private static final Logger logger = Logger.getLogger(AmpMEIndicatorSearchModel.class);


    public enum PARAM implements AmpAutoCompleteModelParam {
        ACTIVITY_PROGRAM, ACTIVITY_SECTOR
    }


    @Override
    protected Collection<AmpIndicator> load() {
        try {
            List<AmpIndicator> ret = new ArrayList<>();
            List<AmpIndicator> filterAmpIndicators = new ArrayList<>();
            session = AmpActivityModel.getHibernateSession();

            Integer maxResults = (Integer) getParams().get(
                    AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);

            Criteria crit = session.createCriteria(AmpIndicator.class);

            Set<Long> activityProgramThemeIds = toProgramThemeIds(getParam(PARAM.ACTIVITY_PROGRAM));
            Set<Long> activitySectorIds = toSectorIds(getParam(PARAM.ACTIVITY_SECTOR));
            logger.info("All sectors: " + activitySectorIds);

            // Get activity location
            crit.setCacheable(false);
            if (!input.trim().isEmpty()) {
                Junction junction = Restrictions.conjunction().add(getTextCriterion("name", input));
                crit.add(junction);
            }

            crit.addOrder(Order.asc("name"));
            if (maxResults != null && maxResults != 0)
                crit.setMaxResults(maxResults);
            ret = crit.list();
            // Re assign all indicators as filtered
            filterAmpIndicators = ret;
            // Check if the indicator filter by program is active
            boolean filterByProgram = FeaturesUtil.isVisibleModule(IndicatorManagerService.FILTER_BY_PROGRAM);

            if (filterByProgram) {
                if (!activityProgramThemeIds.isEmpty()) {
                    // Include siblings (children in AMP hierarchy) via fresh session to avoid lazy-load issues
                    Set<Long> allProgramIds = new HashSet<>(activityProgramThemeIds);
                    for (Long themeId : activityProgramThemeIds) {
                        AmpTheme theme = session.get(AmpTheme.class, themeId);
                        if (theme != null && theme.getSiblings() != null) {
                            for (AmpTheme sibling : theme.getSiblings()) {
                                if (sibling.getAmpThemeId() != null) {
                                    allProgramIds.add(sibling.getAmpThemeId());
                                }
                            }
                        }
                    }
                    filterAmpIndicators = ret.stream()
                            .filter(ind -> ind.getProgram() != null
                                    && allProgramIds.contains(ind.getProgram().getAmpThemeId()))
                            .collect(Collectors.toList());
                }
            }

            // Check if the indicator filter by sector is active
            boolean filterBySector = FeaturesUtil.isVisibleModule(IndicatorManagerService.FILTER_BY_SECTOR);
            if (filterBySector) {
                if (!activitySectorIds.isEmpty()) {
                    filterAmpIndicators = filterAmpIndicators.stream()
                            .filter(ind -> ind.getSectors() != null && ind.getSectors().stream()
                                    .anyMatch(s -> s.getAmpSectorId() != null
                                            && activitySectorIds.contains(s.getAmpSectorId())))
                            .collect(Collectors.toList());
                }
            }

            return filterAmpIndicators;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<Long> toSectorIds(Object param) {
        if (param == null) return Collections.emptySet();
        Set<?> raw = (Set<?>) param;
        if (raw.isEmpty()) return Collections.emptySet();
        return (Set<Long>) raw;
    }

    @SuppressWarnings("unchecked")
    private static Set<Long> toProgramThemeIds(Object param) {
        if (param == null) return Collections.emptySet();
        Set<?> raw = (Set<?>) param;
        if (raw.isEmpty()) return Collections.emptySet();
        Object first = raw.iterator().next();
        if (first instanceof Long) {
            return (Set<Long>) raw;
        }
        // Legacy: stale Wicket session still holds Set<AmpActivityProgram>
        Set<Long> ids = new HashSet<>();
        for (AmpActivityProgram ap : (Set<AmpActivityProgram>) raw) {
            if (ap.getProgram() != null && ap.getProgram().getAmpThemeId() != null) {
                ids.add(ap.getProgram().getAmpThemeId());
            }
        }
        return ids;
    }

}
