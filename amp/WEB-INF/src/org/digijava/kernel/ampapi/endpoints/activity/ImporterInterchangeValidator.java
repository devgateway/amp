package org.digijava.kernel.ampapi.endpoints.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils.intToLong;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ErrorDecorator;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;
import org.digijava.kernel.validation.ConstraintDescriptor;
import org.digijava.kernel.validation.ConstraintValidator;
import org.digijava.kernel.validation.ConstraintViolation;
import org.digijava.kernel.validation.TranslatedValueContext;
import org.digijava.kernel.validation.Path;
import org.digijava.kernel.validation.TranslationContext;
import org.digijava.kernel.validation.Validator;
import org.digijava.kernel.validators.activity.ComponentFundingOrgRoleValidator;
import org.digijava.kernel.validators.activity.FundingWithTransactionsValidator;
import org.digijava.kernel.validators.activity.ImplementationLevelValidator;
import org.digijava.kernel.validators.activity.PledgeOrgValidator;

/**
 * Acts as a bridge between importer and interchangeable validation.
 *
 * Note: current implementation ignores completely constraint violations coming from field validators.
 *
 * @author Octavian Ciubotaru
 */
public class ImporterInterchangeValidator {

    private final Function<Supplier<Set<ConstraintViolation>>, Set<ConstraintViolation>> executor;
    private Validator validator;

    private Map<Integer, ApiErrorMessage> errors;


    public ImporterInterchangeValidator(Map<Integer, ApiErrorMessage> errors,
            Function<Supplier<Set<ConstraintViolation>>, Set<ConstraintViolation>> executor) {
        this.errors = errors;
        this.executor = executor;

        validator = new Validator();
    }

    /**
     * Invoke interchangeable validation on the root object. If there are constraint violations generated by
     * constraints defined at class level, those violations will be injected into the json object and added to the
     * errors map.
     *
     * Validation is recursive and will reveal constraint violations for root object and also for objects deep into
     * the object tree.
     *
     * @param type type information for the object
     * @param root internal representation of the object
     * @param translationContext for accessing translated values
     * @param groups validation groups
     * @return all found constraint violations
     */
    public Set<ConstraintViolation> validate(APIField type, Object root, TranslationContext translationContext,
            Class<?>... groups) {
        return doInValidatorEnv(() -> validator.validate(type, root, translationContext, groups));
    }

    /**
     * Validate only constraints targeting the current field.
     *
     * @param field the field to validate
     * @param value the value of the field
     * @param translatedValueContext for accessing translated values
     * @param groups validation groups
     * @return all found constraint violations
     */
    public Set<ConstraintViolation> validateField(APIField field, Object value,
            TranslatedValueContext translatedValueContext, Class<?>... groups) {
        return doInValidatorEnv(() -> validator.validateField(field, value, translatedValueContext, groups));
    }

    /**
     * Integrate constraint violation related to a field into the result.
     *
     * @param violations violations for the field
     * @param json json relative to the field (json containing the validated field)
     * @param fieldPath path of the json
     */
    public void integrateFieldErrorsIntoResult(Set<ConstraintViolation> violations, Map<String, Object> json,
            String fieldPath) {
        integrateErrorsIntoResult(violations, json, ConstraintDescriptor.ConstraintTarget.FIELD, fieldPath);
    }

    private Set<ConstraintViolation> doInValidatorEnv(Supplier<Set<ConstraintViolation>> supplier) {
        return executor.apply(supplier);
    }

    /**
     * Integrate constraint violations related to types into the result.
     *
     * @param violations all violations, only violations generated by validators defined at type level
     *                   are taken in account
     * @param json json representation of the object (root level)
     */
    public void integrateTypeErrorsIntoResult(Set<ConstraintViolation> violations, Map<String, Object> json) {
        integrateErrorsIntoResult(violations, json, ConstraintDescriptor.ConstraintTarget.TYPE, null);
    }

    /**
     *
     * @param violations constraint violation to integrate into json
     * @param json json representation of the object
     * @param constraintTarget integrate only errors for specific violations
     */
    private void integrateErrorsIntoResult(Set<ConstraintViolation> violations, Map<String, Object> json,
            ConstraintDescriptor.ConstraintTarget constraintTarget, String parentFieldPath) {
        for (ConstraintViolation violation : violations) {
            if (violation.getConstraintDescriptor().getTarget() == constraintTarget) {
                List<Path.Node> nodes = ImmutableList.copyOf(violation.getPath().iterator());

                Predicate<Object> leafFilter = getLeafFilter(violation);

                String fieldPath;
                if (parentFieldPath != null) {
                    fieldPath = parentFieldPath + "~" + violation.getPath().toString();
                } else {
                    fieldPath = violation.getPath().toString();
                }

                boolean errorsAdded = walk(json, nodes, fieldPath, violation.getMessage(), leafFilter);

                if (!errorsAdded) {
                    throw new IllegalStateException("Constraint violation not added to json: " + violation);
                }
            }
        }
    }

    /**
     * Returns value filter for leaf nodes. Some field paths will match multiple fields in the final json object. This
     * filter can be used to select for which fields exactly the error applies to.
     *
     * @param violation constraint violation for which a filter is needed
     * @return filter that accepts the value from json
     */
    private Predicate<Object> getLeafFilter(ConstraintViolation violation) {
        Predicate<Object> leafFilter;

        Class<? extends ConstraintValidator> validatorClass =
                violation.getConstraintDescriptor().getConstraintValidatorClass();

        if (validatorClass.equals(ComponentFundingOrgRoleValidator.class)) {
            Long orgId = (Long) violation.getAttributes().get(ComponentFundingOrgRoleValidator.ATTR_ORG_ID);
            leafFilter = value -> orgId.equals(intToLong(value));
        } else if (validatorClass.equals(ImplementationLevelValidator.class)) {
            Long locId = (Long) violation.getAttributes().get(ImplementationLevelValidator.ATTR_LOC_ID);
            leafFilter = value -> locId == null || locId.equals(intToLong(value));
        } else if (validatorClass.equals(FundingWithTransactionsValidator.class)) {
            // this condition is not sufficient, will match funding items what do not have transactions at all
            leafFilter = Objects::isNull;
        } else if (validatorClass.equals(PledgeOrgValidator.class)) {
            // this condition is not sufficient, will match funding items with correct pledge
            Long orgId = (Long) violation.getAttributes().get(PledgeOrgValidator.FUNDING_PLEDGE_ID);
            leafFilter = value -> orgId.equals(intToLong(value));
        } else {
            leafFilter = o -> true;
        }

        return leafFilter;
    }

    /**
     * Walk the original json and inject errors at the appropriate locations as specified by nodes. If there are
     * multiple leaf nodes where error can be injected, then it will be injected in all places for which leafFilter
     * returns true.
     *
     * If the last node is not present in json, then it will be created.
     *
     * @param json original json
     * @param nodes location where to inject the errors
     * @param fieldPath full fields path of the error
     * @param message error message to inject
     * @param leafFilter errors will be applied only to fields filtered by this predicate, input is the leaf json value
     * @return true if the error message was injected at least once
     */
    private boolean walk(Map<String, Object> json, List<Path.Node> nodes, String fieldPath, ApiErrorMessage message,
            Predicate<Object> leafFilter) {
        boolean errorsAdded = false;
        String fieldName = nodes.get(0).getName();
        Object value = json.get(fieldName);
        if (nodes.size() == 1) { // leaf case
            if (leafFilter.test(value)) {
                errorsAdded = true;
                ErrorDecorator.addError(json, fieldName, fieldPath, message, errors);
            }
        } else if (value != null) {
            List<Path.Node> subNodes = nodes.subList(1, nodes.size());
            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                for (Object element : collection) {
                    errorsAdded |= walk((Map) element, subNodes, fieldPath, message, leafFilter);
                }
            } else {
                errorsAdded = walk((Map) value, subNodes, fieldPath, message, leafFilter);
            }
        }
        return errorsAdded;
    }
}
