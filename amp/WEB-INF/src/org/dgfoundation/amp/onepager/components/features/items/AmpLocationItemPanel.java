package org.dgfoundation.amp.onepager.components.features.items;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.PatternValidator;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * Added in order to allow permissions per row in the list of locations
 * 
 * @author aartimon@developmentgateway.org
 * @since Dec 15, 2012
 */
public class AmpLocationItemPanel extends AmpFeaturePanel<AmpActivityLocation> {
	private static final long serialVersionUID = 1L;
	private IModel<AmpActivityLocation> locationModel;

	public AmpLocationItemPanel(String id, final IModel<AmpActivityLocation> model,
                                String fmName, final IModel<Boolean> disablePercentagesForInternational,
                                final IModel<AmpActivityVersion> am, final AmpRegionalFundingFormSectionFeature regionalFundingFeature,
                                final AmpPercentageCollectionValidatorField<AmpActivityLocation> percentageValidationField,
                                final AmpUniqueCollectionValidatorField<AmpActivityLocation> uniqueCollectionValidationField,
                                final AmpCollectionValidatorField minSizeCollectionValidationField,
                                final AmpCollectionValidatorField treeCollectionValidatorField,
                                final IModel<Set<AmpActivityLocation>> setModel, final ListView<AmpActivityLocation> list) {
		super(id, model, fmName, true);
		
		this.locationModel = model;
		
		PropertyModel<Double> percModel = new PropertyModel<Double>(model, "locationPercentage");
		AmpPercentageTextField percentageField=new AmpPercentageTextField("percentage",percModel,"locationPercentage",percentageValidationField){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onBeforeRender() {
				if (this.isEnabled()){
					this.setEnabled(!disablePercentagesForInternational.getObject());
				}
				super.onBeforeRender();
			}
		};				
		add(percentageField);
		add(new Label("locationLabel", model.getObject().getLocation().getLocation().getAutoCompleteLabel()));

		String expressionLatitude = "(^\\+?([1-8])?\\d(\\.\\d+)?$)|(^-90$)|(^-(([1-8])?\\d(\\.\\d+)?$))";
    	PatternValidator latitudeValidator = new PatternValidator(expressionLatitude) {
			private static final long serialVersionUID = 1L;

			@Override
    		public void validate(IValidatable<String> validatable) {
    			// Check value against pattern
    			if (!getPattern().matcher(validatable.getValue()).matches()){
    				error("CoordinatesValidator");
    			}
    		}
   		};
    	
		AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String> ("latitudeid", 
				new PropertyModel<String>(model, "latitude"), "Latitude", true, true);
		latitude.getTextContainer().add(latitudeValidator);
		latitude.getTextContainer().add(new AttributeModifier("style", "width: 100px"));
		latitude.setTextContainerDefaultMaxSize();
		add(latitude);

		String expressionLongitude = "(^\\+?1[0-7]\\d(\\.\\d+)?$)|(^\\+?([1-9])?\\d(\\.\\d+)?$)|(^-180$)|(^-1[1-7]\\d(\\.\\d+)?$)|(^-[1-9]\\d(\\.\\d+)?$)|(^\\-\\d(\\.\\d+)?$)";
    	PatternValidator longitudeValidator = new PatternValidator(expressionLongitude) {
			private static final long serialVersionUID = 1L;

			@Override
    		public void validate(IValidatable<String> validatable) {
    			if (!getPattern().matcher(validatable.getValue()).matches()){
    				error("CoordinatesValidator");
    			}
    		}
   		};
		AmpTextFieldPanel<String> longitude = new AmpTextFieldPanel<String>("longitudeid", 
				new PropertyModel<String>(model, "Longitude"), "Longitude", true, true);
		longitude.getTextContainer().add(longitudeValidator);
		longitude.setTextContainerDefaultMaxSize();
		longitude.getTextContainer().add(new AttributeModifier("style", "width: 100px"));
		add(longitude);
		
		
		String translatedText = TranslatorUtil.getTranslation("Delete this location and any related funding elements, if any?");
		AmpDeleteLinkField delLocation = new AmpDeleteLinkField("delLocation","Delete Location",new Model<String>(translatedText)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				// toggleHeading(target, setModel.getObject());

				// remove any regional funding with this region
				if (CategoryManagerUtil
						.equalsCategoryValue(
								model.getObject().getLocation()
										.getLocation()
										.getParentCategoryValue(),
								CategoryConstants.IMPLEMENTATION_LOCATION_REGION)) {
					final IModel<Set<AmpRegionalFunding>> regionalFundings = new PropertyModel<Set<AmpRegionalFunding>>(
							am, "regionalFundings");
					Iterator<AmpRegionalFunding> iterator = regionalFundings.getObject().iterator();
					while (iterator.hasNext()) {
						AmpRegionalFunding ampRegionalFunding = (AmpRegionalFunding) iterator
								.next();
						if (ampRegionalFunding.getRegionLocation().equals(
								model.getObject().getLocation()
										.getLocation()))
							iterator.remove();
					}
					regionalFundingFeature.getList().removeAll();
					target.add(regionalFundingFeature);
					target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
					
					percentageValidationField.reloadValidationField(target);							
					uniqueCollectionValidationField.reloadValidationField(target);
                    minSizeCollectionValidationField.reloadValidationField(target);
                    treeCollectionValidatorField.reloadValidationField(target);
				}
				setModel.getObject().remove(model.getObject());
				target.add(list.getParent());
				list.removeAll();
			}

		};
		add(delLocation);
	}
	
	@Override
	protected void onConfigure() {
		AmpCategoryValueLocations loc = locationModel.getObject().getLocation().getLocation();
		AmpAuthWebSession session = (AmpAuthWebSession) getSession();
		if (loc != null){
			PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_REGION, loc);
		}
		super.onConfigure();
		if (loc != null){
			PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_REGION);
		}
	}
}
