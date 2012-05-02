package org.digijava.module.widget.dbentity;

public class AmpSectorTableYear {
    public final static Long TOTAL_TYPE_YEAR=1l;
    public final static Long PERCENT_TYPE_YEAR=2l;
    private Long id;
    private Long year;
    private AmpSectorTableWidget widget;
    private Long type;
    private Long order;

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public  AmpSectorTableWidget getWidget() {
        return widget;
    }

    public void setWidget( AmpSectorTableWidget widget) {
        this.widget = widget;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

}
