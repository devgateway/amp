/**
 * 
 */
package org.digijava.module.aim.helper;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for HeatMapConfigService
 * Note: to be moved to DB when will be managed through Admin UI
 * 
 * @author Nadejda Mandrescu
 */
public class HeatMapConfig {

    public enum Type {
        @JsonProperty("S") SECTOR("S"),
        @JsonProperty("P") PROGRAM("P"),
        @JsonProperty("L") LOCATION("L");
        
        private String name;
        
        Type(String name) {
            this.name = name; 
        }
        @Override
        public String toString() {
            return name;
        }
    }
    
    @NotNull
    public final String name;
    @NotNull
    public final Type type;
    @NotNull
    public final List<String> xColumns;
    @NotNull
    public final List<String> yColumns;
    
    public HeatMapConfig(String name, Type type, List<String> xColumns, List<String> yColumns) {
        this.name = name;
        this.type = type;
        this.xColumns = new ArrayList<String>(xColumns);
        this.yColumns = new ArrayList<String>(yColumns);
    }
}
