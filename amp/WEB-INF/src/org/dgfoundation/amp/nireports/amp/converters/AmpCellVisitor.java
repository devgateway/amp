/**
 * 
 */
package org.dgfoundation.amp.nireports.amp.converters;

import java.text.DecimalFormat;

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
	
	public ReportCell visitNumberedCell(NumberedCell cell) {
		return new org.dgfoundation.amp.newreports.AmountCell(((NumberedCell) cell).getAmount().amount, 
				decimalFormatter);
	}
	
}
