/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpLocationFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.events.LocationChangedEvent;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.module.aim.dbentity.AmpActivityLocation;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.dgfoundation.amp.onepager.components.features.items.AmpLocationItemPanel.canDeleteLocation;
import static org.digijava.module.aim.util.LocationConstants.MULTI_COUNTRY_ISO_CODE;

/**
 * Location section of the one pager form
 *
 * @author mpostelnicu@dgateway.org since Oct 7, 2010
 * @see OnePager
 */
public class AmpLocationFormSectionFeature extends AmpFormSectionFeaturePanel {
    private static final long serialVersionUID = 8671173324087460636L;

    /**
     * @param id
     * @param fmName
     * @param regionalFundingFeature
     * @throws Exception
     */
    protected AmpRegionalFundingFormSectionFeature regionalFundingFeature;
    private AmpCategoryValue previousImplementationLevel;
    final AmpCategorySelectFieldPanel implementationLevel;
    final AmpCategorySelectFieldPanel implementationLocation;


    public AmpRegionalFundingFormSectionFeature getRegionalFundingFeature() {
        return regionalFundingFeature;
    }


    public AmpLocationFormSectionFeature(String id, String fmName,
                                         final IModel<AmpActivityVersion> am, AmpComponentPanel regionalFunding) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;
        this.regionalFundingFeature = (AmpRegionalFundingFormSectionFeature) regionalFunding;

        implementationLevel = new AmpCategorySelectFieldPanel(
                "implementationLevel",
                CategoryConstants.IMPLEMENTATION_LEVEL_KEY,
                new AmpCategoryValueByKeyModel(
                        new PropertyModel<Set<AmpCategoryValue>>(am,
                                "categories"),
                        CategoryConstants.IMPLEMENTATION_LEVEL_KEY),
                CategoryConstants.IMPLEMENTATION_LEVEL_NAME, true, true, null, AmpFMTypes.MODULE);
        add(implementationLevel);


        final Model<Boolean> disablePercentagesForInternational = new Model<Boolean>(false);
        implementationLocation = new AmpCategorySelectFieldPanel(
                "implementationLocation",
                CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
                new AmpCategoryValueByKeyModel(
                        new PropertyModel<Set<AmpCategoryValue>>(am,
                                "categories"),
                        CategoryConstants.IMPLEMENTATION_LOCATION_KEY),
                CategoryConstants.IMPLEMENTATION_LOCATION_NAME, true, true,
                null, implementationLevel.getChoiceModel(), AmpFMTypes.MODULE);
        implementationLocation.setOutputMarkupId(true);

        final AmpLocationFormTableFeature locationsTable = new AmpLocationFormTableFeature(
                "locationsTable", "Locations", am, regionalFundingFeature,
                implementationLocation, implementationLevel, disablePercentagesForInternational);

        implementationLocation.getChoiceContainer().add(
                new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        //if implementation level is not empty the implementation  location should be mandatory
                        if (implementationLevel.getChoiceModel().getObject() != null
                                && !implementationLevel.getChoiceModel().getObject().isEmpty()) {
                            implementationLocation.getChoiceContainer().setRequired(true);
                            target.add(implementationLocation);
                        } else {
                            implementationLocation.getChoiceContainer().setRequired(false);
                            target.add(implementationLocation);
                        }
                        defaultCountryChecks(implementationLevel, implementationLocation, disablePercentagesForInternational,
                                target, locationsTable, regionalFundingFeature);
                    }
                });
        add(implementationLocation);


        // add behavior to update implementation location when implementation
        // level choice changes
        // when chaging implementation level, remove all locations
        implementationLevel.getChoiceContainer().add(
                new AjaxFormComponentUpdatingBehavior("onchange") {
                    private static final long serialVersionUID = -8419230552388122030L;

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        //if implementation level is not empty the implementation  location should be mandatory
                        if (implementationLevel.getChoiceModel().getObject() != null
                                && !implementationLevel.getChoiceModel().getObject().isEmpty()) {
                            implementationLocation.getChoiceContainer().setRequired(true);
                            target.add(implementationLocation);
                        } else {
                            implementationLocation.getChoiceContainer().setRequired(false);
                            target.add(implementationLocation);
                        }

                        target.add(implementationLocation);
                        String mixedImplementationLocation = FeaturesUtil.getGlobalSettingValue(
                                GlobalSettingsConstants.MIXED_IMPLEMENTATION_LOCATION);
                        if ("false".equals(mixedImplementationLocation)) {
                            Set<AmpActivityLocation> set = locationsTable.getSetModel().getObject();
                            if (set != null && set.size() > 0) {
                                if (canDeleteLocation(target, am, null)) {
                                    implementationLevel.getChoiceModel().setObject(
                                            new HashSet<>(Collections.singletonList(previousImplementationLevel)));
                                    target.add(implementationLevel);
                                    return;
                                }
                                locationsTable.getSetModel().getObject().clear();
                                locationsTable.getList().removeAll();
                                //when we remove we need to show the search Component
                                locationsTable.getSearchLocations().setVisibilityAllowed(true);
                                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(locationsTable));
                                target.add(locationsTable);
                                getRegionalFundingFeature().getMeFormSection().clearLocations(null);
                                send(getPage(), Broadcast.BREADTH, new LocationChangedEvent(target));

                            }
                        }
                        defaultCountryChecks(implementationLevel, implementationLocation,
                                disablePercentagesForInternational, target, locationsTable, regionalFundingFeature);
                        previousImplementationLevel = !implementationLevel.getChoiceModel().getObject().isEmpty() ?
                                implementationLevel.getChoiceModel().getObject().iterator().next() : null;
                    }
                });


        add(locationsTable);
        defaultCountryChecks(implementationLevel, implementationLocation, disablePercentagesForInternational,
                null, locationsTable, regionalFundingFeature);

    }

    public void onBeforeRender() {
        super.onBeforeRender();
        previousImplementationLevel = !implementationLevel.getChoiceModel().getObject().isEmpty()
                ? implementationLevel.getChoiceModel().getObject().iterator().next() : null;
        //if implementation level is not empty the implementation  location should be mandatory
        implementationLocation.getChoiceContainer().setRequired(implementationLevel.getChoiceModel().getObject() != null
                && !implementationLevel.getChoiceModel().getObject().isEmpty());
    }

    private boolean checkInternationalCountry(AmpCategorySelectFieldPanel implementationLevel,
                                              AmpCategorySelectFieldPanel implementationLocation) {
        AmpCategoryValue implLevel = null;
        AmpCategoryValue implLocValue = null;
        if (implementationLevel.getChoiceModel() != null) {
            Set<AmpCategoryValue> tmp = implementationLevel.getChoiceModel().getObject();
            if (tmp.size() == 1)
                implLevel = tmp.iterator().next();
        }
        if (implementationLocation.getChoiceModel() != null) {
            Set<AmpCategoryValue> tmp = implementationLocation.getChoiceModel().getObject();
            if (tmp.size() == 1)
                implLocValue = tmp.iterator().next();
        }

        boolean defaultCountryCheck = CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel) &&
                CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(implLocValue);
        return defaultCountryCheck;
    }

    private boolean checkNationalCountry(AmpCategorySelectFieldPanel implementationLevel,
                                         AmpCategorySelectFieldPanel implementationLocation) {
        AmpCategoryValue implLevel = null;
        AmpCategoryValue implLocValue = null;
        if (implementationLevel.getChoiceModel() != null) {
            Set<AmpCategoryValue> tmp = implementationLevel.getChoiceModel().getObject();
            if (tmp.size() == 1)
                implLevel = tmp.iterator().next();
        }
        if (implementationLocation.getChoiceModel() != null) {
            Set<AmpCategoryValue> tmp = implementationLocation.getChoiceModel().getObject();
            if (tmp.size() == 1)
                implLocValue = tmp.iterator().next();
        }

        boolean defaultCountryCheck = (CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.equalsCategoryValue(implLevel)
                || CategoryConstants.IMPLEMENTATION_LEVEL_INTERNATIONAL.equalsCategoryValue(implLevel)) &&
                CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.equalsCategoryValue(implLocValue);
        return defaultCountryCheck;
    }

    private void defaultCountryChecks(AmpCategorySelectFieldPanel implementationLevel,
                                      AmpCategorySelectFieldPanel implementationLocation,
                                      Model<Boolean> disablePercentagesForInternational,
                                      AjaxRequestTarget target, AmpLocationFormTableFeature locationsTable, AmpRegionalFundingFormSectionFeature regionalFundingFeature) {
        disablePercentagesForInternational.setObject(false);
        /**
         * When implementation level is international and implementation location is country
         * we need to set the percentage for the default country to 100% and the rest of the
         * percentages to 0%
         */
        boolean internationalCountryCheck = checkInternationalCountry(implementationLevel, implementationLocation);
        boolean nationalCountryCheck = checkNationalCountry(implementationLevel, implementationLocation);
        if (!"true".equals(FeaturesUtil.getGlobalSettingValue(
                GlobalSettingsConstants.ALLOW_PERCENTAGES_FOR_ALL_COUNTRIES)) && internationalCountryCheck) {
            disablePercentagesForInternational.setObject(true);
        }

        if (nationalCountryCheck) {
            AmpCategoryValueLocations defaultCountry = null;
            defaultCountry = DynLocationManagerUtil.getDefaultCountry();

            if (target != null) { //we're in an ajax context, and not in init
                if (!defaultCountry.getIso().equals(MULTI_COUNTRY_ISO_CODE)) {
                    locationsTable.locationSelected(defaultCountry, am, disablePercentagesForInternational, regionalFundingFeature, true);
                } else {
                    disablePercentagesForInternational.setObject(false);
                }
                locationsTable.reloadValidationFields(target);
                target.add(locationsTable);
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(locationsTable));
            }
        }


    }

}
