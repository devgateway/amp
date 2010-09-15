/**
 * 
 */
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class RequiredTextField extends TextField<String> {
	public RequiredTextField(String id) {
		super(id);
		setRequired(true);
	}
	
	@Override
	public boolean isVisible() {
		System.out.println("Am I visiblesss?");
		return true;
	}

	public RequiredTextField(String id, IModel model) {
		super(id, model);
		setRequired(true);
	}
}