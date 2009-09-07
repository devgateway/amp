package org.dgfoundation.amp.exprlogic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class YearLogicalToken extends LogicalToken {

	private String type;
	private int startYear;
	private int endYear;
	
	

	public YearLogicalToken(int startYear, int endYear,String type) {
		this.startYear=startYear;
		this.type = type;
		this.endYear = endYear;
	}

	@Override
	public boolean evaluate(CategAmountCell c) {
		MetaInfo m = MetaInfo.getMetaInfo(c.getMetaData(), ArConstants.TRANSACTION_DATE);
		Date date = (Date) m.getValue();
		GregorianCalendar gCell = new GregorianCalendar();
		gCell.setTime(date);
		Integer cellYear = gCell.get(Calendar.YEAR);
	
		
		ret = (cellYear.compareTo(startYear) > -1)&& (cellYear.compareTo(endYear) < 1) ;
		return super.evaluate(c);
	}
}
