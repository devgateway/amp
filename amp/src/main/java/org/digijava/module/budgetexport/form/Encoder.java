/**
 * 
 */
package org.digijava.module.budgetexport.form;

import java.util.HashMap;
import java.util.Map;

import org.digijava.module.budgetexport.util.MappingEncoder;
import org.digijava.module.budgetexport.util.MappingRetrieverImplementation;

/**
 * @author Alex Gartner
 *
 */
public class Encoder implements MappingEncoder {
    
    String viewName; //column
    Map<String, String> mapColumn;
    String projectIdStr;
    
    String overwriterString;

    
    
    public Encoder(String projectIdStr, String viewName) {
        super();
        this.viewName       = viewName;
        this.projectIdStr   = projectIdStr;
        this.mapColumn      = this.retrieveMap();
        
        this.overwriterString   = null;
        if ( this.mapColumn != null )
            this.overwriterString   = this.mapColumn.get("All");
    }



    /* (non-Javadoc)
     * @see org.digijava.module.budgetexport.util.MappingEncoder#encode(java.lang.String)
     */
    @Override
    public String encode(String originalString) {
        
        if ( originalString == null ) return null;
        
        if ( this.mapColumn == null ) return  originalString;
         
        String ret  = mapColumn.get(originalString);
        
        if ( ret == null && originalString.toLowerCase().contains("unallocated") )
            ret     = mapColumn.get("None");
                
        
        if ( ret == null )
            return originalString; //.hashCode() + "";
        
        return ret;
    }
    
    private Map<String,String> retrieveMap() {
        try{
            if ( projectIdStr != null) {
                Long projectId  = Long.parseLong(projectIdStr);
                return new MappingRetrieverImplementation(projectId, viewName).retrieveMapping();
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new HashMap<String, String>();
    }



    @Override
    public boolean overwritesEverythingWithDefaultString() {
        if ( this.overwriterString != null ) {
            return true;
        }
        return false;
    }



    @Override
    public String overwriterString() {
        // TODO Auto-generated method stub
        return this.overwriterString;
    }

}
