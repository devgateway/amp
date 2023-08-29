/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.items.AmpMEItemFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.*;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.models.PersistentObjectModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;

import java.util.*;

/**
 * M&E section
 * @author aartimon@dginternational.org 
 * @since Feb 10, 2011
 */
public class AmpMEFormSectionFeature extends AmpFormSectionFeaturePanel {
    private final ListView<IndicatorActivity> list;
    
    private boolean titleSelected;
    private boolean codeSelected;
        private boolean sectorAdded;
    private WebMarkupContainer indicatorFeedbackContainer;
    private Label indicatorFeedbackLabel;
    
    static final private String defaultMsg = "*" + TranslatorUtil.getTranslatedText("Please type indicator name and code and add sector(s)");
    static final private String noCodeMsg = "*" + TranslatorUtil.getTranslatedText("Please choose indicator code");
    static final private String noTitleMsg = "*" + TranslatorUtil.getTranslatedText("Please choose a unique title");
        static final private String noSectorsMsg = "*" + TranslatorUtil.getTranslatedText("Please add sector(s)");
    
    public AmpMEFormSectionFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;
        
        //final IModel<Set<IndicatorActivity>> setModel = new PropertyModel<Set<IndicatorActivity>>(am, "indicators");
        
        if (am.getObject().getIndicators() == null){
            am.getObject().setIndicators(new HashSet<>());
        }
        final IModel<List<IndicatorActivity>>  listModel = OnePagerUtil 
                .getReadOnlyListModelFromSetModel(new PropertyModel<>(am, "indicators"));
        
        final AmpUniqueCollectionValidatorField<IndicatorActivity> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<IndicatorActivity>(
                "uniqueMEValidator", listModel, "Unique MEs Validator") {

            @Override
            public Object getIdentifier(IndicatorActivity t) {
                return t.getIndicator().getName();
            }
        };
        add(uniqueCollectionValidationField);
        
        
        
        list = new ListView<IndicatorActivity>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<IndicatorActivity> item) {
                AmpMEItemFeaturePanel indicator = new AmpMEItemFeaturePanel("item", "ME Item", item.getModel(), PersistentObjectModel.getModel(item.getModelObject().getIndicator()), new PropertyModel(item.getModel(), "values"));
                item.add(indicator);
                
                String translatedMessage = TranslatorUtil.getTranslation("Do you really want to delete this indicator?");
                AmpDeleteLinkField deleteLinkField = new AmpDeleteLinkField(
                        "delete", "Delete ME Item", new Model<String>(translatedMessage)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        am.getObject().getIndicators().remove(item.getModelObject());
                        uniqueCollectionValidationField.reloadValidationField(target);
                        //setModel.getObject().remove(item.getModelObject());
                        list.removeAll();
                        target.add(AmpMEFormSectionFeature.this);
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
                    }
                };
                item.add(deleteLinkField);
            }
        };
        list.setReuseItems(true);
        add(list);
        
        
        
        final AmpAutocompleteFieldPanel<AmpIndicator> searchIndicators=new AmpAutocompleteFieldPanel<AmpIndicator>("search","Search Indicators",AmpMEIndicatorSearchModel.class) {          
            
            private static final long serialVersionUID = 1227775244079125152L;

            @Override
            protected String getChoiceValue(AmpIndicator choice) {
                return DbUtil.filter(choice.getName());
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpIndicator choice) {

                    IndicatorActivity ia = new IndicatorActivity();
                    ia.setActivity(am.getObject());
                    ia.setIndicator(choice);
                    am.getObject().getIndicators().add(ia);
                    uniqueCollectionValidationField.reloadValidationField(target);
                
                    //setModel.getObject().add(ia);
                list.removeAll();
                target.add(list.getParent());
                target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
            }

            @Override
            public Integer getChoiceLevel(AmpIndicator choice) {
                return 0;
            }
        };

        add(searchIndicators);

        final IModel<AmpIndicator> newInd = getNewIndicatorModel();
        
        AmpTextFieldPanel<String> indName = new AmpTextFieldPanel<String>("indName", new PropertyModel<String>(newInd, "name"), "Name", AmpFMTypes.MODULE,Boolean.TRUE);
        //indName.getTextContainer().setRequired(true);
        indName.setOutputMarkupId(true);
        indName.setTextContainerDefaultMaxSize();
        add(indName);

        add(new AmpTextAreaFieldPanel("indDesc", new PropertyModel<String>(newInd, "description"), "Indicator Description", false, AmpFMTypes.MODULE));
        AmpTextFieldPanel<String> indCode = new AmpTextFieldPanel<String>("indCode", new PropertyModel<String>(newInd, "code"), "Code", AmpFMTypes.MODULE,Boolean.TRUE);
        //indCode.getTextContainer().setRequired(true);
        indCode.setTextContainerDefaultMaxSize();
        indCode.setOutputMarkupId(true);
        add(indCode);
        
        AmpDatePickerFieldPanel datePicker = new AmpDatePickerFieldPanel("indDate", new PropertyModel<Date>(newInd, "creationDate"), "Creation Date");
        datePicker.setEnabled(false);
        add(datePicker);
        
        ArrayList<String> typeCol = new ArrayList<String>();
        typeCol.add("A"); typeCol.add("D");
        
        ChoiceRenderer cr = new ChoiceRenderer(){
            @Override
            public Object getDisplayValue(Object object) {
                String s = (String)object;
                // Fixing AMP-12708
                if (s.compareTo("A") == 0)
                    return TranslatorUtil.getTranslation("Ascending");
                else
                    return TranslatorUtil.getTranslation("Descending");
            }
        };
        add(new AmpSelectFieldPanel("indType", new PropertyModel<>(newInd, "type"), typeCol, "Type", false, true, cr));


        
        
        
        final AmpClassificationConfiguration sectorClassification = SectorUtil.getPrimaryConfigClassification();
        final IModel<Set<AmpSector>> sectorSetModel = new PropertyModel<>(
                newInd, "sectors");

        IModel<List<AmpSector>> sectorListModel = new AbstractReadOnlyModel<List<AmpSector>>() {

            @Override
            public List<AmpSector> getObject() {
                return new ArrayList<>(sectorSetModel.getObject());
            }
        };

        ListView<AmpSector> sectorList = new ListView<AmpSector>("listSectors", sectorListModel) {
            @Override
            protected void populateItem(final ListItem<AmpSector> item) {
                final MarkupContainer listParent = this.getParent();
                item.add(new Label("sectorLabel", item.getModelObject().getName()));

                AmpDeleteLinkField delSector = new AmpDeleteLinkField(
                        "delSector", "Delete Sector") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        sectorSetModel.getObject().remove(item.getModelObject());
                        target.add(listParent);
                        list.removeAll();
                    }
                };
                item.add(delSector);

            }
        };
        final WebMarkupContainer sectorContainer=new WebMarkupContainer("meSectorContainer");
        sectorContainer.setOutputMarkupId(true);
        list.setReuseItems(true);
        sectorContainer.add(sectorList);
        
        
        final AmpUniqueCollectionValidatorField<AmpSector> uniqueSectorsValidator = new AmpUniqueCollectionValidatorField<AmpSector>(
                "uniqueSectorsValidator", sectorListModel, "uniqueSectorsValidator")
                {
                public Object getIdentifier(AmpSector sector){
                    return sector.getName();
                }
                }
                ;

        add(uniqueSectorsValidator);


        final AmpAutocompleteFieldPanel<AmpSector> searchSectors=new AmpAutocompleteFieldPanel<AmpSector>("searchSectors", "Search Sectors For " + fmName,AmpSectorSearchModel.class) {         

            private static final long serialVersionUID = 1227775244079125152L;

            @Override
            protected String getChoiceValue(AmpSector choice){
                return DbUtil.filter(choice.getName());
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpSector choice) {
                sectorSetModel.getObject().add(choice);
                list.removeAll();
                                indicatorFeedbackContainer.setVisible(false);
                target.add(sectorContainer);
                                target.add(indicatorFeedbackContainer);
            }

            @Override
            public Integer getChoiceLevel(AmpSector choice) {
                int i = 0;
                AmpSector c = choice;
                while (c.getParentSectorId() != null) {
                    i++;
                    c = c.getParentSectorId();
                }
                return i;

            }
        };

        searchSectors.getModelParams().put(AmpSectorSearchModel.PARAM.SECTOR_SCHEME,    sectorClassification.getClassification());
        searchSectors.getModelParams().put(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

        sectorContainer.add(searchSectors);
        add(sectorContainer);
        
        AmpAjaxLinkField addIndicator = new AmpAjaxLinkField("addIndicator", "Add Indicator", "Add Indicator", AmpFMTypes.MODULE) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AmpIndicator indicator =newInd.getObject(); 
                
                uniqueSectorsValidator.reloadValidationField(target);
                if(uniqueSectorsValidator.getHiddenContainer().getModelObject().length() >0 )
                    return;
                
                if(indicator.getName()!=null && indicator.getName().trim().length()>0 &&
                        indicator.getCode()!=null && indicator.getCode().length()>0&&indicator.getSectors()!=null&&indicator.getSectors().size()>0){
                    try {
                        Session session = PersistenceManager.getSession();
                        //beginTransaction();
                        session.save(indicator);
                        //tr.commit();
                        
                        updateVisibility(newInd);
                        target.add(indicatorFeedbackContainer);
                        
                        IndicatorActivity ia = new IndicatorActivity();
                        ia.setActivity(am.getObject());
                        ia.setIndicator(indicator);
                        am.getObject().getIndicators().add(ia);
                        //setModel.getObject().add(ia);
                        
                        newInd.setObject(getNewIndicator());
                        
                        target.add(list.getParent());
                        target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpMEFormSectionFeature.this));
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }else{
                    updateVisibility(newInd);
                    target.add(indicatorFeedbackContainer);
                }
                
            }
        };
        
        add(addIndicator);

        indicatorFeedbackContainer = new WebMarkupContainer("indicatorFeedbackContainer");
        indicatorFeedbackLabel = new Label("indicatorFeedbackLabel", new Model(defaultMsg));
        indicatorFeedbackContainer.setOutputMarkupId(true);
        indicatorFeedbackContainer.setOutputMarkupPlaceholderTag(true);
        indicatorFeedbackContainer.setVisible(false);
        indicatorFeedbackContainer.add(indicatorFeedbackLabel);
        add(indicatorFeedbackContainer);
        
        
    }
    
    private AmpIndicator getNewIndicator() {
        AmpIndicator newInd = new AmpIndicator();
        newInd.setSectors(new HashSet<>());
        newInd.setCreationDate(new Date());
        return newInd;
    }

    private IModel<AmpIndicator> getNewIndicatorModel() {
        return new Model<>(getNewIndicator());
    }
    
    protected void updateVisibility(IModel<AmpIndicator> indicatorModel){
        AmpIndicator ind = indicatorModel.getObject();
        sectorAdded= ind.getSectors() != null && !ind.getSectors().isEmpty();
        codeSelected = ind.getCode() != null;

        titleSelected = ind.getName() != null && !Objects.equals(ind.getName(), "");

        if (codeSelected && titleSelected&&sectorAdded){
            indicatorFeedbackContainer.setVisible(false);
        }
        else{
            indicatorFeedbackContainer.setVisible(true);
            if (!codeSelected && !titleSelected&&!sectorAdded){
                indicatorFeedbackLabel.setDefaultModelObject(defaultMsg);
            }
            else{
                if (!codeSelected)
                    indicatorFeedbackLabel.setDefaultModelObject(noCodeMsg);
                                else{
                                    if(!titleSelected)
                    indicatorFeedbackLabel.setDefaultModelObject(noTitleMsg);
                                    else{
                                        indicatorFeedbackLabel.setDefaultModelObject(noSectorsMsg);
                                    }
                                    
                                }
                                    
            }
        }
    }

}
