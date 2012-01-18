package org.dgfoundation.amp.permissionmanager.components.features.fields;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;

public class AmpPMSimpleChoiceRenderer extends ChoiceRenderer{

	@Override
	public Object getDisplayValue(Object object) {
		// TODO Auto-generated method stub
		return TranslatorUtil.getTranslation(object.toString());
		//return super.getDisplayValue(object);
	}

}
