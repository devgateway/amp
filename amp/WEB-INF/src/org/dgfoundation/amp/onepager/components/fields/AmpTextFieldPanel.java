/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.StringValidator;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.validators.TranslatableValidators;

/**
 * @author mpostelnicu@dgateway.org
 * since Sep 23, 2010
 */
public class AmpTextFieldPanel<T> extends AmpFieldPanel<T> {

    private static final long serialVersionUID = 611374046300554626L;
    public static int DEFAULT_MAX_SIZE=255;
    protected TextField<T> textContainer;
    private Component translationDecorator;
    /**
     * Since all validation errors are going through TranslatableValidators.onError(), 
     * and there is no way to send the validator that generated the cause, 
     * it will be bound on 
     */
    private boolean uniqueTitleValidatorError = false;


    public TextField<T> getTextContainer() {
        return textContainer;
    }
    
    public void setTextContainerMaxSize(int maxSize) {
         getTextContainer().add(StringValidator.maximumLength(maxSize));
    }
    public void setTextContainerDefaultMaxSize() {
        setTextContainerMaxSize(DEFAULT_MAX_SIZE);
    }

    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpTextFieldPanel(String id, IModel<T> model, String fmName) {
        this(id,model,fmName,false);
    }

    public AmpTextFieldPanel(String id, IModel<T> model, String fmName, AmpFMTypes fmType) {
        this(id, model, fmName);
        this.fmType = fmType;
    }
    
    public AmpTextFieldPanel(String id, IModel<T> model, String fmName,AmpFMTypes fmType, boolean showRedStarForNotReqComp) {
        this(id, model, fmName, false, false,showRedStarForNotReqComp);
    }

    
    public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel) {
        this(id, model, fmName, hideLabel, false, false);
    }

    public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine) {
        this(id, model, fmName, hideLabel, hideNewLine, false);
    }
    public AmpTextFieldPanel(String id, IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine, boolean showRedStarForNotReqComp) {
        this(id, model, fmName, hideLabel, hideNewLine, showRedStarForNotReqComp, false);
    }

    
    @SuppressWarnings("unchecked")
    public AmpTextFieldPanel(String id, final IModel<T> model, String fmName,boolean hideLabel, boolean hideNewLine, boolean showRedStarForNotReqComp, boolean enableReqStar) {
        super(id, model, fmName, hideLabel, hideNewLine, showRedStarForNotReqComp, enableReqStar);

        textContainer = new TextField("textContainer",TranslationDecorator.proxyModel((IModel<String>) model)) {
            @SuppressWarnings("unchecked")
            @Override
            public final <T> IConverter<T> getConverter(Class<T> type){
                IConverter ic = getInternalConverter(type);
                if (ic != null) return ic;
//              if(getInternalConverter(type)!=null) return getInternalConverter(type);
                return super.getConverter(type);
            }

            @Override
            protected void onInitialize() {
                //get validators and put them in the {@link TranslatableValidators}
                TranslatableValidators.alter((IModel<String>) model, this);
                super.onInitialize();
            }
        };
        textContainer.setOutputMarkupId(true);

        translationDecorator = TranslationDecorator.of("trnContainer", (IModel<String>) textContainer.getModel(), textContainer);
        add(translationDecorator);
        addFormComponent(textContainer);
    }

    @Override
    protected void onAjaxOnError(AjaxRequestTarget target) {
        if (translationDecorator instanceof TranslationDecorator){
            ((TranslationDecorator) translationDecorator).getSwitchingDisabled().setObject(Boolean.TRUE);
        }
        
        TranslatableValidators.onError(target, formComponent, translationDecorator);
    }
    @Override
    protected void onAjaxOnUpdate(AjaxRequestTarget target) {
        if (translationDecorator instanceof TranslationDecorator){
            //clear switching disabled
            ((TranslationDecorator) translationDecorator).getSwitchingDisabled().setObject(Boolean.FALSE);
            target.add(((TranslationDecorator) translationDecorator).getCurrentLabel());
        }
    }
    
    public boolean isComponentMultilingual () {
        return translationDecorator instanceof TranslationDecorator;
    }

    public boolean isUniqueTitleValidatorError() {
        return uniqueTitleValidatorError;
    }

    public void setUniqueTitleValidatorError(boolean uniqueTitleValidatorError) {
        this.uniqueTitleValidatorError = uniqueTitleValidatorError;
    }
}
