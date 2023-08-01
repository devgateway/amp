package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AmpStyledSelectFieldPanel<T> extends AmpFieldPanel<T> {

    protected Select<T> selectComponent;

    public Select<T> getSelectComponent() {
        return selectComponent;
    }

    public AmpStyledSelectFieldPanel(String id, IModel<T> model, IModel<? extends List<T>> choicesList, String fmName,
                                     boolean hideLabel, IOptionRenderer<T> renderer, boolean hideNewLine) {
        super(id, model, fmName, hideLabel, hideNewLine);

        selectComponent = new Select<T>("choice", model);
        selectComponent.setOutputMarkupId(true);

        SelectOptions<T> options = new SelectOptions<T>("options", choicesList, renderer) {
            @Override
            protected SelectOption<T> newOption(String text, IModel<? extends T> model) {
                SelectOption<T> option = super.newOption(text, model);
                customizeOption(option, text, model);
                option.setEnabled(optionIsEnabled(model));

                return option;
            }
        };

        selectComponent.add(options);
        addFormComponent(selectComponent);
    }

    /**
     * Set a specific option to be enabled or not based on the model object
     * The method is similar to {@link AmpSelectFieldPanel#dropDownChoiceIsDisabled(Object, int, String)}
     * 
     * @param model
     * @return boolean isEnabled
     */
    public boolean optionIsEnabled(IModel<? extends T> model) {
        return true;
    }

    protected void customizeOption(SelectOption option, String text, IModel<? extends T> model) {
    }
}