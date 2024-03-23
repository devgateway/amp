/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.Date;

/**
 * @author mpostelnicu@dgateway.org since Oct 5, 2010
 */
public class AmpDatePickerFieldPanel extends AmpFieldPanel<Date> {

    private static final long serialVersionUID = 2717405001055382044L;
    protected DateTextField date;

    public DateTextField getDate() {
        return date;
    }

    protected AmpAjaxCheckBoxFieldPanel sameAsOtherDatePicker;
    protected AmpDatePickerFieldPanel otherDatePicker;
    protected AmpTextFieldPanel<String> comment;
    protected WebMarkupContainer dateWrapper;

    /**
     * @param id
     * @param fmName
     * @param hideLabel
     */
    public AmpDatePickerFieldPanel(String id, 
            IModel<Date> model, String fmName,final AmpDatePickerFieldPanel otherDatePicker,
            boolean hideLabel, boolean hideNewLine) {
        super(id, fmName, hideLabel, hideNewLine);
        this.fmType = AmpFMTypes.MODULE;
        
        setOutputMarkupId(true);
        this.otherDatePicker = otherDatePicker;
        dateWrapper = new WebMarkupContainer("dateWrapper");
        dateWrapper.setOutputMarkupId(true);
        add(dateWrapper);

        
        date = AmpDatePickerRegular.newDatePicker("date", model); 
        String readonly = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.READONLY_DATES);
        if (readonly != null && "TRUE".equals(readonly.toUpperCase()))
            date.add(new AttributeModifier("readonly","readonly"));
        String datePattern = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
        date.add(new AttributeModifier("placeholder", datePattern));
        dateWrapper.add(date);
        initFormComponent(date);
        sameAsOtherDatePicker = new AmpAjaxCheckBoxFieldPanel(
                "sameAsOtherDatePicker", "Same As "
                        + (otherDatePicker == null ? "" : otherDatePicker
                                .getTitleLabel()
                                .getDefaultModelObjectAsString()),
                new Model<Boolean>(false)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AmpDatePickerFieldPanel otherPicker = AmpDatePickerFieldPanel.this.otherDatePicker;
                Boolean check = (Boolean) this.checkbox.getDefaultModelObject();
                if (check) {
                    //date.setEnabled(true);
                    dateWrapper.setEnabled(true);
                    String js=String.format("$('#%s').val($('#%s').val());",date.getMarkupId(),otherPicker.getDate().getMarkupId());
                    target.appendJavaScript(js);
                    target.add(dateWrapper);
                } else {
                    date.setEnabled(true);
                    dateWrapper.setEnabled(true);
                    target.add(dateWrapper);
                }
            }
        };
        
        if (otherDatePicker == null){
            sameAsOtherDatePicker.setVisibilityAllowed(false);
        }
        
        add(sameAsOtherDatePicker);

    }

    public AmpDatePickerFieldPanel(String id,
                                   IModel<Date> model, String fmName,final AmpDatePickerFieldPanel otherDatePicker,
                                   boolean hideLabel) {
        this(id, model, fmName, otherDatePicker, hideLabel, false);
    }

    /**
     * Constructs a new datepicker field panel
     * @param id the id of the object markup
     * @param model the model to read/write the converted {@link Date}
     * @param otherDatePicker this is another component of the same kind as this one, which will be used to copy the date from
     * @param fmName
     */
    public AmpDatePickerFieldPanel(String id, IModel<Date> model,
            AmpDatePickerFieldPanel otherDatePicker, String fmName) {
        this(id,  model, fmName,otherDatePicker, false);
        this.fmType = AmpFMTypes.MODULE;
    }

    
    /**
     * @param id
     * @param fmName
     */
    public AmpDatePickerFieldPanel(String id, IModel<Date> model,String fmName) {
        this(id,  model, fmName,null, false);
    }
    
    public AmpDatePickerFieldPanel(String id, IModel<Date> model,String fmName,boolean hideLabel) {
        this(id,  model, fmName,null, hideLabel);
    }
    
    
    
    public void setDuplicateFieldOnChange(final AmpDatePickerFieldPanel peerPickerField){
        AjaxEventBehavior onChange =new AjaxEventBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            protected void onEvent(final AjaxRequestTarget target) {
                Boolean check = (Boolean)peerPickerField.getCheckBox().getModelObject();
                if(check != null && check){
                    String js=String.format("$('#%s').val($('#%s').val());",peerPickerField.date.getMarkupId(),date.getMarkupId());
                    target.appendJavaScript(js);
                    target.add(peerPickerField.date.getParent());
                }
             }
        };
        date.add(onChange);
    }
    
    public AjaxCheckBox getCheckBox()
    {
        return sameAsOtherDatePicker.checkbox;
    }
}
