/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.NewTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alex
 *
 */
public class NewTextColWorker extends TextColWorker {

    /**
     * @param condition
     * @param viewName
     * @param columnName
     * @param generator
     */
    public NewTextColWorker(String condition, String viewName,
            String columnName, ReportGenerator generator) {
        super(condition, viewName, columnName, generator);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        TextCell tc     = (TextCell)super.getCellFromRow(rs);
        NewTextCell ntc = new NewTextCell(tc);
        
        for (int i=3; i< rs.getMetaData().getColumnCount()+1; i++ ) {
            String label        = rs.getMetaData().getColumnLabel(i);
            label               = label.trim().toLowerCase();
            String value        = rs.getString(i);
            ntc.putMetaData(label, value);
        }
        
        return ntc;
        
    }

}
