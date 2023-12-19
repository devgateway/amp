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
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpMinSizeCollectionValidationField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTreeCollectionValidatorField;
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.interfaces.ISectorTableDeleteListener;
import org.dgfoundation.amp.onepager.models.AbstractAmpAutoCompleteModel;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.util.AmpDividePercentageField;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.AmpAutoCompleteDisplayable;
import org.digijava.module.aim.util.DbUtil;

import java.text.DecimalFormat;
import java.util.*;

import org.dgfoundation.amp.onepager.interfaces.ISectorTableUpdateListener;
import org.digijava.module.aim.util.SectorUtil;
import org.hibernate.Session;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.MAXIMUM_PERCENTAGE;

/**
 * @author mpostelnicu@dgateway.org since Oct 20, 2010
 */
public class AmpSectorsFormTableFeature extends
        AmpFormTableFeaturePanel<AmpActivityVersion, AmpActivitySector> {

    private ISectorTableUpdateListener updateListener;
    private ISectorTableDeleteListener deleteListener;

    public void setUpdateListener(ISectorTableUpdateListener listener) {
        this.updateListener = listener;
    }

    public void setDeleteListener(ISectorTableDeleteListener listener) {
        this.deleteListener = listener;
    }

    private AmpAutocompleteFieldPanel<AmpSector> searchSectors;
    public AmpAutocompleteFieldPanel<AmpSector> getSearchSectors() { return this.searchSectors; }

    private IModel<Set<AmpActivitySector>> setModel;
    public IModel<Set<AmpActivitySector>> getSetModel() { return this.setModel; }

    private AmpPercentageCollectionValidatorField<AmpActivitySector> percentageValidationField;
    private AmpUniqueCollectionValidatorField<AmpActivitySector> uniqueCollectionValidationField;
    private AmpMinSizeCollectionValidationField<AmpActivitySector> minSizeCollectionValidationField;
    private AmpTreeCollectionValidatorField<AmpActivitySector> treeCollectionValidationField;


    /**
     * Triggers an update event with the selected sectors based on the given sector classification.
     * Method called when change the list of selected sectors to update (from the parent component)
     * the another list of sectors.
     * Is usefully when exist a mapping between sectors from different classifications.
     *
     * @param selectedSectors           the set of selected sectors
     * @param sectorClassification     the current sector classification configuration
     */
    protected void triggerUpdateEvent(Set<AmpActivitySector> selectedSectors,
                                      AmpClassificationConfiguration sectorClassification) {
        if (updateListener != null) {
            List<AmpSector> sectorsByClassification = new ArrayList<AmpSector>();
            for (AmpActivitySector ampActivitySector : selectedSectors) {
                if (ampActivitySector.getClassificationConfig().getId().equals(sectorClassification.getId()))
                    sectorsByClassification.add(ampActivitySector.getSectorId());
            }

            updateListener.onUpdate(sectorsByClassification);
        }
    }

    /**
     * Updates the component based on the given data.
     * This method is called from the parent component when the data is updated.
     *
     * @param selectedSectors the list of selected sectors
     */
    public void updateBasedOnData(List<AmpSector> selectedSectors) {
        this.searchSectors.getModelParams().put(AmpSectorSearchModel.PARAM.SRC_SECTOR_SELECTED, selectedSectors);
    }

    protected void triggerDeleteEvent(AmpSector sectorDeleted) {
        if (deleteListener != null) {
            deleteListener.onDelete(sectorDeleted);
        }
    }

    /**
     * Deletes all sector in the Activity component that are mapped with the given sector.
     *
     * @param sectorDeleted the sector to delete mappings for
     */
    public void deleteBasedOnData(AmpSector sectorDeleted) {
        Collection<AmpSectorMapping> allMappings = SectorUtil.getAllSectorMappings();
        List<AmpSector> dstSectorsMappedWithDeletedSector = new ArrayList<AmpSector>();
        for (AmpSectorMapping ampSectorMapping : allMappings) {
            if (ampSectorMapping.getSrcSector().getAmpSectorId().equals(sectorDeleted.getAmpSectorId())) {
                dstSectorsMappedWithDeletedSector.add(ampSectorMapping.getDstSector());
            }
        }

        // We search for the destination sectors in Activity
        if (!dstSectorsMappedWithDeletedSector.isEmpty()) {
            Iterator<AmpActivitySector> iterator = setModel.getObject().iterator();
            while (iterator.hasNext()) {
                AmpActivitySector sector = iterator.next();
                for (AmpSector dstSectorMapped : dstSectorsMappedWithDeletedSector) {
                    if (sector.getSectorId().getAmpSectorId().equals(dstSectorMapped.getAmpSectorId())) {
                        // if the sector is not mapped to another sector
                        if (dstSectorHasOnlyOneMap(dstSectorMapped, allMappings)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a given destination sector has only one mapping in a collection of mappings.
     *
     * @param dstSector    the destination sector to check for
     * @param allMappings the collection of all mappings
     * @return true if the destination sector has only one mapping, false otherwise
     */
    private boolean dstSectorHasOnlyOneMap(AmpSector dstSector, Collection<AmpSectorMapping> allMappings) {
        int count = 0;
        for (AmpSectorMapping ampSectorMapping : allMappings) {
            if (ampSectorMapping.getDstSector().getAmpSectorId().equals(dstSector.getAmpSectorId())) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Clears the table and reloads the validation fields.
     *
     * @param target the {@link AjaxRequestTarget} used to refresh the table
     */
    public void refreshTable(AjaxRequestTarget target) {
        list.removeAll();
        percentageValidationField.reloadValidationField(target);
        uniqueCollectionValidationField.reloadValidationField(target);
        minSizeCollectionValidationField.reloadValidationField(target);
        treeCollectionValidationField.reloadValidationField(target);
    }


    /**
     * Constructor for the AmpSectorsFormTableFeature class.
     *
     * @param id                    The id of the component.
     * @param fmName                The name of the feature model.
     * @param am                    The model for the AmpActivityVersion.
     * @param sectorClassification  The classification configuration for sectors.
     *
     * @throws Exception   If there is an exception during construction.
     */
    public AmpSectorsFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am,
            final AmpClassificationConfiguration sectorClassification)
            throws Exception {
        super(id, am, fmName, false, true);
        setModel = new PropertyModel<Set<AmpActivitySector>>(am, "sectors");
        if (setModel.getObject() == null) setModel.setObject(new HashSet<AmpActivitySector>());
        if (sectorClassification.getDescription() != null) addInfoText(sectorClassification.getDescription());


        final IModel<List<AmpActivitySector>> listModel = new AbstractReadOnlyModel<List<AmpActivitySector>>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<AmpActivitySector> getObject() {
                // remove sectors with other classification
                ArrayList<AmpActivitySector> ret = new ArrayList<AmpActivitySector>();

                if (setModel.getObject() != null)
                    for (AmpActivitySector ampActivitySector : setModel.getObject()) {
                        if (ampActivitySector.getClassificationConfig().getId().equals(sectorClassification.getId()))
                            ret.add(ampActivitySector);
                    }
                Comparator<AmpActivitySector> comparator = new Comparator<AmpActivitySector>() {
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
        percentageValidationField = new AmpPercentageCollectionValidatorField<AmpActivitySector>(
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

        minSizeCollectionValidationField = new AmpMinSizeCollectionValidationField<AmpActivitySector>(
                "minSizeSectorsValidator", listModel, "minSizeSectorsValidator"){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                reqStar.setVisible(isVisible());
            }
        };
        minSizeCollectionValidationField.setIndicatorAppender(iValidator);
        add(minSizeCollectionValidationField);

        uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpActivitySector>(
                "uniqueSectorsValidator", listModel, "uniqueSectorsValidator") {
            @Override
            public Object getIdentifier(AmpActivitySector t) {
                return t.getSectorId().getName();
            }
        };
        uniqueCollectionValidationField.setIndicatorAppender(iValidator);
        add(uniqueCollectionValidationField);
        treeCollectionValidationField = new AmpTreeCollectionValidatorField<AmpActivitySector>(
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
                PropertyModel<Double> percModel = new PropertyModel<Double>(item.getModel(), "sectorPercentage");

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

                item.add(new Label("sectorLabel", item.getModelObject().getSectorId().getSectorPathString()));
                AmpDeleteLinkField delSector = new AmpDeleteLinkField("delSector", "Delete Sector") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        AmpActivitySector sectorToDelete = item.getModelObject();
                        setModel.getObject().remove(sectorToDelete);
                        triggerDeleteEvent(sectorToDelete.getSectorId());
                        triggerUpdateEvent(setModel.getObject(), sectorClassification);
                        target.add(listParent);
                        target.add(totalLabel);
                        refreshTable(target);
                    }
                };
                item.add(delSector);
            }
        };
        list.setReuseItems(true);
        add(list);

        add(new AmpDividePercentageField<AmpActivitySector>("dividePercentage", "Divide Percentage",
                "Divide Percentage", setModel, new Model<ListView<AmpActivitySector>>(list)){
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
                if (item.getClassificationConfig().getId().equals(sectorClassification.getId()))
                    return true;
                else return false;
            }
        });

        this.searchSectors = new AmpAutocompleteFieldPanel<AmpSector>(
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
                if (setModel.getObject() == null) setModel.setObject(new HashSet<AmpActivitySector>());
                setModel.getObject().add(activitySector);
                triggerUpdateEvent(setModel.getObject(), sectorClassification);
                target.add(list.getParent());
                refreshTable(target);
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

        this.searchSectors.getModelParams().put(AmpSectorSearchModel.PARAM.SECTOR_SCHEME,
                                            sectorClassification.getClassification());
        this.searchSectors.getModelParams().put(AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS, 0);

        //For mappings between sectors with different classifications, we configure the search to show only the sectors
        // mapped to the selected sectors from the primary classification in dropdown list.
        if (sectorClassification.getName().equals(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME)) {
            List<AmpSector> primarySectorsSelected = new ArrayList<AmpSector>();
            for (AmpActivitySector ampActivitySector : setModel.getObject()) {
                if (ampActivitySector.getClassificationConfig().getName().equals(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                    primarySectorsSelected.add(ampActivitySector.getSectorId());
                }
            }
            if (!primarySectorsSelected.isEmpty()) {
                this.searchSectors.getModelParams().put(AmpSectorSearchModel.PARAM.SRC_SECTOR_SELECTED,
                                                        primarySectorsSelected);
            }
        }

        add(this.searchSectors);
    }
}
