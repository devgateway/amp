/**
 * 
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.dgfoundation.amp.ar.ColumnFilteringInfo;

/**
 * @author mihai
 *
 */
public class AmpColumnsFilters extends ColumnFilteringInfo implements Serializable {
    private Long id;
    private AmpColumns column;
    
    public AmpColumnsFilters(){
    }
    
    public AmpColumnsFilters(AmpColumns column, String beanFieldName, String viewFieldName) {
        this.column=column;
        this.beanFieldName=beanFieldName;
        this.viewFieldName=viewFieldName;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public AmpColumns getColumn() {
        return column;
    }
    
    public void setColumn(AmpColumns column) {
        this.column = column;
    }
    
    @Override public String toString(){
        return String.format("ACF: (bean: %s, view: %s, column: %s", this.beanFieldName, this.viewFieldName, this.column);
    }
    
}
