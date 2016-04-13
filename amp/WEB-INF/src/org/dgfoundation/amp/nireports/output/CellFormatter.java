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

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.newreports.DateCell;
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
	
	public CellFormatter(ReportSettings settings, DecimalFormat defaultDecimalFormatter, String dateDisplayFormat, Function<String, String> translator, OutputSettings outputSettings) {
		if (settings != null && settings.getCurrencyFormat() != null) {
			this.decimalFormatter = settings.getCurrencyFormat();
		} else {
			this.decimalFormatter = defaultDecimalFormatter;
		}
		this.outputSettings = outputSettings;
		this.dateFormatter = DateTimeFormatter.ofPattern(dateDisplayFormat);
		this.translator = translator;
	}

	public String formatNumber(BigDecimal value) {
		if (decimalFormatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return decimalFormatter.format(value);
	}
	
	public ReportCell visitNumberedCell(NumberedCell cell) {
		BigDecimal amt = cell.getAmount();
//		if (!amt.equals(BigDecimal.ZERO))
//			System.out.print("!");
		return new org.dgfoundation.amp.newreports.AmountCell(amt, formattedValues.computeIfAbsent(amt, this::formatNumber));
	}
	
	@Override
	public ReportCell visit(NiAmountCell cell, CellColumn currentColumn) {
		return visitNumberedCell(cell);
	}

	@Override
	public ReportCell visit(NiTextCell cell, CellColumn currentColumn) {
		String text = (StringUtils.isEmpty(cell.getDisplayedValue()) && cell.entityId > 0) ?
			translate("Undefined") : cell.getDisplayedValue();  
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
}
