package org.digijava.module.aim.helper;

import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;

import org.apache.log4j.Logger ;


public class AdvancedReportDatasource 
implements JRDataSource {
	
private static Logger logger = Logger.getLogger(MultilateralDonorDatasource.class);
private Object data[][];
private int index;
public int val = 0;
public int colCnt;
public String name[];
public String m[];
public int cols;
public String labels[];
	
	public AdvancedReportDatasource() {
	data = null;
	index = -1;
	}

	public AdvancedReportDatasource(Object[][] obj, int cols, String str[]) {
		cols = cols;
		labels=str;
	data = new Object[obj.length][obj[0].length];
	index = -1;
	for (int i = 0;i < obj.length;i ++) {
		for (int j = 0;j < obj[0].length;j ++) {
			data[i][j] = obj[i][j];
			//System.out.print(j + " : " + data[i][j]);
		}
		//////System.out.println();
	}
	colCnt = obj[0].length;
	name = new String[colCnt];
	m = new String[labels.length];
		for(int i=0; i<colCnt; i++)
		{
			name[i]= "c" + Integer.toString(i+1);
//			logger.info(name[i]);
		}
		for(int i=0;i<labels.length;i++){
			m[i]="m"+Integer.toString(i+1);
		}
	}
	
	public boolean next() throws JRException
	{
	index++;
	return (index < data.length);
	}

	public Object getFieldValue(JRField field) throws JRException
	{
	String fieldName = field.getName();
	Object value = null;
	for(int i=0; i<colCnt; i++)
	{
			if(name[i].equals(fieldName))
			{
				value = data[index][i];
			}
	}
	
	
	int temp=3+cols+2;
	
	for(int i=0; i<labels.length; i++)
	{
		if(m[i].equals(fieldName))
			value = labels[i];
	}
	return value;
	
	}
	
	
} // end of Class Datasource
