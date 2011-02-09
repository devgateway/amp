/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpContractsItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpFundingItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpContractOrganizationsSubsectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetExtrasPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpActivityBudgetField;
import org.dgfoundation.amp.onepager.components.fields.AmpBudgetClassificationField;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategoryGroupFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTab;
import org.dgfoundation.amp.onepager.components.fields.AmpCommentTabsFieldWrapper;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpBooleanChoiceField;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

import com.rc.retroweaver.runtime.Arrays;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.visural.wicket.component.dropdown.DropDown;

/**
 * Contracting section
 * @author aartimon@dginternational.org 
 * @since Feb 7, 2011
 */
public class AmpContractingFormSectionFeature extends AmpFormSectionFeaturePanel {

	public AmpContractingFormSectionFeature(String id, String fmName,
			final IModel<AmpActivity> am) throws Exception {
		super(id, fmName, am);
		final IModel<Set<IPAContract>> setModel = new PropertyModel<Set<IPAContract>>(am, "contracts");
		
		if (setModel.getObject() == null){
			setModel.setObject(new HashSet<IPAContract>());
		}
		
		AbstractReadOnlyModel<List<IPAContract>> listModel = OnePagerUtil 
				.getReadOnlyListModelFromSetModel(setModel);
		
		ListView<IPAContract> list = new ListView<IPAContract>("list", listModel) {
			@Override
			protected void populateItem(final ListItem<IPAContract> item) {
				AmpContractsItemFeaturePanel contractItem = new AmpContractsItemFeaturePanel(
							"item", "Contract Item", item.getModel());
				item.add(contractItem);
				
				AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
						"delete", "Delete Contract Item", new Model<String>("Do you really want to delete this contract?")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpContractingFormSectionFeature.this);
						target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpContractingFormSectionFeature.this));
						//list.removeAll();
					}
				};
				item.add(deleteLinkField);

			}
		};
		
		list.setReuseItems(true);
		add(list);
		

		AmpButtonField addbutton = new AmpButtonField("add", "Add Contract") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				IPAContract comp = new IPAContract();
				setModel.getObject().add(comp);
				target.addComponent(this.getParent());
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpContractingFormSectionFeature.this));
			}
		};
		add(addbutton);
	}

}
