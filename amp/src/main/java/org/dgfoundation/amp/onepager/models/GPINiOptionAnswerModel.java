package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * GPI Ni Question Answer Model
 * that will store the choice of the user in the underlying object
 * 
 * @author Viorel Chihai
 */
public class GPINiOptionAnswerModel extends Model<String> {
    private static final long serialVersionUID = 1L;
    
    private IModel<String> sourceModel;
    
    public GPINiOptionAnswerModel(IModel<String> sourceModel) {
        this.sourceModel = sourceModel;
    }
    
    @Override
    public void setObject(String object) {
        if (object == null) {
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
