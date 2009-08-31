package org.dgfoundation.amp.exprlogic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class YearLogicalToken extends LogicalToken {

	private String type;
	private int yearsAgo;

	public YearLogicalToken(int yearsAgo, String type) {
		this.type = type;
		this.yearsAgo = yearsAgo;
	}

	@Override
	public boolean evaluate(CategAmountCell c) {
		MetaInfo m = MetaInfo.getMetaInfo(c.getMetaData(), ArConstants.TRANSACTION_DATE);
		Date date = (Date) m.getValue();
		GregorianCalendar gCell = new GregorianCalendar();
		gCell.setTime(date);
		GregorianCalendar current = new GregorianCalendar();
		Integer cellYear = gCell.get(Calendar.YEAR);
		Integer currentYear = current.get(Calendar.YEAR);
		Integer year = currentYear - yearsAgo;
		
		
		ret = (cellYear.compareTo(year) > -1)&& (cellYear.compareTo(currentYear) < 0) ;
		return super.evaluate(c);
	}
}
