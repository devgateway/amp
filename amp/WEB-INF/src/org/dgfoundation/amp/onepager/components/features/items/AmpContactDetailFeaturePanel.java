/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

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
		super(id, model, fmName, hideLabel);
		
		final IModel<Set<AmpContactProperty>> setModel=new PropertyModel<Set<AmpContactProperty>>(model,"properties");
		
		//final IModel<AmpContact> ampContact = new Model(model);
		final IModel<List<AmpContactProperty>> listModel = new AbstractReadOnlyModel<List<AmpContactProperty>>() {
			@Override
			public List<AmpContactProperty> getObject() {
				List<AmpContactProperty> specificContacts = new ArrayList<AmpContactProperty>();
				if(setModel.getObject()!=null){
					for (AmpContactProperty detail : setModel.getObject()) {
						if(detail.getName().equals(contactProperty)){
							if(detail.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)&&detail.getActualValue()==null){
								detail.setActualValue(detail.getActualPhoneNumber());
								detail.setCategoryValue(ContactInfoUtil.getPhoneCategoryValue(detail.getValue()));
							}
							specificContacts.add(detail);
						}
					}
				}	
				return specificContacts;
			}

		};
		final WebMarkupContainer resultcontainer = new   WebMarkupContainer("resultcontainer");
		final ListView<AmpContactProperty> detailsList = new ListView<AmpContactProperty>("detailsList", listModel) {

                @Override
                protected void populateItem(final ListItem<AmpContactProperty> item) {
                    AmpContactProperty property = item.getModelObject();
                    final AmpDeleteLinkField propertyDeleteLink = new AmpDeleteLinkField("removeContact", "Remove Contact Link",new Model<String>( "Are you sure you want to delete this?")) {

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            setModel.getObject().remove(item.getModelObject());
                            target.addComponent(resultcontainer);
                        }
                        

                    };
                    if (!property.getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)) {
                        IModel<String> value = new PropertyModel<String>(property, "value");
                        Fragment frg1 = new Fragment("detailPanel", "frag1",this);
                        AmpTextFieldPanel<String> detailField=new AmpTextFieldPanel<String>("detail", value, fmName, true);
                        if(property.getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
                        	TextField<String> detailTextField=detailField.getTextContainer();
                        	detailTextField.setRequired(true);
                        	detailTextField.add(EmailAddressValidator.getInstance());
                        }
                        else{
                        	TextField<String> detailTextField=detailField.getTextContainer();
                        	String expression = "^\\+?\\s?\\d+[\\s\\d]*";
                        	detailTextField.add(new PatternValidator(expression));
                        	detailTextField.setRequired(true);
                        }
                        frg1.add(detailField);
                        frg1.add(propertyDeleteLink);
                        item.add(frg1);
                    } else {
                        try {
                            IModel<String> valueModel = new PropertyModel(property, "actualValue");
                            IModel<AmpCategoryValue> catValueModel = new PropertyModel(property, "categoryValue");
                            Fragment frg2 = new Fragment("detailPanel", "frag2",this);
                            AmpTextFieldPanel<String> phn = new AmpTextFieldPanel<String>("phone", valueModel, fmName, true);
                            TextField<String> detailTextField=phn.getTextContainer();
                        	detailTextField.setRequired(true);
                        	String expression = "^\\+?\\s?\\d+[\\s\\d]*";
                        	detailTextField.add(new PatternValidator(expression));
                            AmpCategorySelectFieldPanel phoneTitle = new AmpCategorySelectFieldPanel("categoryValue", CategoryConstants.CONTACT_PHONE_TYPE_KEY, catValueModel, CategoryConstants.CONTACT_PHONE_TYPE_NAME, true, true, true);
                            phoneTitle.getChoiceContainer().setRequired(true);
                            frg2.add(phoneTitle);
                            frg2.add(phn);
                            frg2.add(propertyDeleteLink);
                            item.add(frg2);
                        } //item.add(new Label("detail", item.getModelObject().getValue()));
                        catch (Exception ex) {
                            
                        }

                    }
                    

                    //item.add(new Label("detail", item.getModelObject().getValue()));
                }
            };
		//detailsList.setReuseItems(true);
                
		resultcontainer.add(detailsList);
                resultcontainer.setOutputMarkupId(true);
                add(resultcontainer);
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
                                if(contactProperties==null){
                                    contactProperties=new HashSet<AmpContactProperty>();
                                    setModel.setObject(contactProperties);
                                }
				contactProperties.add(fakeContact1);
				//detailsList.removeAll();
                                target.addComponent(resultcontainer);
				

			}

            

		};
		add(addLink);
		
		
		
	}

}
