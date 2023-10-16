/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.QuarterInformationPanel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
//import org.dgfoundation.amp.onepager.components.features.subsections.AmpMEValuesSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEActualValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpMEValuesFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    public AmpMEItemFeaturePanel(String id, String fmName, final IModel<IndicatorActivity> conn,
                                 IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values) throws Exception {
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

        final AmpIndicatorValue baseVal = new AmpIndicatorValue(AmpIndicatorValue.BASE);
        final AmpIndicatorValue targetVal = new AmpIndicatorValue(AmpIndicatorValue.TARGET);

        final Model<Boolean> valuesSet = new Model<Boolean>(false);

        for (AmpIndicatorValue val : values.getObject()){
            
            switch (val.getValueType()) {
                case AmpIndicatorValue.BASE:
                    val.copyValuesTo(baseVal);
                    break;
                case AmpIndicatorValue.TARGET:
                    val.copyValuesTo(targetVal);
                    valuesSet.setObject(true);
                    break;
                default:
                    break;
            }
        }

        final Label indicatorBaseValueLabel = new Label("base", new PropertyModel<String>(baseVal, "value"));
        add(indicatorBaseValueLabel);

        final Label indicatorBaseDateLabel = new Label("baseDate", new PropertyModel<String>(baseVal, "valueDate"));
        add(indicatorBaseDateLabel);

        final Label indicatorTargetValueLabel = new Label("target", new PropertyModel<String>(targetVal, "value"));
        add(indicatorTargetValueLabel);

        final Label indicatorTargetDateLabel = new Label("targetDate", new PropertyModel<String>(targetVal, "valueDate"));
        add(indicatorTargetDateLabel);

        AmpMEActualValuesFormTableFeaturePanel valuesTable = new AmpMEActualValuesFormTableFeaturePanel("valuesSubsection", indicator, conn, "Actual Values", false, 7);
        add(valuesTable);

        AmpAjaxLinkField addActualValue = new AmpAjaxLinkField("addActualValue", "Add Actual Value", "Add Actual Value") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicatorValue value = new AmpIndicatorValue();

                value.setValueDate(new Date(System.currentTimeMillis()));
                value.setValueType(AmpIndicatorValue.ACTUAL);
                valuesTable.getEditorList().addItem(value);
                target.add(valuesTable);
                target.appendJavaScript(QuarterInformationPanel.getJSUpdate(getSession()));
            }
        };

        add(addActualValue);

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
