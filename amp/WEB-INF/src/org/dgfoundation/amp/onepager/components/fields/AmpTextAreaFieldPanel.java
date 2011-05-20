/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.util.CKEditorBehavior;

import wicket.contrib.tinymce.TinyMceBehavior;

/**
 * Wraps a {@link TextArea} container for displaying large text
 * @author mpostelnicu@dgateway.org
 * since Sep 24, 2010
 */
public class AmpTextAreaFieldPanel<T> extends AmpFieldPanel<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 335388041997101521L;
	protected TextArea<T> textAreaContainer;

	public TextArea<T> getTextAreaContainer() {
		return textAreaContainer;
	}
	
	/**
	 * @param id
	 * @param fmName
	 * @param wysiwyg if true, {@link TinyMceBehavior} will be added to the {@link TextArea}
	 */
	public AmpTextAreaFieldPanel(String id,IModel<T> model, String fmName,boolean wysiwyg) {
		super(id, fmName);
		textAreaContainer = new TextArea<T>("richText", model);
		textAreaContainer.setOutputMarkupId(true);
		if(wysiwyg){
			textAreaContainer.add(new SimpleAttributeModifier("onclick", "CKEDITOR.replace('" + textAreaContainer.getMarkupId() + "');"));
		}
		addFormComponent(textAreaContainer);
	}


}
