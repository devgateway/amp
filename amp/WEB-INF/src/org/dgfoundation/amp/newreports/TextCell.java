/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Text cell of a report
 * @author Nadejda Mandrescu
 */
public class TextCell extends ReportCell {
	
	protected static Logger logger = Logger.getLogger(TextCell.class);
	

	public TextCell(Comparable<?> value) {
		super(value, null);
	}
	
	public TextCell(Comparable<?> value, SimpleDateFormat formatter) {
		super(value, formatter);
	}
	
	
	//TODO: we need to decide how to compare Unicode strings
	@Override public int compareTo(ReportCell oth) {
		return Normalizer.normalize(this.displayedValue, Form.NFD)
				.compareToIgnoreCase(
				Normalizer.normalize(oth.displayedValue, Form.NFD));
	}
	
	@Override
	protected String getFormattedValue() {
		String rawValue = (String) value;
		String displayValue = "";
		if (formatter == null || rawValue.trim().equals("")) {
			return displayValue = rawValue;
				
		}
		else { //it is a date 
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
