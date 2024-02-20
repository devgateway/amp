/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import org.digijava.module.aim.dbentity.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;
import java.util.stream.Collectors;

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

    public enum PARAM implements AmpAutoCompleteModelParam {
        ACTIVITY_PROGRAM,
        ACTIVITY_LOCATION
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

            Set<AmpActivityProgram> ampActivityPrograms = (Set<AmpActivityProgram>) getParam(PARAM.ACTIVITY_PROGRAM);

            // Get activity locations
            Set<AmpActivityLocation> ampActivityLocations = (Set<AmpActivityLocation>) getParam(PARAM.ACTIVITY_LOCATION);

            crit.setCacheable(false);
            if (input.trim().length() > 0) {
                Junction junction = Restrictions.conjunction().add(getTextCriterion("name", input));
                crit.add(junction);
            }

            crit.addOrder(Order.asc("name"));
            if (maxResults != null && maxResults != 0)
                crit.setMaxResults(maxResults);
            ret = crit.list();


            // If not activity programs then do not return any indicator
            if (ampActivityPrograms != null && !ampActivityPrograms.isEmpty()) {
                Set<AmpTheme> programThemes = ampActivityPrograms.stream()
                        .map(AmpActivityProgram::getProgram)
                        .collect(Collectors.toSet());
                Set<AmpTheme> programThemesClone = new HashSet<>(programThemes);
                // Check if program has siblings and add them to themes to get all indicators for objectives in a program
                for (AmpTheme program : programThemes) {
                    if (program.getSiblings() != null) {
                        programThemesClone.addAll(program.getSiblings());
                    }
                }


                filterAmpIndicators = ret.stream()
                        .filter(indicator -> programThemesClone.contains(indicator.getProgram()))
                        .collect(Collectors.toList());
            }

            // Filter indicators by the activity locations
            if(ampActivityLocations != null && !ampActivityLocations.isEmpty()){
                for(AmpActivityLocation location: ampActivityLocations){
                    AmpCategoryValueLocations categoryLocation = location.getLocation();
                    System.out.println(categoryLocation);
                    filterAmpIndicators = filterAmpIndicators.stream()
                            .filter(indicator -> indicator.getIndicatorLocations().stream()
                                    .anyMatch(indicatorLocation -> indicatorLocation.getLocation().equals(categoryLocation))
                            ).collect(Collectors.toList());
                }
            }

            return filterAmpIndicators;
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

}
