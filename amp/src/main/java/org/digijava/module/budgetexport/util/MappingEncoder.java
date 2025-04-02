package org.digijava.module.budgetexport.util;

public interface MappingEncoder {

    public String encode ( String originalString );
    
    public boolean overwritesEverythingWithDefaultString() ;
    
    public String overwriterString() ;

}
