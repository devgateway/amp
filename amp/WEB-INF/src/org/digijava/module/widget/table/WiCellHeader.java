package org.digijava.module.widget.table;

/**
 * Represents table header row cell.
 * @author Irakli Kobiashvili
 *
 */
public class WiCellHeader extends WiCell {


	@Override
	public String getValue() {
		if (getColumn()!=null){
			return "<strong>"+getColumn().getName()+"</strong>";	
		}
		return "";
	}

	/**
	 * Dummy metod
	 */
	@Override
	public void setValue(String value) {
	}

}
