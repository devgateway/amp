package org.digijava.module.widget.dbentity;

import org.digijava.module.aim.dbentity.AmpSector;

public class AmpSectorOrder {

    private Long id;
    private AmpSectorTableWidget widget;
    private AmpSector sector;
    private Integer order;

    public AmpSectorOrder() {
    }

    public AmpSectorOrder(AmpSector sector, Integer order) {
        this.sector = sector;
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpSector getSector() {
        return sector;
    }

    public void setSector(AmpSector sector) {
        this.sector = sector;
    }

    public AmpSectorTableWidget getWidget() {
        return widget;
    }

    public void setWidget(AmpSectorTableWidget widget) {
        this.widget = widget;
    }
}
