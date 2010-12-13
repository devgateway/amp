/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpSubsectionFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactsSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpActivity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2114204838953838609L;
	protected ListView<AmpActivityContact> list;

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpContactsSubsectionFeaturePanel(String id, String fmName, final IModel<AmpActivity> am, final String contactType) throws Exception {
		//super(id, contactModel, fmName, true);
		super(id,fmName, am);
		final IModel<Set<AmpActivityContact>> setModel=new PropertyModel<Set<AmpActivityContact>>(am,"activityContacts");
		final String specificType = contactType;
		
		final IModel<List<AmpActivityContact>> listModel = new AbstractReadOnlyModel<List<AmpActivityContact>>() {
		private static final long serialVersionUID = 3706184421459839210L;

			@Override
			public List<AmpActivityContact> getObject() {
				Set<AmpActivityContact> allContacts = setModel.getObject();
				Set<AmpActivityContact> specificContacts = new HashSet<AmpActivityContact>();  
				Iterator<AmpActivityContact> it = allContacts.iterator();
				while (it.hasNext()) {
					AmpActivityContact ampContact = (AmpActivityContact) it.next();
					if (specificType.compareTo(ampContact.getContactType()) == 0)
						specificContacts.add(ampContact);
				}
				return new ArrayList<AmpActivityContact>(specificContacts);
			}
		};
		

		list = new ListView<AmpActivityContact>("contactsList", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityContact> item) {
				final MarkupContainer listParent=this.getParent().getParent();
				item.add(new Label("contactName", item.getModelObject().getContact().getName() + item.getModelObject().getContact().getLastname()));
				item.add(new AmpDeleteLinkField("removeContact", "Remove Contact Link") {
				private static final long serialVersionUID = 3350682075371304996L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpContactsSubsectionFeaturePanel.this);
						target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
						list.removeAll();
					}
				});
			}
		};
		list.setReuseItems(true);
		add(list);	
	}

}

//item.add(new TextField<String>("firstName", new PropertyModel<String>(item.getModel(), "additionalInfo")));
//item.add(new Label("firstName", item.getModelObject().getContact().getName()));		
//item.add(new Label("lastName", item.getModelObject().getContact().getLastname()));
////item.add(new Label("email", item.getModelObject().getContact().getProperties()));
//item.add(new Label("email", "email1"));
//item.add(new Label("organization", item.getModelObject().getContact().getOrganisationName()));
//item.add(new Label("phone", "phone1"));
//item.add(new Label("primary", item.getModelObject().getPrimaryContact()==true?"yes":"no"));
//item.add(new Label("actions", "edit/delete"));

//label("name", item.getModelObject().getOrganisation().getAcronymAndName()));			

//item.add(getDeleteLinkField(id, fmName,item,listModel));
