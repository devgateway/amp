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
import org.apache.wicket.util.convert.IConverter;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.converters.CustomDoubleConverter;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Feb 10, 2011
 */
public class AmpMEItemFeaturePanel extends AmpFeaturePanel<IndicatorActivity> {


    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpMEItemFeaturePanel(String id, String fmName, final IModel<IndicatorActivity> conn,
                                 IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values) {
        super(id, fmName, true);

        if (values.getObject() == null)
            values.setObject(new HashSet<AmpIndicatorValue>());

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

        AmpTextFieldPanel<Double> baseValue = new AmpTextFieldPanel<Double>("baseValue",
                new PropertyModel<Double>(indicator, "baseValue.value"), "Base Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        baseValue.getTextContainer().setEnabled(false);
        add(baseValue);

        AmpDatePickerFieldPanel baseValueDate = new AmpDatePickerFieldPanel("baseValueDate",
                new PropertyModel<>(indicator, "baseValue.valueDate"), "Base Value Date");
        baseValueDate.getDate().setEnabled(false);
        add(baseValueDate);

        AmpTextFieldPanel<Double> targetValue = new AmpTextFieldPanel<Double>("targetValue",
                new PropertyModel<Double>(indicator, "targetValue.value"), "Target Value") {
            public IConverter getInternalConverter(java.lang.Class<?> type) {
                return CustomDoubleConverter.INSTANCE;
            }
        };
        targetValue.getTextContainer().setEnabled(false);
        add(targetValue);

        AmpDatePickerFieldPanel targetValueDate = new AmpDatePickerFieldPanel("targetValueDate",
                new PropertyModel<>(indicator, "targetValue.valueDate"), "Target Value Date");
        targetValueDate.getDate().setEnabled(false);
        add(targetValueDate);

        final AmpIndicatorValue actualVal = new AmpIndicatorValue(AmpIndicatorValue.ACTUAL);

        for (AmpIndicatorValue val : values.getObject()) {
            switch (val.getValueType()) {
                case AmpIndicatorValue.ACTUAL:
                    val.copyValuesTo(actualVal);
                    break;
                default:
                    break;
            }
        }

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
