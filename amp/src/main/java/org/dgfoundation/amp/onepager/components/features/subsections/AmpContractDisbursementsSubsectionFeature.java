/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.AmpTableFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractDisbursementsSubsectionFeature extends
        AmpSubsectionFeaturePanel<IPAContract> {

    public AmpContractDisbursementsSubsectionFeature(String id,
            final IModel<IPAContract> model, String fmName){
        super(id, fmName, model);

        final PropertyModel<Set<IPAContractDisbursement>> disbModel = new PropertyModel<Set<IPAContractDisbursement>>(model, "disbursements");
        
        if (disbModel.getObject() == null)
            disbModel.setObject(new HashSet<IPAContractDisbursement>());
        
        AbstractReadOnlyModel<List<IPAContractDisbursement>> listModel = OnePagerUtil
                        .getReadOnlyListModelFromSetModel(disbModel);

        ListView<IPAContractDisbursement> list = new ListView<IPAContractDisbursement>("list", listModel) {
        
            @Override
            protected void populateItem(final ListItem<IPAContractDisbursement> item) {
                IModel<IPAContractDisbursement> model = item.getModel();
                
                AmpCategorySelectFieldPanel adjTypes = null;
                try {

                    adjTypes = new AmpCategorySelectFieldPanel(
                            "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
                            new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
                            CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
                            false, false, true);
                    
                } catch (Exception e) {
                    logger.error("Can't init categorty value component adjustment types:", e);
                }
                adjTypes.getChoiceContainer().setRequired(true);
                item.add(adjTypes);
                
                AmpFundingAmountComponent<IPAContractDisbursement> funding = new AmpTableFundingAmountComponent<IPAContractDisbursement>("fundingAmount",
                        model, "Amount", "amount", "Currency",
                        "currency", "Transaction Date", "date", false);
                item.add(funding);
                    
                AmpDeleteLinkField delete = new AmpDeleteLinkField("delete", "Delete Contract Disbursement") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        disbModel.getObject().remove(item.getModelObject());
                        target.add(AmpContractDisbursementsSubsectionFeature.this);
                        target.appendJavaScript(OnePagerUtil.getToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
                        target.appendJavaScript(OnePagerUtil.getClickToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
                    }
                };
                item.add(delete);
            }
        };
        list.setReuseItems(true);
        add(list);

        AmpAjaxLinkField addbutton = new AmpAjaxLinkField("add", "Add Disbursement", "Add Disbursement") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                IPAContractDisbursement comp = new IPAContractDisbursement();
                //comp.setContract(model.getObject());
                comp.setCurrency(CurrencyUtil.getWicketWorkspaceCurrency());
                disbModel.getObject().add(comp);
                target.add(this.getParent());
                target.appendJavaScript(OnePagerUtil.getToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
            }
        };
        add(addbutton);
    }

}
