package org.digijava.kernel.ampapi.endpoints.activity.validators.mapping;

import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

/**
 * @author Nadejda Mandrescu
 */
public interface ConstraintViolationBuilder {

    JsonConstraintViolation build(ConstraintViolation v);
    
    default Object getThirdNodeKey(ConstraintViolation v) {
        Iterator<Path.Node> iterator = v.getPropertyPath().iterator();
        iterator.next();
        iterator.next();
        Path.Node transactionNode = iterator.next();
        return transactionNode.getKey();
    }

}
