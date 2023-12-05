/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;


import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.FundingListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.FreezingUpdateEvent;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetailComparator;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org since Nov 8, 2010
 */
public class AmpDonorExpendituresFormTableFeature extends
        AmpDonorFormTableFeaturePanel {
    
    private static Logger logger = Logger.getLogger(AmpDonorExpendituresFormTableFeature.class);
     
    protected AmpCategorySelectFieldPanel getExpenditureClassTypeComponent(
            IModel<AmpFundingDetail> model) {
        String transactionTypeString = "";

        IModel<Set<AmpCategoryValue>> dependantModel = null;
        AmpCategoryClass categClass = CategoryManagerUtil.loadAmpCategoryClassByKey(CategoryConstants.EXPENDITURE_CLASS_KEY);

        //this can be skipped for this particular field
        for (AmpCategoryValue val: categClass.getPossibleValues()) {
//          if (val.equ){
                if (val.getUsedByValues() != null && val.getUsedByValues().size() > 0){
                    HashSet<AmpCategoryValue> tmp = new HashSet<AmpCategoryValue>();
                    tmp.add(val);
                    dependantModel = new Model(tmp);
                }
                break;
//          }
        }
        
        try{
            AmpCategorySelectFieldPanel expenditureClasses = new AmpCategorySelectFieldPanel(
                "expenditureClass", CategoryConstants.EXPENDITURE_CLASS_KEY,
                        new PropertyModel<AmpCategoryValue>(model,"expenditureClass"),
                        CategoryConstants.EXPENDITURE_CLASS_NAME, //fmname
                         false, false, false, dependantModel, false);
//          expenditureClasses.getChoiceContainer().setRequired(true);
            return expenditureClasses;
        }catch(Exception e)
        {
            logger.error("AmpCategorySelectFieldPanel initialization failed");
        }
        return null;

        
    }

    
    
    
    /**
     * @param id
     * @param model
     * @param fmName
     * @param transactionType 
     * @throws Exception
     */
    public AmpDonorExpendituresFormTableFeature(String id,
            final IModel<AmpFunding> model, String fmName, final int transactionType) throws Exception {
        super(id, model, fmName, Constants.EXPENDITURE, 6);
        
        list = new FundingListEditor<AmpFundingDetail>("listExp", setModel, FundingDetailComparator
                .getFundingDetailComparator()) {
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpFundingDetail> item) {
                super.onPopulateItem(item);
                item.add(getAdjustmentTypeComponent(item.getModel(), transactionType));
                
                final AmpCategorySelectFieldPanel expenditureClass = getExpenditureClassTypeComponent(item.getModel());
                expenditureClass.setAffectedByFreezing(false);
                item.add(new AmpComponentPanel("expenditureClassRequired", "Required Validator for Expenditure Class") {
                    @Override
                    protected void onConfigure() {
                        super.onConfigure();
                        if (this.isVisible()){
                            expenditureClass.getChoiceContainer().setRequired(true);//getTextAreaContainer().setRequired(true);
//                          requiredRichTextFormComponents.add(description.getTextAreaContainer());
                        }
                    }
                });     
                 
                item.add(expenditureClass);
                final AmpFundingAmountComponent amountComponent = getFundingAmountComponent(item.getModel());
                item.add(amountComponent);
                addFreezingvalidator(item);
                item.add(UpdateEventBehavior.of(FreezingUpdateEvent.class));
                AmpTextFieldPanel<String> classification = new AmpTextFieldPanel<String>(
                        "classification", new PropertyModel<String>(
                                item.getModel(), "expCategory"),
                        "Expenditure Classification", false, false);

                classification.getTextContainer().add(new AttributeModifier("size", new Model<String>("12")));
                classification.setTextContainerDefaultMaxSize();
                classification.setAffectedByFreezing(false);
                item.add(classification);
                item.add(new ListEditorRemoveButton("delExp", "Delete Expenditure"){
                    protected void onClick(final org.apache.wicket.ajax.AjaxRequestTarget target) {
                        AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                        super.onClick(target);
                        parent.getFundingInfo().configureRequiredFields();
                        target.add(parent.getFundingInfo());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                        target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                    };
                });
            }
        };
        add(list);
        addExpandableList();
    }

}
