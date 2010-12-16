/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;

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
				Set<AmpActivityContact> specificContacts = new TreeSet<AmpActivityContact>();  
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
				AmpContactDetailsSubsectionFeaturePanel contactDetails = null;
				try {
					contactDetails = new AmpContactDetailsSubsectionFeaturePanel("contactDetails", "Contact Details", item.getModel());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				item.add(contactDetails);
			}
		};
		list.setReuseItems(true);
		add(list);
		
		
	}

}
