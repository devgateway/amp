/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.util.ComponentsUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 28, 2010
 */
public class AmpComponentIdentificationFormTableFeature extends AmpFormTableFeaturePanel {
	private boolean titleSelected;
	private boolean typeSelected;
	private WebMarkupContainer feedbackContainer;
	private Label feedbackLabel;
	private WebMarkupContainer componentFundingSection;

	static final private String defaultMsg = "*" + TranslatorUtil.getTranslatedText("Please choose a component type and an unique title");
	static final private String noTypeMsg = "*" + TranslatorUtil.getTranslatedText("Please choose a component type");
	static final private String noTitleMsg = "*" + TranslatorUtil.getTranslatedText("Please choose an unique title");

	
	/**
	 * @param id
	 * @param componentFundingSection 
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpComponentIdentificationFormTableFeature(String id, IModel<AmpActivityVersion> activityModel, 
			final IModel<AmpComponent> componentModel, WebMarkupContainer componentFundingSection, String fmName) throws Exception{
		super(id, activityModel,fmName, true);
		setTitleHeaderColSpan(5);
		
		this.componentFundingSection = componentFundingSection;
		
		final IModel<Set<AmpComponent>> setModel = new PropertyModel<Set<AmpComponent>>(activityModel, "components");

		final DropDownChoice<AmpComponentType> compTypes = new DropDownChoice<AmpComponentType>(
				"compTypes", new PropertyModel<AmpComponentType>(componentModel, "type"), 
				new LoadableDetachableModel<List<AmpComponentType>>() {
					@Override
					protected List<AmpComponentType> load() {
						return new ArrayList(ComponentsUtil.getAmpComponentTypes());
					}
					
				},
				new ChoiceRenderer("name")){
			@Override
			protected boolean isDisabled(AmpComponentType object, int index, String selected) {
				if (object.getSelectable())
					return false;
				else
					return true;
			}
		};
		compTypes.setOutputMarkupId(true);
		compTypes.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				handleChanges(target, componentModel);
			}
			});
		add(compTypes);
		
		final TextField<String> name = new TextField<String>("name", new PropertyModel<String>(componentModel, "title"));
		name.setOutputMarkupId(true);
		OnChangeAjaxBehavior nameOnChange = new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				handleChanges(target, componentModel);
			}
		};
		nameOnChange.setThrottleDelay(Duration.milliseconds(300l));
		name.add(nameOnChange);
		add(name);

		AmpAjaxLinkField addbutton = new AmpAjaxLinkField("deleteComponent", "Delete Component", "Delete Component") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				AmpComponent comp = componentModel.getObject();
				setModel.getObject().remove(comp);
				target.addComponent(this.findParent(AmpComponentsFormSectionFeature.class));
			}
		};
		add(addbutton);
		
		feedbackContainer = new WebMarkupContainer("feedbackContainer");
		feedbackLabel = new Label("feedbackLabel", new Model(defaultMsg));
		feedbackLabel.setOutputMarkupId(true);
		updateVisibility(componentModel);
		feedbackContainer.add(feedbackLabel);
		feedbackContainer.setOutputMarkupId(true);
		add(feedbackContainer);

		
	}


	protected void handleChanges(AjaxRequestTarget target, IModel<AmpComponent> componentModel) {
		boolean update = updateVisibility(componentModel);
		if (update){
			//target.addComponent(this);
			target.addComponent(componentFundingSection);
			target.appendJavascript(OnePagerConst.getToggleChildrenJS(componentFundingSection));
		}
	}
	
	protected boolean updateVisibility(IModel<AmpComponent> componentModel){
		AmpComponent comp = componentModel.getObject();
		boolean oldTypeSelected = typeSelected;
		boolean oldTitleSelected = titleSelected;
		if (comp.getType() == null)
			typeSelected = false;
		else
			typeSelected = true;
		
		if (comp.getTitle() == null || comp.getTitle() == "")
			titleSelected = false;
		else
			titleSelected = true;

		if (typeSelected && titleSelected){
			feedbackContainer.setVisible(false);
			componentFundingSection.setVisible(true);
		}
		else{
			feedbackContainer.setVisible(true);
			componentFundingSection.setVisible(false);
			if (!typeSelected && !titleSelected){
				feedbackLabel.setDefaultModelObject(defaultMsg);
			}
			else{
				if (!typeSelected)
					feedbackLabel.setDefaultModelObject(noTypeMsg);
				else
					feedbackLabel.setDefaultModelObject(noTitleMsg);
			}
		}
		
		if ((oldTitleSelected == titleSelected) && (oldTypeSelected == typeSelected))
			return false;
		else
			return true;
	}

}
