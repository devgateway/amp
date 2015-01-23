package org.dgfoundation.amp.onepager.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.util.convert.ConversionException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class StrictPatternDateConverter extends PatternDateConverter {


    public StrictPatternDateConverter() {
        super(Constants.GLOBALSETTINGS_DATEFORMAT, false);
    }

    private ConversionException getException() {
		ConversionException conv = new ConversionException("StrictPatternDateConverter");
		conv.setResourceKey("StrictPatternDateConverter");
		conv.setVariable("format", FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT));
		return conv;
    }
    
    @Override
    public Date convertToObject(String value, Locale locale) {
    	try {
    		Date date = getDateFormatter().parse(value);
			if (date == null) {
				throw getException();
			}
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 1900);
    		if (date.before(cal.getTime())){
				throw getException();
			}
			cal.set(Calendar.YEAR, 2099);
    		if (date.after(cal.getTime())){
				throw getException();
			}
    			

		} catch (ParseException e) {
			throw getException();
		}
        return super.convertToObject(value, locale);
    }

	private SimpleDateFormat getDateFormatter() {
		SimpleDateFormat formatter = new SimpleDateFormat();
    	formatter.setLenient(false);  
    	formatter.applyPattern(FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT));
		return formatter;
	}

	@Override
	public String convertToString(Date date, Locale locale) {
		String strValue = getDateFormatter().format(date);

		if (date == null) {
			throw getException();
		}
		return strValue;

	}
}