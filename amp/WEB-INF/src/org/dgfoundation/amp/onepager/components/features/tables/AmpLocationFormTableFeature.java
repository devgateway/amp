/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisitor;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpLocationItemPanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.events.LocationChangedEvent;
import org.dgfoundation.amp.onepager.models.AmpLocationSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.text.DecimalFormat;
import java.util.*;

import static org.digijava.module.aim.util.LocationConstants.MULTI_COUNTRY_ISO_CODE;


/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpLocationFormTableFeature extends
        AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivityLocation> {

    private static final long serialVersionUID = 1L;
    private IModel<Set<AmpActivityLocation>> setModel;

    public IModel<Set<AmpActivityLocation>> getSetModel() {
        return setModel;
    }

    final AmpAutocompleteFieldPanel<AmpCategoryValueLocations> searchLocations;

    private AmpPercentageCollectionValidatorField<AmpActivityLocation> percentageValidationField;

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
                                       final AmpCategorySelectFieldPanel implementationLocation, final AmpCategorySelectFieldPanel implementationLevel,
                                       final IModel<Boolean> disablePercentagesForInternational)
            throws Exception {
        super(id, am, fmName, false, true);
        setTitleHeaderColSpan(4);
        setModel = new PropertyModel<Set<AmpActivityLocation>>(
                am, "locations");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityLocation>());

        final AbstractReadOnlyModel<List<AmpActivityLocation>> listModel = new AbstractReadOnlyModel<List<AmpActivityLocation>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpActivityLocation> getObject() {
                // remove sectors with other classification
                ArrayList<AmpActivityLocation> ret = new ArrayList<AmpActivityLocation>();

                if (setModel.getObject() != null) {
                    for (AmpActivityLocation ampActivityLocation : setModel.getObject()) {
                        ret.add(ampActivityLocation);
                    }
                }

                Comparator<AmpActivityLocation> comparator = new Comparator<AmpActivityLocation>() {
                    @Override
                    public int compare(AmpActivityLocation o1, AmpActivityLocation o2) {
                        return o1.getLocation().getAutoCompleteLabel().compareTo(
                                o2.getLocation().getAutoCompleteLabel());
                    }
                };

                Collections.sort(ret, comparator);
                return ret;
            }
        };
        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        final AmpComponentPanel locationPercentageRequired = new AmpComponentPanel("locationPercentageRequired", "Location percentage required") {
        };
        add(locationPercentageRequired);
        final Label totalLabel = new Label("totalPercLocation", new Model() {
            @Override
            public String getObject() {
                double total = 0;
                for (AmpActivityLocation item : listModel.getObject()) {
                    if (item.getLocationPercentage() != null) {
                        total += item.getLocationPercentage();
                    }
                }

                DecimalFormat decimalFormat = FormatHelper.getPercentageDefaultFormat(true);
                return decimalFormat.format(total);
            }
        });
        totalLabel.setOutputMarkupId(true);
        add(totalLabel);
        percentageValidationField =
                new AmpPercentageCollectionValidatorField<AmpActivityLocation>("locationPercentageTotal", listModel, "locationPercentageTotal") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Number getPercentage(AmpActivityLocation item) {
                        return item.getLocationPercentage();
                    }
                };
        percentageValidationField.setIndicatorAppender(iValidator);
        add(percentageValidationField);

        final AmpUniqueCollectionValidatorField<AmpActivityLocation> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivityLocation>(
                "uniqueLocationsValidator", listModel, "uniqueLocationsValidator") {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getIdentifier(AmpActivityLocation t) {
                return t.getLocation().getAutoCompleteLabel();
            }
        };
        uniqueCollectionValidationField.setIndicatorAppender(iValidator);
        add(uniqueCollectionValidationField);

        final AmpMinSizeCollectionValidationField<AmpActivityLocation> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivityLocation>(
                "minSizeValidator", listModel, "Location required validator") {
            @Override
            protected void onConfigure() {
                super.onConfigure();

                //the required star should be visible, depending on whether the validator is active or not
                reqStar.setVisible(isVisible());

            }
        };
        minSizeCollectionValidationField.setIndicatorAppender(iValidator);
        add(minSizeCollectionValidationField);


        final AmpTreeCollectionValidatorField<AmpActivityLocation> treeCollectionValidatorField = new AmpTreeCollectionValidatorField<AmpActivityLocation>("treeValidator", listModel, "Tree Validator") {
            @Override
            public AmpAutoCompleteDisplayable getItem(AmpActivityLocation l) {
                return l.getLocation();
            }
        };
        treeCollectionValidatorField.setIndicatorAppender(iValidator);
        add(treeCollectionValidatorField);

        list = new ListView<AmpActivityLocation>("listLocations", listModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<AmpActivityLocation> item) {
                boolean isCountryNationalAndMultiCountry = false;

                if (CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.
                        equalsCategoryValue(implementationLevel.getChoiceModel().getObject().iterator().next()) &&
                        CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.
                                equalsCategoryValue(implementationLocation.getChoiceModel().getObject().iterator().next())
                        && DynLocationManagerUtil.getDefaultCountry().getIso().equals(MULTI_COUNTRY_ISO_CODE)) {
                    isCountryNationalAndMultiCountry = true;
                    disablePercentagesForInternational.setObject(true);
                    searchLocations.setVisibilityAllowed(false);
                }else{
                    disablePercentagesForInternational.setObject(false);
                    searchLocations.setVisibilityAllowed(true);
                }

                AmpLocationItemPanel li = new AmpLocationItemPanel("locationItem", item.getModel(), "Location Item",
                        disablePercentagesForInternational, am, regionalFundingFeature,
                        AmpLocationFormTableFeature.this, locationPercentageRequired, setModel, list, totalLabel, isCountryNationalAndMultiCountry);
                item.add(li);
            }
        };
        list.setReuseItems(true);
        add(list);

        add(new AmpDividePercentageField<AmpActivityLocation>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, new Model<ListView<AmpActivityLocation>>(list)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onClick(AjaxRequestTarget target) {
                String pattern = FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.DECIMAL_LOCATION_PERCENTAGES_DIVIDE);
                int factor = 1;
                if (pattern != null && Integer.parseInt(pattern) > 0) {
                    Integer numDecimals = Integer.parseInt(pattern);
                    factor = (int) (Math.pow(10, numDecimals));
                }

                Set<AmpActivityLocation> set = setModel.getObject();
                if (set.size() == 0)
                    return;

                int size = 0;
                Iterator<AmpActivityLocation> it = set.iterator();
                while (it.hasNext()) {
                    AmpActivityLocation t = it.next();
                    if (itemInCollection(t))
                        size++;
                }

                if (size == 0)
                    return;

                int alloc = (100 * factor) / size;
                it = set.iterator();
                while (it.hasNext()) {
                    AmpActivityLocation loc = it.next();
                    if (!itemInCollection(loc))
                        continue;
                    setPercentageLocation(loc, alloc / (double) factor);
                }

                int dif = (100 * factor) - alloc * size;
                int delta = 1;
                if (dif < 0)
                    delta = -1;
                it = set.iterator();
                while (dif != 0 && it.hasNext()) {
                    AmpActivityLocation loc = it.next();
                    if (!itemInCollection(loc))
                        continue;
                    setPercentageLocation(loc, (getPercentageLocation(loc) + (delta) / (double) factor));
                    dif = dif - delta;
                }
                list.removeAll();
                target.add(list.getParent());
            }


            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (this.isEnabled()) {
                    this.setEnabled(!disablePercentagesForInternational.getObject());
                }
            }

            public void setPercentageLocation(AmpActivityLocation loc, double val) {
                loc.setLocationPercentage((float) val);
            }

            public double getPercentageLocation(AmpActivityLocation loc) {
                return (double) (loc.getLocationPercentage());
            }

            @Override
            public void setPercentage(AmpActivityLocation loc, int val) {
                loc.setLocationPercentage((float) val);
            }

            @Override
            public int getPercentage(AmpActivityLocation loc) {
                return (int) ((float) (loc.getLocationPercentage()));
            }

            @Override
            public boolean itemInCollection(AmpActivityLocation item) {
                return true; //all items displayed in the same list
            }

        });

        searchLocations =
                new AmpAutocompleteFieldPanel<AmpCategoryValueLocations>("searchLocations", "Search Locations",
                        AmpLocationSearchModel.class, false) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onSelect(AjaxRequestTarget target,
                                         AmpCategoryValueLocations choice) {
                        //if national, country and multicountry then disable percentage
                        if (CategoryConstants.IMPLEMENTATION_LEVEL_NATIONAL.
                                equalsCategoryValue(implementationLevel.getChoiceModel().getObject().iterator().next()) &&
                                CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0.
                                        equalsCategoryValue(implementationLocation.getChoiceModel().getObject().iterator().next())
                                && DynLocationManagerUtil.getDefaultCountry().getIso().equals(MULTI_COUNTRY_ISO_CODE)) {
                            disablePercentagesForInternational.setObject(true);
                            //AmpLocationFormTableFeature.this.getSearchLocations().changeEnabled(false, target);
                            AmpLocationFormTableFeature.this.getSearchLocations().setVisibilityAllowed(false);
                            target.add(AmpLocationFormTableFeature.this.getSearchLocations());
                        }else{
                            disablePercentagesForInternational.setObject(false);
                        }
                        locationSelected(choice, am, disablePercentagesForInternational, regionalFundingFeature);

                        // toggleHeading(target, setModel.getObject());
                        target.add(list.getParent());

                        regionalFundingFeature.getList().removeAll();

                        if (regionalFundingFeature.isVisibleInHierarchy()) {
                            target.add(regionalFundingFeature);
                            target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(regionalFundingFeature));
                        }

                       // regionalFundingFeature.getMeFormSection().reloadMeFormSection(target);
                        send( getPage(), Broadcast.BREADTH, new LocationChangedEvent(target));
                        reloadValidationFields(target);
                        list.removeAll();
                    }

                    @Override
                    public Integer getChoiceLevel(AmpCategoryValueLocations choice) {
                        return null;
                    }

                    @Override
                    protected String getChoiceValue(AmpCategoryValueLocations choice) {
                        return choice.getAutoCompleteLabel();
                    }
                };

        AmpTeamMember ampCurrentMember = ((AmpAuthWebSession) this.getSession()).getAmpCurrentMember();
        AmpApplicationSettings ampAppSettings = null;
        if (ampCurrentMember != null) {
            ampAppSettings = org.digijava.module.aim.util.DbUtil.getTeamAppSettings(ampCurrentMember.getAmpTeam().getAmpTeamId());
        }

        Boolean allSetupCountries = (ampAppSettings == null || !ampAppSettings.getShowAllCountries()) ? new Boolean(false) : new Boolean(true);

        searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LAYER,
                implementationLocation.getChoiceModel());

        searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.LEVEL,
                implementationLevel.getChoiceModel());

        searchLocations.getModelParams().put(AmpLocationSearchModel.PARAM.ALL_SETUP_COUNTRIES, allSetupCountries);


        add(searchLocations);
    }

    public void locationSelected(AmpCategoryValueLocations choice, IModel<AmpActivityVersion> am, IModel<Boolean> disablePercentagesForInternational, AmpRegionalFundingFormSectionFeature regionalFundingFeature) {
        locationSelected(choice, am, disablePercentagesForInternational, regionalFundingFeature,false);
    }

    public void locationSelected(AmpCategoryValueLocations choice, IModel<AmpActivityVersion> am, IModel<Boolean> disablePercentagesForInternational, AmpRegionalFundingFormSectionFeature regionalFundingFeature, boolean isCountryNational) {
        AmpActivityLocation activityLocation = new AmpActivityLocation();

        activityLocation.setLocation(choice);
        if (disablePercentagesForInternational.getObject()) {
            AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getDefaultCountry();
            if (choice.getId().longValue() == defCountry.getId().longValue()
                    || defCountry.getIso().equals(MULTI_COUNTRY_ISO_CODE)) {
                activityLocation.setLocationPercentage(100f);
            } else {
                activityLocation.setLocationPercentage(null);
            }
        } else {
            if (list.size() > 0) {
                activityLocation.setLocationPercentage(null);
            } else {
                activityLocation.setLocationPercentage(100f);
            }
        }

        activityLocation.setActivity(am.getObject());

        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityLocation>());

        Set<AmpActivityLocation> set = setModel.getObject();
        if (isCountryNational) {
            Iterator<AmpActivityLocation> it = set.iterator();
            while (it.hasNext()) {
                AmpActivityLocation loc = it.next();
                if (loc.getLocation() != null && activityLocation.getLocation() != null
                        && loc.getLocation().compareTo(activityLocation.getLocation()) == 0) {
                    return;
                }
            }
        }
        regionalFundingFeature.getMeFormSection().updateAmpLocationModel(activityLocation);

        set.add(activityLocation);
    }

    public void reloadValidationFields(AjaxRequestTarget target) {
        this.visitChildren(AmpCollectionValidatorField.class,
                (IVisitor<AmpCollectionValidatorField, Void>) (component, visit)
                        -> component.reloadValidationField(target));

    }

    public AmpPercentageCollectionValidatorField<AmpActivityLocation> getPercentageValidationField() {
        return percentageValidationField;
    }

    public AmpAutocompleteFieldPanel<AmpCategoryValueLocations> getSearchLocations() {
        return searchLocations;
    }
}
