package org.digijava.module.common.dbentity;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ModuleTeaser {


    private Long id;

    private String showTeaser;

    private Long order;

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
