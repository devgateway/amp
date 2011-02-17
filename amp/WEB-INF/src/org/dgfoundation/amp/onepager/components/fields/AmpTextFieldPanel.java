/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu@dgateway.org
 * since Sep 23, 2010
 */
public class AmpTextFieldPanel<T> extends AmpFieldPanel<T> {

	private static final long serialVersionUID = 611374046300554626L;
	protected TextField<T> textContainer;


	public TextField<T> getTextContainer() {
		return textContainer;
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName) {
		this(id,model,fmName,false);
	}
	
	
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel) {
		super(id, fmName, hideLabel);
		textContainer = new TextField<T>("textContainer",model);
		textContainer.setOutputMarkupId(true);
		addFormComponent(textContainer);
	}
	
	public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
		textContainer = new TextField<T>("textContainer",model);
		textContainer.setOutputMarkupId(true);
		addFormComponent(textContainer);
	}
	
	
}
