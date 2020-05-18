package org.dgfoundation.amp.onepager.util;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        Map<AmpTheme, List<AmpTheme>> mapping = loadMapping(session);
        updateIndirectPrograms(activity, mapping);
    }

    @SuppressWarnings("unchecked")
    private Map<AmpTheme, List<AmpTheme>> loadMapping(Session session) {
        List<AmpIndirectTheme> list = session
                .createCriteria(AmpIndirectTheme.class)
                .setCacheable(true)
                .list();

        return list.stream().collect(groupingBy(
                AmpIndirectTheme::getOldTheme,
                () -> new TreeMap<>(Comparator.comparing(AmpTheme::getAmpThemeId)),
                mapping(AmpIndirectTheme::getNewTheme, toList())));
    }

    private void updateIndirectPrograms(AmpActivityVersion activity, Map<AmpTheme, List<AmpTheme>> themeMapping) {
        activity.getActPrograms().forEach((ap) -> {

            List<AmpTheme> directPrograms = themeMapping.computeIfAbsent(ap.getProgram(), p -> emptyList());

            ap.getIndirectPrograms().clear();

            if (!directPrograms.isEmpty()) {
                PercentagesUtil.SplitResult sr;
                if (ap.getProgramPercentage() != null) {

                    BigDecimal sum = PercentagesUtil.closestTo(ap.getProgramPercentage(),
                            AmpActivityIndirectProgram.PERCENTAGE_PRECISION);

                    sr = PercentagesUtil.split(sum, directPrograms.size(),
                            AmpActivityIndirectProgram.PERCENTAGE_PRECISION);
                } else {
                    sr = null;
                }

                for (int i = 0; i < directPrograms.size(); i++) {
                    ap.addIndirectProgram(new AmpActivityIndirectProgram(directPrograms.get(i),
                            sr == null ? null : sr.getValueFor(i)));
                }
            }
        });
    }
}
