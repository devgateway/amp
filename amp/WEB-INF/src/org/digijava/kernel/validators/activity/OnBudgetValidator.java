package org.digijava.kernel.validators.activity;

import static org.digijava.kernel.ampapi.endpoints.activity.field.APIFieldUtil.readFieldValueOrDefault;

import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Verify that dependent fields specify a value when a project is on budget.
 *
 * @author Octavian Ciubotaru
 */
public class OnBudgetValidator extends ConditionalRequiredValidator {

    /**
     * Used to mark which fields are required when project is on budget.
     */
    public static final String ON_BUDGET_KEY = "on_budget";

    private static final String ACTIVITY_BUDGET_FIELD_NAME = "activity_budget";

    public OnBudgetValidator() {
        super(ON_BUDGET_KEY);
    }

    @Override
    public boolean isActive(APIField type, Object value) {
        APIField activityBudgetField = type.getField(ACTIVITY_BUDGET_FIELD_NAME);

        AmpCategoryValue activityBudget = readFieldValueOrDefault(activityBudgetField, value, null);

        return activityBudget != null
                && activityBudget.getValue().equals(CategoryConstants.ACTIVITY_BUDGET_ON.getValueKey());
    }
}
