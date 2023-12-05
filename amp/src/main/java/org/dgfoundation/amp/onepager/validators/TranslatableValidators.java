package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidatorAdapter;
import org.apache.wicket.validation.validator.StringValidator;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.models.ResourceTranslationModel;
import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aartimon
 * @since 06/12/13
 */
public class TranslatableValidators implements IValidator<String> {
    private final List<IValidator<? super String>> validators;
    private final IModel<String> newModel;
    public IModel<String> originalModel;

    
    private static boolean isUnqTtlValidator(IValidator<? super String> validator) {
        return validator instanceof ValidatorAdapter && 
                ((ValidatorAdapter) validator).getValidator() instanceof AmpUniqueActivityTitleValidator;
    }
    
    private static boolean isStringValidator(IValidator<? super String> validator) {
        return validator instanceof StringValidator;
    }
    
    private List<IValidator<? super String>> excludeUnnestableValidators(List<IValidator<? super String>> originalValidators) {
        List<IValidator<? super String>> res = new ArrayList<>();
        if (validators != null) {
            res = validators.stream().filter(v -> !isUnqTtlValidator(v) && !isStringValidator(v)).collect(Collectors.toList());
        }
        return Collections.unmodifiableList(res);
    }
    
    public TranslatableValidators(IModel<String> originalModel, IModel<String> newModel , List<IValidator<? super String>> validators) {
        this.originalModel = originalModel;
        this.newModel = newModel;
        this.validators = excludeUnnestableValidators(validators);
        
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        //we need to wrap validatable to return the output of the current model
        TranslatableValidatableWrapper<String> v = new TranslatableValidatableWrapper<String>(validatable, newModel) {
            @Override
            protected String newGetValue() {
                if (originalModel.getObject() == null)
                    return "";
                return originalModel.getObject();
            }
        };

        for (IValidator validator: validators){
            validator.validate(v);
        }
    }

    @SuppressWarnings("unchecked")
    public static void alter(IModel<String> origModel, FormComponent<String> component) {
        if (component.getModel() instanceof TranslationDecoratorModel
                || component.getModel() instanceof ResourceTranslationModel){
            List<IValidator<? super String>> validators = component.getValidators();
            //strip all validators from component
            //apart from uniqueTitleValidator
            //and maxStringLength validator
            for (IValidator validator : validators){
                if (!isUnqTtlValidator(validator) && !isStringValidator(validator)){
                    component.remove(validator);
                }
            }
            TranslatableValidators tv = new TranslatableValidators(origModel, component.getModel(), validators);
            component.add(tv);
        }
    }
    
    /**
     * if flag is set, will return true and clear it (set to false)
     * @param formComponent
     * @return
     */
    private static boolean isFlagSet(FormComponent formComponent) {
          try {
                Field thisLevel0Field = formComponent.getClass().getDeclaredField("this$0");
                Field[] fields = formComponent.getClass().getDeclaredFields();
                thisLevel0Field.setAccessible(true);
                Object thisLevel0 = thisLevel0Field.get(formComponent);
                boolean result = false;
                if (thisLevel0 instanceof AmpTextAreaFieldPanel) {
                    result = ((AmpTextAreaFieldPanel) thisLevel0).isUniqueTitleValidatorError();
                }
                if (result) {
                    ((AmpTextAreaFieldPanel) thisLevel0).setUniqueTitleValidatorError(false);
                }
                return result;
            } catch (Exception  e) {
                return false;
            }
    }
    
    public static void onError(AjaxRequestTarget target, FormComponent formComponent, Component translationDecorator) {
        
        final boolean ieUserAgent = WebSession.get().getClientInfo().getUserAgent().contains("Trident");
        //this is hacky but enables the activity form to be usable until a better solution is found
        //On IE validation is triggered 2 times and the second time it is failing making impossible
        //to switch between language tabs. Here we allow to switch tabs in IE even if the validation fails
        //Everything will work as expected, except that now the user can switch on IE to a new tab even when the validation fails
        if (formComponent.getModel() instanceof TranslationDecoratorModel && ieUserAgent != true){
            TranslationDecoratorModel tdm = (TranslationDecoratorModel) formComponent.getModel();
            if (tdm.getLangModel().getObject() != null){
                //we need to switch the language tab to the current interface language
                //since the validation is only done for the current language
                
                /* if the failing validator is AmpUniqueActivityTitleValidator
                 * switching shouldn't be performed
                 * */
                if (!isFlagSet(formComponent)) {
                    tdm.getLangModel().setObject(null);
                    formComponent.clearInput();
                }
                if (translationDecorator != null)
                    target.add(translationDecorator);
            }
        }
    }

    public List<IValidator<? super String>> getValidators() {
        return validators;
    }
}
