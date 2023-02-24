/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.WildcardListModel;
import org.dgfoundation.amp.onepager.models.AmpMultiValueDropDownChoiceModel;

/**
 * FM Wrapper for a select field. (both single and multi-selects)
 * 
 * @author mpostelnicu@dgateway.org since Nov 2, 2010
 */
public class AmpSelectFieldPanel<T> extends AmpFieldPanel<T> {

    protected AbstractChoice<?, T> choiceContainer;

    public AbstractChoice<?, T> getChoiceContainer() {
        return choiceContainer;
    }
    
    public boolean dropDownChoiceIsDisabled(T object, int index, String selected){
        return false;
    }
    
    /**
     * Override this when onchange needs to be used
     * also set wantOnSelectionChangedNotifications
     * to return true
     * @param newSelection
     */
    protected void onSelectionChanged(T newSelection){
    }
    
    protected boolean wantOnSelectionChangedNotifications(){
        return false;
    }

    public AmpSelectFieldPanel(String id, IModel<T> model,
            IModel<? extends List<? extends T>> choicesList, String fmName,
            boolean hideLabel, boolean nullValid,
            IChoiceRenderer<? super T> renderer, boolean hideNewLine) {
        super(id, model, fmName, hideLabel, hideNewLine);
        choiceContainer = new DropDownChoice<T>("choice", model, choicesList,
                renderer){
            protected boolean isDisabled(T object, int index, String selected) {
                return dropDownChoiceIsDisabled(object, index, selected);
            };
            
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return AmpSelectFieldPanel.this.wantOnSelectionChangedNotifications();
            };
            
            @Override
            protected void onSelectionChanged(T newSelection) {
                AmpSelectFieldPanel.this.onSelectionChanged(newSelection);
            };
        }.setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpSelectFieldPanel(String id, IModel<T> model,
                               IModel<? extends List<? extends T>> choicesList, String fmName,
                               boolean hideLabel, boolean nullValid,
                               IChoiceRenderer<? super T> renderer){
        this(id, model, choicesList, fmName, hideLabel, nullValid, renderer, false);
    }
    
    public AmpSelectFieldPanel(String id, IModel<T> model,
            IModel<? extends List<? extends T>> choicesList, String fmName,
            boolean hideLabel, boolean nullValid) {
        this(id,model,choicesList,fmName,hideLabel,nullValid,null);
    }

    public AmpSelectFieldPanel(String id, IModel<T> model,
            List<? extends T> choicesList, String fmName, boolean hideLabel,
            boolean nullValid, IChoiceRenderer<? super T> renderer) {
        this(id, model, new WildcardListModel<T>(choicesList), fmName,
                hideLabel, nullValid, renderer);
    }
    
    public AmpSelectFieldPanel(String id, IModel<T> model,
            List<? extends T> choicesList, String fmName, boolean hideLabel,
            boolean nullValid, IChoiceRenderer<? super T> renderer,boolean hideNewLine) {
        this(id, model, new WildcardListModel<T>(choicesList), fmName,
                hideLabel, nullValid, renderer,hideNewLine);
    }

    /**
     * @param id
     * @param fmName
     * @param hideLabel
     * @param nullValid
     * @param renderer
     */
    public AmpSelectFieldPanel(String id,
            IModel<? extends Collection<T>> model,
            IModel<? extends List<? extends T>> choicesList, String fmName,
            boolean hideLabel, boolean isMultiSelect, boolean nullValid,
            IChoiceRenderer<? super T> renderer) {
        super(id, fmName, hideLabel);
        if (isMultiSelect)
            choiceContainer = new ListMultipleChoice<T>("choice", model,
                    choicesList, renderer);
        else
            choiceContainer = new DropDownChoice<T>("choice",
                    new AmpMultiValueDropDownChoiceModel<T>(model),
                    choicesList, renderer).setNullValid(nullValid);
        choiceContainer.setOutputMarkupId(true);
        addFormComponent(choiceContainer);
    }

    public AmpSelectFieldPanel(String id,
            IModel<? extends Collection<T>> model,
            List<? extends T> choicesList, String fmName, boolean hideLabel,
            boolean isMultiSelect, boolean nullValid,
            IChoiceRenderer<? super T> renderer) {
        this(id, model, new WildcardListModel<T>(choicesList), fmName,
                hideLabel, isMultiSelect, nullValid, renderer);
    }

    public AmpSelectFieldPanel(String id, IModel<T> model,
            List<? extends T> choicesList, String fmName, boolean hideLabel,
            boolean nullValid) {
        this(id, model, choicesList, fmName, hideLabel, nullValid, null);
    }

    public AmpSelectFieldPanel(String id,
            IModel<? extends Collection<T>> model,
            List<? extends T> choicesList, String fmName, boolean hideLabel,
            boolean isMultiSelect, boolean nullValid) {
        this(id, model, choicesList, fmName, hideLabel, isMultiSelect,
                nullValid, null);
    }
    
    public AmpSelectFieldPanel(String id,
            IModel<? extends Collection<T>> model,
            IModel<? extends List<? extends T>> choicesList, String fmName, boolean hideLabel,
            boolean isMultiSelect, boolean nullValid) {
        this(id, model, choicesList, fmName, hideLabel, isMultiSelect,
                nullValid, null);
    }
    
}
