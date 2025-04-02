package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;

import java.util.HashSet;
import java.util.Set;

/**
 * @author aartimon@developmentgateway.org
 * Readonly model to be used for the category manager dropdown conversion
 */
public class ValueToSetModel<T> implements IModel<Set<T>> {
    private IModel<T> baseModel;

    public ValueToSetModel(IModel<T> baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public Set<T> getObject() {
        HashSet<T> ret = new HashSet<T>();
        if (baseModel.getObject() != null)
            ret.add(baseModel.getObject());
        return ret;
    }

    @Override
    public void setObject(Set<T> ts) {
        throw new AssertionError("Shouldn't be used!");
    }

    @Override
    public void detach() {
        baseModel.detach();
    }
}
