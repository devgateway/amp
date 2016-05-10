package org.dgfoundation.amp.nireports.output;

import static org.dgfoundation.amp.algo.AmpCollections.any;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.DateCell;
import org.dgfoundation.amp.newreports.IntCell;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

/**
 * a {@link CellVisitor} used to transform instances of {@link NiOutCell} into instances of {@link ReportCell}
 * @author Dolghier Constantin
 *
 */
public class CellFormatter implements CellVisitor<ReportCell> {

	final protected DateTimeFormatter dateFormatter;
	final protected DecimalFormat decimalFormatter;
	final protected OutputSettings outputSettings;
	final protected Map<BigDecimal, String> formattedValues = new HashMap<>();
	final protected Function<String, String> translator;
	final protected AmountsUnits amountsUnits;
	final protected BigDecimal unitsDivider;
	
	public CellFormatter(ReportSettings settings, DecimalFormat defaultDecimalFormatter, String dateDisplayFormat, Function<String, String> translator, OutputSettings outputSettings) {
		this.decimalFormatter = (settings != null && settings.getCurrencyFormat() != null) ? settings.getCurrencyFormat() : defaultDecimalFormatter;
		this.amountsUnits = (settings != null && settings.getUnitsOption() != null) ? settings.getUnitsOption() : AmountsUnits.AMOUNTS_OPTION_UNITS;
		this.unitsDivider = BigDecimal.valueOf(this.amountsUnits.divider);
		this.outputSettings = outputSettings;
		this.dateFormatter = DateTimeFormatter.ofPattern(dateDisplayFormat);
		this.translator = translator;
	}

	public String formatScalableAmount(BigDecimal value) {
		value = value.divide(unitsDivider);
		if (decimalFormatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return decimalFormatter.format(value);
	}
	
	public ReportCell visitNumberedCell(NumberedCell cell) {
		BigDecimal amt = cell.getAmount();
		if (cell.isScalableByUnits()) {
			return new org.dgfoundation.amp.newreports.AmountCell(amt, formattedValues.computeIfAbsent(amt, this::formatScalableAmount));
		}
		return new org.dgfoundation.amp.newreports.AmountCell(amt, String.valueOf(amt));
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
			cell.undefined ? (cell.entity.name + ": " + translate("Undefined")) : cell.text, 
			any(cell.entityIds, -1l), 
			needSubCells ? cell.entityIds.stream().collect(Collectors.toMap(z -> z, z -> cell.text)) : null);
	}

	@Override
	public ReportCell visit(NiDateCell cell, CellColumn currentColumn) {
		List<String> formattedDates = cell.sortedValues.stream().map(date -> date.format(dateFormatter)).collect(Collectors.toList());
		String formattedValue = String.join(", ", formattedDates);
		return new DateCell(cell.comparableToken, formattedValue, cell.entityId, cell.entitiesIdsValues);
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
}
