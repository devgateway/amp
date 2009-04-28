

package org.digijava.module.widget.helper;

public class OrderHelper {
    private Long order;
    private Long value;
    private Long type;
    private String year;

 
    public OrderHelper(Long value,Long order,Long type,String year){
        this.value=value;
        this.order=order;
        this.type=type;
        this.year=year;

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

   

}
