package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.function.Function;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;

/**
 * a measure defined as a filtered transaction multiplied by a number: for each cell, a configured callback
 * decides a multiplier for it. In case the multiplier is null, the cell is dropped; else the result of
 * multiplying the cell by the result is output
 * @author Dolghier Constantin
 *
 */
public class NiMultipliedFilterTransactionMeasure extends NiPredicateTransactionMeasure {
    
    public final Function<CategAmountCell, BigDecimal> multiplierCalculator;
    
    public NiMultipliedFilterTransactionMeasure(String measureName, Function<CategAmountCell, BigDecimal> multiplierCalculator, String description) {
        this(measureName, multiplierCalculator, TrivialMeasureBehaviour.getInstance(), description);
    }
    
    public NiMultipliedFilterTransactionMeasure(String measureName, Function<CategAmountCell, BigDecimal> multiplierCalculator, Behaviour<?> behaviour, String description) {
        super(measureName, behaviour, description, false);
        this.multiplierCalculator = multiplierCalculator;
    }

    public static NiMultipliedFilterTransactionMeasure filteredOnMeasure(String measureName, NiTransactionMeasure srcMeasure, Function<CategAmountCell, BigDecimal> multiplier, String description) {
        return new NiMultipliedFilterTransactionMeasure(measureName, cell -> srcMeasure.criterion.test(cell) ? multiplier.apply(cell) : null, description);
    }
        
    @Override
    public CategAmountCell processCell(CategAmountCell src) {
        BigDecimal multiplier = multiplierCalculator.apply(src);
        if (multiplier != null)
            return src.multiply(multiplier);
        return null;
    }   
}
