package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

public class GPIYesNoAnswerModel extends Model<String> {
    private IModel<String> sourceModel;

    public GPIYesNoAnswerModel(IModel<String> sourceModel) {
        this.sourceModel = sourceModel;
    }

    @Override
    public void setObject(String object) {
        if (object == null) {
            sourceModel.setObject(null);
        } else
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
