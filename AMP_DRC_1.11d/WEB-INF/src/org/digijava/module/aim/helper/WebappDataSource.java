package org.digijava.module.aim.helper;

import net.sf.jasperreports.engine.*;

public class WebappDataSource implements JRDataSource
{

	private Object[][] data =
		{
			{"Berne", new Integer(22), "DGF", "250 - 20th Ave."},
			{"San Francisco", new Integer(7), "CDAC", "231 Upland Pl."}
		};

	private int index = -1;
	
	public WebappDataSource()
	{
	}

	public boolean next() throws JRException
	{
		index++;

		return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException
	{
		////System.out.println("Inside Jfree Webapp DataSource....5");

		Object value = null;
		
		String fieldName = field.getName();
		
		if ("City".equals(fieldName))
		{
			value = data[index][0];
		}
		else if ("Id".equals(fieldName))
		{
			value = data[index][1];
		}
		else if ("Name".equals(fieldName))
		{
			value = data[index][2];
		}
		else if ("Street".equals(fieldName))
		{
			value = data[index][3];
		}
		
		return value;
	}


}
