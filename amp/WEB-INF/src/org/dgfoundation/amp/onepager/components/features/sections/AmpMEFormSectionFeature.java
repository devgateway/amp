/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEIndicatorFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;

import java.util.HashSet;
import java.util.List;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
//    private final ListView<IndicatorActivity> list;

    protected ListEditor<AmpActivityLocation> tabsList;

    protected ListEditor<AmpActivityLocation> indicatorLocationList;

    private boolean isTabsView = true;

//    private Set<AmpActivityLocation> locations = new HashSet<>();

    public AmpMEFormSectionFeature(String id, String fmName,
                                   final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        if (am.getObject().getIndicators() == null) {
            am.getObject().setIndicators(new HashSet<>());
        }
        final WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.setOutputMarkupId(true);

        final IModel<Set<AmpActivityLocation>> locations = new PropertyModel(am, "locations");



        add(wmc);

        tabsList = new ListEditor<AmpActivityLocation>("locationItemsForTabs", locations) {
            private static final long serialVersionUID = -206108834217110807L;

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpActivityLocation> item) {
                AmpCategoryValueLocations location = item.getModel().getObject().getLocation();
                String locationName = location.getName();
                String locationIso = location.getIso3();

                if(location.getParentLocation() != null){
                    locationIso = location.getParentLocation().getIso3();
                }

                if(location.getParentLocation() != null){
                    locationName = location.getParentLocation().getName() + "[" + location.getName() + "]";
                }

                ExternalLink l = new ExternalLink("locationLinkForTabs", "#tab" + (item.getIndex() + 1));
                l.add(new AttributePrepender("title", new Model<String>(locationName), ""));

                Label label = new Label("locationTabsLabel", new Model<String>(locationIso));

                l.add(label);

                item.add(l);
            }

        };
        tabsList.setVisibilityAllowed(isTabsView);
        wmc.add(tabsList);

        indicatorLocationList = new ListEditor<AmpActivityLocation>("listIndicatorLocation", locations) {

            @Override
            protected void onPopulateItem(org.dgfoundation.amp.onepager.components.ListItem<AmpActivityLocation> item) {
//                AmpCategoryValueLocations location = item.getModel().getObject().getLocation();


//                AmpMEIndicatorFeaturePanel locationIndicator = null;
//                try {
//                    locationIndicator = new AmpMEIndicatorFeaturePanel("indicatorLocation", "ME Location Item",conn, indicator, values, new Model<AmpCategoryValueLocations>(location));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//                item.add(locationIndicator);

                AmpMEItemFeaturePanel indicatorLoc = null;
                try {
                    indicatorLoc = new AmpMEItemFeaturePanel("indicatorLocation", "ME Item Location", item.getModel(), am, locations);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(indicatorLoc);

//                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
//                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
//                        "delete", "Delete ME Item", new Model<String>(translatedMessage)) {
//                    @Override
//                    public void onClick(AjaxRequestTarget target) {
//                        am.getObject().getIndicators().remove(item.getModelObject());
//                        uniqueCollectionValidationField.reloadValidationField(target);
//                        //setModel.getObject().remove(item.getModelObject());
//                        list.removeAll();
//                        target.add(AmpMEFormSectionFeature.this);
//                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
//                    }
//                };
//                item.add(deleteLinkField);
            }

        };

        wmc.add(indicatorLocationList);


//        list = new ListView<IndicatorActivity>("list", listModel) {
//            @Override
//            protected void populateItem(final ListItem<IndicatorActivity> item) {
//                AmpMEItemFeaturePanel indicator = null;
////                try {
////                    indicator = new AmpMEItemFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"), locations);
////                } catch (Exception e) {
////                    throw new RuntimeException(e);
////                }
////                item.add(indicator);
//
//                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
//                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
//                        "delete", "Delete ME Item", new Model<String>(translatedMessage)) {
//                    @Override
//                    public void onClick(AjaxRequestTarget target) {
//                        am.getObject().getIndicators().remove(item.getModelObject());
//                        uniqueCollectionValidationField.reloadValidationField(target);
//                        //setModel.getObject().remove(item.getModelObject());
//                        list.removeAll();
//                        target.add(AmpMEFormSectionFeature.this);
//                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
//                    }
//                };
//                item.add(deleteLinkField);
//            }
//        };
//        list.setReuseItems(true);
//        add(list);

//
//        final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators =
//                new AmpAutocompleteFieldPanel<AmpIndicator>("search", "Search Indicators",
//                        AmpMEIndicatorSearchModel.class) {
//
//                    private static final long serialVersionUID = 1227775244079125152L;
//
//                    @Override
//                    protected String getChoiceValue(AmpIndicator choice) {
//                        return DbUtil.filter(choice.getName());
//                    }
//
//                    @Override
//                    public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {
//                        IndicatorActivity ia = new IndicatorActivity();
//                        ia.setActivity(am.getObject());
//                        ia.setIndicator(choice);
//                        am.getObject().getIndicators().add(ia);
//                        uniqueCollectionValidationField.reloadValidationField(target);
//
//                        //setModel.getObject().add(ia);
//                        list.removeAll();
//                        target.add(list.getParent());
//
//                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
//                        target.appendJavaScript("indicatorTabs();");
//                    }
//
//                    @Override
//                    public Integer getChoiceLevel(AmpIndicator choice) {
//                        return 0;
//                    }
//
//                };
//
//        add(searchIndicators);
    }
}
