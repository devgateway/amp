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
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.output.CellVisitor;
import org.dgfoundation.amp.nireports.output.NiAmountCell;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.digijava.kernel.translator.TranslatorWorker;
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
	public ReportCell visit(NiTextCell cell) {
		String text = ("".equals(cell.getDisplayedValue()) && cell.entityId != -1) ?
			TranslatorWorker.translateText("Undefined") : cell.getDisplayedValue();  
		return asTextCell(text);
	}
	
	@Override
	public ReportCell visit(NiAmountCell cell) {
		return visitNumberedCell(cell);
	}
	
	public String formatNumber(BigDecimal value) {
		if (decimalFormatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return decimalFormatter.format(value);
	}
	
	public ReportCell visitNumberedCell(NumberedCell cell) {
		BigDecimal amt = cell.getAmount();
		return new org.dgfoundation.amp.newreports.AmountCell(amt, formattedValues.computeIfAbsent(amt, this::formatNumber));
	}

	@Override
	public ReportCell visit(NiSplitCell cell) {
		return asTextCell(cell.text);
	}
	
	public org.dgfoundation.amp.newreports.TextCell asTextCell(String text) {
		return new org.dgfoundation.amp.newreports.TextCell(text);
	}
	
}
