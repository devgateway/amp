package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.output.NiAmountCell;
import org.dgfoundation.amp.nireports.output.NiOutCell;
import org.dgfoundation.amp.nireports.output.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * the behaviour of a trivial measure
 * @author Dolghier Constantin
 *
 */
public class TrivialMeasureBehaviour implements Behaviour<NiAmountCell> {
	public static TrivialMeasureBehaviour getInstance() {return instance;}
	public static TrivialMeasureBehaviour getTotalsOnlyInstance() {return totalsOnlyInstance;}
	
	protected final TimeRange timeRange;
	
	private final static TrivialMeasureBehaviour instance = new TrivialMeasureBehaviour();
	private final static TrivialMeasureBehaviour totalsOnlyInstance = new TrivialMeasureBehaviour(TimeRange.NONE);
	
	protected TrivialMeasureBehaviour() {
		this(TimeRange.MONTH);
	}
	
	protected TrivialMeasureBehaviour(TimeRange timeRange) {
		this.timeRange = timeRange;
	}
	
	@Override
	public TimeRange getTimeRange() {
		return /*TimeRange.MONTH; */ timeRange;
	}
	
	@Override
	public NiAmountCell doHorizontalReduce(List<NiCell> cells) {
		NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
		BigDecimal res = precision.adjustPrecision(BigDecimal.ZERO);
		for(NiCell cell:cells) {
			BigDecimal percentage = cell.calculatePercentage();
			BigDecimal toAdd = ((NumberedCell) cell.getCell()).getAmount().multiply(percentage);
			res = res.add(toAdd);
		}
		//System.err.format("reduced %d cells to %.2f: %s\n", cells.size(), res.doubleValue(), cells.toString());
		return new NiAmountCell(res, precision);
	}

	@Override
	public NiAmountCell getZeroCell() {
		return NiAmountCell.ZERO;
	}
	
	@Override
	public NiAmountCell doVerticalReduce(Collection<NiAmountCell> cells) {
		if (cells.isEmpty()) {
			return getZeroCell();
		}
		
		java.util.Iterator<NiAmountCell> it = cells.iterator();
		NiAmountCell first = it.next();
		NiPrecisionSetting precisionSetting = first.getPrecision();
		BigDecimal res = first.getAmount();
		
		while(it.hasNext())
			res = res.add(it.next().amount);
		return new NiAmountCell(res, precisionSetting);
	}

	@Override
	public NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
		throw new RuntimeException("doing hierarchies by numeric values not supported");
	}

	@Override
	public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
		throw new RuntimeException("doing hierarchies by numeric values not supported");
	}

	@Override
	public boolean isKeepingSubreports() {
		return true;
	}

	@Override
	public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
		// trivial measures are copied verbatim to totals
		return new ImmutablePair<String, ColumnContents>(entity.name, fetchedContents);
	}

	@Override
	public NiOutCell getEmptyCell(ReportSpecification spec) {
		return NiAmountCell.ZERO;
	}

	@Override
	public boolean hasPercentages() {
		return false;
	}

}
