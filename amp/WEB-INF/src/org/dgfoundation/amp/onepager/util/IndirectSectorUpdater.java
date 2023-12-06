package org.dgfoundation.amp.onepager.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.activity.ActivityCloser;
import org.digijava.module.aim.util.activity.GenericUserHelper;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;

/**
 * @author Diego Rossi
 */
public class IndirectSectorUpdater {

    public AmpActivityVersion updateIndirectSectors(AmpActivityVersion activity, Session session) {
        Map<AmpSector, Set<AmpSector>> mapping = loadMapping(session);
        updateIndirectSectors(activity, mapping);
        return activity;
    }

    public void updateIndirectSectorMapping(Long ampActivityId) {
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
    private Map<AmpSector, Set<AmpSector>> loadMapping(Session session) {
        List<AmpSectorMapping> list = session
                .createCriteria(AmpSectorMapping.class)
                .setCacheable(true)
                .list();

        return list.stream().collect(groupingBy(
                AmpSectorMapping::getSrcSector,
                () -> new TreeMap<>(Comparator.comparing(AmpSector::getAmpSectorId)),
                mapping(AmpSectorMapping::getDstSector, toCollection(this::newSetComparingById))));
    }

    private Set<AmpSector> newSetComparingById() {
        return new TreeSet<>(Comparator.comparing(AmpSector::getAmpSectorId));
    }

    private void updateIndirectSectors(AmpActivityVersion activity, Map<AmpSector, Set<AmpSector>> mapping) {
        activity.getSectors().forEach((as) -> {
            List<AmpSector> indirectSectors = new ArrayList<>(mapping.getOrDefault(as.getSectorId(), emptySet()));
            as.getIndirectSectors().clear();

            if(!indirectSectors.isEmpty()) {
                PercentagesUtil.SplitResult sr;
                if (as.getSectorPercentage() != null) {
                    BigDecimal sum = PercentagesUtil.closestTo(as.getSectorPercentage(),
                            AmpActivityIndirectSector.PERCENTAGE_PRECISION);
                    sr = PercentagesUtil.split(sum, indirectSectors.size(),
                            AmpActivityIndirectSector.PERCENTAGE_PRECISION);
                } else { sr = null; }

                for (int i = 0; i < indirectSectors.size(); i++) {
                    as.addIndirectSector(new AmpActivityIndirectSector(indirectSectors.get(i),
                            sr == null ? null : sr.getValueFor(i)));
                }
            }
        });
    }

}
