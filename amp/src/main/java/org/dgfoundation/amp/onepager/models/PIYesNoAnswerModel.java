/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * Paris Indicator Question Answer Model
 * that will store the choice of the user as 
 * "yes" or "no" in the underlying object
 * 
 * @author aartimon@dginternational.org
 * @since Mar 31, 2011
 */
public class PIYesNoAnswerModel extends Model<String> {
    private IModel<String> sourceModel;
    
    public PIYesNoAnswerModel(IModel<String> sourceModel) {
        this.sourceModel = sourceModel;
    }
    
    @Override
    public void setObject(String object) {
        if (object == null){
            sourceModel.setObject(null);
        }
        else
            sourceModel.setObject((object).toLowerCase());
    }
    
    @Override
    public String getObject() {
        
        String val = sourceModel.getObject();
        
        if (val == null)
            return null;
        else
            return Strings.capitalize(val);
    }
}
