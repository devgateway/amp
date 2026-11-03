package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class AmpIndicatorMeasureTest {

    @Test
    void selectsDisaggregatedActualsAtAllSupportedLevelsButNotBasicValues() {
        AmpIndicatorMeasure measure = new AmpIndicatorMeasure(
                MeasureConstants.INDICATOR_PRIMARY_DISAGGREGATION_ACTUAL_VALUE, AmpIndicatorValue.ACTUAL, true, null);
        CategAmountCell parentOnly = amount(AmpIndicatorValue.ACTUAL, 100L, null, null);
        CategAmountCell parentAndChild = amount(AmpIndicatorValue.ACTUAL, 100L, 200L, null);
        CategAmountCell allLevels = amount(AmpIndicatorValue.ACTUAL, 100L, 200L, 300L);
        CategAmountCell otherCombination = amount(AmpIndicatorValue.ACTUAL, 101L, 201L, 301L);

        assertSame(parentOnly, measure.processCell(parentOnly));
        assertSame(parentAndChild, measure.processCell(parentAndChild));
        assertSame(allLevels, measure.processCell(allLevels));
        assertSame(otherCombination, measure.processCell(otherCombination));
        assertNull(measure.processCell(amount(AmpIndicatorValue.ACTUAL, null, null, null)));
        assertNull(measure.processCell(amount(AmpIndicatorValue.BASE, 100L, 200L, 300L)));
        assertNull(measure.processCell(amount(AmpIndicatorValue.TARGET, 100L, 200L, 300L)));
    }

    @Test
    void preservesExistingActualBaselineAndTargetSelection() {
        AmpIndicatorMeasure actual = new AmpIndicatorMeasure(
                MeasureConstants.INDICATOR_ACTUAL_VALUE, AmpIndicatorValue.ACTUAL, false, null);
        AmpIndicatorMeasure base = new AmpIndicatorMeasure(
                MeasureConstants.INDICATOR_PRIMARY_DISAGGREGATION_BASE_VALUE, AmpIndicatorValue.BASE, false, null);
        AmpIndicatorMeasure target = new AmpIndicatorMeasure(
                MeasureConstants.INDICATOR_PRIMARY_DISAGGREGATION_TARGET_VALUE, AmpIndicatorValue.TARGET, false, null);
        CategAmountCell basicActual = amount(AmpIndicatorValue.ACTUAL, null, null, null);
        CategAmountCell disaggregatedActual = amount(AmpIndicatorValue.ACTUAL, 100L, 200L, null);
        CategAmountCell basicBase = amount(AmpIndicatorValue.BASE, null, null, null);
        CategAmountCell disaggregatedBase = amount(AmpIndicatorValue.BASE, 100L, null, null);
        CategAmountCell basicTarget = amount(AmpIndicatorValue.TARGET, null, null, null);
        CategAmountCell disaggregatedTarget = amount(AmpIndicatorValue.TARGET, 100L, 200L, 300L);

        assertSame(basicActual, actual.processCell(basicActual));
        assertSame(disaggregatedActual, actual.processCell(disaggregatedActual));
        assertSame(basicBase, base.processCell(basicBase));
        assertSame(disaggregatedBase, base.processCell(disaggregatedBase));
        assertSame(basicTarget, target.processCell(basicTarget));
        assertSame(disaggregatedTarget, target.processCell(disaggregatedTarget));
        assertNull(base.processCell(disaggregatedActual));
        assertNull(target.processCell(disaggregatedActual));
    }

    private CategAmountCell amount(int type, Long parent, Long child, Long third) {
        MetaInfoSet meta = new MetaInfoSet(new MetaInfoGenerator());
        meta.add(MetaCategory.INDICATOR_VALUE_TYPE.category, (long) type);
        if (parent != null) {
            meta.add(MetaCategory.CATEGORY_VALUE_ID.category, parent);
        }
        if (child != null) {
            meta.add(MetaCategory.CHILD_CATEGORY_VALUE_ID.category, child);
        }
        if (third != null) {
            meta.add(MetaCategory.THIRD_CATEGORY_VALUE_ID.category, third);
        }
        return new CategAmountCell(10L,
                new MonetaryAmount(BigDecimal.TEN, NiPrecisionSetting.IDENTITY_PRECISION_SETTING),
                meta, Collections.emptyMap(), null);
    }
}
