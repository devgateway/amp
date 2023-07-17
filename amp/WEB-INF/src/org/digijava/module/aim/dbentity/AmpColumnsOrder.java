/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_COLUMNS_ORDER")
public class AmpColumnsOrder implements Serializable, Comparable {
    /**
     * 
     */
    private static final long serialVersionUID = 5670661886522418984L;
    /** Comment for <code>serialVersionUID</code> */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_columns_order_seq")
    @SequenceGenerator(name = "amp_columns_order_seq", sequenceName = "AMP_COLUMNS_ORDER_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "columnName")
    private String columnName;

    @Column(name = "indexOrder")
    private Long indexOrder;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public Long getIndexOrder() {
        return indexOrder;
    }
    public void setIndexOrder(Long index) {
        this.indexOrder = index;
    }
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        AmpColumnsOrder obj=(AmpColumnsOrder) arg0;
        return this.indexOrder.compareTo(obj.getIndexOrder());
    }
    
        
}
