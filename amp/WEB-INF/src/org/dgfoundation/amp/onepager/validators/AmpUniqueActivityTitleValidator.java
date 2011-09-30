/**
 * 
 */
package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * @author mihai
 * @since 30.09.2011
 * Checks if the current activity title text is unique
 */
public class AmpUniqueActivityTitleValidator implements IValidator<String> {

	private final AmpActivityGroup ampActivityGroup;

	/**
	 * @param ampActivityGroup 
	 * 
	 */
	public AmpUniqueActivityTitleValidator(AmpActivityGroup ampActivityGroup) {
		this.ampActivityGroup = ampActivityGroup;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	public void validate(IValidatable<String> validatable) {
			if(validatable.getValue().trim().length()==0) return;
			AmpActivity activityByName = ActivityUtil.getActivityByNameExcludingGroup(validatable.getValue(),ampActivityGroup);
			if(activityByName!=null)
			{
				ValidationError error = new ValidationError();
				error.addMessageKey("AmpUniqueActivityTitleValidator");
				if(activityByName.getTeam()!=null) 
					error.setVariable("workspace", activityByName.getTeam().getName());
				validatable.error(error);
			}


	}

}
