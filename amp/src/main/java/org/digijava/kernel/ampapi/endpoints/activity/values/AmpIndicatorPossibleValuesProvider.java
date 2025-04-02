package org.digijava.kernel.ampapi.endpoints.activity.values;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.IndicatorExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.AbstractPossibleValuesBaseProvider;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;

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
            IndicatorExtraInfo extraInfo = new IndicatorExtraInfo(indicator.getCode(), sectorIds);
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

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isIndicatorValid(id);
    }
}
