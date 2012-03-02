/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.ListEditor;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoModel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoRenderer;
import org.dgfoundation.amp.onepager.models.AmpTrTypeLocationRegionalFundingDetailModel;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author mpostelnicu@dgateway.org since Nov 12, 2010
 */
public abstract class AmpRegionalFormTableFeaturePanel extends
		AmpFormTableFeaturePanel<Set<AmpRegionalFunding>, AmpRegionalFunding> {
	
	protected ListEditor<AmpRegionalFunding> list;

	protected IModel<Set<AmpRegionalFunding>> setModel;

	public AmpRegionalFormTableFeaturePanel(String id,
			final IModel<Set<AmpRegionalFunding>> model, String fmName, int transactionType,
			int titleHeaderColSpan, IModel<AmpCategoryValueLocations> cvLocationModel) throws Exception {
		super(id, model, fmName);

		setTitleHeaderColSpan(titleHeaderColSpan);
		
		setModel = new AmpTrTypeLocationRegionalFundingDetailModel(model,
				transactionType,cvLocationModel);
	}

	protected AmpGroupFieldPanel<MetaInfo<Integer>> getAdjustmentTypeComponent(
			IModel<AmpRegionalFunding> model) {
		Collection<AmpCategoryValue> funding_type_values= CategoryManagerUtil.getAmpCategoryValueCollectionByKey("funding_type");
		ArrayList<MetaInfo<Integer>> metaInfoList = new ArrayList<MetaInfo<Integer>>();
		for(AmpCategoryValue categoryValue: funding_type_values)
		{
			metaInfoList.add( new MetaInfo<Integer>(categoryValue.getValue(), categoryValue.getIndex()));
		}
		MetaInfo<Integer>[] metainfoArray = metaInfoList.toArray(new MetaInfo[]{}); 
		
		return new AmpGroupFieldPanel<MetaInfo<Integer>>("adjustmentType",
				new AmpMetaInfoModel<Integer>(new PropertyModel<Integer>(model,
						"adjustmentType"), metainfoArray),metaInfoList,
				CategoryConstants.ADJUSTMENT_TYPE_NAME, true, false,
				new AmpMetaInfoRenderer<Integer>());
	}

	protected AmpFundingAmountComponent getFundingAmountComponent(
			IModel<AmpRegionalFunding> model) {
		return new AmpFundingAmountComponent<AmpRegionalFunding>("fundingAmount",
				model, "Amount", "transactionAmount", "Currency",
				"currency", "Transaction Date", "transactionDate");
	}

	protected AmpDeleteLinkField getDeleteLinkField(String id, String fmName,
			final ListItem<AmpRegionalFunding> item) {
		return new AmpDeleteLinkField(id, fmName) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				model.getObject().remove(item.getModelObject());
				target.addComponent(AmpRegionalFormTableFeaturePanel.this);
				list.removeAll();
			}
		};
	}
	
	public ListEditor<AmpRegionalFunding> getListEditor() {
		return list;
	}
}
