package org.digijava.module.aim.helper;

import java.util.Date;

public class DateCustomField extends CustomField<Date> {
	
	@Override
	public void setValue(Date value) {
		this.value = value;
	}

	public void setStrDate(String value) {
		Date date = DateConversion.getDate(value);
		this.value = date;
	}

	public String getStrDate() {
		return DateConversion.ConvertDateToString(this.getValue());
	}
	
}
