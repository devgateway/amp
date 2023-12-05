/**
 * TextColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.TextCell;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 11, 2006
 *
 */
public class TextColWorker extends ColumnWorker {

    /**
     * @param condition
     */
    public TextColWorker(String condition,String viewName,String columnName,ReportGenerator generator) {
        super(condition,viewName, columnName,generator);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.ColumnExtractor#getCellFromRow(java.sql.ResultSet)
     */
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        Long ownerId = new Long(rs.getLong(1));

        String value;

        Object objValue = rs.getObject(2);
        if (objValue == null)
            value = "";
        else if (objValue instanceof SerialClob){
            value = serialClobToString((SerialClob) objValue);
        }
        else {
            value = objValue.toString();
        }
        
        boolean thirdColumnIsLanguage = (rsmd.getColumnCount() == 3) && rsmd.getColumnLabel(1).equals("amp_activity_id") && 
                (rsmd.getColumnLabel(3).equals("locale") || rsmd.getColumnLabel(3).equals("language"));
        boolean thirdRowIsId = rsmd.getColumnCount() > 2 && (!thirdColumnIsLanguage) && (!this.viewName.equals("v_pledges_funding_range_date"));
        
        Long id = null;
        if (thirdRowIsId) {
            id = new Long(rs.getLong(3));
            if (id <= 0 && !this.columnName.equals(ColumnConstants.PLEDGES_TITLES))
                return null; // <pledges titles>'s id is different
        }
        
        TextCell ret=new TextCell(ownerId);
        ret.setId(id);
        value=(value!=null)?value.trim():"";
        ret.setValue(value);
        return ret;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
     */
    protected Cell getCellFromCell(Cell src) {
        return null;
    }
    
    protected String retrieveValueFromRSObject( Object rsObj ) {
        return rsObj.toString();
    }
    
    protected String serialClobToString(SerialClob clobValue){
        try{
            String line;
            StringBuilder str = new StringBuilder();
            BufferedReader b = new BufferedReader(clobValue.getCharacterStream());
            while ((line = b.readLine()) != null) {
                str.append(line);
            }
            return str.toString();
        }
        catch(SerialException | IOException e){
            throw new RuntimeException(e);
        }
    }

    public Cell newCellInstance() {
        TextCell tx= new TextCell();
        tx.setValue(ArConstants.UNALLOCATED);
        return tx;
    }

}
