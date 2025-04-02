/**
 * 
 */
package org.dgfoundation.amp.onepager.components.features.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpAddLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.ContactChangedEvent;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.validators.ContactEmailValidator;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

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
    
    private WebMarkupContainer detailFeedbackContainer;
    private Label detailFeedbackLabel;
    
//  private WebMarkupContainer phonesFeedbackContainer;
//  private Label pronesFeedbackLabel;
//  
//  private WebMarkupContainer faxesFeedbackContainer;
//  private Label faxesFeedbackLabel;
    
    public AmpContactDetailFeaturePanel(String id,final IModel<AmpContact> model,final String fmName, boolean hideLabel,final String contactProperty) throws Exception {
          
        super(id, model, fmName, hideLabel);
       final IModel<Set<AmpContactProperty>> setModel=new PropertyModel<Set<AmpContactProperty>>(model,"properties");
        if (setModel.getObject() == null)
            setModel.setObject(new TreeSet<AmpContactProperty>());
        
        //final IModel<AmpContact> ampContact = new Model(model);
        final IModel<List<AmpContactProperty>> listModel = new AbstractReadOnlyModel<List<AmpContactProperty>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpContactProperty> getObject() {
                List<AmpContactProperty> specificContacts = new ArrayList<AmpContactProperty>();
                if(setModel.getObject()!=null){
                    for (AmpContactProperty detail : setModel.getObject()) {
                        if(detail.getName().equals(contactProperty)){
                            specificContacts.add(detail);
                        }
                    }
                }   
                return specificContacts;
            }

        };
        final WebMarkupContainer resultcontainer = new   WebMarkupContainer("resultcontainer");
        final ListView<AmpContactProperty> detailsList = new ListView<AmpContactProperty>("detailsList", listModel) {

            private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(final ListItem<AmpContactProperty> item) {
                    //AmpContactProperty property = item.getModelObject();
                    String translatedMessage = TranslatorUtil.getTranslatedText("Are you sure you want to delete this?");
                    AmpDeleteLinkField propertyDeleteLink = new AmpDeleteLinkField("removeContact", "Remove Contact Link",new Model<String>(translatedMessage)) {

                        /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            Set<AmpContactProperty> properties=setModel.getObject();
                            AmpContactProperty property=item.getModelObject();
                            boolean succesfuldelete=false;
                            Iterator<AmpContactProperty> iter=properties.iterator();
                            while(iter.hasNext()){
                                AmpContactProperty pr=iter.next();
                                if(pr.getName().equals(property.getName())&&pr.getValue().equals(property.getValue())){
                                    iter.remove();
                                    succesfuldelete=true;
                                    break;
                                }
                            }
                            if(succesfuldelete){
                                detailFeedbackContainer.setVisible(false);
                                target.add(detailFeedbackContainer);
                            }                            
                            send(getPage(), Broadcast.BREADTH,
                                    new ContactChangedEvent(target));
                            target.add(resultcontainer);
                        }
                    };
                    
                    if (!item.getModelObject().getName().equals(Constants.CONTACT_PROPERTY_NAME_PHONE)) {
                        IModel<String> value = new PropertyModel<String>(item.getModel(), "value");
                        Fragment frg1 = new Fragment("detailPanel", "frag1",this);
                        final AmpTextFieldPanel<String> detailField=new AmpTextFieldPanel<String>("detail", value, fmName, true,true);
                        detailField.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                            @Override
                            protected void onUpdate(AjaxRequestTarget target) {
                                send(getPage(), Broadcast.BREADTH,
                                        new ContactChangedEvent(target));
                            }
                        });
                  
                        if(item.getModelObject().getName().equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)){
                            TextField<String> detailTextField=detailField.getTextContainer();
                            detailTextField.add(new AttributeModifier("size", "20"));
                            detailTextField.setRequired(true);
                            ContactEmailValidator validator=new ContactEmailValidator(model.getObject().getId());
                            detailTextField.add(validator);
                        }
                        else{
                            TextField<String> detailTextField=detailField.getTextContainer();
                            detailTextField.add(new AttributeModifier("size", "20"));
                            detailTextField.add(new PatternValidator(ActivityEPConstants.REGEX_PATTERN_PHONE));
                            detailTextField.setRequired(true);
                        }
                        frg1.add(detailField);
                        frg1.add(propertyDeleteLink);
                        item.add(frg1);
                    } else {
                        try {
                            IModel<String> valueModel = new PropertyModel<>(item.getModel(), "value");
                            IModel<String> extensionValueModel = new PropertyModel<String>(item.getModel(),"extensionValue");
                            IModel<AmpCategoryValue> typeModel = new PropertyModel<>(item.getModel(), "type");
                            Fragment frg2 = new Fragment("detailPanel", "frag2",this);
                            final AmpTextFieldPanel<String> phn = new AmpTextFieldPanel<String>("phone", valueModel, fmName, true,true);
                            phn.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                                @Override
                                protected void onUpdate(AjaxRequestTarget target) {
                                    send(getPage(), Broadcast.BREADTH,
                                            new ContactChangedEvent(target));
                                }
                            });
                            
                      
                            TextField<String> detailTextField=phn.getTextContainer();
                            detailTextField.setRequired(true);
                            detailTextField.add(new PatternValidator(ActivityEPConstants.REGEX_PATTERN_PHONE));
                            detailTextField.add(new AttributeModifier("size", "20"));
                            final AmpTextFieldPanel<String> phnExt = new AmpTextFieldPanel<String>("phoneExt", extensionValueModel, fmName, true,true);
                            phnExt.getTextContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                                @Override
                                protected void onUpdate(AjaxRequestTarget target) {
                                    send(getPage(), Broadcast.BREADTH,
                                            new ContactChangedEvent(target));
                                    
                                }
                            });
                      
                            TextField<String> detailTextFieldExt=phnExt.getTextContainer();
                            detailTextFieldExt.setRequired(false);
                            detailTextFieldExt.add(new PatternValidator(ActivityEPConstants.REGEX_PATTERN_PHONE));
                            detailTextFieldExt.add(new AttributeModifier("size", "5"));
                            final AmpCategorySelectFieldPanel typeField = new AmpCategorySelectFieldPanel("type",
                                    CategoryConstants.CONTACT_PHONE_TYPE_KEY, typeModel,
                                    CategoryConstants.CONTACT_PHONE_TYPE_NAME, true, true, true, null, true);
                            typeField.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
                                @Override
                                protected void onUpdate(AjaxRequestTarget target) {
                                    send(getPage(), Broadcast.BREADTH,
                                            new ContactChangedEvent(target));
                                    
                                }
                            });
                      
                            List<AmpCategoryValue> collectionByKey = new ArrayList<AmpCategoryValue>();
                            collectionByKey.addAll(CategoryManagerUtil
                                    .getAmpCategoryValueCollectionByKey(CategoryConstants.CONTACT_PHONE_TYPE_KEY));
                            //choiceContainer.setRequired(true);
                            frg2.add(typeField);
                            frg2.add(phn);
                            frg2.add(phnExt);
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
                if (detailsList.getModelObject().size() >= ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE) {
                    String property = "";
                    if (contactProperty.equals(Constants.CONTACT_PROPERTY_NAME_EMAIL)) {
                        property = "emails";
                    } else if (contactProperty.equals(Constants.CONTACT_PROPERTY_NAME_PHONE)) {
                        property = "phones";
                    } else if (contactProperty.equals(Constants.CONTACT_PROPERTY_NAME_FAX)) {
                        property = "faxes";
                    }
                    
                    if (StringUtils.isNotBlank(property)) {
                        detailFeedbackLabel.setDefaultModelObject("*" 
                                   + TranslatorUtil.getTranslatedText(String.format("Max limit for %s is %s", property, 
                            ContactEPConstants.CONTACT_PROPERTY_MAX_SIZE)));
                    }
                    detailFeedbackContainer.setVisible(true);
                    target.add(detailFeedbackContainer);
                    send(getPage(), Broadcast.BREADTH,
                            new ContactChangedEvent(target));
                    return;
                }
                    
                AmpContactProperty fakeContact1 = AmpContactProperty.instantiate(contactProperty);
                fakeContact1.setContact(model.getObject());
                fakeContact1.setName(contactProperty);
                fakeContact1.setValue("");
//              contactProperties.clear();
//              contactProperties.addAll(detailsList.getModelObject());
                Set<AmpContactProperty> contactProperties=setModel.getObject();
                if(contactProperties==null){
                    contactProperties=new TreeSet<AmpContactProperty>();
                    setModel.setObject(contactProperties);
                }
                contactProperties.add(fakeContact1);
                //detailsList.removeAll();
                target.add(resultcontainer);
                

            }

        };      
        add(addLink);   
        
        
        detailFeedbackContainer = new WebMarkupContainer("detailFeedbackContainer");
        detailFeedbackLabel = new Label("detailFeedbackLabel", new Model(""));
        detailFeedbackLabel.setOutputMarkupId(true);
        detailFeedbackContainer.setOutputMarkupId(true);
        detailFeedbackContainer.setOutputMarkupPlaceholderTag(true);
        detailFeedbackContainer.setVisible(false);
        detailFeedbackContainer.add(detailFeedbackLabel);
        add(detailFeedbackContainer);
    }
    
//  protected boolean updateVisibility(IModel<AmpContact> indicatorModel){
//      AmpIndicator ind = indicatorModel.getObject();
//      boolean oldCodeSelected = codeSelected;
//      boolean oldTitleSelected = titleSelected;
//      if (ind.getCode() == null)
//          codeSelected = false;
//      else
//          codeSelected = true;
//      
//      if (ind.getName() == null || ind.getName() == "")
//          titleSelected = false;
//      else
//          titleSelected = true;
//
//      if (codeSelected && titleSelected){
//          indicatorFeedbackContainer.setVisible(false);
//      }
//      else{
//          indicatorFeedbackContainer.setVisible(true);
//          if (!codeSelected && !titleSelected){
//              indicatorFeedbackLabel.setDefaultModelObject(defaultMsg);
//          }
//          else{
//              if (!codeSelected)
//                  indicatorFeedbackLabel.setDefaultModelObject(noCodeMsg);
//              else
//                  indicatorFeedbackLabel.setDefaultModelObject(noTitleMsg);
//          }
//      }
//      
//      if ((oldTitleSelected == titleSelected) && (oldCodeSelected == codeSelected))
//          return false;
//      else
//          return true;
//  }

}
