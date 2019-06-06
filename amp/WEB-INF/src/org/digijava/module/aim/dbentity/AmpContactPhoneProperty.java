package org.digijava.module.aim.dbentity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.ALWAYS;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author Octavian Ciubotaru
 */
public class AmpContactPhoneProperty extends AmpContactProperty {

    @Interchangeable(fieldTitle = "Value", required = ALWAYS, importable = true,
            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE)
    private String value;

    @Interchangeable(fieldTitle = "Extension Value", importable = true,
            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE_EXTENSION)
    private String extensionValue;

    @Interchangeable(fieldTitle = "Type", importable = true, pickIdOnly = true,
            discriminatorOption = CategoryConstants.CONTACT_PHONE_TYPE_KEY)
    private AmpCategoryValue type;

    public String getExtensionValue() {
        return extensionValue;
    }

    public void setExtensionValue(String extensionValue) {
        this.extensionValue = extensionValue;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public AmpCategoryValue getType() {
        return type;
    }

    public void setType(AmpCategoryValue type) {
        this.type = type;
    }

    public String getPhoneCategory() {
        if (type != null) {
            return type.getValue();
        } else {
            return "None";
        }
    }

    public String getValueAsFormatedPhoneNum() {
        return ContactInfoUtil.getFormattedPhoneNum(type, getValue());
    }
}
