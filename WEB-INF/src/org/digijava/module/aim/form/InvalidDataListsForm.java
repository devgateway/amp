package org.digijava.module.aim.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.util.InvalidDataUtil;

/**
 * Form for invalid data lists
 * @author Irakli Kobiashvili
 *
 */
public class InvalidDataListsForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	private List<InvalidDataUtil.ActivitySectorPercentages> invalidSectorpercentages;
	
	public void setInvalidSectorpercentages(List<InvalidDataUtil.ActivitySectorPercentages> invalidSectorpercentages) {
		this.invalidSectorpercentages = invalidSectorpercentages;
	}
	public List<InvalidDataUtil.ActivitySectorPercentages> getInvalidSectorpercentages() {
		return invalidSectorpercentages;
	}
}
