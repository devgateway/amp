package org.digijava.kernel.ampapi.endpoints.activity.field;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.FEContext;
import org.digijava.kernel.ampapi.endpoints.activity.FMService;
import org.digijava.kernel.ampapi.endpoints.activity.FieldMap;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * Enumerator which sets IATI identifiers to be importable and mandatory.
 * IATI identifier name is configurable.
 *
 * @author Octavian Ciubotaru
 */
public class ActivityFieldsEnumerator extends FieldsEnumerator {

    private String iatiIdentifierField;

    public ActivityFieldsEnumerator(FieldInfoProvider fieldInfoProvider,
            FMService fmService, TranslatorService translatorService,
            Function<String, Boolean> allowMultiplePrograms) {
        super(fieldInfoProvider, fmService, translatorService, allowMultiplePrograms);
        this.iatiIdentifierField = InterchangeUtils.getAmpIatiIdentifierFieldName();
    }

    @Override
    protected APIField describeField(Field field, FEContext context) {
        APIField apiField = super.describeField(field, context);

        Interchangeable interchangeable = context.getIntchStack().peek();
        String fieldTitle = FieldMap.underscorify(interchangeable.fieldTitle());

        if (!AmpOfflineModeHolder.isAmpOfflineMode() && isFieldIatiIdentifier(fieldTitle)) {
            apiField.setRequired(ActivityEPConstants.FIELD_ALWAYS_REQUIRED);
            apiField.setImportable(true);
        }

        return apiField;
    }

    /**
     * Decides whether a field stores iati-identifier value
     *
     * @param fieldName
     * @return true if is iati-identifier
     */
    private boolean isFieldIatiIdentifier(String fieldName) {
        return StringUtils.equals(this.iatiIdentifierField, fieldName);
    }

    protected boolean isVisible(String fmPath, FEContext context) {
        Interchangeable interchangeable = context.getIntchStack().peek();
        String fieldTitle = FieldMap.underscorify(interchangeable.fieldTitle());

        if (!AmpOfflineModeHolder.isAmpOfflineMode() && isFieldIatiIdentifier(fieldTitle)) {
            return true;
        } else {
            return super.isVisible(fmPath, context);
        }
    }
}
