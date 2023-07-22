/*
*   ModuleTeaser.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id$
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/

package org.digijava.module.common.dbentity;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "DG_MODULE_TEASER_SETTINGS")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModuleTeaser {

    @Id
    @Column(name = "module_instance_id")
    private Long id;

    @Column(name = "show_teaser")
    private String showTeaser;

    @Column(name = "teaser_order")
    private Long order;

    @Column(name = "show_num_items_teaser")
    private String showItemsPerTeaser;

    public ModuleTeaser() {
    }

    public ModuleTeaser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }


    public String getShowTeaser() {
        return showTeaser;
    }

    public void setShowTeaser(String showTeaser) {
        this.showTeaser = showTeaser;
    }
    public String getShowItemsPerTeaser() {
        return showItemsPerTeaser;
    }

    public void setShowItemsPerTeaser(String showItemsPerTeaser) {
        this.showItemsPerTeaser = showItemsPerTeaser;
    }
}
