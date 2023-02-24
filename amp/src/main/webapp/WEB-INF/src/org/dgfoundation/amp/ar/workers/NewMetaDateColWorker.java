/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.DateCell;
import org.dgfoundation.amp.ar.cell.NewMetaDateCell;

/**
 * @author Alex
 *
 */
public class NewMetaDateColWorker extends DateColWorker {
    /**
     * @param condition
     * @param viewName
     * @param columnName
     */
    public NewMetaDateColWorker(String condition, String viewName, String columnName,ReportGenerator generator) {
        super(condition, viewName, columnName,generator);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected Cell getCellFromRow(ResultSet rs)
            throws SQLException { 
        
        DateCell dc         = (DateCell)super.getCellFromRow(rs);
        NewMetaDateCell ndc = new NewMetaDateCell(dc);
        
        for (int i=3; i< rs.getMetaData().getColumnCount()+1; i++ ) {
            String label        = rs.getMetaData().getColumnLabel(i);
            label               = label.trim().toLowerCase();
            String value        = rs.getString(i);
            ndc.putMetaData(label, value);
        }
        
        return ndc;
        
    }
    
    
}
