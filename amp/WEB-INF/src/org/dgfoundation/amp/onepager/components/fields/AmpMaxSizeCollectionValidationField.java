/**
 * 
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.validators.AmpMaxSizeCollectionValidator;

import java.util.Collection;

/**
 * @author aartimon@developmentgateway.org
 * @since 31 July 2013
 */
public class AmpMaxSizeCollectionValidationField<T> extends
        AmpSizeCollectionValidationField<T> {

    /**
     * @param id
     * @param collectionModel
     * @param fmName
     */
    public AmpMaxSizeCollectionValidationField(String id,
                                               IModel<? extends Collection<T>> collectionModel, String fmName) {
        super(id, collectionModel, fmName,new AmpMaxSizeCollectionValidator(1));
    }

}
