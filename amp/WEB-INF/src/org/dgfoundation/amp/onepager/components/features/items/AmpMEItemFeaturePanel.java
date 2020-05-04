/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpIndicatorGroupField;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

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
            IModel<AmpIndicator> indicator, final IModel<Set<AmpIndicatorValue>> values){
        super(id, fmName, true);
        
        if (values.getObject() == null)
            values.setObject(new HashSet<AmpIndicatorValue>());
        
        final Label indicatorNameLabel = new Label("indicatorName", new PropertyModel<String>(indicator, "name"));
        add(indicatorNameLabel);

        String indCodeString = "";
        if (indicator.getObject().getCode() != null && indicator.getObject().getCode().trim().compareTo("") != 0)
            indCodeString = " - " + indicator.getObject().getCode();
        
        final Label indicatorCodeLabel = new Label("indicatorCode", new Model<String>(indCodeString));
        add(indicatorCodeLabel);

        final IModel<AmpCategoryValue> logFrameModel = new PropertyModel<>(conn, "logFrame");
        try {
            AmpCategorySelectFieldPanel logframe = new AmpCategorySelectFieldPanel("logframe", CategoryConstants.LOGFRAME_KEY, logFrameModel, "Logframe Category", true, true);
            add(logframe);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        final IModel<AmpIndicatorRiskRatings> riskModel = new PropertyModel<>(conn, "risk");
        ChoiceRenderer cr = new ChoiceRenderer(){
            @Override
            public Object getDisplayValue(Object object) {
                AmpIndicatorRiskRatings rating = (AmpIndicatorRiskRatings)object;           
                return TranslatorUtil.getTranslation(rating.getRatingName());
            }
        };
        final AbstractSingleSelectChoice<AmpIndicatorRiskRatings> risk = new DropDownChoice<AmpIndicatorRiskRatings>(
                "risk",
                riskModel, new LoadableDetachableModel<List<AmpIndicatorRiskRatings>>() {
                    @Override
                    protected List<AmpIndicatorRiskRatings> load() {
                        return (List<AmpIndicatorRiskRatings>) MEIndicatorsUtil.getAllIndicatorRisks();
                    }
                }, cr).setNullValid(true);
        risk.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                riskModel.setObject(risk.getConvertedInput());
            }
        });
                
        add(risk);

        final AmpIndicatorValue baseVal = new AmpIndicatorValue(AmpIndicatorValue.BASE);
        final AmpIndicatorValue targetVal = new AmpIndicatorValue(AmpIndicatorValue.TARGET);
        final AmpIndicatorValue revisedVal = new AmpIndicatorValue(AmpIndicatorValue.REVISED);
        final AmpIndicatorValue currentVal = new AmpIndicatorValue(AmpIndicatorValue.ACTUAL);
        
        final Model<Boolean> valuesSet = new Model<Boolean>(false);

//      Iterator<AmpIndicatorValue> iterator = values.getObject().iterator();
//      while (iterator.hasNext()) {
//      AmpIndicatorValue val = (AmpIndicatorValue) iterator
//      .next();

        for (AmpIndicatorValue val : values.getObject()){
            
            switch (val.getValueType()) {
            case AmpIndicatorValue.BASE:
                val.copyValuesTo(baseVal);
                break;
            case AmpIndicatorValue.TARGET:
                val.copyValuesTo(targetVal);
                valuesSet.setObject(true);
                break;
            case AmpIndicatorValue.REVISED:
                val.copyValuesTo(revisedVal);
                break;
            case AmpIndicatorValue.ACTUAL:
                val.copyValuesTo(currentVal);
                break;
            default:
                break;
            }
        }

        final AmpIndicatorGroupField base = new AmpIndicatorGroupField("base", new PropertyModel(baseVal, "value"), new PropertyModel(baseVal, "valueDate"), new PropertyModel(baseVal, "comment"), "Base Value", "Base", true);
        base.setOutputMarkupId(true);
        add(base);
        
        final AmpIndicatorGroupField target = new AmpIndicatorGroupField("target", new PropertyModel(targetVal, "value"), new PropertyModel(targetVal, "valueDate"), new PropertyModel(targetVal, "comment"), "Target Value", "Target", true);
        target.setIgnorePermissions(true);
        if (valuesSet.getObject()){
            target.setEnabled(false);
        }
        else
            target.setEnabled(true);
        target.setOutputMarkupId(true);
        add(target);

        final AmpIndicatorGroupField revised = new AmpIndicatorGroupField("revised", new PropertyModel(revisedVal, "value"), new PropertyModel(revisedVal, "valueDate"), new PropertyModel(revisedVal, "comment"), "Revised Value", "Revised", false);
        if (valuesSet.getObject())
            revised.setVisibilityAllowed(true);
        else
            revised.setVisibilityAllowed(false);
        revised.setOutputMarkupId(true);
        add(revised);

        final AmpIndicatorGroupField current = new AmpIndicatorGroupField("current", new PropertyModel(currentVal, "value"), new PropertyModel(currentVal, "valueDate"), new PropertyModel(currentVal, "comment"), "Current Value", "Current", true);
        current.setOutputMarkupId(true);
        add(current);
        
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
                
                AmpIndicatorValue tmp = (AmpIndicatorValue) baseVal.clone();
                tmp.setIndicatorConnection(conn.getObject());
                tmp.setIndValId(null); //for hibernate to think it's a new object
                vals.add(tmp);
                
                tmp = (AmpIndicatorValue) revisedVal.clone();
                tmp.setIndicatorConnection(conn.getObject());
                tmp.setIndValId(null); //for hibernate to think it's a new object
                vals.add(tmp);
                
                tmp = (AmpIndicatorValue) targetVal.clone(); 
                tmp.setIndicatorConnection(conn.getObject());
                tmp.setIndValId(null); //for hibernate to think it's a new object
                vals.add(tmp);
                
                tmp = (AmpIndicatorValue) currentVal.clone(); 
                tmp.setIndicatorConnection(conn.getObject());
                tmp.setIndValId(null); //for hibernate to think it's a new object
                vals.add(tmp);

                if (!valuesSet.getObject()){
                    target.setEnabled(false);
                    revised.setVisibilityAllowed(true);
                    valuesSet.setObject(true);
                    art.add(target);
                }

                art.add(base.getParent());
                art.appendJavaScript(OnePagerUtil.getToggleChildrenJS(this.getParent()));
                art.appendJavaScript(OnePagerUtil.getClickToggle2JS(this.getParent()));
            }
        };
        add(setValue);
    }

}
