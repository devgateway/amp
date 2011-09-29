/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import java.io.Serializable;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.models.EditorWrapperModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
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
	public AmpTextAreaFieldPanel(String id,IModel<T> model, String fmName,boolean wysiwyg,boolean hideLabel, boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
		if (wysiwyg){
			model = (IModel<T>) new EditorWrapperModel((IModel<String>) model, id);
		}
		
		textAreaContainer = new TextArea<T>("richText", model);
		textAreaContainer.setOutputMarkupId(true);
		Label preview = (Label) new Label("previewText", model).setEscapeModelStrings(false);
		preview.setVisible(false);
		preview.setOutputMarkupId(true);
		if(wysiwyg){
			preview.setVisible(true);
			textAreaContainer.add(new SimpleAttributeModifier("style", "display: none;"));
			preview.add(new SimpleAttributeModifier("onclick", "$('#"+ preview.getMarkupId() +"').hide();CKEDITOR.replace('" + textAreaContainer.getMarkupId() + "', {on:{instanceReady : function( ev ){this.focus();}}} );$('#"+ textAreaContainer.getMarkupId() +"').show();"));
		}
		add(preview);
		addFormComponent(textAreaContainer);
	}

	public AmpTextAreaFieldPanel(String id,IModel<T> model, String fmName,boolean wysiwyg, AmpFMTypes fmType) {
		this(id, model, fmName, wysiwyg);
		this.fmType = fmType;
	}
	
	public AmpTextAreaFieldPanel(String id,IModel<T> model, String fmName,boolean wysiwyg) {
		this(id, model, fmName, wysiwyg, false, true);
	}
	

}
