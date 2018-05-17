package org.dgfoundation.amp.nireports.behaviours;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.behavior.Behavior;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * the {@link Behavior} of a Currency entity - measures vertically divided by the original currency
 * @author Viorel Chihai
 *
 */
public class CurrencyMeasureBehaviour extends TrivialMeasureBehaviour {
    
    public static final String PREFIX_DEFAULT_CURRENCY = "~";
    public static final String UNDEFINED_CURRENCY = "Undefined";
        
    protected final String pseudoColumnName;
    
    public CurrencyMeasureBehaviour(String pseudocolumnName) {
        this.pseudoColumnName = pseudocolumnName;
    }
    
    @Override
    public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
        VSplitStrategy byOriginalCurrency = getSplittingStrategy(null, pseudoColumnName);
        return Arrays.asList(byOriginalCurrency);
    }

    public static String getOriginalCurrency(Cell cell) {
        String originalCurrency = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            originalCurrency = ((CategAmountCell) cell).amount.origCurrency.getCurrencyCode();
        }
        
        return originalCurrency;
    }
    
    public static String getOriginalCurrencyValue(AmpCurrency usedCurrency, Cell cell) {
        String originalCurrencyValue = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            originalCurrencyValue = ((CategAmountCell) cell).amount.origCurrency.getCurrencyCode();
            if (usedCurrency != null && usedCurrency.getCurrencyCode().equals(originalCurrencyValue)) {
                originalCurrencyValue = PREFIX_DEFAULT_CURRENCY + originalCurrencyValue;
            }
        }
        
        return originalCurrencyValue;
    }
    
    /**
     * Builds a splitting strategy which splits by original currency. 
     * The currend used currency should be at the end of the list, those a prefix should be added.
     * 
     * @param usedCurrency 
     * @param pseudoColumnName
     * @return
     */
    public static VSplitStrategy getSplittingStrategy(AmpCurrency usedCurrency, String pseudoColumnName) {
        VSplitStrategy byTaggedCategory = VSplitStrategy.build(
                cell -> 
                    new ComparableValue<String>(
                        getOriginalCurrency(cell.getCell()),
                        getOriginalCurrencyValue(usedCurrency, cell.getCell())), 
                pseudoColumnName,
                null,
                s -> !s.equals(usedCurrency.getCurrencyCode()));
        return byTaggedCategory;
    }
    
    @Override
    public boolean shouldDeleteLeafIfEmpty(CellColumn column) {
        return true;
    }
    
}
