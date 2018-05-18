package org.dgfoundation.amp.nireports.behaviours;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.NiCurrency;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.digijava.module.aim.dbentity.AmpCurrency;

/**
 * the {@link VSplitStrategy} of a Currency entity - measures vertically divided by the original currency
 * @author Viorel Chihai
 *
 */
public class CurrencyMeasureSplittingStrategy implements VSplitStrategy {
    
    public static final String PREFIX_DEFAULT_CURRENCY = "~";
    public static final String UNDEFINED_CURRENCY = "Undefined";
    
    private final Function<NiCell, ComparableValue<String>> cat;
    private final AmpCurrency usedCurrency;
        
    public CurrencyMeasureSplittingStrategy(Function<NiCell, ComparableValue<String>> cat, AmpCurrency usedCurrency) {
        this.cat = cat;
        this.usedCurrency = usedCurrency;
    }

    public static String getCurrencyCode(AmpCurrency usedCurrency, Cell cell) {
        String currencyCode = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            CategAmountCell categCell = (CategAmountCell) cell;
            NiCurrency currency = categCell.hasConvertedAmount() ? usedCurrency : categCell.amount.origCurrency;
            currencyCode = currency.getCurrencyCode();
        }
        
        return currencyCode;
    }
    
    public static String getCurrencyValue(AmpCurrency usedCurrency, Cell cell) {
        String currencyValue = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            CategAmountCell categCell = (CategAmountCell) cell;
            String categCurrencyCode = categCell.amount.origCurrency.getCurrencyCode();
            if (categCell.hasConvertedAmount() || categCurrencyCode.equals(usedCurrency.getCurrencyCode())) {
                currencyValue = PREFIX_DEFAULT_CURRENCY + usedCurrency.getCurrencyCode();
            } else {
                currencyValue = categCurrencyCode;
            }
        }
        
        return currencyValue;
    }
    
    /**
     * Builds a splitting strategy which splits by original currency. 
     * The currend used currency should be at the end of the list, those a prefix should be added.
     * 
     * @param usedCurrency 
     * @param pseudoColumnName
     * @return
     */
    public static VSplitStrategy getInstance(AmpCurrency usedCurrency, String pseudoColumnName) {
        VSplitStrategy byCurrency = new CurrencyMeasureSplittingStrategy(cell -> 
                            new ComparableValue<String>(
                                    getCurrencyCode(usedCurrency, cell.getCell()),
                                    getCurrencyValue(usedCurrency, cell.getCell())), 
                            usedCurrency);
        
        return byCurrency;
    }

    @Override
    public ComparableValue<String> categorize(NiCell cell) {
        return cat.apply(cell);
    }
    
    @Override
    public List<ComparableValue<String>> getSubcolumnsNames(Set<ComparableValue<String>> existant, boolean isTotal) {
        if (isTotal) {
            existant.removeIf(val -> !val.getValue().equals(usedCurrency.getCurrencyCode()));
        }
        
        return VSplitStrategy.super.getSubcolumnsNames(existant, isTotal);
    }

    public Function<NiCell, ComparableValue<String>> getCat() {
        return cat;
    }

    public AmpCurrency getUsedCurrency() {
        return usedCurrency;
    }
    
}
