package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import mondrian.util.Bug;

import org.dgfoundation.amp.ar.workers.ComputedCountingAmountColWorker;
import org.dgfoundation.amp.exprlogic.TokenRepository;

public class ComputedCountingAmountCell extends ComputedAmountCell {

	private static String COMPUTED_VALUE = "COMPUTED_VALUE";
	private boolean computedVaule = false;

	public void setComputedVaule(BigDecimal value) {
		computedVaule = true;
		values.put(COMPUTED_VALUE, value);
	}

	public double getAmount() {
		if (id != null)
			return convert() * (getPercentage() / 100);
		//for each item get all funding information 
		super.collectValues();
		if (computedVaule) {
			return values.get(COMPUTED_VALUE).doubleValue();
		}

		return 0d;
	}

	public ComputedCountingAmountCell(Long ownerID) {
		super(ownerID);
	}

	public ComputedCountingAmountCell() {
		super();
	}

	public Class getWorker() {
		return ComputedCountingAmountColWorker.class;
	}

	public Cell merge(Cell c) {
		AmountCell ret = (AmountCell) super.merge(c);
		ComputedCountingAmountCell realRet = new ComputedCountingAmountCell(ret.getOwnerId());
		realRet.getMergedCells().addAll(ret.getMergedCells());
		CategAmountCell categ = (CategAmountCell) c;
		realRet.getMetaData().addAll(categ.getMetaData());
		return realRet;
	}

	public Cell newInstance() {
		return new ComputedCountingAmountCell();
	}

	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public HashMap<String, BigDecimal> getValues() {
		return values;
	}

	@Override
	public Cell filter(Cell metaCell, Set ids) {
		return super.filter(metaCell, ids);
	}
}
