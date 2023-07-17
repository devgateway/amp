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
import javax.persistence.*;

@Entity
@Table(name = "AMP_COLUMNS_FILTERS")
public class AmpColumnsFilters extends ColumnFilteringInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_columns_filters_seq")
    @SequenceGenerator(name = "amp_columns_filters_seq", sequenceName = "AMP_COLUMNS_FILTERS_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private AmpColumns column;

    @Column(name = "bean_field_name")
    private String beanFieldName;

    @Column(name = "view_field_name")
    private String viewFieldName;

    
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
