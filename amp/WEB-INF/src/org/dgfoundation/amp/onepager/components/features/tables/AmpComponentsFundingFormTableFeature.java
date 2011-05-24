/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoModel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoRenderer;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author aartimon@dginternational.org 
 * @since Nov 25, 2010
 */
public class AmpComponentsFundingFormTableFeature extends
		AmpFormTableFeaturePanel {

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
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
							if (/*comp.getComponent().equals(componentModel.getObject())*/ comp.getComponent().hashCode() == componentModel.getObject().hashCode())
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
				item.add(new AmpGroupFieldPanel<MetaInfo<Integer>>("adjustmentType",
						new AmpMetaInfoModel<Integer>(new PropertyModel<Integer>(model,
						"adjustmentType"), OnePagerConst.adjustmentTypesShort),
						Arrays.asList(OnePagerConst.adjustmentTypesShort),
						"Adjustment Type", true, false,
						new AmpMetaInfoRenderer<Integer>()));
				
				item.add(new AmpFundingAmountComponent<AmpComponentFunding>("fundingAmount",
						model, "Amount", "transactionAmount", "Currency",
						"currency", "Transaction Date", "transactionDate"));
				 
				item.add(new AmpDeleteLinkField("delete", "Delete") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						compFundsModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpComponentsFundingFormTableFeature.this);
						list.removeAll();
					}
					});
			}
		};
		list.setReuseItems(true);
		add(list);

	}

}
