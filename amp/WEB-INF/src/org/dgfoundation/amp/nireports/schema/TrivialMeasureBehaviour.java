package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.List;

import org.dgfoundation.amp.nireports.AmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.runtime.HierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * the behaviour of a trivial measure
 * @author Dolghier Constantin
 *
 */
public class TrivialMeasureBehaviour implements Behaviour {
	public static TrivialMeasureBehaviour getInstance() {return instance;}
	private final static TrivialMeasureBehaviour instance = new TrivialMeasureBehaviour();
	private TrivialMeasureBehaviour() {}
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.MONTH;
	}
	
	@Override
	public Cell doHorizontalReduce(List<NiCell> cells, HierarchiesTracker hiersTracker) {
		MonetaryAmount res = new MonetaryAmount(BigDecimal.ZERO, ((NumberedCell) cells.get(0).getCell()).getAmount().precisionSetting);
		for(NiCell cell:cells) {
			BigDecimal percentage = hiersTracker.calculatePercentage(cell.getMainId(), cell.getEntity().getBehaviour());
			res = res.add(((NumberedCell) cell.getCell()).getAmount().multiplyBy(percentage));
		}
		return new AmountCell(cells.get(0).getMainId(), res);
	}

	@Override
	public Cell getZeroCell() {
		return new AmountCell(-1, new MonetaryAmount(BigDecimal.ZERO, NiPrecisionSetting.IDENTITY_PRECISION_SETTING));
	}
}
