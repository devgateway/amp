/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.MinimumValidator;

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
			IModel<? extends Collection<T>> collectionModel, String fmName) {
		super(id, collectionModel, fmName);
		hiddenContainer.setType(Integer.class);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.onepager.components.fields.AmpCollectionValidatorField#getHiddenContainerModel(org.apache.wicket.model.IModel)
	 */
	@Override
	public AbstractReadOnlyModel getHiddenContainerModel(
			final IModel<? extends Collection<T>> collectionModel) {
		AbstractReadOnlyModel<Integer> model=new AbstractReadOnlyModel<Integer>() {
			@Override
			public Integer getObject() {
				return collectionModel.getObject().size();
			}
		};
		return model;
	}

}
