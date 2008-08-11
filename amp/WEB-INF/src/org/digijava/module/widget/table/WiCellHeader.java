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

}
