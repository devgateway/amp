/**
 * 
 */
package org.digijava.module.budgetexport.form;

import java.util.HashMap;
import java.util.Map;

import org.digijava.module.budgetexport.util.MappingEncoder;

/**
 * @author Alex Gartner
 *
 */
public class Encoder implements MappingEncoder {
	
	String viewName; //column
	Map<String, String> mapColumn;

	
	
	public Encoder(String type, String viewName) {
		super();
		this.viewName = viewName;
		this.mapColumn = this.retrieveMap();
	}



	/* (non-Javadoc)
	 * @see org.digijava.module.budgetexport.util.MappingEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String originalString) {
		
		if ( originalString == null ) return null;
		
		if ( this.mapColumn == null ) return  originalString;
		
		 
		String ret	= mapColumn.get(originalString);
		if ( ret == null )
			return originalString.hashCode() + "";
		
		return ret;
	}
	
	public Map<String,String> retrieveMap() {
		return new HashMap<String, String>();
	}

}
