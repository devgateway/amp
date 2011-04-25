/**
 * TextCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.regex.Pattern;

import org.dgfoundation.amp.ar.workers.TextColWorker;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 29, 2006
 * regular cell holding a string
 **/
public class TextCell extends Cell {
	
	public static final int shortLength=160;
	
	protected String value;
	
	public TextCell() {
		super();
		value="";		
	}
	
	public Class getWorker() {
		return TextColWorker.class;
	}
	
	public TextCell(Long id) {
		super(id);
		value="";
	}


	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		this.value=(String) value;
		
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.cell.Cell#add(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Cell merge(Cell c) {
		TextCell ret=new TextCell();		
		ret.setValue((value+(String)c.getValue()).trim());
		return ret;
		
	}

	public Cell newInstance() {
		return new TextCell();
	}
	
	public int getTextLength() {
		if(value==null) return 0;
		return value.length();
	}
	
	/**
	 * Please use this as key in translations, it generates hash from value.
	 * this is workaround because value usually contains huge HTML tags on several lines and this kills many things. 
	 * @return hash code of value or '0' if values is NULL.
	 */
	public String getTranslationKey(){
		String result = "0";
		if (value != null && !"".equals(value)){
			result = TranslatorWorker.generateTrnKey(value);
		}
		return result;
	}
	
	public String getShortTextVersion() {
		Pattern ptr	= Pattern.compile("<!--.*-->", Pattern.DOTALL);
		if (!getHasLongVersion())
			return ptr.matcher(value).replaceAll("");
		String alteredValue =  value.replaceAll("\\<.*?>","");
		alteredValue		= ptr.matcher(alteredValue).replaceAll("");
		if(alteredValue.length()<shortLength)
			return alteredValue;
		return alteredValue.substring(0, shortLength-1);
	}
	
	
	public String getFullTextVersion() {
		//if (!getHasLongVersion())
		//	return value;
		
		Pattern ptr	= Pattern.compile("<!--.*-->", Pattern.DOTALL);
		String alteredValue	= ptr.matcher(value).replaceAll("");
		alteredValue 		= alteredValue.replaceAll("\\<.*?>", "");
		return alteredValue;

	}
	
	public boolean getHasLongVersion() {
		if(getTextLength()>shortLength) return true;
		return false;
	}

	public Comparable comparableToken() {
		return value.toLowerCase();
	}

	@Override
	public void merge(Cell c1, Cell c2) {
		this.setValue((this.equals(c1)?"":c1.getValue()+(String)(this.equals(c2)?"":c2.getValue())));
	}
	public String toString() {
		// TODO Auto-generated method stub
		return getValue()!=null?getValue().toString().replaceAll("\\<.*?>",""):"";
	}
	
}
