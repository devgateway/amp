/**
 * DateColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedDateCell;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComputedDateColWorker extends ColumnWorker {

    private static final String AMP_ACTIVITY_ID = "amp_activity_id";
    private static final String ACTUAL_START_DATE = "actual_start_date";
    private static final String ACTUAL_APPROVAL_DATE = "actual_approval_date";
    //private static final String ACTIVITY_APPROVAL_DATE = "activity_approval_date";
    private static final String PROPOSED_START_DATE = "proposed_start_date";
    private static final String ACTUAL_COMPLETION_DATE = "actual_completion_date";
    private static final String PROPOSED_COMPLETION_DATE = "proposed_completion_date";
    private static final String ORIGINAL_COMPLETION_DATE = "original_comp_date";

    /**
     * @param condition
     * @param viewName
     * @param columnName
     */
    public ComputedDateColWorker(String condition, String viewName, String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);

    }

    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        java.util.HashMap<String, BigDecimal> values = new java.util.HashMap<String, BigDecimal>();

        if (rs.getDate(ACTUAL_START_DATE) != null) {
            values.put(ArConstants.ACTUAL_START_DATE_VALUE, new BigDecimal(rs.getDate(ACTUAL_START_DATE).getTime()));
        }

        if (rs.getDate(ACTUAL_COMPLETION_DATE) != null) {
            values.put(ArConstants.ACTUAL_COMPLETION_DATE_VALUE, new BigDecimal(rs.getDate(ACTUAL_COMPLETION_DATE).getTime()));
        }

        if (rs.getDate(PROPOSED_COMPLETION_DATE) != null) {
            values.put(ArConstants.PROPOSED_COMPLETION_DATE_VALUE, new BigDecimal(rs.getDate(PROPOSED_COMPLETION_DATE).getTime()));
        }
        
        if (rs.getDate(ORIGINAL_COMPLETION_DATE) != null) {
            values.put(ArConstants.ORIGINAL_COMPLETION_DATE_VALUE, new BigDecimal(rs.getDate(ORIGINAL_COMPLETION_DATE).getTime()));
        }
        
        if (rs.getDate(PROPOSED_START_DATE) != null) {
            values.put(ArConstants.PROPOSED_START_DATE_VALUE, new BigDecimal(rs.getDate(PROPOSED_START_DATE).getTime()));
        }
        
        if (rs.getDate(ACTUAL_APPROVAL_DATE) != null) {
            values.put(ArConstants.ACTUAL_APPROVAL_DATE_VALUE, new BigDecimal(rs.getDate(ACTUAL_APPROVAL_DATE).getTime()));
        }
        
//      if (rs.getDate(ACTIVITY_APPROVAL_DATE) != null) {
//          values.put(ArConstants.ACTIVITY_APPROVAL_DATE_VALUE, new BigDecimal(rs.getDate(ACTIVITY_APPROVAL_DATE).getTime()));
//      }
        
        values.put(ArConstants.CURRENT_DATE_VALUE, new BigDecimal((new java.util.Date().getTime())));

        Long id = new Long(rs.getLong(AMP_ACTIVITY_ID));
        String expression = this.getRelatedColumn().getTokenExpression();
        BigDecimal days = MathExpressionRepository.get(expression).result(values);
        ComputedDateCell ret = new ComputedDateCell(id);
        if (days != null) {
            ret.setValue(days.toString());
        } else {
            ret.setValue("");

        }
        return ret;
    }

    protected Cell getCellFromCell(Cell src) {
        // TODO Auto-generated method stub
        return null;
    }

    public Cell newCellInstance() {
        return new ComputedDateCell();
    }

}
