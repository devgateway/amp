package org.dgfoundation.amp.ar.cell;

import java.math.BigDecimal;
import java.util.Iterator;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.exprlogic.MathExpression;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.Values;
import org.digijava.module.aim.helper.FormatHelper;

public class ComputedMeasureCell extends AmountCell {

    Values values = null;

    private static String COMPUTED_VALUE = "COMPUTED_VALUE";

    // if is a trail cell the value will be set by the Column
    public void setComputedVaule(BigDecimal value) {
        getValues().put(COMPUTED_VALUE, value);
    }

    public ComputedMeasureCell() {
        super();
    }

    public ComputedMeasureCell(AmountCell ac) {
        super(ac.getOwnerId());
        this.mergedCells = ac.getMergedCells();
    }

    /**
     * @param id
     */
    public ComputedMeasureCell(Long id) {
        super(id);
    }

    /**
     * Overrider of the normal behavior of AmountCell.getAmount. This will take
     * into consideration only undisbursed related merged cells and will also
     * make the required calculations
     * 
     * @return Returns the amount.
     */
    public double getAmount() {
        BigDecimal ret = new BigDecimal(0);
        if (id != null)
            return (convert() * (getPercentage() / 100));

        MathExpression math = null;
        if (getValues().containsKey(COMPUTED_VALUE)) {
            BigDecimal val = getValues().get(COMPUTED_VALUE);
            if (val != null) {
                return val.doubleValue() * (getPercentage() / 100);
            } else {
                return 0d;
            }

        } else {
            if (this.getColumn().getExpression() != null) {
                math = MathExpressionRepository.get(this.getColumn().getExpression());
            } else {
                math = MathExpressionRepository.get(this.getColumn().getWorker().getRelatedColumn().getTokenExpression());
            }
            BigDecimal val=math.result(getValues());
            if (val!=null){
                return val.doubleValue();
            }else{
                return 0d;
            }
        }
    }

    @Override
    public AmountCell merge(Cell c) {
        AmountCell ac = (AmountCell) super.merge(c);
        ComputedMeasureCell uac = new ComputedMeasureCell(ac);
        return uac;
    }

    @Override
    public AmountCell newInstance() {
        return new ComputedMeasureCell();
    }

    public String toString() {
        return ReportContextData.formatNumberUsingCustomFormat(getAmount());
    }

    public Values getValues() {
        if (values == null) {
            values = new Values();
            Iterator<AmountCell> i = mergedCells.iterator();
            while (i.hasNext()) {
                values.collectCellVariables((CategAmountCell) i.next());
            }
        }
        values.importValues(getNearestReportData());
        return values;
    }
    
    public void setValues(Values values) {
        this.values = values;
    }   
    
}
