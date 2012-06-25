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
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpStructuresFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.util.StructuresUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 28, 2010
 */
public class AmpStructureIdentificationFormTableFeature extends AmpFormTableFeaturePanel {
	private boolean titleSelected;
	private boolean typeSelected;
//	private WebMarkupContainer feedbackContainer;
//	private Label feedbackLabel;

	static final private String defaultMsg = "*" + TranslatorUtil.getTranslatedText("Please choose a structure type and a unique title");
	static final private String noTypeMsg = "*" + TranslatorUtil.getTranslatedText("Please choose an structure type");
	static final private String noTitleMsg = "*" + TranslatorUtil.getTranslatedText("Please choose a unique title");

	
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
		
		final IModel<Set<AmpStructure>> setModel = new PropertyModel<Set<AmpStructure>>(activityModel, "structures");
		
		AmpSelectFieldPanel structureTypes = new  AmpSelectFieldPanel("structureTypes", new PropertyModel<AmpStructureType>(structureModel, "type"),
				new LoadableDetachableModel<List<AmpStructureType>>() {
			@Override
			protected List<AmpStructureType> load() {
				return new ArrayList<AmpStructureType>(StructuresUtil.getAmpStructureTypes());
			}		
		}, "Structure Type",true, false,  new ChoiceRenderer<AmpStructureType>("name")) ;
		

		structureTypes.getChoiceContainer().setRequired(true);
		structureTypes.setOutputMarkupId(true);

		add(structureTypes);
		
		final AmpTextFieldPanel<String> name = new AmpTextFieldPanel<String>("name", new PropertyModel<String>(structureModel, "title"), "Structure Title",true, true);
		name.setOutputMarkupId(true);
		name.getTextContainer().add(new AttributeAppender("size", new Model("10px"), ";"));
		name.setTextContainerDefaultMaxSize();
		name.getTextContainer().setRequired(true);
		add(name);
		
		final AmpTextAreaFieldPanel<String> description = new AmpTextAreaFieldPanel<String>("description", new PropertyModel<String>(structureModel, "description"),"Structure Description",false, true, true);
		description.setOutputMarkupId(true);

		description.getTextAreaContainer().add(new SimpleAttributeModifier("cols", "20"));
		add(description);		

		final AmpTextFieldPanel<String> longitude = new AmpTextFieldPanel<String>("longitude", new PropertyModel<String>(structureModel, "longitude"),"Structure Longitude", true, true);
		longitude.setOutputMarkupId(true);
		longitude.setTextContainerDefaultMaxSize();

		longitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
		add(longitude);

		final AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String>("latitude", new PropertyModel<String>(structureModel, "latitude"),"Structure Latitude", true, true);
		latitude.setTextContainerDefaultMaxSize();
		latitude.setOutputMarkupId(true);

		latitude.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
		add(latitude);

		final AmpTextFieldPanel<String> shape = new AmpTextFieldPanel<String>("shape", new PropertyModel<String>(structureModel, "shape"),"Structure Shape", true, true);
		shape.setOutputMarkupId(true);
		shape.setTextContainerDefaultMaxSize();

		shape.getTextContainer().add(new AttributeAppender("size", new Model("7px"), ";"));
		add(shape);

		AmpDeleteLinkField addbutton = new AmpDeleteLinkField("deleteStructure", "Delete Structure") {
			@Override
			protected void onClick(AjaxRequestTarget target) {
				try{
				AmpStructure stru = structureModel.getObject();
				setModel.getObject().remove(stru);
				target.add(this.findParent(AmpStructuresFormSectionFeature.class));
				
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		add(addbutton);

	}

}
