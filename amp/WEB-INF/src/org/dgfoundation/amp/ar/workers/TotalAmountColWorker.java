/**
 * TotalAmountColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 11, 2006
 *
 */
public class TotalAmountColWorker extends ColumnWorker {

    /**
     * @param condition
     * @param viewName
     * @param columnName
     */
    public TotalAmountColWorker(String condition, String viewName,
            String columnName,ReportGenerator generator) {
        super(condition, viewName, columnName,generator);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param destName
     * @param sourceGroup
     */
    public TotalAmountColWorker(String destName, GroupColumn source,ReportGenerator generator, Integer position) {
        super(destName, source,generator);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
     */
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
     */
    protected Cell getCellFromCell(Cell src) {
        // TODO Auto-generated method stub
        return null;
    }

    public Cell newCellInstance() {
        return new AmountCell();
    }

}
