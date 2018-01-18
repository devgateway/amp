/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.util.IdWithValueShim;

/**
 * @author mihai
 * @since 30.09.2011
 * Checks if the current activity title text is unique
 */
public class AmpUniqueActivityTitleValidator implements IValidator<String> {

    private final PropertyModel<AmpActivityGroup> ampActivityGroupModel;

    /**
     * @param propertyModel 
     * 
     */
    public AmpUniqueActivityTitleValidator(PropertyModel<AmpActivityGroup> propertyModel) {
        this.ampActivityGroupModel = propertyModel;
    }

    
    
    /**
     * Sets a boolean in the validatable component, after reaching for the outer classes
     * via reflection hacks
     * @param validatable
     */
    private static void setFlag(IValidatable<String> validatable) {
          try {
                Field thisLevel0Field = validatable.getClass().getDeclaredField("this$0");
                Field[] fields = validatable.getClass().getDeclaredFields();
                thisLevel0Field.setAccessible(true);
                Object thisLevel0 = thisLevel0Field.get(validatable);
                Class thisLevel1Class = thisLevel0Field.get(validatable).getClass();
                Field thisLevel1Field = thisLevel1Class.getDeclaredField("this$0");
                thisLevel1Field.setAccessible(true);
                Object thisLevel1 = thisLevel1Field.get(thisLevel0);
                if (thisLevel1 instanceof AmpTextAreaFieldPanel) {
                    ((AmpTextAreaFieldPanel) thisLevel1).setUniqueTitleValidatorError(true);
                }
            } catch (Exception  e) {

            }
    }
    
    
    /* (non-Javadoc)
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void validate(IValidatable<String> validatable) {
        
        if (StringUtils.isBlank(validatable.getValue())) {
            return;
        }
        
        IdWithValueShim shim = ActivityUtil.getActivityCollisions(validatable.getValue(),
                ampActivityGroupModel.getObject());

        if (shim != null) {
            ValidationError error = new ValidationError();
            if (shim.getValue() != null) {
                error.addKey("AmpUniqueActivityTitleValidator");
                error.setVariable("workspace", shim.getValue());
            } else {
                error.addKey("AmpUniqueActivityTitleNoWorkspaceValidator");
            }

            setFlag(validatable);
            if (AmpTextAreaFieldPanel.class.isAssignableFrom(validatable.getClass())) {
                ((AmpTextAreaFieldPanel) validatable).setUniqueTitleValidatorError(true);
            }
            
            validatable.error(error);
        }
    }

}
