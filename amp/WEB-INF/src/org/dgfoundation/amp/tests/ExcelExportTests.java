package org.dgfoundation.amp.tests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.dgfoundation.amp.ar.view.xls.AmountCellXLS;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import static org.junit.Assert.*;

//@RunWith(Suite.class)
//@SuiteClasses({})
public class ExcelExportTests 
{
	@Test
	public void testNumberFormatting()
	{		
		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format.setGroupingUsed(false);
		format.setDecimalFormatSymbols(symbols);
		format.setMaximumFractionDigits(3);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123}, AmountCellXLS.formatCell(123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456}, AmountCellXLS.formatCell(123456, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789}, AmountCellXLS.formatCell(123456789, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.1}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.12}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.123}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.123}, AmountCellXLS.formatCell(123456789.1234, format));
		
		format.setMaximumFractionDigits(0);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789}, AmountCellXLS.formatCell(123456789.1234, format));
		
		symbols.setDecimalSeparator(',');
		format.setMaximumFractionDigits(3);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.1}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.12}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.123}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_NUMERIC, (double) 123456789.123}, AmountCellXLS.formatCell(123456789.1234, format));
	}
	
	@Test
	public void testStringFormatting()
	{		
		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format.setGroupingUsed(true);
		format.setGroupingSize(3);
		symbols.setGroupingSeparator(' ');
		format.setDecimalFormatSymbols(symbols);
		format.setMaximumFractionDigits(3);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123"}, AmountCellXLS.formatCell(123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456"}, AmountCellXLS.formatCell(123456, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789"}, AmountCellXLS.formatCell(123456789, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789.1"}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789.12"}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789.123"}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789.123"}, AmountCellXLS.formatCell(123456789.1234, format));
		
		format.setMaximumFractionDigits(0);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789"}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789"}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789"}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789"}, AmountCellXLS.formatCell(123456789.1234, format));
		
		symbols.setDecimalSeparator(',');
		format.setDecimalFormatSymbols(symbols);
		format.setMaximumFractionDigits(3);
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789,1"}, AmountCellXLS.formatCell(123456789.1, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789,12"}, AmountCellXLS.formatCell(123456789.12, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789,123"}, AmountCellXLS.formatCell(123456789.123, format));
		assertArrayEquals(new Object[] {HSSFCell.CELL_TYPE_STRING, "123 456 789,123"}, AmountCellXLS.formatCell(123456789.1234, format));
	}
}

