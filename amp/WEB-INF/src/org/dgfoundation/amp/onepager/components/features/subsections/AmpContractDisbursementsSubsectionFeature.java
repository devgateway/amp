/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpFundingAmountComponent;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpDonorFormTableFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAjaxLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDatePickerFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpSelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoModel;
import org.dgfoundation.amp.onepager.models.AmpMetaInfoRenderer;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.IPAContractDisbursement;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import edu.emory.mathcs.backport.java.util.Arrays;

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
				AmpGroupFieldPanel<MetaInfo<Integer>> adjType = new AmpGroupFieldPanel<MetaInfo<Integer>>("adjustmentType",
						new AmpMetaInfoModel<Integer>(new PropertyModel<Integer>(model, "adjustmentType"), OnePagerConst.adjustmentTypes),
						Arrays.asList(OnePagerConst.adjustmentTypes),
						"Adjustment Type", true, false, new AmpMetaInfoRenderer<Integer>());
				item.add(adjType);
				
				AmpFundingAmountComponent<IPAContractDisbursement> funding = new AmpFundingAmountComponent<IPAContractDisbursement>("fundingAmount",
						model, "Amount", "amount", "Currency",
						"currency", "Transaction Date", "date");
				item.add(funding);
					
				AmpDeleteLinkField delete = new AmpDeleteLinkField("delete", "Delete Contract Disbursement") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						disbModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpContractDisbursementsSubsectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
						target.appendJavascript(OnePagerConst.getClickToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
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
				disbModel.getObject().add(comp);
				target.addComponent(this.getParent());
				target.appendJavascript(OnePagerConst.getToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
				target.appendJavascript(OnePagerConst.getClickToggleJS(AmpContractDisbursementsSubsectionFeature.this.getSlider()));
			}
		};
		add(addbutton);
	}

}
