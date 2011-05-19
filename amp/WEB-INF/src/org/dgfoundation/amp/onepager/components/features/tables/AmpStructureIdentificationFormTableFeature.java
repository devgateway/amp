/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.util.StructuresUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 28, 2010
 */
public class AmpStructureIdentificationFormTableFeature extends AmpFormTableFeaturePanel {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private boolean titleSelected;
	private boolean typeSelected;
	private WebMarkupContainer feedbackContainer;
	private Label feedbackLabel;
//	private WebMarkupContainer componentFundingSection;

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
	public AmpStructureIdentificationFormTableFeature(String id, IModel<AmpActivityVersion> activityModel, 
			final IModel<AmpStructure> structureModel, String fmName) throws Exception{
		super(id, activityModel,fmName, true);
		setTitleHeaderColSpan(7);
		
//		this.componentFundingSection = componentFundingSection;
		
		final IModel<Set<AmpStructure>> setModel = new PropertyModel<Set<AmpStructure>>(activityModel, "structures");

		final DropDownChoice<AmpStructureType> structureTypes = new DropDownChoice<AmpStructureType>(
				"structureTypes", new PropertyModel<AmpStructureType>(structureModel, "type"), 
				new LoadableDetachableModel<List<AmpStructureType>>() {
					@Override
					protected List<AmpStructureType> load() {
						return new ArrayList(StructuresUtil.getAmpStructureTypes());
					}
					
				},
				new ChoiceRenderer("name")){
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

		};
		structureTypes.setOutputMarkupId(true);
//		structureTypes.add(new OnChangeAjaxBehavior() {
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) {
////				handleChanges(target, componentModel);
//			}
//			});
		add(structureTypes);
		
		final TextField<String> name = new TextField<String>("name", new PropertyModel<String>(structureModel, "title"));
		name.setOutputMarkupId(true);
		add(name);

		final TextField<String> description = new TextField<String>("description", new PropertyModel<String>(structureModel, "description"));
		name.setOutputMarkupId(true);
		add(description);

		final TextField<String> longitude = new TextField<String>("longitude", new PropertyModel<String>(structureModel, "longitude"));
		longitude.setOutputMarkupId(true);
		add(longitude);

		final TextField<String> latitude = new TextField<String>("latitude", new PropertyModel<String>(structureModel, "latitude"));
		latitude.setOutputMarkupId(true);
		add(latitude);

		final TextField<String> shape = new TextField<String>("shape", new PropertyModel<String>(structureModel, "shape"));
		shape.setOutputMarkupId(true);
		add(shape);

		AmpButtonField addbutton = new AmpButtonField("deleteStructure", "Delete Structure") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				AmpStructure structure = structureModel.getObject();
				setModel.getObject().remove(structure);
				target.addComponent(this.findParent(AmpStructuresFormSectionFeature.class));
			}
		};
		add(addbutton);
		
		feedbackContainer = new WebMarkupContainer("feedbackContainer");
		feedbackLabel = new Label("feedbackLabel", new Model(defaultMsg));
		feedbackLabel.setOutputMarkupId(true);
//		updateVisibility(structureModel);
		feedbackContainer.add(feedbackLabel);
		feedbackContainer.setOutputMarkupId(true);
		add(feedbackContainer);

		
	}


//	protected void handleChanges(AjaxRequestTarget target, IModel<AmpComponent> componentModel) {
//		boolean update = updateVisibility(componentModel);
//		if (update){
//			target.addComponent(this);
//			target.addComponent(componentFundingSection);
//			target.appendJavascript(OnePagerConst.slideToggle);
//		}
//	}
	
//	protected boolean updateVisibility(IModel<AmpComponent> componentModel){
//		AmpComponent comp = componentModel.getObject();
//		boolean oldTypeSelected = typeSelected;
//		boolean oldTitleSelected = titleSelected;
//		if (comp.getType() == null)
//			typeSelected = false;
//		else
//			typeSelected = true;
//		
//		if (comp.getTitle() == null || comp.getTitle() == "")
//			titleSelected = false;
//		else
//			titleSelected = true;
//
//		if (typeSelected && titleSelected){
//			feedbackContainer.setVisible(false);
//			componentFundingSection.setVisible(true);
//		}
//		else{
//			feedbackContainer.setVisible(true);
//			componentFundingSection.setVisible(false);
//			if (!typeSelected && !titleSelected){
//				feedbackLabel.setDefaultModelObject(defaultMsg);
//			}
//			else{
//				if (!typeSelected)
//					feedbackLabel.setDefaultModelObject(noTypeMsg);
//				else
//					feedbackLabel.setDefaultModelObject(noTitleMsg);
//			}
//		}
//		
//		if ((oldTitleSelected == titleSelected) && (oldTypeSelected == typeSelected))
//			return false;
//		else
//			return true;
//	}

}
