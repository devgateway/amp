package org.digijava.module.sdm.dbentity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public  class SdmItemKey implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SDM_ID", referencedColumnName = "ID")
    private Sdm document;

    @Column(name = "PARAGRAPH_ORDER")
    private Long paragraphOrder;

    // Constructors, getters, and setters (omitted for brevity)

    // You can add constructors, getters, setters, and other methods here

    // Override equals() and hashCode() methods (omitted for brevity)
}
