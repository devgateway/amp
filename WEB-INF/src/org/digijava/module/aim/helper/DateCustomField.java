package org.digijava.module.aim.helper;

import java.util.Date;

public class DateCustomField extends CustomField<Date> {
	
	@Override
	public void setValue(Date value) {
		super.setValue(value);
	}

	public void setStrDate(String value) {
		Date date = DateConversion.getDate(value);
		super.setValue(date);
	}

	public String getStrDate() {
		return DateConversion.ConvertDateToString(this.getValue());
	}
	
}
