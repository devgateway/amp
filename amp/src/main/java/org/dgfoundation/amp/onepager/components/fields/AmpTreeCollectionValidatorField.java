/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpTreeCollectionValidator;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author mihai
 *
 */
public abstract class AmpTreeCollectionValidatorField<T> extends
        AmpCollectionValidatorField<T, String> {

    /**
     * @param id
     * @param collectionModel
     * @param fmName
     */
    public AmpTreeCollectionValidatorField(String id,
            IModel<? extends Collection<T>> collectionModel, String fmName) {
        super(id, collectionModel, fmName,new AmpTreeCollectionValidator());
        hiddenContainer.setType(String.class);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.model.IModel)
     */
    @Override
    public IModel getHiddenContainerModel(
            final IModel<? extends Collection<T>> collectionModel) {
        Model<String> model=new Model<String>() {
            @Override
            public String getObject() {
                Set<AmpAutoCompleteDisplayable> quickItems=new TreeSet<AmpAutoCompleteDisplayable>();
                for (T t : collectionModel.getObject()) quickItems.add(getItem(t));

                Set<String> ret=new TreeSet<String>();
                for (T t : collectionModel.getObject()) {
                    AmpAutoCompleteDisplayable node=getItem(t).getParent();
                    while(node!=null) {
                        if(quickItems.contains(node)) ret.add(getItem(t)+"("+node+")");
                        node=node.getParent();
                    }
                }

                if(ret.size()>0)
                        return ret.toString();
                return "";
            }
        };
        return model;
    }

    public abstract AmpAutoCompleteDisplayable getItem(T t);
    
}
