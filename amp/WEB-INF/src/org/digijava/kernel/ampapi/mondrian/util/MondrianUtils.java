/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import mondrian.olap.MondrianException;

import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXAttribute;
import org.digijava.kernel.ampapi.mondrian.queries.entities.MDXLevel;
import org.olap4j.OlapException;
import org.olap4j.mdx.parser.MdxParseException;

/**
 * Mondrian utility class 
 * @author Nadejda Mandrescu
 *
 */
public class MondrianUtils {
	
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
		//TBD: proposed is to have a duplicate hierarchy with same name or if original has no name, then the duplicate to have it set 
		String hierarchy = (newLevel.getHierarchy() == null ? "" : newLevel.getHierarchy()) + "Duplicate";   
		newLevel.setHierarchy(hierarchy);
		return newLevel;
	}
}
