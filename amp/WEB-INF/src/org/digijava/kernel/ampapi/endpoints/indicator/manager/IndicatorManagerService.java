package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants.DESCRIPTION;
import static org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants.NAME;


/**
 * Indicator Manager Service
 * 
 * @author vchihai
 */
public class IndicatorManagerService {

    protected static final Logger logger = Logger.getLogger(IndicatorManagerService.class);

    private TranslationUtil contentTranslator = new TranslationUtil();

    public List<MEIndicatorDTO> getIndicators() {
        Session session = PersistenceManager.getSession();

        List<AmpIndicator> indicators = session.createCriteria(AmpIndicator.class)
                .addOrder(Order.asc("id"))
                .list();

        return indicators.stream()
                .map(MEIndicatorDTO::new)
                .collect(Collectors.toList());
    }


    public List<SectorDTO> getSectors() {
        return SectorUtil.getAllParentSectors().stream()
                .map(SectorDTO::new)
                .collect(Collectors.toList());
    }

    public MEIndicatorDTO getIndicatorById(final String id) {
        Session session = PersistenceManager.getSession();

        AmpIndicator indicator = (AmpIndicator) session.get(AmpIndicator.class, Long.valueOf(id));
        if (indicator != null) {
            return new MEIndicatorDTO(indicator);
        }

        throw new IllegalArgumentException("Indicator with id " + id + " not found");
    }

    public MEIndicatorDTO createAmpIndicator(final MEIndicatorDTO indicatorRequest) {
        Session session = PersistenceManager.getSession();
        AmpIndicator indicator = new AmpIndicator();

        indicator.setName(contentTranslator.extractTranslationsOrSimpleValue(NAME, indicator,
                indicatorRequest.getName()));
        indicator.setDescription(contentTranslator.extractTranslationsOrSimpleValue(DESCRIPTION, indicator,
                indicatorRequest.getDescription()));

        indicator.setCode(indicatorRequest.getCode());
        indicator.setType(indicatorRequest.isAscending() ? "A" : "D");
        indicator.setCreationDate(indicatorRequest.getCreationDate());

        Set<AmpIndicatorGlobalValue> indicatorValues = new HashSet<>();
        if (indicatorRequest.getBaseValue() != null) {
            indicatorValues.add(indicatorRequest.getBaseValue());
        }
        if (indicatorRequest.getTargetValue() != null) {
            indicatorValues.add(indicatorRequest.getTargetValue());
        }
        indicatorValues.stream().forEach(value -> value.setIndicator(indicator));
        indicator.setIndicatorValues(indicatorValues);

        Set<AmpSector> sectors = indicatorRequest.getSectorIds().stream()
                .map(id -> (AmpSector) session.get(AmpSector.class, id))
                .collect(Collectors.toSet());
        indicator.setSectors(sectors);

        Set<AmpTheme> programs = indicatorRequest.getProgramIds().stream()
                .map(id -> (AmpTheme) session.get(AmpTheme.class, id))
                .collect(Collectors.toSet());
        indicator.setPrograms(programs);

        session.save(indicator);

        return indicatorRequest;
    }
}
