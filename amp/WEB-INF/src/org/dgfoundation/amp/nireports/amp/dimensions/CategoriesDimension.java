package org.dgfoundation.amp.nireports.amp.dimensions;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

import java.util.Arrays;

/**
 * 
 * an <i>amp_category_value</i>-backed dimension consisting of (amp_category_class[level=0], amp_category_value[level=1]) 
 * @author Dolghier Constantin
 *
 */
public class CategoriesDimension extends SqlSourcedNiDimension {
    public final static CategoriesDimension instance = new CategoriesDimension("cats");
    
    private CategoriesDimension(String name) {
        super(name, "amp_category_value", Arrays.asList("amp_category_class_id", "id"));
    }
    
    public final static int LEVEL_CAT_CLASS = 0;
    public final static int LEVEL_CAT_VALUE = 1;
}
