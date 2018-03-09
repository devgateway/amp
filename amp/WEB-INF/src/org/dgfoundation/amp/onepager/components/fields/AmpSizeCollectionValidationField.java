/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpSemanticValidator;

/**
 * @author mihai
 * Checks the size of a collection of items and attaches validators to it
 */
public abstract class AmpSizeCollectionValidationField<T> extends
        AmpCollectionValidatorField<T,Integer> {

    /**
     * @param id
     * @param collectionModel
     * @param fmName
     */
    public AmpSizeCollectionValidationField(String id,
            IModel<? extends Collection<T>> collectionModel, String fmName,AmpSemanticValidator<Integer> semanticValidator) {
        super(id, collectionModel, fmName,semanticValidator);
        hiddenContainer.setType(Integer.class);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.model.IModel)
     */
    @Override
    public IModel getHiddenContainerModel(
            final IModel<? extends Collection<T>> collectionModel) {
        Model<Integer> model=new Model<Integer>() {
            @Override
            public Integer getObject() {
                return collectionModel.getObject().size();
            }
        };
        return model;
    }

}
