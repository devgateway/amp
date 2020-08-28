package org.digijava.kernel.ampapi.endpoints.activity.values;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.activity.IndicatorRiskRatingExtraInfo;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValue;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.AbstractPossibleValuesBaseProvider;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndicatorRiskRatingsPossibleValuesProvider extends AbstractPossibleValuesBaseProvider {

    @Override
    public List<PossibleValue> getPossibleValues(TranslatorService translatorService) {
        List<PossibleValue> pvs = new ArrayList<>();
        Integer index = 0;
        List<AmpIndicatorRiskRatings> indicatorRiskRatings = possibleValuesDAO.getIndicatorRiskRatings();
        indicatorRiskRatings.sort((AmpIndicatorRiskRatings ir1, AmpIndicatorRiskRatings ir2)
                ->ir2.getRatingValue() - ir1.getRatingValue());
        for (AmpIndicatorRiskRatings r : indicatorRiskRatings) {
            IndicatorRiskRatingExtraInfo extra = new IndicatorRiskRatingExtraInfo(r.getRatingValue(), ++index);
            pvs.add(new PossibleValue(r.getAmpIndRiskRatingsId(), r.getRatingName(), ImmutableMap.of(), extra));
        }
        return pvs;
    }

    @Override
    public boolean isAllowed(Long id) {
        return possibleValuesDAO.isIndicatorRiskRatingValid(id);
    }
}
