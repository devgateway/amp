/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;

/**
 * @author dan
 *
 */
public class AmpContactDetailFeaturePanel extends AmpFeaturePanel<AmpContact> {

	/**
	 * @param id
	 * @param fmName
	 * @throws Exception
	 */
	
	protected ListView<AmpContactProperty> detailsList;
	
	public AmpContactDetailFeaturePanel(String id, String fmName)
			throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpContactDetailFeaturePanel(String id, IModel<AmpContact> model, String fmName)
			throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLabel
	 * @throws Exception
	 */
	public AmpContactDetailFeaturePanel(String id, IModel<AmpContact> model, String fmName, boolean hideLabel) throws Exception {
		super(id, model, fmName, hideLabel);
		// TODO Auto-generated constructor stub
		
	}

	public AmpContactDetailFeaturePanel(String id,final IModel<AmpContact> model,final String fmName, boolean hideLabel,final String contactProperty) throws Exception {
		// TODO Auto-generated constructor stub
		super(id, model, fmName, hideLabel);
		
		final IModel<Set<AmpContactProperty>> setModel=new PropertyModel<Set<AmpContactProperty>>(model,"properties");
		
		//final IModel<AmpContact> ampContact = new Model(model);
 		final IModel<List<AmpContactProperty>> listModel = new AbstractReadOnlyModel<List<AmpContactProperty>>() {
		
						@Override
						public List<AmpContactProperty> getObject() {
							List<AmpContactProperty> specificContacts = new ArrayList<AmpContactProperty>();  
							Set<AmpContactProperty> contactProperties= new TreeSet<AmpContactProperty>(setModel.getObject());
							if(contactProperties!=null){
								for (AmpContactProperty detail : contactProperties) {
									if(detail.getName().equals(contactProperty)){
										specificContacts.add(detail);
									}
								}
							}	
							if(specificContacts.size() < 1){
								AmpContactProperty fakeContact = new AmpContactProperty();
								fakeContact.setContact(model.getObject());
								fakeContact.setName(contactProperty);
								fakeContact.setValue("");
								specificContacts.add(fakeContact);
							}
							return new ArrayList<AmpContactProperty>(specificContacts);
						}
		};
		detailsList = new ListView<AmpContactProperty>("detailsList", listModel) {
		
					@Override
					protected void populateItem(final ListItem<AmpContactProperty> item) {
						IModel<String> value = new PropertyModel<String>(item.getModelObject(), "value");
						item.add(new AmpTextFieldPanel<String>("detail", value,fmName, true));
						//item.add(new Label("detail", item.getModelObject().getValue()));
					}
				};
		detailsList.setReuseItems(true);
		add(detailsList);
		
		AmpAddLinkField addLink = new AmpAddLinkField("addDetailButton","Add Detail Button") {
		
			@Override
			protected void onClick(AjaxRequestTarget target) {
				if(detailsList.getModelObject().size() >= 3) 
					return;
				AmpContactProperty fakeContact1 = new AmpContactProperty();
				fakeContact1.setContact(model.getObject());
				fakeContact1.setName(contactProperty);
				fakeContact1.setValue("");
//				contactProperties.clear();
//				contactProperties.addAll(detailsList.getModelObject());
				Set<AmpContactProperty> contactProperties=setModel.getObject();
				contactProperties.add(fakeContact1);
				detailsList.removeAll();
				target.addComponent(this.getParent());//.getParent());
			}
		};
		add(addLink);
		
		
		
	}

}
