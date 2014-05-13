package org.dgfoundation.amp.onepager.validators;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.dgfoundation.amp.onepager.models.TranslationDecoratorModel;

/**
 * @author aartimon
 * @since 06/12/13
 */
public abstract class TranslatableValidatableWrapper<T> implements IValidatable<T> {
    private final IModel<T> newModel;
    private IValidatable<T> originalValidatable;

    public TranslatableValidatableWrapper(IValidatable<T> originalValidatable, IModel<T> newModel) {
        this.originalValidatable = originalValidatable;
        this.newModel = newModel;
    }

    @Override
    public final T getValue() {
        if (newModel instanceof TranslationDecoratorModel){
            TranslationDecoratorModel tdm = (TranslationDecoratorModel) newModel;
            if (tdm.getLangModel().getObject() == null) //we're in the current language then use the original getValue
                return originalValidatable.getValue();
        }
        return newGetValue();
    }

    protected abstract T newGetValue();

    @Override
    public void error(IValidationError error) {
        originalValidatable.error(error);
    }

    @Override
    public boolean isValid() {
        return originalValidatable.isValid();
    }

    @Override
    public IModel<T> getModel() {
        return originalValidatable.getModel();
    }
}
