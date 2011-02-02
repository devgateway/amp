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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpContactSearchModel;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpContact;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactsSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpActivity> implements IHeaderContributor{
	
	private static final long serialVersionUID = -2114204838953838609L;
	protected ListView<AmpActivityContact> idsList;
	private List<TransparentWebMarkupContainer> sliders;
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
					AmpActivityContact ampActContact = (AmpActivityContact) it.next();
					if (specificType.compareTo(ampActContact.getContactType()) == 0)
						specificContacts.add(ampActContact);
				}
				return new ArrayList<AmpActivityContact>(specificContacts);
			}
		};
		
		sliders = new ArrayList<TransparentWebMarkupContainer>();

		idsList = new ListView<AmpActivityContact>("contactsList", listModel) {
			
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityContact> item) {
				
				item.add(new Label("contactName", item.getModelObject().getContact().getNameAndLastName()));
				
				item.add(new AmpDeleteLinkField("removeContact", "Remove Contact Link") {

					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(AmpContactsSubsectionFeaturePanel.this);
						target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
						idsList.removeAll();
					}
				});
				AmpContactDetailsSubsectionFeaturePanel contactDetails = null;
				try {
					contactDetails = new AmpContactDetailsSubsectionFeaturePanel("contactDetails", "Contact Details", item.getModel());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contactDetails.setOutputMarkupId(true);
				sliders.add(contactDetails.getSlider());
				
				item.add(contactDetails);
			}
		};
		idsList.setReuseItems(true);
		add(idsList);
		
		//search contact
		
		final AbstractAmpAutoCompleteTextField<AmpContact> autoComplete = new AbstractAmpAutoCompleteTextField<AmpContact>(AmpContactSearchModel.class) {

			@Override
			protected String getChoiceValue(AmpContact choice) throws Throwable {
				return choice.getNameAndLastName();
			}
			
			@Override
			public void onSelect(AjaxRequestTarget target, AmpContact choice) {
				AmpActivityContact aaContact = new AmpActivityContact();
				aaContact.setActivity(am.getObject());
				aaContact.setContact(choice);
				aaContact.setContactType(contactType);
				Set<AmpActivityContact> set = setModel.getObject();
				set.add(aaContact);
				idsList.removeAll();
				target.addComponent(AmpContactsSubsectionFeaturePanel.this);
				target.appendJavascript(OnePagerConst.getToggleJS(AmpContactsSubsectionFeaturePanel.this.getSlider()));
			}

			@Override
			public Integer getChoiceLevel(AmpContact choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(25));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpContact> searchContacts=new AmpComboboxFieldPanel<AmpContact>("searchContacts", "Search Contacts", autoComplete);
		add(searchContacts);
		
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		 for (TransparentWebMarkupContainer c: sliders) {
			response.renderOnDomReadyJavascript(OnePagerConst.getToggleJS(c));	
			System.out.println("-------"+OnePagerConst.getToggleJS(c));
		} ;
		 
	}
}
