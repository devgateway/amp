/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.util;

import mondrian.olap.MondrianException;

import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
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
				+ (e.getCause() instanceof MondrianException ? getMondrianException((MondrianException)e.getCause()) : "" );
	}
	
	public static String getMdxParseException(MdxParseException e) {
		return getMdxParseException(e, 2);
	}
	
	private static String getMdxParseException(MdxParseException e, int depth) {
		if (e==null || depth==0) return "";
		return "MdxParseException at region=" + e.getRegion() + ", msg=" + e.getMessage() 
				+ ". " + getMdxParseException(((MdxParseException)e.getCause()), depth-1);
	}
	
	public static String getMondrianException(MondrianException e) {
		return getMondrianException(e, 2);
	}
	public static String getMondrianException(MondrianException e, int depth) {
		if (e==null || depth==0) return "";
		return "MondrianException: " + e.getMessage() + ". " + getMondrianException((MondrianException)e.getCause(), depth-1);
	}
}
