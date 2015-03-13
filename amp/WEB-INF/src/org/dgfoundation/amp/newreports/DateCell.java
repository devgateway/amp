package org.dgfoundation.amp.newreports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * a @link {@link ReportCell} containing an amount 
 * @author Emanuel Perez
 *
 */
public final class DateCell extends ReportCell {
	
protected static Logger logger = Logger.getLogger(TextCell.class);
	

	public DateCell(Comparable<?> value) {
		super(value,null);
	}
	public DateCell(Comparable<?> value, SimpleDateFormat formatter) {
		super(value, formatter);
	}
	
	@Override
	protected String getFormattedValue() {
		String rawValue = (String) value;
		String displayValue = "";
		if (formatter == null || rawValue.trim().equals("")) {
			return displayValue = rawValue;
				
		}
		else { 
			SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parsedDate = sdfInput.parse(rawValue);
				displayValue = (String)((SimpleDateFormat) formatter).format(parsedDate); 
			}
			catch(ParseException e) {
				logger.warn("Invalid date for TextCell can't be parsed: "+ value); 
			}
		}
		return displayValue;
	}

}
