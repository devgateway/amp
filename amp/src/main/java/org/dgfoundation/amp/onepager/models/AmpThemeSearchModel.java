/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.digijava.module.aim.util.ProgramUtil.INDIRECT_PRIMARY_PROGRAM;

/**
 * @author aartimon@dginternational.org since Oct 22, 2010
 */
public class AmpThemeSearchModel extends AbstractAmpAutoCompleteModel<AmpTheme> {

    public AmpThemeSearchModel(String input, String language,
                               Map<AmpAutoCompleteModelParam, Object> params) {
        super(input, language, params);
    }

    private static final long serialVersionUID = 2L;

    public enum PARAM implements AmpAutoCompleteModelParam {
        PROGRAM_TYPE, ACTIVITY_PROGRAMS
    }

    @Override
    protected Collection<AmpTheme> load() {
        List<AmpTheme> ret = new ArrayList<>();
        try {
            AmpActivityProgramSettings indirectProgramSetting =
                    ProgramUtil.getAmpActivityProgramSettings(INDIRECT_PRIMARY_PROGRAM);
            Long indProgram = indirectProgramSetting != null ? indirectProgramSetting.getDefaultHierarchyId() : null;
            AmpTheme def = getCurrentRootTheme();
            if (def != null) {
                Set<AmpTheme> mappedPrograms = getMappedDirectPrograms();

                List<AmpTheme> filteredPrograms = getAllPrograms().stream()
                        .filter(p -> p.getRootTheme() != null)
                        .filter(p -> p.getRootTheme().getAmpThemeId().equals(def.getAmpThemeId()))
                        .filter(p -> mappedPrograms == null || mappedPrograms.contains(p))
                        .filter(p -> indProgram == null || !p.getRootTheme().getAmpThemeId().equals(indProgram))
                        .collect(Collectors.toList());

                if (mappedPrograms != null) {
                    filteredPrograms.sort(Comparator.comparing(AmpTheme::getHierarchicalName));
                    ret.addAll(filteredPrograms);
                } else {
                    ret.addAll((Collection<? extends AmpTheme>) createTreeView(filteredPrograms));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot retrieve all themes from db", e);
        }

        return ret;
    }

    private List<AmpTheme> getAllPrograms() {
        Criteria crit = PersistenceManager.getRequestDBSession().createCriteria(AmpTheme.class);
        crit.setCacheable(false);
        if (input.trim().length() > 0) {
            Object o = getTextCriterion("name", input);
            if (o instanceof SimpleExpression) {
                crit.add(((SimpleExpression) o).ignoreCase());
            } else {
                crit.add((Criterion) o);
            }
        }

        crit.add(Restrictions.or(Restrictions.eq("deleted", Boolean.FALSE), Restrictions.isNull("deleted")));

        Integer maxResults = (Integer) getParams().get(
                AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
        if (maxResults != null && maxResults != 0) {
            crit.setMaxResults(maxResults);
        }

        return (List<AmpTheme>) crit.list();
    }

    private AmpTheme getCurrentRootTheme() throws DgException {
        return getCurrentProgramSettings().getDefaultHierarchy();
    }

    private AmpActivityProgramSettings getCurrentProgramSettings() throws DgException {
        return ProgramUtil.getAmpActivityProgramSettings((String) getParams().get(PARAM.PROGRAM_TYPE));
    }

    private Set<AmpTheme> getMappedDirectPrograms() throws DgException {
        if (ProgramUtil.isDestinationMappedProgram(getCurrentProgramSettings())) {
            IModel<Set<AmpActivityProgram>> activityProgramsModel = (IModel<Set<AmpActivityProgram>>)
                    getParam(AmpThemeSearchModel.PARAM.ACTIVITY_PROGRAMS);

            Map<AmpTheme, Set<AmpTheme>> mapping = ProgramUtil.loadProgramMappings();

            return activityProgramsModel.getObject().stream()
                    .map(AmpActivityProgram::getProgram)
                    .filter(p -> mapping.containsKey(getAdjustedLevel(p)))
                    .map(p -> ProgramUtil.getProgramsIncludingAncestors(mapping.get(getAdjustedLevel(p))))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }

        return null;
    }

    private AmpTheme getAdjustedLevel(AmpTheme p) {
        Integer level = FeaturesUtil.getGlobalSettingValueInteger(GlobalSettingsConstants.MAPPING_PROGRAM_LEVEL);
        if (p.getIndlevel() == level) {
            return p;
        } else {
            if (p.getIndlevel() > level) {
                return getAdjustedLevel(p.getParentThemeId());
            } else {
                return null;
            }
        }
    }

}
