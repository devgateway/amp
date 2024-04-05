package org.dgfoundation.amp.onepager.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityIndirectProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.activity.ActivityCloser;
import org.digijava.module.aim.util.activity.GenericUserHelper;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;
import static org.digijava.kernel.ampapi.endpoints.ndd.NDDService.getMappingLevel;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.MAPPING_PROGRAM_LEVEL;

/**
 * @author Octavian Ciubotaru
 */
public class IndirectProgramUpdater {
    private static final Logger LOGGER = Logger.getLogger(IndirectProgramUpdater.class);

    public AmpActivityVersion updateIndirectPrograms(AmpActivityVersion activity, Session session) {
        Map<AmpTheme, Set<AmpTheme>> mapping = loadMapping(session);
        includeAncestors(mapping);
        updateIndirectPrograms(activity, mapping);
        return activity;
    }

    public void updateIndirectProgramMapping(Long ampActivityId) {
        PersistenceManager.inTransaction(() -> {
            try {
                AmpActivityVersion o = ActivityUtil.loadActivity(ampActivityId);
                ActivityCloser.cloneActivity(GenericUserHelper.getAmpTeamMemberModifier(o.getTeam()),
                        o, SaveContext.admin());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Map<AmpTheme, Set<AmpTheme>> loadMapping(Session session) {
        List<AmpIndirectTheme> list = session
                .createCriteria(AmpIndirectTheme.class)
                .setCacheable(true)
                .list();

        return list.stream().collect(groupingBy(
                AmpIndirectTheme::getOldTheme,
                () -> new TreeMap<>(Comparator.comparing(AmpTheme::getAmpThemeId)),
                mapping(AmpIndirectTheme::getNewTheme, toCollection(this::newSetComparingById))));
    }

    private Set<AmpTheme> newSetComparingById() {
        return new TreeSet<>(Comparator.comparing(AmpTheme::getAmpThemeId));
    }

    private void includeAncestors(Map<AmpTheme, Set<AmpTheme>> mapping) {
        Set<AmpTheme> queued = newSetComparingById();
        Deque<AmpTheme> queue = new ArrayDeque<>(mapping.keySet());

        while (!queue.isEmpty()) {
            AmpTheme srcProgram = queue.removeFirst();
            AmpTheme srcParentProgram = srcProgram.getParentThemeId();

            if (srcParentProgram != null) {
                Set<AmpTheme> dstParentPrograms = mapping.computeIfAbsent(srcParentProgram, p -> newSetComparingById());

                mapping.getOrDefault(srcProgram, emptySet()).stream()
                        .map(AmpTheme::getParentThemeId)
                        .forEach(dstParentPrograms::add);

                if (!queued.contains(srcParentProgram)) {
                    queued.add(srcParentProgram);
                    queue.add(srcParentProgram);
                }
            }
        }
    }

    private void updateIndirectPrograms(AmpActivityVersion activity, Map<AmpTheme, Set<AmpTheme>> themeMapping) {
        activity.getActPrograms().forEach((ap) -> {

            List<AmpTheme> indirectPrograms = getIndirectPrograms(themeMapping, ap.getProgram());

            ap.getIndirectPrograms().clear();

            if (!indirectPrograms.isEmpty()) {
                PercentagesUtil.SplitResult sr;
                if (ap.getProgramPercentage() != null) {

                    BigDecimal sum = PercentagesUtil.closestTo(ap.getProgramPercentage(),
                            AmpActivityIndirectProgram.PERCENTAGE_PRECISION);

                    sr = PercentagesUtil.split(sum, indirectPrograms.size(),
                            AmpActivityIndirectProgram.PERCENTAGE_PRECISION);
                } else {
                    sr = null;
                }

                for (int i = 0; i < indirectPrograms.size(); i++) {
                    ap.addIndirectProgram(new AmpActivityIndirectProgram(indirectPrograms.get(i),
                            sr == null ? null : sr.getValueFor(i)));
                }
            }
        });
    }

    private List<AmpTheme> getIndirectPrograms(Map<AmpTheme, Set<AmpTheme>> themeMapping, AmpTheme program) {
        if (program!=null){
            while (program.getIndlevel() > getMappingLevel(MAPPING_PROGRAM_LEVEL)) {
                program = program.getParentThemeId();
            }
            return new ArrayList<>(themeMapping.getOrDefault(program, emptySet()));
        }
        return emptyList();
    }
}
