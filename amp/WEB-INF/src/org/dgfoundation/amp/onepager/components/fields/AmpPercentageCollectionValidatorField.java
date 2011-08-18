/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.validators.AbstractTrnValidator;

/**
 * This field can be used to count percentage items and show an error message when 100% is not reached.
 * This is done on the fly while the user types percentages...
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public abstract class AmpPercentageCollectionValidatorField<T> extends
		AmpCollectionValidatorField<T,Double> {

	
	/**
	 * @author mihai
	 * Checks if the given value is 100 (used for validating collection percentages)
	 */
	public class AmpPercentageCollectionValidator extends
			AbstractTrnValidator<Double> {

		/**
		 * @param component
		 */
		public AmpPercentageCollectionValidator() {
			super(AmpPercentageCollectionValidatorField.this);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
		 */
		@Override
		public void validate(IValidatable<Double> validatable) {
			Double value = validatable.getValue();
			if(value.doubleValue()!=100)
			{
				ValidationError error = getTrnError("Sum of all percentages is ${input}, it needs to be 100!");
				validatable.error(error);
			}


		}
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param setModel
	 * @param fmName
	 */
	public AmpPercentageCollectionValidatorField(String id,
			IModel<? extends Collection<T>> setModel, String fmName) {
		super(id, setModel, fmName);
		
		hiddenContainer.setType(Double.class);
		hiddenContainer.add(new AmpPercentageCollectionValidator());
	
	}

	
	@Override
	public AbstractReadOnlyModel getHiddenContainerModel(final IModel<? extends Collection<T>> collectionModel) {
		AbstractReadOnlyModel<Double> model=new AbstractReadOnlyModel<Double>() {
			@Override
			public Double getObject() {
				if(collectionModel.getObject().size()==0) return 100d;
				double total=0;
				for( T item : collectionModel.getObject()) 
					if(getPercentage(item)!=null) total+=getPercentage(item).doubleValue();
				return total;
			}
		};
		return model;
	}
	
	/**
	 * Implement in subclasses to retrieve the specific percentage from the <T> item
	 * @param item
	 * @return the percentage, as a {@link Number}
	 */
	public abstract Number getPercentage(T item);
	

}
