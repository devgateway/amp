package org.dgfoundation.amp.nireports.behaviours;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCurrency;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * the {@link VSplitStrategy} of a Currency entity - measures vertically divided by the original currency
 * @author Viorel Chihai
 *
 */
public class CurrencySplittingStrategy implements VSplitStrategy {
    
    public static final String UNDEFINED_CURRENCY = "Undefined";
    public static final String PREFIX_TOTAL_VALUE = "~";
    public static final String PREFIX_TOTAL = "TOTAL";
    
    private final Function<NiCell, ComparableValue<String>> cat;
    private final AmpCurrency usedCurrency;
        
    public CurrencySplittingStrategy(Function<NiCell, ComparableValue<String>> cat, AmpCurrency usedCurrency) {
        this.cat = cat;
        this.usedCurrency = usedCurrency;
    }

    /**
     * 
     * @param usedCurrency the used currency
     * @param cell cell
     * @return the currency code. For cells with informative amount it would be the original currency
     */
    public static String getCurrencyCode(AmpCurrency usedCurrency, Cell cell) {
        String currencyCode = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            CategAmountCell categCell = (CategAmountCell) cell;
            if (categCell.isInformativeAmount()) {
                currencyCode = categCell.amount.origCurrency.getCurrencyCode();
            } else {
                currencyCode = getTotalCurrencyCode(usedCurrency);
            }
        }
        
        return currencyCode;
    }
    
    /**
     * 
     * @param usedCurrency the used currency
     * @param cell cell
     * @return the currency value. For cells with non-informative amount it would be the used currency.
     * The prefix is added to make the currency be the last in the list during the normal sorting.
     */
    public static String getCurrencyValue(AmpCurrency usedCurrency, Cell cell) {
        String currencyValue = UNDEFINED_CURRENCY;
        if (cell instanceof CategAmountCell) {
            CategAmountCell categCell = (CategAmountCell) cell;
            if (categCell.isInformativeAmount()) {
                currencyValue = categCell.amount.origCurrency.getCurrencyCode();
            } else {
                currencyValue = getTotalCurrencyValue(usedCurrency);
            }
        }
        
        return currencyValue;
    }
    
    /**
     * Builds a splitting strategy which splits by original currency. 
     * The current used currency should be at the end of the list, those a prefix should be added.
     * 
     * @param usedCurrency 
     * @return
     */
    public static VSplitStrategy getInstance(AmpCurrency usedCurrency) {
        VSplitStrategy byCurrency = new CurrencySplittingStrategy(cell -> 
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
            existant.removeIf(val -> !val.getValue().contains(PREFIX_TOTAL));
        }
        
        return VSplitStrategy.super.getSubcolumnsNames(existant, isTotal);
    }

    public Function<NiCell, ComparableValue<String>> getCat() {
        return cat;
    }

    public AmpCurrency getUsedCurrency() {
        return usedCurrency;
    }
    
    @Override
    public ComparableValue<String> getEmptyTotalSubcolumnName() {
        return new ComparableValue<>(getTotalCurrencyCode(usedCurrency), getTotalCurrencyValue(usedCurrency));
    }
    
    private static String getTotalCurrencyCode(AmpCurrency usedCurrency) {
        return String.format("%s %s", TranslatorWorker.translateText(PREFIX_TOTAL), usedCurrency.getCurrencyCode());
    }
    
    private static String getTotalCurrencyValue(AmpCurrency usedCurrency) {
        return String.format("%s%s %s", PREFIX_TOTAL_VALUE, 
                TranslatorWorker.translateText(PREFIX_TOTAL), usedCurrency.getCurrencyCode());
    }
    
}
