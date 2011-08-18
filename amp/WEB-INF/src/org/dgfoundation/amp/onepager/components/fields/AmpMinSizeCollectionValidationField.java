/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.validators.AbstractTrnValidator;

/**
 * Common collection validator for AMP form, check if at least one item was selected for given collection
 * @author mihai
 *
 */
public class AmpMinSizeCollectionValidationField<T> extends
		AmpSizeCollectionValidationField<T> {	
	
	
	/**
	 *  Return a nice error when a collection is of a size smaller than the given (usually 1)
	 * @author mihai
	 *
	 */
	public class AmpMinSizeCollectionValidator extends AbstractTrnValidator<Integer>{

		private Integer minimum;
		
		public AmpMinSizeCollectionValidator(int minimum) {
			super(AmpMinSizeCollectionValidationField.this);
			this.minimum=minimum;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
		 */
		@Override
		public void validate(IValidatable<Integer> validatable) {
			Integer value = validatable.getValue();
			if (value.compareTo(minimum) < 0)
			{
				ValidationError error = getTrnError("Please add at least ${minimum} item(s) here!");
				error.setVariable("minimum", minimum);		
				validatable.error(error);
			}

		}

	}
	
	/**
	 * @param id
	 * @param collectionModel
	 * @param fmName
	 */
	public AmpMinSizeCollectionValidationField(String id,
			IModel<? extends Collection<T>> collectionModel, String fmName) {
		super(id, collectionModel, fmName);
		hiddenContainer.add(new AmpMinSizeCollectionValidator(1));
	}

}
