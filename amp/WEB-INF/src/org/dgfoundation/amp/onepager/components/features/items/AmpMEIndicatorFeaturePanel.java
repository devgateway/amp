package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEActualValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEBaseTargetValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * M&E Indicator feature
 * @author dmbugua@developmentgateway.org
 * @since December 28, 2023
 */
public class AmpMEIndicatorFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {
    private static Logger logger = Logger.getLogger(AmpMEItemFeaturePanel.class);
    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    
    public AmpMEIndicatorFeaturePanel(String id, String fmName, final IModel<IndicatorActivity> conn,
                                 IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values, IModel<AmpActivityLocation> location) throws Exception {
        super(id, fmName, true);

        if (values.getObject() == null) values.setObject(new HashSet<>());

        final Label indicatorNameLabel = new Label("indicatorName", new PropertyModel<String>(indicator, "name"));
        add(indicatorNameLabel);

        String indCodeString = "";
        if (indicator.getObject().getCode() != null && indicator.getObject().getCode().trim().compareTo("") != 0) {
            indCodeString = " - " + indicator.getObject().getCode();
        }

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

        final AmpIndicatorGlobalValue globalBaseVal = new AmpIndicatorGlobalValue(AmpIndicatorGlobalValue.BASE);
        final AmpIndicatorGlobalValue globalTargetVal = new AmpIndicatorGlobalValue(AmpIndicatorGlobalValue.TARGET);

        final Model<Boolean> valuesSet = new Model<Boolean>(false);

        for (AmpIndicatorGlobalValue val : indicator.getObject().getIndicatorValues()){

            switch (val.getType()) {
                case AmpIndicatorValue.BASE:
                    val.copyValuesTo(globalBaseVal);
                    break;
                case AmpIndicatorValue.TARGET:
                    val.copyValuesTo(globalTargetVal);
                    valuesSet.setObject(true);
                    break;
                default:
                    break;
            }
        }

        final Label indicatorBaseValueLabel = new Label("base", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return globalBaseVal.getOriginalValue() != null ? String.valueOf(globalBaseVal.getOriginalValue()) : "N/A";
            }
        });
        add(indicatorBaseValueLabel);

        final Label indicatorBaseDateLabel = new Label("baseDate", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (globalBaseVal.getOriginalValueDate() != null) {
                    SimpleDateFormat format = new SimpleDateFormat(Objects.requireNonNull(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT)));
                    return format.format(globalBaseVal.getOriginalValueDate());
                } else {
                    return "N/A";
                }
            }
        });
        add(indicatorBaseDateLabel);

        final Label indicatorTargetValueLabel = new Label("target", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return globalTargetVal.getOriginalValue() != null ? String.valueOf(globalTargetVal.getOriginalValue()) : "N/A";
            }
        });
        add(indicatorTargetValueLabel);

        final Label indicatorTargetDateLabel = new Label("targetDate", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (globalTargetVal.getOriginalValueDate() != null) {
                    SimpleDateFormat format = new SimpleDateFormat(Objects.requireNonNull(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT)));
                    return format.format(globalTargetVal.getOriginalValueDate());
                } else {
                    return "N/A";
                }
            }
        });
        add(indicatorTargetDateLabel);

        AmpMEActualValuesFormTableFeaturePanel valuesTable = new AmpMEActualValuesFormTableFeaturePanel("valuesSubsection", indicator, conn, location,"Actual Values", false, 7);
        add(valuesTable);

        AmpAjaxLinkField addActualValue = new AmpAjaxLinkField("addActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();
                value.setIndicatorConnection(conn.getObject());
                value.setActivityLocation(location.getObject());
                value.setValueDate(new Date(System.currentTimeMillis()));
                value.setValueType(AmpIndicatorValue.ACTUAL);
                valuesTable.getEditorList().addItem(value);
                target.add(valuesTable);
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };

        add(addActualValue);

//        AmpAjaxLinkField addBaseTargetValue = new AmpAjaxLinkField("addBaseTargetValue", "Add Base Target Value", "Add Base Target Value") {
//            @Override
//            public void onClick(AjaxRequestTarget target) {
//                AmpIndicatorValue value = new AmpIndicatorValue();
//                value.setIndicatorConnection(conn.getObject());
//                value.setActivityLocation(location.getObject());
//                value.setValueDate(new Date(System.currentTimeMillis()));
//                value.setValueType(AmpIndicatorValue.ACTUAL);
//                valuesTable.getEditorList().addItem(value);
//                target.add(valuesTable);
//                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
//            }
//        };

        AmpMEIndicatorBaseFeaturePanel baseValues = null;

        try {
            baseValues = new AmpMEIndicatorBaseFeaturePanel("addBaseTargetValue", "Add Base Target Value", conn, indicator, values, location);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        add(baseValues);
        AmpAjaxLinkField setValue = new AmpAjaxLinkField("setValues", "Set Value", "Set Value") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                this.getButton().add(new AttributeAppender("class", new Model("mon_eval_button"), " "));
            }


            @Override
            protected void onClick(AjaxRequestTarget art) {
                Set<AmpIndicatorValue> vals = values.getObject();
                vals.clear();
//
//                AmpIndicatorValue tmp = (AmpIndicatorValue) actualVal.clone();
//                tmp.setIndicatorConnection(conn.getObject());
//                tmp.setIndValId(null); //for hibernate to think it's a new object
//                vals.add(tmp);
//
//                if (!valuesSet.getObject()) {
//                    target.setEnabled(false);
//                    valuesSet.setObject(true);
//                    art.add(target);
//                }

                art.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
                art.appendJavaScript(OnePagerUtil.getClickToggle2JS(this.getParent()));
            }
        };

        add(setValue);
    }
}
