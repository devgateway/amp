package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.markup.html.form.ChoiceRenderer;

public class TranslatedChoiceRenderer<T> extends ChoiceRenderer<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object getDisplayValue(T object) {
		return TranslatorUtil.getTranslatedText(object.toString());
	}
	
}
