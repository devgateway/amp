package org.dgfoundation.amp.ar.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HierarchycalItem {
    
    public static Comparator<HierarchycalItem> DATE_COMPARTOR   = new DateComparator() {
                                        @Override
                                        public int compare(HierarchycalItem o1, HierarchycalItem o2) {
                                            return super.compare(o1, o2) * -1;
                                        }
                            };
    
    
    private String name = null;
    private String description  = null;
    private Date date   = null;
    private HierarchycalItem parent;
    private List<HierarchycalItem> children;
    
    private SimpleDateFormat simpleDateFormat   = null;
     
    

    public String getDateString() {
        if ( this.date != null ) {
            SimpleDateFormat sdf    = this.simpleDateFormat!=null?this.simpleDateFormat:new SimpleDateFormat();
            return sdf.format(this.date);
        }
        return "";
    }
    
    public void addChild (HierarchycalItem hi) {
        hi.setParent(this);
        if (children == null) {
            children = new ArrayList<HierarchycalItem>();
        }
        this.children.add(hi);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * @return the children
     */
    public List<HierarchycalItem> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<HierarchycalItem> children) {
        this.children = children;
    }

    /**
     * @return the parent
     */
    public HierarchycalItem getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(HierarchycalItem parent) {
        this.parent = parent;
    }

    /**
     * @return the simpleDateFormat
     */
    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    /**
     * @param simpleDateFormat the simpleDateFormat to set
     */
    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
    
    private static class DateComparator implements Comparator<HierarchycalItem> {

        @Override
        public int compare(HierarchycalItem o1, HierarchycalItem o2) {
            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null)
                return -1;
            if (o2 == null)
                return 1;
            if (o1.getDate() == null && o2.getDate() == null )
                return 0;
            if (o1.getDate() == null ) 
                return -1;
            if (o2.getDate() == null)
                return 1;
            
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}
