/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.ListItem;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEActualValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEItemFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {


    private static Logger logger = Logger.getLogger(AmpMEItemFeaturePanel.class);
    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    private final ListView<IndicatorActivity> list;
    protected ListEditor<AmpActivityLocation> tabsList;

    protected ListEditor<AmpActivityLocation> indicatorLocationList;

    private boolean isTabsView = true;
    public AmpMEItemFeaturePanel(String id, String fmName, IModel<AmpActivityLocation> location,
                                 final IModel<AmpActivityVersion> conn, IModel<Set<AmpActivityLocation>> locations) throws Exception {
        super(id, fmName, true);

//        if (values.getObject() == null) values.setObject(new HashSet<>());
        String locationName = location.getObject().getLocation().getName();

        if(location.getObject().getLocation().getParentLocation() != null){
            locationName = location.getObject().getLocation().getParentLocation().getName() + "[" + location.getObject().getLocation().getName() + "]";
        }

        add(new Label("countryLocation", locationName));

        final IModel<List<IndicatorActivity>> listModel = OnePagerUtil
                .getReadOnlyListModelFromSetModel(new PropertyModel(conn, "indicators"));

        final AmpUniqueCollectionValidatorField<IndicatorActivity> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<IndicatorActivity>(
                "uniqueMEValidator", listModel, "Unique MEs Validator") {

            @Override
            public Object getIdentifier(IndicatorActivity t) {
                return t.getIndicator().getName();
            }
        };
        add(uniqueCollectionValidationField);

        list = new ListView<IndicatorActivity>("list", listModel) {
            @Override
            protected void populateItem(org.apache.wicket.markup.html.list.ListItem<IndicatorActivity> item) {
//                AmpMEItemFeaturePanel indicator = null;
//                try {
//                    indicator = new AmpMEItemFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"), locations);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                item.add(indicator);
                AmpMEIndicatorFeaturePanel indicatorItem = null;
                try {
                    indicatorItem = new AmpMEIndicatorFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                item.add(indicatorItem);

                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
                        "delete", "Delete ME Item", new Model<String>(translatedMessage)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        conn.getObject().getIndicators().remove(item.getModelObject());
                        uniqueCollectionValidationField.reloadValidationField(target);
                        //setModel.getObject().remove(item.getModelObject());
                        list.removeAll();
                        target.add(AmpMEItemFeaturePanel.this);
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEItemFeaturePanel.this));
                    }
                };
                item.add(deleteLinkField);
            }
        };
        list.setReuseItems(true);
        add(list);


        final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators =
                new AmpAutocompleteFieldPanel<AmpIndicator>("search", "Search Indicators",
                        AmpMEIndicatorSearchModel.class) {

                    private static final long serialVersionUID = 1227775244079125152L;

                    @Override
                    protected String getChoiceValue(AmpIndicator choice) {
                        return DbUtil.filter(choice.getName());
                    }

                    @Override
                    public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {
                        IndicatorActivity ia = new IndicatorActivity();
                        ia.setActivity(conn.getObject());
                        ia.setIndicator(choice);
                        conn.getObject().getIndicators().add(ia);
                        uniqueCollectionValidationField.reloadValidationField(target);

                        //setModel.getObject().add(ia);
                        list.removeAll();
                        target.add(list.getParent());

                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEItemFeaturePanel.this));
//                        target.appendJavaScript("indicatorTabs();");
                    }

                    @Override
                    public Integer getChoiceLevel(AmpIndicator choice) {
                        return 0;
                    }

                };

        add(searchIndicators);

        final WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.setOutputMarkupId(true);

//        final Label indicatorNameLabel = new Label("indicatorName", new PropertyModel<String>(indicator, "name"));
//        add(indicatorNameLabel);

        String indCodeString = "";
//        if (indicator.getObject().getCode() != null && indicator.getObject().getCode().trim().compareTo("") != 0) {
//            indCodeString = " - " + indicator.getObject().getCode();
//        }

        final Label indicatorCodeLabel = new Label("indicatorCode", new Model<String>(indCodeString));
        add(indicatorCodeLabel);

        final IModel<AmpCategoryValue> logFrameModel = new PropertyModel<>(conn, "logFrame");
        try {
            AmpCategorySelectFieldPanel logframe = new AmpCategorySelectFieldPanel("logframe",
                    CategoryConstants.LOGFRAME_KEY, logFrameModel, "Logframe Category", true, true);
            add(logframe);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }

        final AmpSelectFieldPanel<AmpIndicatorRiskRatings> riskSelect = new AmpSelectFieldPanel("risk",
                new PropertyModel<>(conn, "risk"), MEIndicatorsUtil.getAllIndicatorRisks(), "Risk", false, false,
                new TranslatedChoiceRenderer<AmpIndicatorRiskRatings>(), false);
        add(riskSelect);

        final AmpIndicatorValue baseVal = new AmpIndicatorValue(AmpIndicatorValue.BASE);
        final AmpIndicatorValue targetVal = new AmpIndicatorValue(AmpIndicatorValue.TARGET);

        final Model<Boolean> valuesSet = new Model<Boolean>(false);

//        for (AmpIndicatorValue val : values.getObject()){
//
//            switch (val.getValueType()) {
//                case AmpIndicatorValue.BASE:
//                    val.copyValuesTo(baseVal);
//                    break;
//                case AmpIndicatorValue.TARGET:
//                    val.copyValuesTo(targetVal);
//                    valuesSet.setObject(true);
//                    break;
//                default:
//                    break;
//            }
//        }

        final Label indicatorBaseValueLabel = new Label("base", new PropertyModel<String>(baseVal, "value"));
        add(indicatorBaseValueLabel);

        final Label indicatorBaseDateLabel = new Label("baseDate", new PropertyModel<String>(baseVal, "valueDate"));
        add(indicatorBaseDateLabel);

        final Label indicatorTargetValueLabel = new Label("target", new PropertyModel<String>(targetVal, "value"));
        add(indicatorTargetValueLabel);

        final Label indicatorTargetDateLabel = new Label("targetDate", new PropertyModel<String>(targetVal, "valueDate"));
        add(indicatorTargetDateLabel);

//        AmpMEActualValuesFormTableFeaturePanel valuesTable = new AmpMEActualValuesFormTableFeaturePanel("valuesSubsection", indicator, conn, "Actual Values", false, 7);
//        add(valuesTable);
//
//        AmpAjaxLinkField addActualValue = new AmpAjaxLinkField("addActualValue", "Add Actual Value", "Add Actual Value") {
//            @Override
//            public void onClick(AjaxRequestTarget target) {
//                AmpIndicatorValue value = new AmpIndicatorValue();
//                value.setIndicatorConnection(conn.getObject());
//                value.setValueDate(new Date(System.currentTimeMillis()));
//                value.setValueType(AmpIndicatorValue.ACTUAL);
//                valuesTable.getEditorList().addItem(value);
//                target.add(valuesTable);
//                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
//            }
//        };
//
//        add(addActualValue);

//        AmpAjaxLinkField setValue = new AmpAjaxLinkField("setValues", "Set Value", "Set Value") {
//            @Override
//            protected void onConfigure() {
//                super.onConfigure();
//                this.getButton().add(new AttributeAppender("class", new Model("mon_eval_button"), " "));
//            }
//
//
//            @Override
//            protected void onClick(AjaxRequestTarget art) {
//                Set<AmpIndicatorValue> vals = values.getObject();
//                vals.clear();
////
////                AmpIndicatorValue tmp = (AmpIndicatorValue) actualVal.clone();
////                tmp.setIndicatorConnection(conn.getObject());
////                tmp.setIndValId(null); //for hibernate to think it's a new object
////                vals.add(tmp);
////
////                if (!valuesSet.getObject()) {
////                    target.setEnabled(false);
////                    valuesSet.setObject(true);
////                    art.add(target);
////                }
//
//                art.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
//                art.appendJavaScript(OnePagerUtil.getClickToggle2JS(this.getParent()));
//            }
//        };

        add(wmc);

        tabsList = new ListEditor<AmpActivityLocation>("locationItemsForTabs", locations) {
            private static final long serialVersionUID = -206108834217110807L;

            @Override
            protected void onPopulateItem(ListItem<AmpActivityLocation> item) {
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
            protected void onPopulateItem(ListItem<AmpActivityLocation> item) {
                AmpCategoryValueLocations location = item.getModel().getObject().getLocation();


//                AmpMEIndicatorFeaturePanel locationIndicator = null;
//                try {
//                    locationIndicator = new AmpMEIndicatorFeaturePanel("indicatorLocation", "ME Location Item",conn, indicator, values, new Model<AmpCategoryValueLocations>(location));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }

//                item.add(locationIndicator);
            }

        };

        wmc.add(indicatorLocationList);

//        add(setValue);
    }

}
