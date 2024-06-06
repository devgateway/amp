package org.digijava.kernel.ampapi.endpoints.activity.values;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.IndicatorExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.AbstractPossibleValuesBaseProvider;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorTheme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<AmpIndicator> indicators = possibleValuesDAO.getIndicators();
        List<PossibleValue> pvs = new ArrayList<>();
        for (AmpIndicator indicator : indicators) {
            List<Long> sectorIds = getSectorIds(indicator.getSectors());
            Set<Long> programIds = getProgramIds(indicator.getValuesTheme());
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

    private Set<Long> getProgramIds(Set<IndicatorTheme> indicatorThemes) {
        Set<Long> programIds = new HashSet<>();
        for (IndicatorTheme indicatorTheme : indicatorThemes) {
            programIds.add(indicatorTheme.getTheme().getAmpThemeId());
        }
        return programIds;
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isIndicatorValid(id);
    }
}
