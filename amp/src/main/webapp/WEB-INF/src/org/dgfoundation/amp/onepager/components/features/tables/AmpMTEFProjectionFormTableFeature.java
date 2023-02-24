/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.FundingListEditor;
import org.dgfoundation.amp.onepager.components.ListEditorRemoveButton;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * @author mpostelnicu@dgateway.org since Nov 5, 2010
 */
public class AmpMTEFProjectionFormTableFeature extends
        AmpFundingFormTableFeaturePanel<AmpFunding,AmpFundingMTEFProjection> {

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpMTEFProjectionFormTableFeature(String id, String fmName,
            final IModel<AmpFunding> model) throws Exception {
        super(id, model, fmName);
        
        getTableId().add(new AttributeModifier("width", "620"));
        
        final IModel<Set<AmpFundingMTEFProjection>> setModel = new PropertyModel<Set<AmpFundingMTEFProjection>>(
                model, "mtefProjections");
        if (setModel.getObject() == null)
            setModel.setObject(new TreeSet<AmpFundingMTEFProjection>());
        setTitleHeaderColSpan(5);
        list = new FundingListEditor<AmpFundingMTEFProjection>("listMTEF",
                setModel, new AmpFundingMTEFProjection.FundingMTEFProjectionComparator()) {
            @Override
            protected void onPopulateItem(
                    org.dgfoundation.amp.onepager.components.ListItem<AmpFundingMTEFProjection> item) {
            super.onPopulateItem(item);
                AmpCategorySelectFieldPanel projection;
                try {
                    projection = new AmpCategorySelectFieldPanel("projection",
                            CategoryConstants.MTEF_PROJECTION_KEY,
                            new PropertyModel<AmpCategoryValue>(
                                    item.getModel(), "projection"),
                            CategoryConstants.MTEF_PROJECTION_NAME, true,
                            false, false, null, false);
                    projection.getChoiceContainer().setRequired(true);
                    item.add(projection);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                AmpFundingAmountComponent<AmpFundingMTEFProjection> fundingAmount    = new AmpFundingAmountComponent<AmpFundingMTEFProjection>(
                        "fundingAmount", item.getModel(), "Amount", "displayedAmount",
                        "Currency", "ampCurrency", "Projection Date", "projectionDate", true);
                item.add(fundingAmount);

                item.add(new ListEditorRemoveButton("delMtef", "Delete MTEF Projection"){
                    protected void onClick(org.apache.wicket.ajax.AjaxRequestTarget target) {
                        AmpFundingItemFeaturePanel parent = this.findParent(AmpFundingItemFeaturePanel.class);
                        super.onClick(target);
                        parent.getFundingInfo().configureRequiredFields();
                        target.add(parent.getFundingInfo());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(parent.getFundingInfo()));
                        target.appendJavaScript(OnePagerUtil.getClickToggleJS(parent.getFundingInfo().getSlider()));
                    };
                });
                //we create the role selector for recipient organization for commitments
                item.add(OnePagerUtil.getFundingFlowRoleSelector(model, item.getModel()));
            }
        };
        add(list);
    }

}
