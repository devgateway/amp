/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;

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
			IModel<Date> model, String fmName,AmpDatePickerFieldPanel otherDatePicker,
			boolean hideLabel) {
		super(id, fmName, hideLabel);
		this.fmType = AmpFMTypes.MODULE;
		
		setOutputMarkupId(true);
		this.otherDatePicker = otherDatePicker;
		dateWrapper = new WebMarkupContainer("dateWrapper");
		dateWrapper.setOutputMarkupId(true);
		add(dateWrapper);

		date = new DateTextField("date", model);
		date.setOutputMarkupId(true);
		DatePicker dp = new DatePicker() {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean enableMonthYearSelection() {
				return true;
			}
		};
		dp.setShowOnFieldClick(true);
		date.add(dp);
		dateWrapper.add(date);
		initFormComponent(date);
		sameAsOtherDatePicker = new AmpAjaxCheckBoxFieldPanel(
				"sameAsOtherDatePicker", "Same As "
						+ (otherDatePicker == null ? "" : otherDatePicker
								.getTitleLabel()
								.getDefaultModelObjectAsString()),
				new Model<Boolean>(false)) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				AmpDatePickerFieldPanel otherPicker = AmpDatePickerFieldPanel.this.otherDatePicker;
				Boolean check = (Boolean) this.checkbox.getDefaultModelObject();
				if (check) {
					date.setEnabled(false);
					dateWrapper.setEnabled(false);
					String js=String.format("$('#%s').val($('#%s').val());",date.getMarkupId(),otherPicker.getDate().getMarkupId());
					target.appendJavascript(js);
					target.addComponent(dateWrapper);
				} else {
					date.setEnabled(true);
					dateWrapper.setEnabled(true);
					target.addComponent(dateWrapper);
				}
			}
		};

		if (otherDatePicker == null){
			sameAsOtherDatePicker.setVisible(false);
		}
		add(sameAsOtherDatePicker);

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
}
