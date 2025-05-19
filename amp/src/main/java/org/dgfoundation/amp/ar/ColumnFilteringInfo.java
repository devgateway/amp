package org.dgfoundation.amp.ar;

/**
 * class holding info regarding filtering rows of a view when fetching
 * @author Dolghier Constantin
 *
 */
public abstract class ColumnFilteringInfo implements Comparable<ColumnFilteringInfo>{
    
    /**
     * the name of the view column to filter by (for example, v_pledges_funding_st)
     */
    protected String viewFieldName;
    
    /**
     * the name of the AmpARFilter property which holds the ids of by which to filter
     */
    protected String beanFieldName;

    public String getBeanFieldName() {
        return beanFieldName;
    }
    
    public void setBeanFieldName(String beanFieldName) {
        this.beanFieldName = beanFieldName;
    }
    
    public String getViewFieldName() {
        return viewFieldName;
    }
    
    public void setViewFieldName(String viewFieldName) {
        this.viewFieldName = viewFieldName;
    }
    
    public String getRepresentativeString(){
        return getViewFieldName() + "#" + getBeanFieldName();
    }

    @Override public int compareTo(ColumnFilteringInfo other){
        return this.getViewFieldName().compareTo(other.getViewFieldName());
    }
    
    @Override public boolean equals(Object other){
        return this.compareTo((ColumnFilteringInfo) other) == 0;
    }
    
    @Override public int hashCode(){
        return getViewFieldName().hashCode();
    }
}
