package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpLocationFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.LocationChangedEvent;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * Added in order to allow permissions per row in the list of locations
 *
 * @author aartimon@developmentgateway.org
 * @since Dec 15, 2012
 */
public class AmpLocationItemPanel extends AmpFeaturePanel<AmpActivityLocation> {
    private static final long serialVersionUID = 1L;
    private final IModel<AmpActivityLocation> locationModel;

    public AmpLocationItemPanel(String id, final IModel<AmpActivityLocation> model,
                                String fmName, final IModel<Boolean> disablePercentagesForInternational,
                                final IModel<AmpActivityVersion> am,
                                final AmpRegionalFundingFormSectionFeature regionalFundingFeature,
                                final AmpLocationFormTableFeature locationTable,
                                final AmpComponentPanel locationPercentageRequired,
                                final IModel<Set<AmpActivityLocation>> setModel,
                                final ListView<AmpActivityLocation> list, final Label totalLabel,
                                final boolean isCountryAndMultiCountry) {
        super(id, model, fmName, true);

        this.locationModel = model;

        PropertyModel<Double> percModel = new PropertyModel<Double>(model, "locationPercentage");
        final AmpPercentageTextField percentageField = new AmpPercentageTextField("percentage", percModel,
                "locationPercentage", locationTable.getPercentageValidationField(),
                locationPercentageRequired.isVisible()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeRender() {
                if (this.isEnabled()) {
                    this.setEnabled(!disablePercentagesForInternational.getObject());
                }
                super.onBeforeRender();
            }

            @Override
            protected void onAjaxOnUpdate(AjaxRequestTarget target) {
                locationTable.getPercentageValidationField().reloadValidationField(target);
                target.add(totalLabel);

            }

        };
        RangeValidator<Double> validator = new RangeValidator<Double>(0.1d, 100d);
        percentageField.getTextContainer().add(validator);
        add(percentageField);
        add(new Label("locationLabel", model.getObject().getLocation().getAutoCompleteLabel()));

        String expressionLatitude = "(^\\+?([1-8])?\\d(\\.\\d+)?$)|(^-90$)|(^-(([1-8])?\\d(\\.\\d+)?$))";
        PatternValidator latitudeValidator = new PatternValidator(expressionLatitude) {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(IValidatable<String> validatable) {
                if (!getPattern().matcher(validatable.getValue()).matches()) {
                    ValidationError error = new ValidationError();
                    error.addKey("CoordinatesValidator");
                    validatable.error(error);
                }
            }
        };

        AmpTextFieldPanel<String> latitude = new AmpTextFieldPanel<String>("latitudeid",
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
                if (!getPattern().matcher(validatable.getValue()).matches()) {
                    ValidationError error = new ValidationError();
                    error.addKey("CoordinatesValidator");
                    validatable.error(error);
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
        AmpDeleteLinkField delLocation = new AmpDeleteLinkField("delLocation", "Delete Location", new Model<String>(translatedText)) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                // remove any regional funding with this region
                if (canDeleteLocation(target, am, model)) return;
                if (CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_1.
                        equalsCategoryValue(model.getObject().getLocation().getParentCategoryValue())) {
                    final IModel<Set<AmpRegionalFunding>> regionalFundings = new PropertyModel<Set<AmpRegionalFunding>>(am, "regionalFundings");
                    Iterator<AmpRegionalFunding> iterator = regionalFundings.getObject().iterator();
                    while (iterator.hasNext()) {
                        AmpRegionalFunding ampRegionalFunding = iterator.next();
                        if (ampRegionalFunding.getRegionLocation().equals(model.getObject().getLocation())) {
                            iterator.remove();
                        }
                    }
                    regionalFundingFeature.getList().removeAll();
                    target.add(regionalFundingFeature);

                    target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
                    target.add(totalLabel);

                }

                if (isCountryAndMultiCountry) {
                    disablePercentagesForInternational.setObject(false);
                    locationTable.getSearchLocations().setVisibilityAllowed(true);
                }
                findParent(AmpLocationFormSectionFeature.class).getRegionalFundingFeature()
                        .getMeFormSection().clearLocations(model.getObject().getLocation());
                locationTable.reloadValidationFields(target);
                setModel.getObject().remove(model.getObject());
                send(getPage(), Broadcast.BREADTH, new LocationChangedEvent(target));
                target.add(list.getParent());
                list.removeAll();
            }

        };
        add(delLocation);
    }

    public static boolean canDeleteLocation(AjaxRequestTarget target, final IModel<AmpActivityVersion> am,
                                            final IModel<AmpActivityLocation> model) {
        if (am.getObject().getIndicators() != null
                && !am.getObject().getIndicators().isEmpty()
                && (model == null || model.getObject() == null ||
                am.getObject().getIndicators().stream().anyMatch(indicator ->
                        indicator.getActivityLocation() != null &&
                                indicator.getActivityLocation().getLocation().getName().
                                        equals(model.getObject().getLocation().getName())))) {
            String translatedMessage = TranslatorUtil.getTranslation("Cannot delete location with indicators");
            String alert = "alert('" + translatedMessage + "');";
            target.appendJavaScript(alert);

            return true;
        }
        return false;
    }


    @Override
    protected void onConfigure() {
        AmpCategoryValueLocations loc = locationModel.getObject().getLocation();
        AmpAuthWebSession session = (AmpAuthWebSession) getSession();
        if (loc != null) {
            PermissionUtil.putInScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_REGION, loc);
        }
//        super.onConfigure();
        if (loc != null) {
            PermissionUtil.removeFromScope(session.getHttpSession(), GatePermConst.ScopeKeys.CURRENT_REGION);
        }
    }
}
