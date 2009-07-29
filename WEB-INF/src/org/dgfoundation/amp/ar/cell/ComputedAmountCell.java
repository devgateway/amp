package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.workers.ComputedAmountColWorker;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.TokenRepository;

public class ComputedAmountCell extends CategAmountCell {

	HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>();

	public Class getWorker() {
		return ComputedAmountColWorker.class;
	}

	public void collectValues() {
		
		Iterator<CategAmountCell> i = mergedCells.iterator();
		// total
		BigDecimal totalCommitments = new BigDecimal(0);
		// planed
		BigDecimal actualCommitments = new BigDecimal(0);
		BigDecimal actualDisburments = new BigDecimal(0);
		BigDecimal actualExpenditures = new BigDecimal(0);

		// actual
		BigDecimal plannedCommitments = new BigDecimal(0);
		BigDecimal plannedDisburments = new BigDecimal(0);
		BigDecimal plannedExpenditures = new BigDecimal(0);
		
		BigDecimal countActualCommitments = new BigDecimal(0);
		BigDecimal countActualDisburments = new BigDecimal(0);
		BigDecimal countPlanedCommitments = new BigDecimal(0);
		BigDecimal countPlanedDisburments = new BigDecimal(0);


		//for each element get each funding type
		while (i.hasNext()) {
			ComputedAmountCell element = (ComputedAmountCell) i.next();
			//using the logicExpression we will get the result of each funding type
			totalCommitments = totalCommitments.add(new BigDecimal(TokenRepository.buildTotalCommitmentsLogicalToken().evaluate(element)));
			actualCommitments = actualCommitments.add(new BigDecimal(TokenRepository.buildActualCommitmentsLogicalToken().evaluate(element)));
			actualDisburments = actualDisburments.add(new BigDecimal(TokenRepository.buildActualDisbursementsLogicalToken().evaluate(element)));
			plannedCommitments = plannedCommitments.add(new BigDecimal(TokenRepository.buildPLannedCommitmentsLogicalToken().evaluate(element)));
			plannedDisburments = plannedDisburments.add(new BigDecimal(TokenRepository.buildPLannedDisbursementsLogicalToken().evaluate(element)));

			if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.ACTUAL)) {
				if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
					countActualCommitments = countActualCommitments.add(new BigDecimal(1));
				}
				if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
					countActualDisburments = countActualDisburments.add(new BigDecimal(1));
				}

			}

			if (element.getMetaValueString(ArConstants.ADJUSTMENT_TYPE).equalsIgnoreCase(ArConstants.PLANNED)) {
				if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.COMMITMENT)) {
					countPlanedCommitments = countPlanedCommitments.add(new BigDecimal(1));
				}
				if (element.getMetaValueString(ArConstants.TRANSACTION_TYPE).equalsIgnoreCase(ArConstants.DISBURSEMENT)) {
					countPlanedDisburments = countPlanedDisburments.add(new BigDecimal(1));
				}
			}
		}
		// crate variable values map
		values.put(ArConstants.TOTAL_COMMITMENTS, totalCommitments);
		values.put(ArConstants.ACTUAL_COMMITMENT, actualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT, actualDisburments);
		// values.put(ArConstants.ACTUAL_EXPENDITURET, total_commitments);
		values.put(ArConstants.PLANNED_COMMITMENT, plannedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT, plannedDisburments);
		// values.put(ArConstants.PLANNED_EXPENDITURE, total_commitments);
		values.put(ArConstants.ACTUAL_COMMITMENT_COUNT, countActualCommitments);
		values.put(ArConstants.ACTUAL_DISBURSEMENT_COUNT, countActualDisburments);

		values.put(ArConstants.PLANNED_COMMITMENT_COUNT, countPlanedCommitments);
		values.put(ArConstants.PLANNED_DISBURSEMENT_COUNT, countPlanedDisburments);
	}

	public double getAmount() {
		BigDecimal ret = new BigDecimal(0);
		if (id != null)
			return (convert() * (getPercentage() / 100));
		collectValues();
		
		MathExpression math=null;
		
		if (this.getColumn().getExpression()!=null){
			math=MathExpressionRepository.get(this.getColumn().getExpression());
		}else{
			math=MathExpressionRepository.get(this.getColumn().getWorker().getRelatedColumn().getTokenExpression());
		}
		return math.result(values).doubleValue();
	}

	public ComputedAmountCell(Long ownerId) {
		super(ownerId);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public ComputedAmountCell(AmountCell ac) {
		super(ac.getOwnerId());
		this.setColumn(ac.getColumn());
		this.mergedCells = ac.getMergedCells();
	}

	public ComputedAmountCell() {
		super();
	}

	public void merge(Cell c1, Cell c2) {
		super.merge(c1, c2);
		CategAmountCell categ1 = (CategAmountCell) c1;
		CategAmountCell categ2 = (CategAmountCell) c2;
		categ1.getMetaData().addAll(categ2.getMetaData());
	}

	public Cell merge(Cell c) {
		AmountCell ret = (AmountCell) super.merge(c);
		ComputedAmountCell realRet = new ComputedAmountCell(ret.getOwnerId());
		realRet.getMergedCells().addAll(ret.getMergedCells());
		CategAmountCell categ = (CategAmountCell) c;
		realRet.getMetaData().addAll(categ.getMetaData());
		return realRet;

	}

	public Cell newInstance() {
		return new ComputedAmountCell();
	}

	@Override
	public Cell filter(Cell metaCell, Set ids) {
		Cell ret = super.filter(metaCell, ids);
		if (ret != null) {
			ret.setColumn(this.getColumn());
		}
		return ret;
	}

	public void setValuesFromCell(CategAmountCell categ){
		ComputedAmountCell cell = new ComputedAmountCell();
		
		this.setId(categ.getId());
		this.setOwnerId(categ.getOwnerId());
		this.setValue(categ.getValue());
		this.setFromExchangeRate(categ.getFromExchangeRate());
		this.setCurrencyDate(categ.getCurrencyDate());
		this.setCurrencyCode(categ.getCurrencyCode());
		this.setToExchangeRate(categ.getToExchangeRate());
		this.setColumn(categ.getColumn());
		this.setColumnCellValue(categ.getColumnCellValue());
		this.setColumnPercent(categ.getColumnPercent());
		this.setCummulativeShow(categ.isCummulativeShow());
		this.setShow(categ.isShow());
		this.setRenderizable(categ.isRenderizable());
		this.setCummulativeShow(categ.isCummulativeShow());
		this.setMetaData(categ.getMetaData());
		
	}
	
}
