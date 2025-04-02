/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.validators.AmpPercentageCollectionValidator;

/**
 * This field can be used to count percentage items and show an error message when 100% is not reached.
 * This is done on the fly while the user types percentages...
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public abstract class AmpPercentageCollectionValidatorField<T> extends
        AmpCollectionValidatorField<T,Double> {

    
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
        super(id, setModel, fmName,new AmpPercentageCollectionValidator());
        
        hiddenContainer.setType(Double.class);
    
    }

    
    @Override
    public IModel getHiddenContainerModel(final IModel<? extends Collection<T>> collectionModel) {
        Model<Double> model=new Model<Double>() {
            @Override
            public void setObject(Double object) {
            }
            
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
