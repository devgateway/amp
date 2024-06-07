package org.digijava.kernel.ampapi.endpoints.activity.values;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.IndicatorExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.AbstractPossibleValuesBaseProvider;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {
    private static final Logger logger= LoggerFactory.getLogger(AmpIndicatorPossibleValuesProvider.class);
    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<AmpIndicator> indicators = possibleValuesDAO.getIndicators();
        List<PossibleValue> pvs = new ArrayList<>();
        logger.info("Indicators: "+indicators);
        for (AmpIndicator indicator : indicators) {
            List<Long> sectorIds = getSectorIds(indicator.getSectors());
            List<Long> programIds = getProgramIds(indicator.getIndicatorId());
            IndicatorExtraInfo extraInfo = new IndicatorExtraInfo(indicator.getCode(), sectorIds, programIds);
            pvs.add(new PossibleValue(indicator.getIndicatorId(), indicator.getName(), ImmutableMap.of(), extraInfo));
        }
        return pvs;
    }

    private List<Long> getSectorIds(Set<AmpSector> sectors) {
        List<Long> sectorIds = new ArrayList<>();
        for (AmpSector sector : sectors) {
            sectorIds.add(sector.getAmpSectorId());
        }
        return sectorIds;
    }

    private List<Long> getProgramIds(Long indicatorId) {

        Session session = PersistenceManager.getRequestDBSession();
        String sql = "SELECT theme_id FROM AMP_INDICATOR_CONNECTION " +
                "WHERE indicator_id = :indicatorId AND sub_clazz = 'p'";
        List<Long> themeIds = session.createNativeQuery(sql)
                .setParameter("indicatorId", indicatorId, LongType.INSTANCE)
                .getResultList();
        session.close();
        return themeIds;

    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isIndicatorValid(id);
    }
}
