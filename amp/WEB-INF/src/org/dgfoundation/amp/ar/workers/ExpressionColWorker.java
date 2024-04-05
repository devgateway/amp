/**
 * CummulativeColWorker.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ExpressionAmountCell;
import org.dgfoundation.amp.exprlogic.TokenExpression;
import org.dgfoundation.amp.exprlogic.TokenRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006
 *
 */
public class ExpressionColWorker extends ColumnWorker { 

    /**
     * @param destName
     * @param sourceGroup
     */
    public ExpressionColWorker(String condition,String viewName,String columnName,ReportGenerator generator) {
        super(condition,viewName, columnName,generator);
    }

    public ExpressionColWorker(String columnName,GroupColumn rawColumns,ReportGenerator generator) {
        super(columnName,rawColumns,generator);
        sourceName=ArConstants.COLUMN_FUNDING;
    }


    public CellColumn newColumnInstance(int initialCapacity) {
        TotalAmountColumn cc=new TotalAmountColumn(columnName,false,initialCapacity);
        return cc;
    }
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
     */
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        return null;
    }
    /**
     * We show cummulative disbursement UP TO the ToYear selected in the filters (we do not care about FromYear)
     * @param src
     * @return 
     */
    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
     */ 
    protected Cell getCellFromCell(Cell src) {
        CategAmountCell c=(CategAmountCell) src;
        String tokenExpressionString = this.getRelatedColumn().getTokenExpression();
        if(tokenExpressionString==null) return null;
        TokenExpression tokenExpression=  TokenRepository.tokens.get(tokenExpressionString);
        if(tokenExpression==null) return null;
        AmountCell evaluateAsAmountCell = tokenExpression.evaluateAsAmountCell(c);
        return evaluateAsAmountCell;
    }

    public Cell newCellInstance() {
        return new ExpressionAmountCell();  
    }

}
