/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu@dgateway.org since Nov 3, 2010
 */
public class AmpCheckBoxFieldPanel extends AmpFieldPanel<Boolean> {

    protected CheckBox checkBox;

    /**
     * @param id
     * @param fmName
     * @param hideLabel
     */
    public AmpCheckBoxFieldPanel(String id, String fmName, boolean hideLabel) {
        this(id, null, fmName, hideLabel);
        // TODO Auto-generated constructor stub
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    /**
     * @param id
     * @param fmName
     * @param model
     */
    public AmpCheckBoxFieldPanel(String id, String fmName, IModel<Boolean> model) {
        this(id, model, fmName, false);
        // TODO Auto-generated constructor stub
    }

    public AmpCheckBoxFieldPanel(String id, IModel<Boolean> model, String fmName, boolean hideLabel) {
        this(id, model, fmName, hideLabel, false);
    }

    /**
     * @param id
     * @param model
     * @param fmName
     * @param hideLabel
     */
    public AmpCheckBoxFieldPanel(String id, IModel<Boolean> model, String fmName, boolean hideLabel, boolean hideNewLine) {
        super(id, model, fmName, hideLabel);
        newLine.setVisible(!hideNewLine);
        checkBox = new AjaxCheckBox("checkbox", model){
            @Override
            protected void onUpdate(AjaxRequestTarget arg0) {
                onAjaxOnUpdate(arg0);
            }
        };
        add(checkBox);
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpCheckBoxFieldPanel(String id, String fmName) {
        this(id, null, fmName, false);
    }

    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpCheckBoxFieldPanel(String id, IModel<Boolean> model, String fmName) {
        this(id, model, fmName, false);
        // TODO Auto-generated constructor stub
    }

}
