/**
 * TextColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialClob;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.TextCell;

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
		// added Clob support
		String value = "";

		Object objValue = rs.getObject(2);
		if (objValue!=null){
			if (objValue instanceof SerialClob) {
				SerialClob clobValue = (SerialClob) objValue;
				String line;
				String str = "";
				BufferedReader b = new BufferedReader(clobValue.getCharacterStream());
				try {
					while ((line = b.readLine()) != null) {
						str += line;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				value = str;
	
			} else {
				value = this.retrieveValueFromRSObject( objValue );

		}
		}
		
		
		
		Long id = null;
		if (rsmd.getColumnCount() > 2) {
			id = new Long(rs.getLong(3));
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
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String retrieveValueFromRSObject( Object rsObj ) {
		return rsObj.toString();
	}

	public Cell newCellInstance() {
		TextCell tx= new TextCell();
		tx.setValue(ArConstants.UNALLOCATED);
		return tx;
	}

}
