/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.models.EditorWrapperModel;
import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.validators.TranslatableValidators;

/**
 * Wraps a {@link TextArea} container for displaying large text
 * @author mpostelnicu@dgateway.org
 * since Sep 24, 2010
 */
public class AmpTextAreaFieldPanel extends AmpFieldPanel<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 335388041997101521L;
    private final boolean wysiwyg;
    protected TextArea<String> textAreaContainer;
    private Component translationDecorator;
    private WebMarkupContainer closeLink;

    public TextArea<String> getTextAreaContainer() {
		return textAreaContainer;
	}
	
	/**
	 * @param id
	 * @param fmName
	 * @param wysiwyg if true, CKeditor will be added to the {@link TextArea}
	 */
	public AmpTextAreaFieldPanel(String id,IModel<String> model, String fmName,boolean wysiwyg,boolean hideLabel, boolean hideNewLine) {
		super(id, fmName, hideLabel, hideNewLine);
        this.wysiwyg = wysiwyg;
		if (wysiwyg){
			model = (IModel<String>) new EditorWrapperModel((IModel<String>) model, id);
		}
        final IModel<String> finalModel = model;
		textAreaContainer = new TextArea<String>("richText", TranslationDecorator.proxyModel((IModel<String>) model)){
            @Override
            protected void onInitialize() {
                //get validators and put them in the {@link TranslatableValidators}
                TranslatableValidators.alter(finalModel, this);
                super.onInitialize();
            }
        };
		textAreaContainer.setOutputMarkupId(true);
		
		final Label preview = (Label) new Label("previewText", model).setEscapeModelStrings(false);

        closeLink = new WebMarkupContainer("closeLink");
		closeLink.setOutputMarkupId(true);
		closeLink.add(new AttributeModifier("onclick",
                "CKEDITOR.instances['" + textAreaContainer.getMarkupId() + "'].updateElement(); " +
                        "$('#" + preview.getMarkupId() + "').html($('#" + textAreaContainer.getMarkupId() + "').val()); " +
                        "$('#" + preview.getMarkupId() + "').show(); " +
                        "CKEDITOR.instances['" + textAreaContainer.getMarkupId() + "'].destroy(); " +
                        "$('#" + textAreaContainer.getMarkupId() + "').show();" +
                        "$('#" + textAreaContainer.getMarkupId() + "').focus();" +
                        "$('#" + textAreaContainer.getMarkupId() + "').blur();" +
                        "$('#" + textAreaContainer.getMarkupId() + "').hide(); " +
                        "$('#" + closeLink.getMarkupId() + "').hide(); " +
                        "return false;"));
		add(closeLink);
		
		preview.setVisible(false);
		preview.setOutputMarkupId(true);
		if(wysiwyg){
			preview.setVisible(true);
			AmpAuthWebSession ses = (AmpAuthWebSession) getSession();
			
			String language = ses.getLocale().getLanguage();
			if (language == null || language.length() == 0)
				language = "en";
			
			textAreaContainer.add(new AttributeModifier("style", "display: none;"));
			preview.add(new AttributeModifier("onclick",
                    "$('#"+ preview.getMarkupId() +"').hide();" +
                    "CKEDITOR.replace('" + textAreaContainer.getMarkupId() + "', {language: '" + language + "', on:{instanceReady : function( ev ){this.focus();}}} );" +
                    "$('#"+ closeLink.getMarkupId() +"').show();"));
		}
		add(preview);

        translationDecorator = TranslationDecorator.of("trnContainer", (IModel<String>) textAreaContainer.getModel(), (wysiwyg ? this : textAreaContainer));
        add(translationDecorator);
		addFormComponent(textAreaContainer);
	}

	public AmpTextAreaFieldPanel(String id,IModel<String> model, String fmName,boolean wysiwyg, AmpFMTypes fmType) {
		this(id, model, fmName, wysiwyg);
		this.fmType = fmType;
	}
	
	public AmpTextAreaFieldPanel(String id,IModel<String> model, String fmName,boolean wysiwyg) {
		this(id, model, fmName, wysiwyg, false, false);
	}

    public WebMarkupContainer getCloseLink() {
        return closeLink;
    }

    public boolean isWysiwyg() {
        return wysiwyg;
    }

    @Override
    protected void onAjaxOnError(AjaxRequestTarget target) {
        TranslatableValidators.onError(target, formComponent, translationDecorator);
    }
}
