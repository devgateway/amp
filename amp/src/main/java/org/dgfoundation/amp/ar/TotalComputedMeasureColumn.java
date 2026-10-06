/**
 * 
 */
package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedMeasureCell;
import org.dgfoundation.amp.ar.workers.ColumnWorker;
import org.dgfoundation.amp.exprlogic.Values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class TotalComputedMeasureColumn extends TotalAmountColumn {

//  private BigDecimal totalVariable;

    /**
     * @param worker
     */
    public TotalComputedMeasureColumn(ColumnWorker worker) {
        super(worker);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param name
     */
    public TotalComputedMeasureColumn(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param name
     * @param filterShowable
     */
    public TotalComputedMeasureColumn(String name, boolean filterShowable) {
        super(name, filterShowable);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param parent
     * @param name
     */
    public TotalComputedMeasureColumn(Column parent, String name) {
        super(parent, name);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param source
     */
    public TotalComputedMeasureColumn(Column source) {
        super(source);
        this.setExpression(source.getExpression());
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Overrides the method for adding cells, to make sure we add only UndisbursedAmountCellS
     * 
     * @param c
     *            the cell to be added
     * @see UndisbursedAmountCell
     */
    @Override
    public void addCell(Cell c) {
        AmountCell ac = (AmountCell) c;
        ComputedMeasureCell uac = new ComputedMeasureCell(ac.getOwnerId());
        uac.merge(uac, ac);
        super.addCell(uac);
    }

    public Column newInstance() {
        return new TotalComputedMeasureColumn(this);
    }

    public List<ComputedMeasureCell> getTrailCells() {
        ArrayList<ComputedMeasureCell> ar = new ArrayList<ComputedMeasureCell>();
        ComputedMeasureCell ac = new ComputedMeasureCell();
        Iterator i = items.iterator();
        Values groupValues=new Values();

        while (i.hasNext()) {
            Object el = i.next();
            ComputedMeasureCell element = (ComputedMeasureCell) el;
            ac.merge(element, ac);
            groupValues.collectTrailVariables(((ComputedMeasureCell) element).getValues());
        }
        ac.setColumn(this);
        ReportData root = getRootReportData(this.getNearestReportData());
        if(root instanceof GroupReportData && ((GroupReportData)root).getTotalActualCommitments() != null)
            groupValues.put(ArConstants.GRAND_TOTAL_ACTUAL_COMMITMENTS, ((GroupReportData)root).getTotalActualCommitments());
        
        ac.setValues(groupValues);
        
        ar.add(ac);
        return ar;
    }
    
    private ReportData getRootReportData(ReportData rp) {
        if (rp.parent != null)
            rp = getRootReportData(rp.parent);
        return rp;
    }

}
