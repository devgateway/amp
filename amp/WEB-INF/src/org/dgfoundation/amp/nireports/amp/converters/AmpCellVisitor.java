/**
 * 
 */
package org.dgfoundation.amp.nireports.amp.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.output.CellVisitor;
import org.dgfoundation.amp.nireports.output.NiAmountCell;
import org.dgfoundation.amp.nireports.output.NiReportData;
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
	private List<NiReportData> currentRDs = new ArrayList<>();
	private int level = -1;
	private final int levelReset;
	private boolean isLeaf = false;
	
	public AmpCellVisitor(ReportSpecification spec, int columnsCount) {
		if (spec.getSettings() != null && spec.getSettings().getCurrencyFormat() != null) {
			this.decimalFormatter = spec.getSettings().getCurrencyFormat();
		} else {
			this.decimalFormatter = FormatHelper.getDefaultFormat();
		}
		this.levelReset = columnsCount;
	}
	
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public void incLevel() {
		level = (++level) % levelReset;
	}
	
	public void pushRD(NiReportData niReportData) {
		currentRDs.add(niReportData);
	}
	
	public void popRD() {
		if (currentRDs.size() > 0)
			currentRDs.remove(currentRDs.size() - 1);
	}

	@Override
	public ReportCell visit(NiTextCell cell) {
		if (!isLeaf || level < currentRDs.size()) {
			if (!isLeaf && level == currentRDs.size() - 1)
				return visit(currentRDs.get(level).splitter);
			else
				return null;
		}
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
		return asTextCell(cell.undefined ? cell.entity.name + ": " + TranslatorWorker.translateText("Undefined"): cell.text);
	}
	
	public org.dgfoundation.amp.newreports.TextCell asTextCell(String text) {
		return new org.dgfoundation.amp.newreports.TextCell(text);
	}
	
}
