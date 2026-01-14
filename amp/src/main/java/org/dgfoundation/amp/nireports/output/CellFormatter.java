package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.output.nicells.*;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dgfoundation.amp.algo.AmpCollections.any;

/**
 * a {@link CellVisitor} used to transform instances of {@link NiOutCell} into instances of {@link ReportCell}
 * @author Dolghier Constantin
 *
 */
public class CellFormatter implements CellVisitor<ReportCell> {

    final protected NiReportDateFormatter dateFormatter;
    final protected DecimalFormat decimalFormatter;
    final protected OutputSettings outputSettings;
    final protected Map<BigDecimal, String> scaledAndFormattedAmounts = new HashMap<>();
    final protected Map<BigDecimal, String> formattedAmounts = new HashMap<>();
    final protected Function<String, String> translator;
    final protected AmountsUnits amountsUnits;
    final protected BigDecimal unitsDivider;
    
    public CellFormatter(ReportSettings settings, DecimalFormat defaultDecimalFormatter, 
            String dateDisplayFormat, Function<String, String> translator, OutputSettings outputSettings, 
            CalendarConverter defaultCalendar) {
        
        this.decimalFormatter = (settings != null && settings.getCurrencyFormat() != null) 
                ? settings.getCurrencyFormat() : defaultDecimalFormatter;
        this.amountsUnits = (settings != null && settings.getUnitsOption() != null) 
                ? settings.getUnitsOption() : AmountsUnits.AMOUNTS_OPTION_UNITS;
        this.unitsDivider = BigDecimal.valueOf(this.amountsUnits.divider);
        this.outputSettings = outputSettings;
        this.translator = translator;
        this.dateFormatter = new NiReportDateFormatter(settings, dateDisplayFormat, defaultCalendar);
    }

    private String scaleAndFormatAmount(BigDecimal value) {
        return formatAmount(value.divide(unitsDivider));
    }

    private String formatAmount(BigDecimal value) {
        if (decimalFormatter == null)
            return String.valueOf(value);
        return decimalFormatter.format(value);
    }

    public ReportCell visitNumberedCell(NumberedCell cell) {
        BigDecimal amt = cell.getAmount();
        String formattedAmount;
        if (cell.isScalableByUnits()) {
            formattedAmount = scaledAndFormattedAmounts.computeIfAbsent(amt, this::scaleAndFormatAmount);
        } else {
            formattedAmount = formattedAmounts.computeIfAbsent(amt, this::formatAmount);
        }
        return new org.dgfoundation.amp.newreports.AmountCell(amt, formattedAmount);
    }
    
    @Override
    public ReportCell visit(NiAmountCell cell, CellColumn currentColumn) {
        return visitNumberedCell(cell);
    }

    @Override
    public ReportCell visit(NiTextCell cell, CellColumn currentColumn) {
        String text = cell.getDisplayedValue();
        return asTextCell(text, cell.entityId, outputSettings.needsIdsValues(currentColumn) ? cell.entitiesIdsValues : null);
    }

    @Override
    public ReportCell visit(NiSplitCell cell, CellColumn currentColumn) {
        boolean needSubCells = outputSettings.needsIdsValues(currentColumn);
        return asTextCell(
            cell.undefined ? (translate(cell.entity.name) + ": " + translate("Undefined")) : cell.text,
            any(cell.entityIds, -1l), 
            needSubCells ? cell.entityIds.stream().collect(Collectors.toMap(z -> z, z -> cell.text)) : null);
    }

    @Override
    public ReportCell visit(NiDateCell cell, CellColumn currentColumn) {
        String formattedValue = cell.values.stream().map(this::formatDate).collect(Collectors.joining(", "));
        return new DateCell(cell.comparableToken, formattedValue, cell.entityId, cell.entitiesIdsValues);
    }

    private String formatDate(LocalDate date) {
        return dateFormatter.formatDate(date);
    }

    protected String translate(String str) {
        return translator.apply(str);
    }
    
    public TextCell asTextCell(String text, long entityId, Map<Long, String> entityIdsValues) {
        TextCell tc = new TextCell(text, entityId, entityIdsValues);
        return tc;
    }

    @Override
    public IntCell visit(NiIntCell cell, CellColumn currentColumn) {
        IntCell res = new IntCell(cell.value, cell.entityId);
        return res;
    }

    @Override
    public ReportCell visit(NiFormulaicAmountCell cell, CellColumn currentColumn) {
        if (cell.isDefined())
            return visitNumberedCell(cell);
        return null;
    }
}
