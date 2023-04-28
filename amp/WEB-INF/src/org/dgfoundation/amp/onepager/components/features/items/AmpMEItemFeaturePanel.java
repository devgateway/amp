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
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMEBaseValuesSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMEFieldsSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMETargetValuesSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpMEValuesSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.translation.TranslatedChoiceRenderer;
import org.digijava.module.aim.dbentity.*;
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

        final AmpMEValuesSubsectionFeature valuesSubsection = new AmpMEValuesSubsectionFeature("valuesSubsection",
                fmName, indicator);

        add(valuesSubsection);

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
