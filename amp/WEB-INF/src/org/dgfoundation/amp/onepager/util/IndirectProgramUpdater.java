package org.dgfoundation.amp.onepager.util;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;
import static org.digijava.kernel.ampapi.endpoints.ndd.NDDService.INDIRECT_MAPPING_LEVEL;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.digijava.module.aim.dbentity.AmpActivityIndirectProgram;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpIndirectTheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.hibernate.Session;

/**
 * @author Octavian Ciubotaru
 */
public class IndirectProgramUpdater {

    public void updateIndirectPrograms(AmpActivityVersion activity, Session session) {
        Map<AmpTheme, Set<AmpTheme>> mapping = loadMapping(session);
        includeAncestors(mapping);
        updateIndirectPrograms(activity, mapping);
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
        while (program.getIndlevel() > INDIRECT_MAPPING_LEVEL) {
            program = program.getParentThemeId();
        }

        return new ArrayList<>(themeMapping.getOrDefault(program, emptySet()));
    }
}
