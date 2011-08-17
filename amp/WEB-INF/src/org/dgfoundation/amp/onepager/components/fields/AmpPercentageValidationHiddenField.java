/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * This field can be used to count percentage items and show an error message when 100% is not reached.
 * This is done on the fly while the user types percentages...
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public abstract class AmpPercentageValidationHiddenField<T> extends
		AmpValidationHiddenField<T> {

	/**
	 * @param id
	 * @param setModel
	 * @param fmName
	 */
	public AmpPercentageValidationHiddenField(String id,
			IModel<? extends Collection<T>> setModel, String fmName) {
		super(id, setModel, fmName);
		
		hiddenContainer.setType(Double.class);
		hiddenContainer.add(new RangeValidator<Double>(100d, 100d));
	
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
