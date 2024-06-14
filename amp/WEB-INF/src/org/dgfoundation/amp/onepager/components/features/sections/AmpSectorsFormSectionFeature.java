/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.dgfoundation.amp.onepager.components.features.tables.AmpSectorsFormTableFeature;
import org.dgfoundation.amp.onepager.interfaces.ISectorTableDeleteListener;
import org.dgfoundation.amp.onepager.models.AmpSectorSearchModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.util.SectorUtil;

import org.dgfoundation.amp.onepager.interfaces.ISectorTableUpdateListener;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpSectorsFormSectionFeature extends AmpFormSectionFeaturePanel
        implements ISectorTableUpdateListener, ISectorTableDeleteListener {

    private static final long serialVersionUID = -5601918041949098629L;

    private AmpSectorsFormTableFeature primarySectorsTable;
    private AmpSectorsFormTableFeature secondarySectorsTable;
    AmpClassificationConfiguration primaryConf = new AmpClassificationConfiguration();
    AmpClassificationConfiguration secondaryConf = new AmpClassificationConfiguration();

    /**
     * @param id
     * @param fmName
     * @throws Exception
     */
    public AmpSectorsFormSectionFeature(String id, String fmName,final IModel<AmpActivityVersion> am) throws Exception {
        super(id, fmName, am);
        this.fmType = AmpFMTypes.MODULE;

        RepeatingView view = new RepeatingView("allSectorsTables");
        view.setOutputMarkupId(true);
        add(view);

        List<AmpClassificationConfiguration> allClassificationConfigs = SectorUtil.getAllClassificationConfigsOrdered();


        for (AmpClassificationConfiguration conf : allClassificationConfigs) {
            if (conf.getName().equals(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                primaryConf = conf;
            } else if (conf.getName().equals(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME)) {
                secondaryConf = conf;
            }
        }

        primarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                primaryConf.getName() + " Sectors", am, primaryConf);
        primarySectorsTable.setUpdateListener(this);
        primarySectorsTable.setDeleteListener(this);

        secondarySectorsTable = new AmpSectorsFormTableFeature(view.newChildId(),
                secondaryConf.getName() + " Sectors", am, secondaryConf);


        view.add(primarySectorsTable);
        view.add(secondarySectorsTable);
    }


    /**
     * Updates the user interface based on the given data. This method is called when the trigger is fired.
     * Used to filter secondary sectors based on the primary sector selection when exist mapping between them.
     *
     * @param data the list of AmpSector objects to update the UI with
     */
    @Override
    public void onUpdate(List<AmpSector> data) {
        if (secondarySectorsTable != null) {
            // Update user interface
             AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
             if (target != null) {
                 secondarySectorsTable.updateBasedOnData(data);
                 target.add(secondarySectorsTable.getSearchSectors());
             }
             if (this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.ACTION)=="add" || this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.ACTION)=="add") {
                 populateSecondarySectorsFor1Choice( target, secondaryConf);
             }

        }




    }

    private void populateSecondarySectorsFor1Choice(AjaxRequestTarget target, AmpClassificationConfiguration sectorClassification)
    {

        AmpSector selectedSector =(AmpSector) this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.CURRENT_SRC_SECTOR_SELECTED);
        logger.info("Selected sector: " + selectedSector);
        try {
            if (selectedSector!=null) {
                List<AmpSector> choices = searchSectorsDstFromMapping(selectedSector);
                logger.info("Choices found: " + choices);
                if (choices.size() == 1) {
                    for (AmpSector secondarySector : choices) {
                        AmpActivitySector newSector = new AmpActivitySector();
                        newSector.setSectorId(secondarySector);
                        newSector.setActivityId(secondarySectorsTable.getSetModel().getObject().iterator().next().getActivityId()); // Assuming activityId is the same
                        newSector.setClassificationConfig(sectorClassification);
                        this.secondarySectorsTable.getSearchSectors().getModelParams().computeIfAbsent(AmpSectorSearchModel.PARAM.DST_SECTOR_SELECTED,
                                k -> new ArrayList<>());
                        ((List<AmpActivitySector>)secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.DST_SECTOR_SELECTED)).add(newSector);

                        secondarySectorsTable.getSetModel().getObject().add(newSector);
                    }
                    target.add(secondarySectorsTable.getList().getParent());

                }
                List<AmpSector> srcSectorSelected = (List<AmpSector>) this.primarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SRC_SECTOR_SELECTED);

//                secondarySectorsTable.updateBasedOnData(srcSectorSelected);
                secondarySectorsTable.triggerUpdateEvent(secondarySectorsTable.getSetModel().getObject(), sectorClassification);

                target.add(secondarySectorsTable.getSearchSectors());

            }
        }catch (Exception e) {
            logger.error("Error",e);
        }




    }


    private List<AmpSector> searchSectorsDstFromMapping(AmpSector srcSector) {
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpSector> dstSectorIds = new ArrayList<>();
        String hql = "SELECT sm.dstSector FROM AmpSectorMapping sm WHERE sm.srcSector.ampSectorId = :srcSectorId";

        try {
            Query query = session.createQuery(hql);
            query.setParameter("srcSectorId", srcSector.getAmpSectorId(), LongType.INSTANCE);
            dstSectorIds = query.list();
            if (dstSectorIds.isEmpty())
            {
                AmpSectorScheme scheme = (AmpSectorScheme) this.secondarySectorsTable.getSearchSectors().getModelParams().get(AmpSectorSearchModel.PARAM.SECTOR_SCHEME);

                session = PersistenceManager.getSession();
                String sql = "select pi from "
                        + AmpSector.class.getName()
                        + " pi where pi.ampSecSchemeId=:schemeId and pi.parentSectorId IS null and (pi.deleted is null or pi.deleted = false)";
                query = session.createQuery(sql);
                query.setParameter("schemeId", scheme.getAmpSecSchemeId(), LongType.INSTANCE);
                dstSectorIds = query.list();
            }

        } catch (HibernateException e) {
            // Handle the exception
            e.printStackTrace();
        }

        return dstSectorIds;
    }

    /**
     * Updates the user interface when a deletion event is triggered.
     * This method is called when the onDelete method is invoked in the containing class.
     *
     * @param data the AmpSector object to be deleted
     */
    @Override
    public void onDelete(AmpSector data) {
        if (secondarySectorsTable != null) {
            // Update user interface
            AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
            if (target != null) {
                secondarySectorsTable.deleteBasedOnData(data);
                secondarySectorsTable.refreshTable(target);
                target.add(secondarySectorsTable);
            }
        }
    }
}
