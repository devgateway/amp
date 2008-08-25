package org.digijava.module.widget.table;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.digijava.kernel.exception.DgException;
import org.digijava.module.widget.dbentity.AmpDaColumn;
import org.digijava.module.widget.dbentity.AmpDaValue;

/**
 * Standard simple cell where value is entered manually.
 * @author Irakli Kobiashvili
 *
 */
public class WiCellStandard extends WiCell {

	private String value = "";
	
	public WiCellStandard(){
		
	}
	public WiCellStandard(WiColumn col){
//		setHeaderCell(true);
		setColumn(col);
//		value = col.getName();
	}
	public WiCellStandard(AmpDaValue dbValue){
		this.value = dbValue.getValue();
	}
	
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String tagContent() {
		String content = "";
		if (getValue()==null || getValue().trim().equals("")){
			content = "&nbsp;";
		}else{
			content = getValue();
		}
		if (isEditMode()){
			StringBuffer buff = new StringBuffer("<input type='edit' name='cell");
			buff.append(getColumn().getId().toString());
			buff.append("'>");
			buff.append(content);
			buff.append("</imput>");
			return buff.toString();
		}
		return content;
	}
	@Override
	public void saveData(Session dbSession, AmpDaColumn dbColumn)throws DgException {
		try {
			AmpDaValue dbValue = null;
			if (this.getId() == null || this.getId().longValue() <= 0) {
				dbValue = new AmpDaValue();
			} else {
				dbValue = (AmpDaValue) dbSession.load(AmpDaValue.class,this.getId());
			}
			dbValue.setPk(this.getPk());
			dbValue.setValue(getValue());
			dbValue.setColumn(dbColumn);
			dbSession.saveOrUpdate(dbValue);
		} catch (HibernateException e) {
			throw new DgException("cannot save cell, ID=" + getId()+ ", columnID=" + getColumn().getId(), e);
		}
	}

}
