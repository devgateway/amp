/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.validation.Path;

import mondrian.olap.MondrianException;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.dgfoundation.amp.reports.mondrian.MondrianReportGenerator;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.olap4j.CellSet;
import org.olap4j.OlapException;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.mdx.parser.MdxParseException;

/**
 * Mondrian utility class 
 * @author Nadejda Mandrescu
 *
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
	
	public static MDXAttribute getDuplicate(MDXAttribute mdxAttr) {
		MDXLevel newLevel = new MDXLevel(mdxAttr);
		String hierarchy = MondrianMaping.getDuplicateHierarchy(newLevel.getHierarchy());   
		newLevel.setHierarchy(hierarchy);
		return newLevel;
	}
	
	/**
	 * Prints formated cellSet to standard system console or file if {@link PRINT_PATH} is configured
	 * @param cellSet
	 * @param reportName
	 */
	public static void print(CellSet cellSet, String reportName) {
		RectangularCellSetFormatter formatter = new RectangularCellSetFormatter(false);
		
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
		
		formatter.format(cellSet, writer);
		writer.flush();
		writer.close();
	}
}
