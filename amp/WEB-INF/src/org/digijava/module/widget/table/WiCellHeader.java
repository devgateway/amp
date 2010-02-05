package org.digijava.module.widget.table;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.hibernate.Session;

/**
 * Represents table header row cell.
 * @author Irakli Kobiashvili
 *
 */
public class WiCellHeader extends WiCell {


	@Override
	public String getValue() {
		if (getColumn()!=null){
			return getColumn().getName();	
		}
		return "";
	}

	/**
	 * Dummy metod
	 */
	@Override
	public void setValue(String value) {
	}

	@Override
	public String tagContent() {
		return "<strong>"+getValue()+"</strong>";
	}

	@Override
	public void saveData(Session dbSession, AmpDaColumn dbColumn) throws DgException {
		//dummy method, header does not need save!
	}
        
        @Override
	public void removeData(Session dbSession, AmpDaColumn dbColumn) throws DgException {
		//dummy method, header does not need save!
	}

}
