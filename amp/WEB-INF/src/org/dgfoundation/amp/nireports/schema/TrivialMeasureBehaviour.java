package org.dgfoundation.amp.nireports.schema;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.nireports.AmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.runtime.MultiHierarchiesTracker;
import org.dgfoundation.amp.nireports.runtime.NiCell;

/**
 * the behaviour of a trivial measure
 * @author Dolghier Constantin
 *
 */
public class TrivialMeasureBehaviour implements Behaviour<AmountCell> {
	public static TrivialMeasureBehaviour getInstance() {return instance;}
	private final static TrivialMeasureBehaviour instance = new TrivialMeasureBehaviour();
	private TrivialMeasureBehaviour() {}
	
	@Override
	public TimeRange getTimeRange() {
		return TimeRange.MONTH;
	}
	
	@Override
	public AmountCell doHorizontalReduce(List<NiCell> cells) {
		NiPrecisionSetting precision = ((NumberedCell) cells.get(0).getCell()).getPrecision();
		BigDecimal res = precision.adjustPrecision(BigDecimal.ZERO);
		for(NiCell cell:cells) {
			BigDecimal percentage = cell.calculatePercentage();
			BigDecimal toAdd = ((NumberedCell) cell.getCell()).getAmount().multiply(percentage);
			res = res.add(toAdd);
		}
		return new AmountCell(cells.get(0).getMainId(), new MonetaryAmount(res, precision));
	}

	@Override
	public AmountCell getZeroCell() {
		return new AmountCell(-1, new MonetaryAmount(BigDecimal.ZERO, NiPrecisionSetting.IDENTITY_PRECISION_SETTING));
	}
	
	@Override
	public AmountCell doVerticalReduce(Collection<AmountCell> cells) {
		if (cells.isEmpty()) {
			return getZeroCell();
		}
		
		java.util.Iterator<AmountCell> it = cells.iterator();
		MonetaryAmount res = it.next().amount;
		while(it.hasNext())
			res = res.add(it.next().amount);
		return new AmountCell(-1, res);
	}
}
