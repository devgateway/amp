/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpAutoCompleteModelParam;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;

/**
 * Encapsulates a "google suggest" textbox - auto complete text field for AMP
 * designed to return the selected object (not the string). This class also
 * implements {@link IAjaxIndicatorAware} making the control show a LOADING icon
 * whenever an async request is made
 * 
 * @author mpostelnicu@dgateway.org since Sep 30, 2010
 * @deprecated
 * @see AmpAutocompleteFieldPanel
 */
public abstract class AbstractAmpAutoCompleteTextField<CHOICE> extends
AbstractAutoCompleteTextField<CHOICE> implements IAjaxIndicatorAware {
    private static final long serialVersionUID = -4601873923915411280L;
    protected Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass;
    protected Map<AmpAutoCompleteModelParam, Object> modelParams;
    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

    public Map<AmpAutoCompleteModelParam, Object> getModelParams() {
        return modelParams;
    }

    /**
     * @see org.apache.wicket.ajax.IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
     */
    public String getAjaxIndicatorMarkupId() {
        return indicatorAppender.getMarkupId();
    }

    /**
     * Invoked when the choice is picked from the autocomplete list
     * 
     * @param target
     * @param choice
     *            the <CHOICE> object selected
     */
    public abstract void onSelect(AjaxRequestTarget target, CHOICE choice);

    /**
     * @param choice
     *            the current choice to be rendered
     * @return the level in the tree of the current choice, or null if no levels
     *         should be drawn
     */
    public abstract Integer getChoiceLevel(CHOICE choice);

    public static final String DEFAULT_SEARCH_TEXT = "press down arrow to view all";
    protected String input;

    /**
     * Creates a new auto complete text field with the given id and the given
     * model for searching objects
     * 
     * @param id
     *            the id of the component
     * @param objectListModelClass
     *            the model that will be used to search the objects. This model
     *            receives as input in constructor the keyword search and a Map
     *            of parameters.
     * @see AbstractAmpAutoCompleteModel
     */
    public AbstractAmpAutoCompleteTextField(
            final Class<? extends AbstractAmpAutoCompleteModel<CHOICE>> objectListModelClass) {
        super("autoCompleteText", new Model<String>(DEFAULT_SEARCH_TEXT),
                new AutoCompleteSettings());

        setType((Class<?>) null);
        this.objectListModelClass = objectListModelClass;
        this.setOutputMarkupId(true);
        add(new AttributeModifier("autocomplete", "off"));
        this.modelParams = new HashMap<AmpAutoCompleteModelParam, Object>();

        // generate the renderer using the abstract method of the wrapper to
        // display the object (we write less code in pages)
        AbstractAutoCompleteRenderer<CHOICE> renderer = new AbstractAutoCompleteRenderer<CHOICE>() {
            private static final long serialVersionUID = 8827807777556937702L;

            @Override
            protected void renderChoice(CHOICE object, Response response,
                    String criteria) {
                try {
                    Integer level = getChoiceLevel(object);
                    if (level != null && level > 0)
                        response.write(new String(new char[level * 2]).replace(
                                "\0", "&nbsp;"));
                    if (level != null && level == 1)
                        response.write("<i>");
                    if(input.length()<2) response.write(getChoiceValue(object));else
                        response.write(Pattern
                                .compile(input, Pattern.CASE_INSENSITIVE)
                                .matcher(getChoiceValue(object))
                                .replaceAll("<b>$0</b>"));
                    if (level != null && level == 1)
                        response.write("</i>");
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected String getTextValue(CHOICE choice) {
                try {
                    return getChoiceValue(choice);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // create behavior for this renderer

        AutoCompleteSettings settings = new AutoCompleteSettings();
        settings.setShowListOnEmptyInput(true);
        settings.setUseSmartPositioning(true);
        settings.setAdjustInputWidth(false);

        settings.setThrottleDelay(1000);
        //      settings.setShowCompleteListOnFocusGain(true);
        //      settings.setShowListOnFocusGain(true);

        autoCompleteBehavior = createAutoCompleteBehavior(renderer,
                settings);
        if (autoCompleteBehavior == null) {
            throw new NullPointerException(
                    "Auto complete behavior cannot be null");
        }

        // remove any previously added default behaviors - that may include
        // StringAutoCompleteRenderer
        List<? extends Behavior> behaviors = getBehaviors();
        for (Behavior element: behaviors) {
            if (element instanceof AbstractAutoCompleteTextField.AutoCompleteChoiceBehavior) remove(element);
        }

        add(autoCompleteBehavior);

        // add the onchange event to capture when the user types the keyword
        // refresh the self in ajax
        // perform a search using the model given in constructor
        add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 42407337969570350L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                //target.add(AbstractAmpAutoCompleteTextField.this);
                CHOICE choice = findChoice();
                if (choice != null)
                    onSelect(target, choice);
            }
        });

        // add the onfocus event to capture when the search control is first
        // focused
        // this is used to delete the default text as well as to perform the
        // initial ajax refresh
        add(new AjaxFormComponentUpdatingBehavior("onfocus") {
            private static final long serialVersionUID = -1825633287227536260L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(AbstractAmpAutoCompleteTextField.this);
                if (getModelObject() != null)
                    setModelObject("");
            }
        });

        // add the --Loading-- icon indicator
        add(indicatorAppender);
    }

    @Override
    public Collection<CHOICE> getChoiceList(String input) {
        this.input = input;
        if (input!=null && input.length()>0 && input.length() < 2)
            return Collections.emptyList();
        Constructor<? extends AbstractAmpAutoCompleteModel<CHOICE>> constructor;
        try {
            constructor = objectListModelClass.getConstructor(String.class,
                    Map.class);
            AbstractAmpAutoCompleteModel<CHOICE> newInstance = constructor
                    .newInstance(input, modelParams);
            return newInstance.getObject();
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
