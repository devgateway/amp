/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.MinimumValidator;

/**
 * Common collection validator for AMP form, check if at least one item was selected for given collection
 * @author mihai
 *
 */
public class AmpMinSizeCollectionValidationField<T> extends
		AmpSizeCollectionValidationField<T> {	
	
	/**
	 * @param id
	 * @param collectionModel
	 * @param fmName
	 */
	public AmpMinSizeCollectionValidationField(String id,
			IModel<? extends Collection<T>> collectionModel, String fmName) {
		super(id, collectionModel, fmName);
		hiddenContainer.add(new MinimumValidator<Integer>(1));
	}

}
