/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.exprlogic.Values;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedAmountColumn extends TotalAmountColumn<ComputedAmountCell> {

    /**
     * @param worker
     */
    public TotalComputedAmountColumn(ColumnWorker worker) {
        super(worker);

    }

    public TotalComputedAmountColumn(String name, boolean filterShowable, int initialCapacity) {
        super(name, filterShowable, initialCapacity);
    }

    /**
     * @param name
     */
    public TotalComputedAmountColumn(String name) {
        super(name);

    }

    /**
     * @param name
     * @param filterShowable
     */
    public TotalComputedAmountColumn(String name, boolean filterShowable) {
        super(name, filterShowable);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param parent
     * @param name
     */
    public TotalComputedAmountColumn(Column parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param source
     */
    public TotalComputedAmountColumn(Column source) {
        super(source);

    }

    /**
     * Overrides the method for adding cells, to make sure we add only ComputedAmountCell
     * 
     * @param c
     *            the cell to be added
     * @see ComputedAmountCell
     */
    @Override
    public void addCell(Cell c) {
        AmountCell ac = (AmountCell) c;
        ComputedAmountCell uac = new ComputedAmountCell(ac.getOwnerId());
        uac.merge(uac, ac);
        uac.setColumn(this);
        super.addCell(uac);
    }

    public Column newInstance() {
        return new TotalComputedAmountColumn(this);
    }

    public List<ComputedAmountCell> getTrailCells() {
        ArrayList<ComputedAmountCell> ar = new ArrayList<ComputedAmountCell>();
        ComputedAmountCell ac = new ComputedAmountCell();       
        Iterator i = items.iterator();
        
        Values groupValues=new Values();
        
        while (i.hasNext()) {
            AmountCell element = (AmountCell) i.next();
            ac.merge(element,ac);
            if (element instanceof ComputedAmountCell)
                groupValues.collectTrailVariables(((ComputedAmountCell)element).getValues());
        }
        
        ///ac=new trail cell 
        ac.setColumn(this);
        int totalUniqueRows=0;
        if (this.getParent() instanceof org.dgfoundation.amp.ar.ColumnReportData) {
            totalUniqueRows=((ColumnReportData)this.getParent()).getTotalUniqueRows();
        }
        groupValues.put(ArConstants.COUNT_PROJECTS, new BigDecimal(totalUniqueRows));
        ac.setValues(groupValues);
        //set the collected values to ac
        //set the computed value to ac
        if (this.getWorker().getRelatedColumn().getTotalExpression()!=null){
            BigDecimal computedValue=MathExpressionRepository.get(this.getWorker().getRelatedColumn().getTotalExpression()).result(groupValues);
            ac.setComputedVaule(computedValue);
        }
        
        ar.add(ac);
        return ar;
    }

    
}
