/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.newreports.FilterRule;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.ICalendarWorker;
import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.mdx.parser.MdxParseException;
import org.olap4j.metadata.Datatype;

import mondrian.olap.MondrianException;

/**
 * Mondrian utility class
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianUtils {
	protected static final Logger logger = Logger.getLogger(MondrianUtils.class);
	
	public static String PRINT_PATH = null;
	
	/**
	 * Gets full OlapException error details
	 * @param e OlapException
	 * @return String with full OlapException error details
	 */
	public static String getOlapExceptionMessage(OlapException e) {
		return "OlapException [" + e.getErrorCode() + "]: ctx=" + e.getContext()+ ", region=" + e.getRegion() + ", msg=" + e.getMessage() 
				+ ", SQLException: " + ErrorReportingPlugin.getSQLExceptionMessage(e.getNextException(), 1)
				+ getExceptionDetails((Exception)e.getCause(), 2);
	}
	
	public static String getMdxParseException(MdxParseException e) {
		return "MdxParseException at region=" + e.getRegion() + ", msg=" + e.getMessage()
				+ getExceptionDetails((Exception)e.getCause(), 1);
	}
	
	public static String getMondrianException(MondrianException e) {
		return "MondrianException: " + e.getMessage() + ". " 
				+ getExceptionDetails((MondrianException)e.getCause(), 1);
	}
	
	private static String getExceptionDetails(Exception e, int depth) {
		if (e==null || depth==0) return "";
		return e.getClass() + ": " + e.getMessage() + ". " + getExceptionDetails((Exception)e.getCause(), depth-1);
	}
	
	/**
	 * Identifies if it is an Olap specific exception and reconstructs the error message details
	 * @param e - an exception
	 * @return error message
	 */
	public static String toString(Exception e) {
		if (e instanceof MdxParseException)
			return getMdxParseException((MdxParseException)e);
		if (e instanceof OlapException)
			return getOlapExceptionMessage((OlapException)e);
		if (e instanceof MondrianException)
			return getMondrianException((MondrianException)e);
		if (e instanceof RuntimeException) {
			if (e.getCause() instanceof MdxParseException) {
				return getMdxParseException((MdxParseException)e.getCause());
			}
		}
		return e.getMessage();
	}
	
	/**
	 * Prints formated cellSet to standard system console or file if {@link PRINT_PATH} is configured
	 * @param cellSet
	 * @param reportName
	 */
	public static void print(CellSet cellSet, String reportName) {
		RectangularCellSetFormatter formatter = new RectangularCellSetFormatter(false);
		
		PrintWriter writer = getOutput(reportName);
		
		formatter.format(cellSet, writer);
		writer.flush();
		writer.close();
	}
	
	public static PrintWriter getOutput(String reportName) {
		PrintWriter writer = null;
		if (PRINT_PATH != null){
			String fileName = PRINT_PATH + (PRINT_PATH.endsWith(File.separator) ? "" : File.separator) + reportName + ".txt";
			try {
				File file = new File(fileName);
				file.getParentFile().mkdirs();
				file.createNewFile();
				writer = new PrintWriter(file);	
			} catch (IOException e) {
				logger.error("Writing to standard output, because cannot write to specified file path \"" + fileName + "\": " + e.getMessage());
			}
		}
		if (writer == null)
			writer = new PrintWriter(System.out);
		
		return writer;
	}
	
	public static String getDatatypeName(Datatype type) {
		switch(type) {
		case INTEGER:
		case UNSIGNED_INTEGER : return "Integer";
		//to see if other needs conversion
		default: return type.name();
		}
	}
	
	/**
	 * Builds a filter for specific  [from .. to] quarters, with no year limits
	 * @param from - from quarter limit
	 * @param to - to quarter limit
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @throws Exception if range is invalid
	 */
	public static FilterRule getQuarterRangeFilterRule(Integer from, Integer to, 
			AmpFiscalCalendar calendar) throws Exception {
		return null; //getDatesRangeFilterRule(ElementType.QUARTER, from, to, false);
	}
	
	/**
	 * Builds a filter for specific months in all years. Month numbers between [1..12] 
	 * @param from - first month number of the range
	 * @param to - last month number of the range 
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @throws Exception if range is invalid
	 */
	public static FilterRule getMonthRangeFilterRule(Integer from, Integer to, 
			AmpFiscalCalendar calendar) throws Exception {
		return null;//getDatesRangeFilterRule(ElementType.MONTH, from, to, true);
	}
	
	/**
	 * Builds a filter for a list of quarters 
	 * @param quarters - the list of quarters [1..4]
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @param valuesToInclude - configures if this is a list of quarters to be kept (true) or to be excluded (false)
	 * @throws Exception if range is invalid
	 */
	public static FilterRule getQuarterFilterRule(List<Integer> quarters, AmpFiscalCalendar calendar, 
			boolean valuesToInclude) throws Exception {
//		List<String> quarterNames = calendar == null ? null : new ArrayList<String>(quarters.size());
//		if (calendar != null)
//			for (Integer quarter : quarters)
//				quarterNames.add(getFiscalQuarter(quarter, calendar));
		return null;//getDatesListFilterRule(ElementType.QUARTER, quarters, valuesToInclude);
	}
	
	/**
	 * Builds a filter for specific months list in all years
	 * @param months - month numbers between [1..12]
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @param valuesToInclude - true if this months must be kept, false if they must be excluded
	 * @throws Exception if range is invalid
	 */
	public static FilterRule getMonthsFilterRule(List<Integer> months, AmpFiscalCalendar calendar, 
			boolean valuesToInclude) throws Exception {
//		List<String> monthNames = calendar == null ? null : new ArrayList<String>(months.size());
//		if (calendar != null)
//			for (Integer month : months)
//				monthNames.add(getFiscalMonth(month, calendar));
		return null;//getDatesListFilterRule(ElementType.MONTH, months, valuesToInclude);
	}
	
	/**
	 * Builds a single quarter filter, no years filter
	 * @param quarter
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @param valueToInclude
	 * @throws Exception 
	 */
	public static FilterRule getSingleQuarterFilterRule(Integer quarter, AmpFiscalCalendar calendar,
			boolean valueToInclude) throws Exception {
		return null; //getSingleDateFilterRule(ElementType.QUARTER, quarter, valueToInclude);
	}
	
	/**
	 * Builds a single month filter, no years filter
	 * @param month
	 * @param calendar - (optional) the calendar to use to store actual names
	 * @param valueToInclude
	 * @throws Exception 
	 */
	public static FilterRule getSingleMonthFilterRule(Integer month, AmpFiscalCalendar calendar,
			boolean valueToInclude) throws Exception {
		return null;//getSingleDateFilterRule(ElementType.MONTH, month, valueToInclude);
	}
	
	public static String getFiscalYear(Integer year, AmpFiscalCalendar calendar) throws Exception {
		if (calendar == null || year == null) 
			return null;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		ICalendarWorker worker = calendar.getworker();
		worker.setTime(cal.getTime());
		return worker.getFiscalYear();
	}
	
	public static String getFiscalQuarter(Integer quarter, AmpFiscalCalendar calendar) throws Exception {
		if (calendar == null || quarter == null) 
			return null;
		// TODO: 
		return "Q" + quarter;
	}
	
	public static String getFiscalMonth(Integer month, AmpFiscalCalendar calendar) throws Exception {
		if (calendar == null || month == null) 
			return null;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);
		ICalendarWorker worker = calendar.getworker();
		worker.setTime(cal.getTime());
		return worker.getFiscalMonth().toString();
	}
}
