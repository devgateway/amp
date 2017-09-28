/**
 * 
 */
package org.digijava.kernel.ampapi.mondrian.queries.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;

/**
 * Hierarchy Level configuration based on dimension, (optionally) hierarchy, level name and (optionally) slicing values 
 * @author Nadejda Mandrescu
 *
 */
public class MDXLevel extends MDXAttribute {
    private String hierarchy;
    private List<String> values; 
    
    /**
     * Configures Level settings
     * @param dimension 
     * @param hierarchy - optional, i.e. can be null if there is only 1 hierarchy in the dimension
     * @param level
     * @param values - optional list of hierarchy values to filter by
     */
    public MDXLevel(String dimension, String hierarchy, String level, String... values) {
        super(dimension, level, null);
        this.hierarchy = hierarchy;
        values = (String[])ArrayUtils.removeElement((Object[])values,  null);
        this.values = (values!=null && values.length > 0) ?  Arrays.asList(values) : null;
    }
    
    public MDXLevel(MDXAttribute mdxAttr) {
        this(mdxAttr.getDimension(), (mdxAttr instanceof MDXLevel ? ((MDXLevel)mdxAttr).getHierarchy() : null), mdxAttr.getName(), mdxAttr.getValue());
        if (mdxAttr instanceof MDXLevel) { 
            MDXLevel toCopy = ((MDXLevel)mdxAttr);
            this.values = toCopy.values == null ? null : toCopy.values.subList(0, toCopy.values.size());
        }
    }
    
    @Override
    public MDXLevel clone() {
        return new MDXLevel(this.dimension, this.hierarchy, this.name, (this.values == null ? null : this.values.toArray(new String[this.values.size()])));
    }
    
    @Override
    public String toString() {
        String keys = valuesToKey();
        return getFullName() + (keys != null ? keys : "." + MoConstants.MEMBERS); 
    }
    
    private String valuesToKey() {
        String res = null;
        if (values!=null && values.size()>0 ) {
            res = ".";
            if (values.size()==1) 
                res += quote(values.get(0));
            else 
                for (String val:values) {
                    res += "." + quote(val);
                }
        }
        return res;
    }
    
    @Override
    public String getFullName() {
        return getDimensionAndHierarchy() + (this.name==null? "" : "." + quote(this.name));
    }
    
    @Override
    public String getDimensionAndHierarchy() {
        return quote(this.dimension + (this.hierarchy==null ? "" : "." + this.hierarchy));
    }
    
    public String getSortName() {
        if (this.values !=null)
            return toString();
        return getCurrentMemberName() + "." + MoConstants.MEMBER_NAME;
    }
    
    public String getLevel() {
        return this.name;
    }

    /**
     * @return the hierarchy
     */
    public String getHierarchy() {
        return this.hierarchy;
    }

    /**
     * @param hierarchy the hierarchy to set
     */
    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }
    
    /**
     * Sets a single value filter over the level. 
     * Use {@link MDXLevel#setValues(List)} to configure hierarchy filter list 
     */
    @Override
    public void setValue(String value) {
        this.values = new ArrayList<String>();
        this.values.add(value);
    }
    
    /**
     * Configures level hierarchy filter values
     * @param values
     */
    public void setValues(List<String> values) {
        this.values = values;
    }
    
    public List<String> getValues() {
        return this.values;
    }
}
