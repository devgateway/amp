package org.digijava.kernel.ampapi.endpoints.activity.validators;

import java.util.Map;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityErrors;
import org.digijava.kernel.ampapi.endpoints.activity.ObjectImporter;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorMessage;

/**
 * Checks "changed" fields that they are importable
 * 
 * @author Nadejda Mandrescu
 */
public class AllowedInputValidator extends InputValidator {

    @Override
    public ApiErrorMessage getErrorMessage() {
        return ActivityErrors.FIELD_READ_ONLY;
    }

    @Override
    public boolean isValid(ObjectImporter importer, Map<String, Object> newFieldParent,
            APIField fieldDescription, String fieldPath) {
        return true;
//      if (importer.isUpdate()) {
//          if (fieldDescription.get(ActivityEPConstants.FIELD_NAME).equals(ActivityEPConstants.))
//      }
        
//      if (!importable && newField != null) {
//          Object oldField = null;
//          if (oldFieldParent != null)
//              oldField = oldFieldParent.get(fieldDescription.getString(ActivityEPConstants.FIELD_NAME));
//          // this is an import and fields are from allowed values
//          // TODO: check they match with allowed values, skipping for now, validation of id will happen within allowed values
//          if (oldField == null && !importer.isUpdate())
//              return true;
//          if (newField != null && !newField.equals(oldField))
//              return false;
//          if (oldField != null && !oldField.equals(newField))
//              return false;
//          //otherwise: newfield & oldfield are both null -> true
//          //       or: newfield & oldfield are equal and not null -> true
//      }
//      return true;
    }

}
