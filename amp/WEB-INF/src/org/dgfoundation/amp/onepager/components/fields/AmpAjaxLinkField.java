/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

/**
 * Encaspulates an ajax link of type {@link AjaxLink}
 * @author aartimon@dginternational.org
 * @since May 9, 2011
 */
public abstract class AmpAjaxLinkField extends AmpFieldPanel<Void> {

	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;

	public IndicatingAjaxLink getButton() {
		return button;
	}

	/**
	 * Escalated method invoker for wrapped {@link AjaxButton#onSubmit()}
	 * @param target
	 * @param form
	 */
	protected abstract void onClick(AjaxRequestTarget target);
	
	/**
	 * 
	 * @param id
	 * @param fmName
	 */
	public AmpAjaxLinkField(String id, String fmName, String buttonCaption) {
		super(id, fmName,true);
		button = new IndicatingAjaxLink("fieldButton") {
			private static final long serialVersionUID = -5699378405978605979L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				AmpAjaxLinkField.this.onClick(target);
			}
		};
		button.add(new AttributeModifier("value", new Model(buttonCaption)));
		
		add(button);
	}

}
