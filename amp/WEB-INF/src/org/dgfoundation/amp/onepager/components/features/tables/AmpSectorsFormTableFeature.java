/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTreeCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DbUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.dgfoundation.amp.onepager.OnePagerMessages.HAS_SECTOR_INDICATOR_ALERT_MSG;
import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.MAXIMUM_PERCENTAGE;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpSectorsFormTableFeature extends
        AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivitySector> {

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpSectorsFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am,
            final AmpClassificationConfiguration sectorClassification)
            throws Exception {
        super(id, am, fmName, false, true);
        final IModel<Set<AmpActivitySector>> setModel = new PropertyModel<Set<AmpActivitySector>>(
                am, "sectors");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivitySector>());
        if (sectorClassification.getDescription() != null)
            addInfoText(sectorClassification.getDescription());
        
        final IModel<List<AmpActivitySector>> listModel = new AbstractReadOnlyModel<List<AmpActivitySector>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpActivitySector> getObject() {
                // remove sectors with other classification
                ArrayList<AmpActivitySector> ret = new ArrayList<AmpActivitySector>();

                if(setModel.getObject()!=null)
                for (AmpActivitySector ampActivitySector : setModel.getObject()) {
                    if (ampActivitySector.getClassificationConfig().getId()
                            .equals(sectorClassification.getId()))
                        ret.add(ampActivitySector);
                }
                
                Comparator<AmpActivitySector> comparator = new Comparator<AmpActivitySector>(){

                @Override
                public int compare(AmpActivitySector o1, AmpActivitySector o2) {
                    return o1.getSectorId().getSectorPathString().compareTo(o2.getSectorId().getSectorPathString());
                }
                
                
            };
            
               Collections.sort(ret, comparator);
                 
                return ret;
            }
        };

        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        final AmpPercentageCollectionValidatorField<AmpActivitySector> percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivitySector>(
                "sectorPercentageTotal", listModel, "sectorPercentageTotal") {
            @Override
            public Number getPercentage(AmpActivitySector item) {
                return item.getSectorPercentage();
            }
        };
        
        final Label totalLabel = new Label("totalPercSector", new Model() {
            @Override
            public String getObject() {
                if (FMUtil.isVisibleChildWithFmName(AmpSectorsFormTableFeature.this, "sectorPercentage")) {
                    double total = 0;
                    for (AmpActivitySector a : listModel.getObject()) {
                        if (a.getSectorPercentage() != null) {
                            total += a.getSectorPercentage();
                        }
                    }
                    DecimalFormat decimalFormat = FormatHelper.getPercentageDefaultFormat(true);
                    return decimalFormat.format(total);
                }
                return null;
            }
        });
        totalLabel.setOutputMarkupId(true);
        add(totalLabel);
        
        percentageValidationField.setIndicatorAppender(iValidator);
        add(percentageValidationField);

        final AmpMinSizeCollectionValidationField<AmpActivitySector> minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivitySector>(
                "minSizeSectorsValidator", listModel, "minSizeSectorsValidator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                reqStar.setVisible(isVisible());
            }
        };
        minSizeCollectionValidationField.setIndicatorAppender(iValidator);
        add(minSizeCollectionValidationField);
        
        
        final AmpUniqueCollectionValidatorField<AmpActivitySector> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivitySector>(
                "uniqueSectorsValidator", listModel, "uniqueSectorsValidator") {
            @Override
            public Object getIdentifier(AmpActivitySector t) {
                return t.getSectorId().getName();
            }   
        };
        uniqueCollectionValidationField.setIndicatorAppender(iValidator);
        add(uniqueCollectionValidationField);
        final AmpTreeCollectionValidatorField<AmpActivitySector> treeCollectionValidationField = new AmpTreeCollectionValidatorField<AmpActivitySector>(
                "treeSectorsValidator", listModel, "treeSectorsValidator") {
                    @Override
                    public AmpAutoCompleteDisplayable getItem(AmpActivitySector t) {
                        return t.getSectorId();
                    }           
        };
        treeCollectionValidationField.setIndicatorAppender(iValidator);
        add(treeCollectionValidationField);
         

        list = new ListView<AmpActivitySector>("listSectors", listModel) {
            @Override
            protected void populateItem(final ListItem<AmpActivitySector> item) {
                final MarkupContainer listParent = this.getParent();
                PropertyModel<Double> percModel = new PropertyModel<Double>(
                        item.getModel(), "sectorPercentage");

                AmpPercentageTextField percentageField = new AmpPercentageTextField(
                        "percentage", percModel, "sectorPercentage",
                        percentageValidationField){
                    @Override
                    protected void onAjaxOnUpdate(
                            AjaxRequestTarget target) {
                        super.onAjaxOnUpdate(target);
                        target.add(totalLabel);
                    }
                };

                item.add(percentageField);

                item.add(new Label("sectorLabel", item.getModelObject()
                        .getSectorId().getSectorPathString()));
                AmpDeleteLinkField delSector = new AmpDeleteLinkField(
                        "delSector", "Delete Sector") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        AmpActivityVersion activity = am.getObject();
                        AmpActivitySector sector = item.getModelObject();

                        boolean hasIndicators = ActivityUtil.hasSectorIndicatorsInActivity(activity, sector);
                        if (hasIndicators) {
                            String message = TranslatorUtil.getTranslation(HAS_SECTOR_INDICATOR_ALERT_MSG);
                            target.appendJavaScript(OnePagerUtil.createJSAlert(message));
                            return;
                        }

                        setModel.getObject().remove(item.getModelObject());
                        target.add(listParent);
                        target.add(totalLabel);
                        list.removeAll();
                        percentageValidationField.reloadValidationField(target);
                        uniqueCollectionValidationField.reloadValidationField(target);
                        minSizeCollectionValidationField.reloadValidationField(target);     
                        treeCollectionValidationField.reloadValidationField(target);
                    }
                };
                item.add(delSector);

            }
        };
        list.setReuseItems(true);
        add(list);
        
        add(new AmpDividePercentageField<AmpActivitySector>("dividePercentage", "Divide Percentage", "Divide Percentage", setModel, new Model<ListView<AmpActivitySector>>(list)){
            private static final long serialVersionUID = 1L;

            @Override
            public void setPercentage(AmpActivitySector loc, int val) {
                loc.setSectorPercentage((float) val);
            }

            @Override
            public int getPercentage(AmpActivitySector loc) {
                return (int)((float)loc.getSectorPercentage());
            }

            @Override
            public boolean itemInCollection(AmpActivitySector item) {
                if (item.getClassificationConfig().getId()
                        .equals(sectorClassification.getId()))
                    return true;
                else
                    return false;
            }

        });
        
        final AmpAutocompleteFieldPanel<AmpSector> searchSectors = new AmpAutocompleteFieldPanel<AmpSector>(
                "searchSectors", "Search " + fmName, AmpSectorSearchModel.class) {
            private static final long serialVersionUID = 1227775244079125152L;

            @Override
            protected String getChoiceValue(AmpSector choice) {
                return DbUtil.filter(choice.getName());
            }

            @Override
            public void onSelect(AjaxRequestTarget target, AmpSector choice) {
                AmpActivitySector activitySector = new AmpActivitySector();
                activitySector.setSectorId(choice);
                activitySector.setActivityId(am.getObject());

                if (list.size() == 1) {
                    activitySector.setSectorPercentage(MAXIMUM_PERCENTAGE.floatValue());
                }

                activitySector.setClassificationConfig(sectorClassification);
                if(setModel.getObject()  == null)
                    setModel.setObject(new HashSet<AmpActivitySector>());
                setModel.getObject().add(activitySector);
                list.removeAll();
                target.add(list.getParent());
                percentageValidationField.reloadValidationField(target);
                uniqueCollectionValidationField.reloadValidationField(target);
                minSizeCollectionValidationField.reloadValidationField(target);
                treeCollectionValidationField.reloadValidationField(target);
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

        searchSectors.getModelParams().put(
                AmpSectorSearchModel.PARAM.SECTOR_SCHEME,
                sectorClassification.getClassification());
        searchSectors.getModelParams().put(
                AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

        add(searchSectors);
    }

}
