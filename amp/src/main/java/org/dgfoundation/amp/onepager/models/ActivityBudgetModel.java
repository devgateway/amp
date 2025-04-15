/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class ActivityBudgetModel extends Model {

    private IModel sourceModel;
    private String[] ddvalues;
    
    public ActivityBudgetModel(IModel sourceModel, String[] ddvalues) {
        this.sourceModel = sourceModel;
        this.ddvalues = ddvalues;
    }
    
    @Override
    public void setObject(Serializable object) {
        if (object == null){
            sourceModel.setObject(new Integer(-1));
        }
        else
            if (object instanceof String){
                String o = (String) object;
                ArrayList<String> a = (ArrayList<String>) new ArrayList(Arrays.asList(ddvalues));
                int idx = a.indexOf(o) - 1;
                Integer val = new Integer(idx);
                sourceModel.setObject(val);
            }
            else
                throw new RuntimeException("Invalid use of ActivityBudgetModel");
    }
    
    @Override
    public Serializable getObject() {
        Integer val = (Integer) sourceModel.getObject();
        val = val + 1;
        ArrayList<String> a = (ArrayList<String>) new ArrayList(Arrays.asList(ddvalues));
        if (val > -1)
            return a.get(val);
        else
            return a.get(0);
    }

}
