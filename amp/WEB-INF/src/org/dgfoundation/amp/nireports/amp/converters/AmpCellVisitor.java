/**
 * 
 */
package org.dgfoundation.amp.nireports.amp.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.AmountCell;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.CellVisitor;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.TextCell;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * Amp Visitor for NiReports cells 
 * @author Nadejda Mandrescu
 */
public class AmpCellVisitor implements CellVisitor<ReportCell> {
	
	private DecimalFormat decimalFormatter;
	private Map<BigDecimal, String> formattedValues = new HashMap<>();
	
	public AmpCellVisitor(ReportSpecification spec) {
		if (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null) {
			this.decimalFormatter = spec.getSettings().getCurrencyFormat();
		} else {
			this.decimalFormatter = FormatHelper.getDefaultFormat();
		}
	}

	@Override
	public ReportCell visit(TextCell cell) {
		return new org.dgfoundation.amp.newreports.TextCell(cell.getDisplayedValue());
	}
	
	@Override
	public ReportCell visit(PercentageTextCell cell) {
		return new org.dgfoundation.amp.newreports.TextCell(cell.getDisplayedValue());
	}

	@Override
	public ReportCell visit(AmountCell cell) {
		return visitNumberedCell(cell);
	}

	@Override
	public ReportCell visit(CategAmountCell cell) {
		return visitNumberedCell(cell);
	}
	
	public String formatNumber(BigDecimal value) {
		if (decimalFormatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return decimalFormatter.format(value.doubleValue()); // TODO: research a BigDecimal formatter?
	}
	
	public ReportCell visitNumberedCell(NumberedCell cell) {
		BigDecimal amt = cell.getAmount();
		return new org.dgfoundation.amp.newreports.AmountCell(amt, formattedValues.computeIfAbsent(amt, this::formatNumber));
	}
	
}
