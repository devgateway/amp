/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.AmpTableFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 25, 2010
 */
public class AmpComponentsFundingFormTableFeature extends
		AmpFormTableFeaturePanel {
	/**
	 */
	public AmpComponentsFundingFormTableFeature(String id,
			final IModel<AmpComponent> componentModel,
			final IModel<Set<AmpComponentFunding>> compFundsModel, 
			final IModel<AmpActivityVersion> activityModel, String fmName,
			final int transactionType) throws Exception {
		super(id, activityModel, fmName);
		setTitleHeaderColSpan(5);

		AbstractReadOnlyModel<List<AmpComponentFunding>> listModel = new AbstractReadOnlyModel<List<AmpComponentFunding>>() {
			private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpComponentFunding> getObject() {
				List<AmpComponentFunding> result = new ArrayList<AmpComponentFunding>();
				Set<AmpComponentFunding> allComp = compFundsModel.getObject();
				if (allComp != null){
					Iterator<AmpComponentFunding> iterator = allComp.iterator();
					while (iterator.hasNext()) {
						AmpComponentFunding comp = (AmpComponentFunding) iterator
						.next();
						if (comp.getTransactionType() == transactionType)
							if (comp.getComponent().hashCode() == componentModel.getObject().hashCode())
								result.add(comp);
					}
				}
				
				return result;
			}
		};

		list = new ListView<AmpComponentFunding>("list", listModel) {

			@Override
			protected void populateItem(final ListItem<AmpComponentFunding> item) {
				IModel<AmpComponentFunding> model = item.getModel();
                try{
                    AmpCategorySelectFieldPanel adjustmentTypes = new AmpCategorySelectFieldPanel(
                            "adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_KEY,
                            new PropertyModel<AmpCategoryValue>(model,"adjustmentType"),
                            CategoryConstants.ADJUSTMENT_TYPE_NAME, //fmname
                            false, false, true, null, true);
                    adjustmentTypes.getChoiceContainer().setRequired(true);
                    item.add(adjustmentTypes);

                } catch(Exception e)
                {
                    logger.error("AmpCategoryGroupFieldPanel initialization failed");
                }
                AmpFundingAmountComponent amountComponent = new AmpTableFundingAmountComponent<AmpComponentFunding>("fundingAmount",
                        model, "Amount", "transactionAmount", "Currency",
                        "currency", "Transaction Date", "transactionDate", false);
				item.add(amountComponent);
				 
				item.add(new AmpDeleteLinkField("delete", "Delete") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						compFundsModel.getObject().remove(item.getModelObject());
						target.add(AmpComponentsFundingFormTableFeature.this);
						list.removeAll();
					}
				});
			}
		};
		list.setReuseItems(true);
		add(list);

	}
	
	
	private AbstractReadOnlyModel<List<AmpComponentFunding>> getSubsetModel( final IModel<Set<AmpComponentFunding>> compFundsModel , final int transactionType)
	{
		
		
		return new AbstractReadOnlyModel<List<AmpComponentFunding>>() {
			private static final long serialVersionUID = 370618487459839210L;

			@Override
			public List<AmpComponentFunding> getObject() {
				List<AmpComponentFunding> result = new ArrayList<AmpComponentFunding>();
				Set<AmpComponentFunding> allComp = compFundsModel.getObject();
				if (allComp != null){
					Iterator<AmpComponentFunding> iterator = allComp.iterator();
					while (iterator.hasNext()) {
						AmpComponentFunding comp = (AmpComponentFunding) iterator
						.next();
						if (comp.getTransactionType() == transactionType)
							//if (comp.getComponent().hashCode() == componentModel.getObject().hashCode())
								result.add(comp);
					}
				}
				
				return result;
			}
		};
	}

}
