/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.behaviors.ChoiceComponentVisualErrorBehavior;
import org.dgfoundation.amp.onepager.behaviors.ComponentVisualErrorBehavior;
import org.dgfoundation.amp.onepager.models.AmpMultiValueDropDownChoiceModel;

/**
 * @author mpostelnicu@dgateway.org since Nov 2, 2010
 */
public class AmpGroupFieldPanel<T> extends AmpFieldPanel<T> {

	private static final long serialVersionUID = 8689694759950444529L;
	protected AbstractChoice<?, T> choiceContainer;

	public AbstractChoice<?, T> getChoiceContainer() {
		return choiceContainer;
	}

	/**
	 * Construct a group field panel for a {@link RadioChoice} (so just single select)
	 * @param id
	 * @param model
	 * @param choicesList
	 * @param fmName
	 * @param hideLabel
	 * @param nullValid
	 * @param renderer
	 */
	public AmpGroupFieldPanel(String id, IModel<T> model,
			List<? extends T> choicesList, String fmName,
			boolean hideLabel, boolean nullValid, IChoiceRenderer<? super T> renderer) {
		super(id, fmName, hideLabel);
		choiceContainer = new RadioChoice<T>("choice", model, choicesList,renderer);
		//((RadioChoice<?>)choiceContainer).setNullValid(nullValid);
		choiceContainer.setOutputMarkupId(true);
		addFormComponent(choiceContainer);
	}
	
	@Override
	protected void initFormComponent(FormComponent<?> fc) {
		formComponent = fc;
		feedbackLabel.setComponent(fc);
		fc.setLabel(new Model<String>(fmName));
		final ChoiceComponentVisualErrorBehavior cvb = new ChoiceComponentVisualErrorBehavior( feedbackContainer);
		fc.add(cvb);
	}

	/**
	 * Construct a group field panel for a {@link RadioChoice} or for a {@link CheckBoxMultipleChoice}
	 * depending if @param isMultiSelect is true or not
	 * @param id
	 * @param fmName
	 * @param hideLabel
	 * @param nullValid
	 * @param renderer optional choice renderer
	 */
	public AmpGroupFieldPanel(String id,
			IModel<? extends Collection<T>> model,
			List<? extends T> choicesList, String fmName, boolean hideLabel,
			boolean isMultiSelect, boolean nullValid, IChoiceRenderer<? super T> renderer) {
		super(id, fmName, hideLabel);
		if (isMultiSelect)
			choiceContainer = new CheckBoxMultipleChoice<T>("choice", model,
					choicesList,renderer);
		else {
			choiceContainer = new RadioChoice<T>("choice",
					new AmpMultiValueDropDownChoiceModel<T>(model), choicesList,renderer);
			//((RadioChoice<?>)choiceContainer).setNullValid(nullValid);
		}
		choiceContainer.setOutputMarkupId(true);
		addFormComponent(choiceContainer);
	}

	/**
	 * @see AmpGroupFieldPanel#AmpGroupFieldPanel(String, IModel, List, String, boolean, boolean, IChoiceRenderer)
	 * @param id
	 * @param model
	 * @param choicesList
	 * @param fmName
	 * @param hideLabel
	 * @param nullValid
	 */
	public AmpGroupFieldPanel(String id, IModel<T> model,
			List<? extends T> choicesList, String fmName,
			boolean hideLabel, boolean nullValid) {
		this(id,model,choicesList,fmName,hideLabel,nullValid,null);
	}
	
	/**
	 * @see AmpGroupFieldPanel#AmpGroupFieldPanel(String, IModel, List, String, boolean, boolean, boolean, IChoiceRenderer)
	 * @param id
	 * @param model
	 * @param choicesList
	 * @param fmName
	 * @param hideLabel
	 * @param isMultiSelect
	 * @param nullValid
	 */
	public AmpGroupFieldPanel(String id,
			IModel<? extends Collection<T>> model,
			List<? extends T> choicesList, String fmName, boolean hideLabel,
			boolean isMultiSelect, boolean nullValid) {
		this(id,model,choicesList,fmName,hideLabel,isMultiSelect,nullValid,null);
	}
	
}
