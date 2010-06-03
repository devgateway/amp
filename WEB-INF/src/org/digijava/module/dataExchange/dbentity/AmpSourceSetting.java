/**
 * 
 */
package org.digijava.module.dataExchange.dbentity;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTeam;
/**
 * 
 *
 */
public class AmpSourceSetting {
	public final static String SOURCE_URL			= "URL";
	public final static String SOURCE_FILE			= "FILE";
	public final static String SOURCE_WEB_SERVICE	= "WEB_SERVICE";
	
	/**
	 * Decides if it is an import or export source
	 */
	private Boolean isImport;
	
	/**
	 * 
	 */
	private String source;
	
	private AmpTeam importWorkspace;
	private String approvalStatus;
	private List  fields;
	private String uniqueIdentifier;
	private String uniqueIdentifierSeparator;
	
	private List exportFilters;
	
	private String languageId;
	/**import option (dropdown):    
	                        1. add only new projects
	                        2. update only existing projects
	                        3. add new projects and update existing projects*/
	private String importStrategy;
	
	/**
	 * In case the source would be a URL
	 */
	private String url;
	
}
