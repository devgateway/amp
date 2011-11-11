/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.PatternValidator;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpLocationSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpLocationFormTableFeature extends
		AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivityLocation> {

	private IModel<Set<AmpActivityLocation>> setModel;

	public IModel<Set<AmpActivityLocation>> getSetModel() {
		return setModel;
	}

	/**
	 * @param id
	 * @param fmName
	 * @param regionalFundingFeature
	 * @param implementationLevel 
	 * @throws Exception
	 */
	public AmpLocationFormTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am,
			final AmpRegionalFundingFormSectionFeature regionalFundingFeature,
			final AmpCategorySelectFieldPanel implementationLocation, final AmpCategorySelectFieldPanel implementationLevel)
			throws Exception {
		super(id, am, fmName);
		setTitleHeaderColSpan(4);
		setModel = new PropertyModel<Set<AmpActivityLocation>>(
				am, "locations");
		if (setModel.getObject() == null)
			setModel.setObject(new HashSet());

		AbstractReadOnlyModel<List<AmpActivityLocation>> listModel = OnePagerUtil
		.getReadOnlyListModelFromSetModel(setModel);
	
		final AmpPercentageCollectionValidatorField<AmpActivityLocation> percentageValidationField=
			new AmpPercentageCollectionValidatorField<AmpActivityLocation>("locationPercentageTotal",listModel,"locationPercentageTotal") {
				@Override
				public Number getPercentage(AmpActivityLocation item) {
					return item.getLocationPercentage();
				}
		};
		
		add(percentageValidationField);
		
		
		final AmpUniqueCollectionValidatorField<AmpActivityLocation> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityLocation>(
				"uniqueLocationsValidator", listModel, "uniqueLocationsValidator") {
			@Override
		 	public Object getIdentifier(AmpActivityLocation t) {
				return t.getLocation().getLocation().getName();
		 	}	
		};

		add(uniqueCollectionValidationField);
		
		list = new ListView<AmpActivityLocation>("listLocations", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpActivityLocation> item) {
				final MarkupContainer listParent = this.getParent();
				PropertyModel<Double> percModel = new PropertyModel<Double>(item.getModel(),"locationPercentage");
				AmpPercentageTextField percentageField=new AmpPercentageTextField("percentage",percModel,"locationPercentage",percentageValidationField);				
				item.add(percentageField);
				item.add(new Label("locationLabel", item.getModelObject().getLocation().getLocation().getAutoCompleteLabel()));

				String expressionLatitude = "(^\\+?([1-8])?\\d(\\.\\d+)?$)|(^-90$)|(^-(([1-8])?\\d(\\.\\d+)?$))";
            	PatternValidator latitudeValidator = new PatternValidator(expressionLatitude) {
            		@Override
            		protected void onValidate(IValidatable<String> validatable)
            		{
            			// Check value against pattern
            			if (!getPattern().matcher(validatable.getValue()).matches())
            			{
            				error(validatable, "CoordinatesValidator");
            			}
            		}
           		};
            	
				AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String> ("latitudeid", new PropertyModel<String>
						(item.getModel(), "latitude"),"Latitude",false);
				latitude.getTextContainer().add(latitudeValidator);
				item.add(latitude);

				String expressionLongitude = "(^\\+?1[0-7]\\d(\\.\\d+)?$)|(^\\+?([1-9])?\\d(\\.\\d+)?$)|(^-180$)|(^-1[1-7]\\d(\\.\\d+)?$)|(^-[1-9]\\d(\\.\\d+)?$)|(^\\-\\d(\\.\\d+)?$)";
            	PatternValidator longitudeValidator = new PatternValidator(expressionLongitude) {
            		@Override
            		protected void onValidate(IValidatable<String> validatable)
            		{
            			if (!getPattern().matcher(validatable.getValue()).matches())
            			{
            				error(validatable, "CoordinatesValidator");
            			}
            		}            	
            		};
				AmpTextFieldPanel<String> longitude = new AmpTextFieldPanel<String>
				("longitudeid", new PropertyModel<String>(item.getModel(), "Longitude"),"Longitude",false);
				longitude.getTextContainer().add(longitudeValidator);
				item.add(longitude);
				
				
				String translatedText = TranslatorUtil.getTranslation("Delete this location and any related funding elements, if any?");
				AmpDeleteLinkField delLocation = new AmpDeleteLinkField("delLocation","Delete Location",new Model<String>(translatedText)) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						
						// toggleHeading(target, setModel.getObject());

						// remove any regional funding with this region
						if (CategoryManagerUtil
								.equalsCategoryValue(
										item.getModelObject().getLocation()
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
										item.getModelObject().getLocation()
												.getLocation()))
									iterator.remove();
							}
							regionalFundingFeature.getList().removeAll();
							target.addComponent(regionalFundingFeature);
							target.appendJavascript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
							
							percentageValidationField.reloadValidationField(target);							
							uniqueCollectionValidationField.reloadValidationField(target);
						}
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
					}

				};
				item.add(delLocation);

			}
		};
		list.setReuseItems(true);
		add(list);

		
		
		final AmpAutocompleteFieldPanel<AmpCategoryValueLocations> searchLocations=new AmpAutocompleteFieldPanel<AmpCategoryValueLocations>("searchLocations","Search Locations",AmpLocationSearchModel.class,false) {			
			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpCategoryValueLocations choice) {
				AmpActivityLocation activityLocation = new AmpActivityLocation();
				AmpLocation ampLoc = LocationUtil
						.getAmpLocationByCVLocation(choice.getId());
				if (ampLoc == null) {
					ampLoc = new AmpLocation();

					ampLoc.setDescription(new String(" "));
					ampLoc.setLocation(choice);
					if (choice != null) {
						AmpCategoryValueLocations regionLocation = DynLocationManagerUtil
								.getAncestorByLayer(
										choice,
										CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
						if (regionLocation != null) {
							ampLoc.setRegionLocation(regionLocation);
							ampLoc.setRegion(regionLocation.getName());
						}
					}
					DbUtil.add(ampLoc);
				}
				activityLocation.setLocation(ampLoc);
				if(list.size()>0)
					activityLocation.setLocationPercentage(0f);
				else 
					activityLocation.setLocationPercentage(100f); 

				activityLocation.setActivity(am.getObject());
				Set<AmpActivityLocation> set = setModel.getObject();
				set.add(activityLocation);
				// toggleHeading(target, setModel.getObject());
				target.addComponent(list.getParent());
				regionalFundingFeature.getList().removeAll();
				target.addComponent(regionalFundingFeature);
				target.appendJavascript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
				percentageValidationField.reloadValidationField(target);		
				uniqueCollectionValidationField.reloadValidationField(target);
				list.removeAll();
			}

			@Override
			public Integer getChoiceLevel(AmpCategoryValueLocations choice) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getChoiceValue(AmpCategoryValueLocations choice) {
				return choice.getAutoCompleteLabel();
			}
		};

		searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LAYER,
				implementationLocation.getChoiceModel());
	
		searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LEVEL,
				implementationLevel.getChoiceModel());
		
		
		add(searchLocations);
	}
}
