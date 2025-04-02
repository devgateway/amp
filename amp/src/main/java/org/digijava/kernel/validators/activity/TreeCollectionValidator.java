package org.digijava.kernel.validators.activity;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ValidationErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintValidatorContext;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.TreeNodeAware;

/**
 * Ensure that a node is not included twice via its ancestors.
 *
 * @author Octavian Ciubotaru
 */
public class TreeCollectionValidator implements ConstraintValidator {

    @Override
    public void initialize(Map<String, String> arguments) {
    }

    @Override
    public boolean isValid(APIField type, Object value, ConstraintValidatorContext context) {
        SortedSet<AmpAutoCompleteDisplayable<?>> treeNodes = new TreeSet<>();

        Collection col = (Collection) value;
        for (Object item : col) {
            AmpAutoCompleteDisplayable<?> treeNode = ((TreeNodeAware) item).getTreeNode();
            if (treeNode != null) {
                treeNodes.add(treeNode);
            }
        }

        for (AmpAutoCompleteDisplayable<?> node : treeNodes) {
            for (AmpAutoCompleteDisplayable ancestor : node.ancestors()) {
                if (treeNodes.contains(ancestor)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ValidationErrors.FIELD_PARENT_CHILDREN_NOT_ALLOWED;
    }
}
